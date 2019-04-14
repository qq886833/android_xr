package com.bsoft.componentXR;

import android.app.Application;
import android.os.Bundle;

import com.bsoft.baselib.util.ToastUtil;
import com.bsoft.commonlib.init.BaseAppInit;
import com.bsoft.commonlib.init.BaseInitConfig;
import com.bsoft.commonlib.init.InitListener;


/**
 * Created by chenkai on 2018/6/1.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseAppInit.getInstance().init(this, getConfig(), new InitListener() {

            @Override
            public void needLogin(String path, Bundle param) {
                //TODO 需要登录
                ToastUtil.toast(R.string.common_not_login);
            }

            @Override
            public void tokenError() {
                ToastUtil.toast(R.string.common_account_error);
                //TODO token失效
            }

            @Override
            public void userInfoError(String path, Bundle param) {
                //TODO 未完善信息
                ToastUtil.toast(R.string.common_account_not_perfect);
            }
        });
    }

    private BaseInitConfig getConfig() {
        BaseInitConfig config = new BaseInitConfig();
        config.setEnvironment(BuildConfig.environment);
        config.setInternalVersion(BuildConfig.internalversion);
        config.setVersionCode(BuildConfig.VERSION_CODE);
        config.setVersionName(BuildConfig.VERSION_NAME);
        config.setDebug(BuildConfig.DEBUG);
        return config;
    }
}
