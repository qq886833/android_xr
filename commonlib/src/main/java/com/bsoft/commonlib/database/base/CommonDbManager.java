package com.bsoft.commonlib.database.base;

import com.bsoft.baselib.core.CoreAppInit;

/**
 * Created by chenkai on 2018/6/5.
 */

public class CommonDbManager {
    /*Default*/
    private static final String DATABASE_NAME = "yjhealthcommon.db";

    private static class DatabaseUtilHolder {
        private static final CommonDbCreateHelper INSTANCE = new CommonDbCreateHelper(CoreAppInit.getApplication(),
                DATABASE_NAME);
    }

    public static CommonDbCreateHelper getInstance() {
        return CommonDbManager.DatabaseUtilHolder.INSTANCE;
    }
}
