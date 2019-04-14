package com.bsoft.commonlib.arouter;

import android.os.Bundle;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.commonlib.R;


/**
 * Created by chenkai on 2018/6/28.
 */

public class CommonArouterGroup {
    public static final String PATH = "path";
    public static final String PARAM = "param";
    public static final String CALLBACK = "callback";
    public static String AROUTER_NAME = CoreAppInit.getApplication().getString(R.string.common_arouter_name);
    //TODO 根据项目修改
    public static final int PRIORITY = 111;
    private static final String PROJECT = "/yjhealthcore_";


    public static final String MAIN_TAB_ACTIVITY = PROJECT + "app/home/mainTabActivity";

    //******************** commonlib **********************
    public static final String CHANGE_NET_ACTIVITY = PROJECT + "commonlib/changenet/ChangeNetActivity";
    public static final String COMMON_WEB_ACTIVITY = PROJECT + "commonlib/web/CommonWebActivity";
    public static final String COMMON_WEB_FRAGMENT = PROJECT + "commonlib/web/CommonWebFragment";

    //******************** businessDemo *******************
    public static final String DEMO_TEST_ACTIVITY = PROJECT + "businessdemo/business/DemoTestActivity";

    public static Postcard getArouter(String path) {
        Postcard postcard = ARouter.getInstance()
                .build(path);
        postcard.setName(AROUTER_NAME);
        return postcard;
    }

    /**
     * go to Activity
     */
    public static void gotoActivity(String path) {
        getArouter(path).navigation();
    }

    /**
     * go to Activity
     */
    public static void gotoActivity(String path, Bundle bundle) {
        Postcard postcard = getArouter(path)
                .with(bundle);
        postcard.navigation();
    }
}
