package com.bsoft.baselib.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.bsoft.baselib.core.CoreAppInit;

import androidx.annotation.StringRes;


/**
 * Created by chenkai on 2018/6/5.
 */

public class ToastUtil {
    public static void toast(String msg) {
        if (!TextUtils.isEmpty(msg)){
            Toast.makeText(CoreAppInit.getApplication(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void toast(@StringRes int resId) {
        Toast.makeText(CoreAppInit.getApplication(), CoreAppInit.getApplication().getText(resId),
                Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(@StringRes int resId) {
        Toast.makeText(CoreAppInit.getApplication(), CoreAppInit.getApplication().getText(resId),
                Toast.LENGTH_LONG).show();
    }
}
