package com.bsoft.databaselib.base;


import com.bsoft.baselib.core.CoreAppInit;

/**
 * Created by chenkai on 2018/6/5.
 */

public class CoreDbManager {
    /*Default*/
    private static final String DATABASE_NAME = "core.db";

    private static class DatabaseUtilHolder {
        private static final CoreDbCreateHelper INSTANCE = new CoreDbCreateHelper(CoreAppInit.getApplication(),
                DATABASE_NAME);
    }

    public static CoreDbCreateHelper getInstance() {
        return CoreDbManager.DatabaseUtilHolder.INSTANCE;
    }
}
