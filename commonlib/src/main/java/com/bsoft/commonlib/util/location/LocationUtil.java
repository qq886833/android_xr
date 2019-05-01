package com.bsoft.commonlib.util.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.bsoft.baselib.log.CoreLogTag;
import com.bsoft.baselib.util.EffectUtil;
import com.bsoft.baselib.util.ToastUtil;
import com.bsoft.baselib.widget.dialog.CoreDialog;
import com.bsoft.commonlib.R;
import com.bsoft.commonlib.util.AppUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LocationUtil {

    public static void showLocationDialog(final Activity activity, final LocationConfig config) {
        if (config == null) {
            return;
        }
        final CoreDialog dialog = new CoreDialog(LayoutInflater.from(activity).inflate(R.layout.common_core_dialog_location, null));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout llBaidu = dialog.findViewById(R.id.llBaidu);
        LinearLayout llGaode = dialog.findViewById(R.id.llGaode);

        EffectUtil.addClickEffect(llBaidu);
        llBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBaiduMap(activity, config);
                dialog.dismiss();
            }
        });
        EffectUtil.addClickEffect(llGaode);
        llGaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGaodeMap(activity, config);
                dialog.dismiss();
            }
        });

        dialog.show(activity);
    }

    /**
     * 跳转百度地图
     */
    public static void goToBaiduMap(Context context, LocationConfig config) {
        if (config == null) {
            Log.e(CoreLogTag.TAG, "LocationUtil;goToBaiduMap;config null");
            return;
        }
        if (!AppUtil.isInstalled("com.baidu.BaiduMap")) {
            ToastUtil.toast(R.string.common_core_no_baidu);
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/marker?location="
                    + config.latitude + ","
                    + config.longitude
                    + "&title=" + URLEncoder.encode(config.name, "utf-8")
                    + "&coord_type=" + "gcj02"));
            context.startActivity(intent); // 启动调用
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转高德地图
     */
    public static void goToGaodeMap(Context context, LocationConfig config) {
        if (config == null) {
            Log.e(CoreLogTag.TAG, "LocationUtil;goToGaodeMap;config null");
            return;
        }
        if (!AppUtil.isInstalled("com.autonavi.minimap")) {
            ToastUtil.toast(R.string.common_core_no_gaode);
            return;
        }
        try {
            StringBuffer stringBuffer = null;
            stringBuffer = new StringBuffer("androidamap://viewMap?sourceApplication=")
                    .append(URLEncoder.encode(AppUtil.getAppName(), "utf-8"));
            stringBuffer.append("&poiname=").append(URLEncoder.encode(config.name, "utf-8"))
                    .append("&lat=").append(config.latitude)
                    .append("&lon=").append(config.longitude)
                    .append("&dev=").append(0);
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


}
