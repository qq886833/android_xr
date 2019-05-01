package com.bsoft.commonlib.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;


/**
 * Created by chenkai on 2018/8/9.
 */

public class NoticeUtil {

    /**
     * 判断通知是否打开
     * @param context
     * @return
     */
    public static boolean isNoticeOpen(@NonNull Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            return manager.areNotificationsEnabled();
        } else {
            return true;
        }
    }

    /**
     * 启动通知设置
     * @param context
     */
    public static void openNoticeSet(@NonNull Context context){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
