package com.bsoft.commonlib.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by Administrator on 2018/4/18.
 */

public class NotificationUtil {
    
    public static String buildChannel(Context baseContext){
        String channelId = "my_channel_01";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = AppUtil.getAppName();
            String description = AppUtil.getAppName();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            ((NotificationManager) baseContext
                    .getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(mChannel);
        }
        return channelId;
    }
}
