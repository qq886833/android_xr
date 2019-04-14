package com.bsoft.commonlib.shapref;

import android.content.Context;
import android.text.TextUtils;

import com.bsoft.baselib.core.CoreAppInit;

import androidx.annotation.NonNull;


/**
 * Created by kai.chen on 2017/7/3.
 * one SHARED_NAME,one manager
 */

public class AccountSharpref {
    /*Default*/
    private static final int SHARED_MODE = Context.MODE_PRIVATE;
    private static final String SHARED_NAME = "share_account";

    //key
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_USERID = "user_id";
    //用户信息是否完善
    private static final String KEY_USER_INFO_PERFECT = "user_info_perfect";

    /*Util*/
    private Context context;
    /*Flag*/
    private volatile static AccountSharpref instance;

    public static AccountSharpref getInstance() {
        if (instance == null) {
            synchronized (AccountSharpref.class) {
                if (instance == null) {
                    instance = new AccountSharpref(CoreAppInit.getApplication());
                }
            }
        }
        return instance;
    }

    private AccountSharpref(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    //********************* Token *******************

    /**
     * @return
     */
    public String getToken() {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getString(KEY_TOKEN, "");
        }
    }

    public boolean setToken(String token) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putString(KEY_TOKEN, token)
                    .commit();
        }
    }

    public boolean isHaveToken() {
        String token = getToken();
        return !TextUtils.isEmpty(token);
    }

    //************************ userId **********************

    /**
     * @return
     */
    public String getUserId() {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getString(KEY_USERID, "");
        }
    }

    public boolean setUserId(String useId) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putString(KEY_USERID, useId)
                    .commit();
        }
    }


    //************************ Account **********************

    /**
     * @return
     */
    public String getAccount() {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getString(KEY_ACCOUNT, "");
        }
    }

    public boolean setAccount(String account) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putString(KEY_ACCOUNT, account)
                    .commit();
        }
    }


    //************************ 用户信息是否完善 **********************

    /**
     * @return
     */
    public boolean isUserInfoPerfect() {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .getBoolean(KEY_USER_INFO_PERFECT, false);
        }
    }

    public boolean setUserInfoPerfect(boolean perfect) {
        synchronized (this) {
            return context
                    .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                    .edit()
                    .putBoolean(KEY_USER_INFO_PERFECT, perfect)
                    .commit();
        }
    }
}
