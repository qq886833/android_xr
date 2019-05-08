package com.bsoft.commonlib.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.bsoft.baselib.core.CoreAppInit;


public class ScreenUtil {
    // 分辨率-宽带
    private static int widthPixels;
    private static int heightPixels;
    /**
     * 获取分辨率的宽度
     *
     * @return
     */
    public static int getWidthPixels() {
        if (0 == widthPixels) {
            
            WindowManager localWindowManager = (WindowManager) CoreAppInit.getApplication()
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            localWindowManager.getDefaultDisplay().getMetrics(
                    localDisplayMetrics);
            widthPixels = localDisplayMetrics.widthPixels;
            heightPixels = localDisplayMetrics.heightPixels;
        }
        return widthPixels;
    }

    /**
     * 获取分辨率的高度
     *
     * @return
     */
    public static int getHeightPixels() {
        if (0 == heightPixels) {
            WindowManager localWindowManager = (WindowManager) CoreAppInit.getApplication()
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            localWindowManager.getDefaultDisplay().getMetrics(
                    localDisplayMetrics);
            widthPixels = localDisplayMetrics.widthPixels;
            heightPixels = localDisplayMetrics.heightPixels;
        }
        return heightPixels;
    }
}
