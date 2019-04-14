package com.bsoft.commonlib.database.base;


import com.bsoft.commonlib.database.entity.DaoMaster;
import com.bsoft.databaselib.core.MigrationCoreUtil;

import org.greenrobot.greendao.database.Database;

/**
 * Created by chenkai on 2018/3/13.
 */

public class CommonMigrationUtil extends MigrationCoreUtil {
    private static class MigrationHelperHolder {
        private static final CommonMigrationUtil INSTANCE = new CommonMigrationUtil();
    }

    public static CommonMigrationUtil getInstance() {
        return CommonMigrationUtil.MigrationHelperHolder.INSTANCE;
    }

    public void migrate(Database db) {
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, false);
    }

}
