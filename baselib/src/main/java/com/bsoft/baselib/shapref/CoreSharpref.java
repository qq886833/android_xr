package com.bsoft.baselib.shapref;

import android.content.Context;

import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;

import androidx.annotation.NonNull;


/**
 * Created by kai.chen on 2017/7/3.
 * one SHARED_NAME,one manager
 */

public class CoreSharpref {
    /*Default*/
    private static final int SHARED_MODE = Context.MODE_PRIVATE;
    private static final String SHARED_NAME = "hospat_share_core";

    //debug
    private static final String KEY_DEBUG = "debug";
    //network
    public static final String KEY_HTTP_ENVIROMENT = "http_enviroment";
    public static final String KEY_HTTP_API_URL = "http_api_url";

    /*Util*/
    private Context context;
    /*Flag*/
    private volatile static CoreSharpref instance;

    public static CoreSharpref getInstance() {
        if (instance == null) {
            synchronized (CoreSharpref.class) {
                if (instance == null) {
                    instance = new CoreSharpref(CoreAppInit.getApplication());
                }
            }
        }
        return instance;
    }

    private CoreSharpref(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * @return
     */
    public boolean getDebug() {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getBoolean(KEY_DEBUG, CoreConstant.isDebug);
        }
    }

    public boolean setDebug(boolean debug) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putBoolean(KEY_DEBUG, debug)
                    .commit();
        }
    }

    /**
     * 获取当前网络环境
     * @return
     */
    public String getNetEnviroment(String def) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getString(KEY_HTTP_ENVIROMENT, def);
        }
    }

    /**
     * 设置当前网络环境
     * @param enviroment
     * @return
     */
    public boolean setNetEnviroment(String enviroment) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putString(KEY_HTTP_ENVIROMENT, enviroment)
                    .commit();
        }
    }

    /**
     * 获取手动网络url
     * @return
     */
    public String getNetApiUrl(String def) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getString(KEY_HTTP_API_URL, def);
        }
    }

    /**
     * 设置手动网络url
     * @param url
     * @return
     */
    public boolean setNetApiUrl(String url) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putString(KEY_HTTP_API_URL, url)
                    .commit();
        }
    }
}
