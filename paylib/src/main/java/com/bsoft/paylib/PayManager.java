package com.bsoft.paylib;
import android.app.Activity;
import android.app.FragmentManager;
import android.text.TextUtils;

import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.paylib.alipay.AlipayUtil;
import com.bsoft.paylib.dic.PayTypeDic;
import com.bsoft.paylib.hbpay.HbpayUtil;
import com.bsoft.paylib.weixin.WeixinPayUtil;


public class PayManager {
    /*Default*/
    public static final String PAY_FRAGMENT_TAG = "core_pay_fragment";
    /*Util*/
    private AlipayUtil alipayUtil;
    private WeixinPayUtil weixinPayUtil;
    private HbpayUtil hbpayUtil;
    private PayResultListener listener;

    public PayManager(Activity activity, PayResultListener listener) {
        this.listener = listener;
        alipayUtil = new AlipayUtil(getRxFragment(activity), listener);
        weixinPayUtil = new WeixinPayUtil(getRxFragment(activity), listener);
        hbpayUtil = new HbpayUtil(getRxFragment(activity), listener);
    }

    public void setListener(PayResultListener listener) {
        alipayUtil.setListener(listener);
        weixinPayUtil.setListener(listener);
        hbpayUtil.setListener(listener);
    }

    public void pay(String payType, String payInfo) {
        if (TextUtils.isEmpty(payInfo)) {
            PayResult resultVo = new PayResult();
            resultVo.setCode(PayTypeDic.RESULT_CODE_PARAME_ERROR);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_core_param_error));
            if (listener != null) {
                listener.error(payType, resultVo);
            }
            return;
        }
        if (TextUtils.equals(payType, PayTypeDic.TYPE_ALI)) {
            alipayUtil.goAlipay(payInfo);
        } else if (TextUtils.equals(payType, PayTypeDic.TYPE_WEIXIN)) {
            weixinPayUtil.goWechatPay(payInfo);
        } else if (TextUtils.equals(payType, PayTypeDic.TYPE_HBPAY)) {
            hbpayUtil.goHbpay(payInfo);
        } else {
            PayResult resultVo = new PayResult();
            resultVo.setCode(PayTypeDic.RESULT_CODE_PARAME_ERROR);
            resultVo.setMsg(CoreAppInit.getApplication().getString(R.string.base_core_param_error));
            if (listener != null) {
                listener.error(payType, resultVo);
            }
        }
    }

    private RxPayFragment getRxFragment(Activity activity) {
        RxPayFragment fragment = findRxFragment(activity);
        boolean isNewInstance = fragment == null;
        if (isNewInstance) {
            fragment = new RxPayFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, PayManager.PAY_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private RxPayFragment findRxFragment(Activity activity) {
        return (RxPayFragment) activity.getFragmentManager().findFragmentByTag(PayManager.PAY_FRAGMENT_TAG);
    }
}
