package com.bsoft.commonlib.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


import com.bsoft.baselib.thirdpart.rxpermissions2.RxPermissions;
import com.bsoft.baselib.util.ToastUtil;

import androidx.annotation.NonNull;
import io.reactivex.functions.Consumer;

public class CallPhoneUtil {

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void diallPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }


    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    @SuppressLint("CheckResult")
    public static void callPhone(final Activity activity, String phoneNum) {
        final Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                            activity.startActivity(intent);
                        } else {
                            ToastUtil.toast("电话权限获取失败");
                        }
                    }
                });

    }
}
