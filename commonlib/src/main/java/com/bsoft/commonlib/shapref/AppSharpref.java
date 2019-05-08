package com.bsoft.commonlib.shapref;

import android.content.Context;

import com.bsoft.baselib.core.CoreAppInit;

import androidx.annotation.NonNull;


public class AppSharpref extends BaseSharpref{
    private static final String SHARED_NAME = "share_app";

    public static final String SHOW_GUIDE = "show_guide";
    public static final String ROOT_TRUST_STATE = "root_trust_state";
    
    /*Flag*/
    private volatile static AppSharpref instance;

    public static AppSharpref getInstance() {
        if (instance == null) {
            synchronized (AppSharpref.class) {
                if (instance == null) {
                    instance = new AppSharpref(CoreAppInit.getApplication(), SHARED_NAME);
                }
            }
        }
        return instance;
    }

    private AppSharpref(@NonNull Context context, String name) {
        super(context, name);
    }

    public boolean setShowGuide(boolean flag) {
        synchronized (this) {
            return setBoolean(flag, SHOW_GUIDE);
        }
    }

    public boolean isShowGuide() {
        synchronized (this) {
            return getBoolean(SHOW_GUIDE, true);
        }
    }
    public boolean setRootTrust(boolean flag) {
        synchronized (this) {
            return setBoolean(flag, ROOT_TRUST_STATE);
        }
    }

    public boolean isRootTrust() {
        synchronized (this) {
            return getBoolean(ROOT_TRUST_STATE);
        }
    }


}
