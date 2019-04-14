package com.bsoft.paylib.hbpay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.log.LogUtil;
import com.bsoft.baselib.thirdpart.rxpermissions2.RxPermissions;
import com.bsoft.baselib.util.ToastUtil;
import com.bsoft.paylib.PayResult;
import com.bsoft.paylib.PayResultListener;
import com.bsoft.paylib.R;
import com.bsoft.paylib.RxPayFragment;
import com.bsoft.paylib.dic.PayTypeDic;
import com.bsoft.paylib.hbpay.HBTradeVo;
import com.bsoft.paylib.weixin.WXPayBackUtil;
import com.healthpay.payment.hpaysdk.HPayAPIFactory;
import com.healthpay.payment.hpaysdk.HPayApi;
import com.healthpay.payment.hpaysdk.interfaces.HPayResultListener;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.trello.rxlifecycle2.android.FragmentEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import io.reactivex.functions.Consumer;

public class HbpayUtil {
    /*Default*/
    /*Util*/
    private RxPayFragment rxPayFragment;
    private PayResultListener listener;
    private RxPermissions rxPermissions;
    /*Flag*/
    private boolean localWeixinEnable;

    @SuppressLint("CheckResult")
    public HbpayUtil(@NonNull RxPayFragment rxPayFragment, PayResultListener listener) {
        this.rxPayFragment = rxPayFragment;
        this.listener = listener;

        rxPermissions = new RxPermissions(rxPayFragment.getActivity());
        EventBus.getDefault().register(this);
        rxPayFragment.lifecycle().subscribe(new Consumer<FragmentEvent>() {
            @Override
            public void accept(FragmentEvent activityEvent) throws Exception {
                if (activityEvent == FragmentEvent.DESTROY) {
                    EventBus.getDefault().unregister(this);
                    setListener(null);
                }
            }
        });

    }

    public void setListener(PayResultListener listener) {
        this.listener = listener;
    }

    @SuppressLint("CheckResult")
    public void goHbpay(final String payInfo) {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                            pay(payInfo);
                        } else {
                            ToastUtil.toast(R.string.base_core_permission_deny);
                            PayResult resultVo = new PayResult();
                            resultVo.setCode(PayTypeDic.RESULT_CODE_PERMISSION_DENY);
                            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_core_permission_deny));
                            if (listener != null) {
                                listener.error(PayTypeDic.TYPE_HBPAY, resultVo);
                            }
                        }
                    }
                });
    }

    private void pay(final String payInfo) {
        if (TextUtils.isEmpty(payInfo)) {
            PayResult resultVo = new PayResult();
            resultVo.setCode(PayTypeDic.RESULT_CODE_PARAME_ERROR);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_core_param_error));
            if (listener != null) {
                listener.error(PayTypeDic.TYPE_HBPAY, resultVo);
            }
        }

        final HBTradeVo hbTradeVo = JSON.parseObject(payInfo, HBTradeVo.class);

        WXPayBackUtil.wxPayBackEnable = true;
        localWeixinEnable = true;
        if (listener != null) {
            String appId = null;
            if (hbTradeVo != null) {
                appId = hbTradeVo.getMerchantId();
            }
            listener.start(PayTypeDic.TYPE_HBPAY, appId, payInfo);
        }

        HPayApi hPayApi = HPayAPIFactory.createHPayApi(hbTradeVo.getMerchantId(), hbTradeVo.getUrl());
        hPayApi.doPay(rxPayFragment.getActivity(), hbTradeVo.getSign(), hbTradeVo.getOrderInfo()
                , new HPayResultListener() {
                    @Override
                    public void result(String code, String msg) {
                        LogUtil.d("HbpayUtil,code=" + code + ",msg=" + msg);

                        PayResult resultVo = new PayResult();
                        resultVo.setExtra(msg);
                        if (TextUtils.equals("1001", code)) {//健保付回调成功
                            if (!TextUtils.equals(hbTradeVo.getPayType(), "03")) {//"03"健宝付的微信
                                //微信有自己的回调，当非微信情况下，成功即可查询服务端支付结果
                                resultVo.setCode(PayTypeDic.RESULT_CODE_OK);
                                resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_success));
                                if (listener != null) {
                                    listener.success(PayTypeDic.TYPE_HBPAY, resultVo);
                                }
                            }
                        } else {
                            resultVo.setCode(PayTypeDic.RESULT_CODE_ERROR);
                            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_error));
                            if (listener != null) {
                                listener.error(PayTypeDic.TYPE_HBPAY, resultVo);
                            }
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(BaseResp baseResp) {
        if (!localWeixinEnable) {
            return;
        }

        WXPayBackUtil.wxPayBackEnable = false;
        localWeixinEnable = false;

        PayResult resultVo = new PayResult();
        resultVo.setExtra(baseResp.errStr);
        if (baseResp.errCode == 0) {
            resultVo.setCode(PayTypeDic.RESULT_CODE_OK);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_success));
            if (listener != null) {
                listener.success(PayTypeDic.TYPE_HBPAY, resultVo);
            }
        } else if (baseResp.errCode == -2) {
            resultVo.setCode(PayTypeDic.RESULT_CODE_CANCEL);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_cancel));
            if (listener != null) {
                listener.cancel(PayTypeDic.TYPE_HBPAY, resultVo);
            }
        } else {
            resultVo.setCode(PayTypeDic.RESULT_CODE_ERROR);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_error));
            if (listener != null) {
                listener.error(PayTypeDic.TYPE_HBPAY, resultVo);
            }
        }
    }


}
