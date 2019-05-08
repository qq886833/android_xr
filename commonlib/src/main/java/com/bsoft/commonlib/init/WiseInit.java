package com.bsoft.commonlib.init;

import android.app.Application;

import com.bsoft.commonlib.changenet.NetEnvironmentUtil;


public class WiseInit {
    public static void init(Application application, BaseInitConfig config, InitListener listener) {
        BaseAppInit.getInstance().init(application, config, listener);
        NetEnvironmentUtil.initConstans(application);
    }
}
