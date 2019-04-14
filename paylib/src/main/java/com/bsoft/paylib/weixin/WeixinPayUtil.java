package com.bsoft.paylib.weixin;

import android.Manifest;
import android.annotation.SuppressLint;

import android.text.TextUtils;

import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.thirdpart.rxpermissions2.RxPermissions;
import com.bsoft.baselib.util.ToastUtil;
import com.bsoft.paylib.PayResult;
import com.bsoft.paylib.PayResultListener;
import com.bsoft.paylib.R;
import com.bsoft.paylib.RxPayFragment;
import com.bsoft.paylib.dic.PayTypeDic;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.FragmentEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import io.reactivex.functions.Consumer;

public class WeixinPayUtil {
    /*Default*/
    /*Util*/
    private RxPayFragment rxPayFragment;
    private PayResultListener listener;
    private RxPermissions rxPermissions;
    /*Flag*/
    public static String weixinAppId = "";
    private boolean localWeixinEnable;

    @SuppressLint("CheckResult")
    public WeixinPayUtil(@NonNull RxPayFragment rxPayFragment, PayResultListener listener) {
        this.rxPayFragment = rxPayFragment;
        this.listener = listener;

        rxPermissions = new RxPermissions(rxPayFragment.getActivity());
        EventBus.getDefault().register(this);
        rxPayFragment.lifecycle().subscribe(new Consumer<FragmentEvent>() {
            @Override
            public void accept(FragmentEvent activityEvent) throws Exception {
                if (activityEvent == FragmentEvent.DESTROY) {
                    EventBus.getDefault().unregister(this);
                }
            }
        });

    }

    public void setListener(PayResultListener listener) {
        this.listener = listener;
    }

    @SuppressLint("CheckResult")
    public void goWechatPay(final String payInfo) {
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
                                listener.error(PayTypeDic.TYPE_WEIXIN, resultVo);
                            }
                        }
                    }
                });
    }

    private void pay(String payInfo) {
        if (TextUtils.isEmpty(payInfo)) {
            PayResult resultVo = new PayResult();
            resultVo.setCode(PayTypeDic.RESULT_CODE_PARAME_ERROR);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_core_param_error));
            if (listener != null) {
                listener.error(PayTypeDic.TYPE_WEIXIN, resultVo);
            }
            return;
        }

        try {
            JSONObject ob = new JSONObject(payInfo);
            PayReq req = new PayReq();
            req.appId = ob.getString("appid");
            weixinAppId = req.appId;
            req.partnerId = ob.getString("partnerid");
            req.packageValue = ob.getString("package");
            req.timeStamp = ob.getString("timestamp");
            req.sign = ob.getString("sign");
            req.nonceStr = ob.getString("noncestr");
            req.prepayId = ob.getString("prepayid");
            if (ob.has("extData")) {
                req.extData = ob.getString("extData");
            }
            IWXAPI wxApi = WXAPIFactory.createWXAPI(rxPayFragment.getActivity(), null);
            wxApi.registerApp(req.appId);
            if (!wxApi.isWXAppInstalled()) {
                ToastUtil.toast(R.string.base_pay_no_weixin);
                PayResult resultVo = new PayResult();
                resultVo.setCode(PayTypeDic.RESULT_CODE_NO_WEIXIN_APP);
                resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_no_weixin));
                if (listener != null) {
                    listener.error(PayTypeDic.TYPE_WEIXIN, resultVo);
                }
                return;
            } else {
                WXPayBackUtil.wxPayBackEnable = true;
                localWeixinEnable = true;
                if (listener != null) {
                    listener.start(PayTypeDic.TYPE_WEIXIN, weixinAppId, payInfo);
                }
                wxApi.sendReq(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
            PayResult resultVo = new PayResult();
            resultVo.setCode(PayTypeDic.RESULT_CODE_PARAME_ERROR);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_core_param_error));
            if (listener != null) {
                listener.error(PayTypeDic.TYPE_WEIXIN, resultVo);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(BaseResp baseResp) {
        if (!localWeixinEnable) {
            return;
        }
        //appid 初始化
        weixinAppId = "";
        WXPayBackUtil.wxPayBackEnable = false;
        localWeixinEnable = false;

        PayResult resultVo = new PayResult();
        resultVo.setExtra(baseResp.errStr);
        if (baseResp.errCode == 0) {
            resultVo.setCode(PayTypeDic.RESULT_CODE_OK);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_success));
            if (listener != null) {
                listener.success(PayTypeDic.TYPE_WEIXIN, resultVo);
            }
        } else if (baseResp.errCode == -2) {
            resultVo.setCode(PayTypeDic.RESULT_CODE_CANCEL);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_cancel));
            if (listener != null) {
                listener.cancel(PayTypeDic.TYPE_WEIXIN, resultVo);
            }
        } else {
            resultVo.setCode(PayTypeDic.RESULT_CODE_ERROR);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_error));
            if (listener != null) {
                listener.error(PayTypeDic.TYPE_WEIXIN, resultVo);
            }
        }
    }


}
