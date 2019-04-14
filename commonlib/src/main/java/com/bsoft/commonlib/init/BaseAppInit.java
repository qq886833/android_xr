package com.bsoft.commonlib.init;

import android.app.Application;

import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.commonlib.changenet.NetEnvironmentUtil;


/**
 * Created by chenkai on 2018/6/1.
 */

public class BaseAppInit {
    private InitListener listener;

    private static class Holder {
        private static final BaseAppInit INSTANCE = new BaseAppInit();
    }

    public static BaseAppInit getInstance() {
        return BaseAppInit.Holder.INSTANCE;
    }

    public void init(Application app, BaseInitConfig config, InitListener listener) {
        this.listener = listener;
        initConstants(config);
        CoreAppInit.getInstance().init(app);
        NetEnvironmentUtil.initConstans(app);
        ArouterInit.init(app);
    }

    private void initConstants(BaseInitConfig config) {
        if (!CoreAppInit.getInstance().isInit()) {
            CoreConstant.isDebug = config.isDebug();
            CoreConstant.ENVIRONMENT = config.getEnvironment();
            CoreConstant.INTERNALVERSION = config.getInternalVersion();
            CoreConstant.VERSION_CODE = config.getVersionCode();
            CoreConstant.VERSION_NAME = config.getVersionName();
        }
    }

    public InitListener getListener() {
        return listener;
    }

    public void setListener(InitListener listener) {
        this.listener = listener;
    }

}
