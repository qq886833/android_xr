package com.bsoft.paylib.alipay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;


import com.alipay.sdk.app.PayTask;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.thirdpart.rxpermissions2.RxPermissions;
import com.bsoft.baselib.util.ToastUtil;
import com.bsoft.paylib.PayResult;
import com.bsoft.paylib.PayResultListener;
import com.bsoft.paylib.R;
import com.bsoft.paylib.RxPayFragment;
import com.bsoft.paylib.dic.PayTypeDic;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.functions.Consumer;

public class AlipayUtil {
    /*Default*/
    /*Util*/
    private PayResultListener listener;
    private RxPermissions rxPermissions;
    private RxPayFragment rxPayFragment;

    @SuppressLint("CheckResult")
    public AlipayUtil(@NonNull RxPayFragment rxPayFragment, PayResultListener listener) {
        this.listener = listener;
        this.rxPayFragment = rxPayFragment;

        rxPermissions = new RxPermissions(rxPayFragment.getActivity());
        rxPayFragment.lifecycle().subscribe(new Consumer<FragmentEvent>() {
            @Override
            public void accept(FragmentEvent activityEvent) throws Exception {
                if (activityEvent == FragmentEvent.DESTROY
                        && mHandler != null) {
                    mHandler.removeCallbacksAndMessages(null);
                }
            }
        });

    }

    public void setListener(PayResultListener listener) {
        this.listener = listener;
    }

    @SuppressLint("CheckResult")
    public void goAlipay(final String payInfo) {
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
                                listener.error(PayTypeDic.TYPE_ALI, resultVo);
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
                listener.error(PayTypeDic.TYPE_ALI, resultVo);
            }
        }
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(rxPayFragment.getActivity());
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(payInfo, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        if (listener != null) {
            Uri uri = Uri.parse("?" + payInfo);
            String appId = uri.getQueryParameter("app_id");
            listener.start(PayTypeDic.TYPE_ALI, appId, payInfo);
        }
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //支付宝支付handler返回
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            PayResult resultVo = new PayResult();
            switch (msg.what) {
                case 1: {
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
//                    resultVo.setCode(resultStatus);
                    resultVo.setExtra(resultInfo);
                    if (TextUtils.equals(resultStatus, "9000")) {
                        resultVo.setCode(PayTypeDic.RESULT_CODE_OK);
                        resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_success));
                        if (listener != null) {
                            listener.success(PayTypeDic.TYPE_ALI, resultVo);
                        }
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        resultVo.setCode(PayTypeDic.RESULT_CODE_CANCEL);
                        resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_cancel));
                        if (listener != null) {
                            listener.cancel(PayTypeDic.TYPE_ALI, resultVo);
                        }
                    } else {
                        resultVo.setCode(PayTypeDic.RESULT_CODE_ERROR);
                        resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_error));
                        if (listener != null) {
                            listener.error(PayTypeDic.TYPE_ALI, resultVo);
                        }
                    }
                    break;
                }
                default:
                    resultVo.setCode(PayTypeDic.RESULT_CODE_ERROR);
                    resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_pay_error));
                    if (listener != null) {
                        listener.error(PayTypeDic.TYPE_ALI, resultVo);
                    }
                    break;
            }
        }
    };


}
