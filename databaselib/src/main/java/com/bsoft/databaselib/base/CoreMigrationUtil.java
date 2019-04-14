package com.bsoft.databaselib.base;

import com.bsoft.databaselib.core.MigrationCoreUtil;
import com.bsoft.databaselib.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by chenkai on 2018/3/13.
 */

public class CoreMigrationUtil extends MigrationCoreUtil {
    private static class MigrationHelperHolder {
        private static final CoreMigrationUtil INSTANCE = new CoreMigrationUtil();
    }

    public static CoreMigrationUtil getInstance() {
        return CoreMigrationUtil.MigrationHelperHolder.INSTANCE;
    }

    public void migrate(Database db) {
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, false);
    }

}
