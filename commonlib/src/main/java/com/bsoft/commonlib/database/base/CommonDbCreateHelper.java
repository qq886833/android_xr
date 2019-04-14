package com.bsoft.commonlib.database.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bsoft.commonlib.database.entity.DaoMaster;
import com.bsoft.commonlib.database.entity.DaoSession;

import androidx.annotation.NonNull;


/**
 * Created by chenkai on 2018/6/5.
 */

public class CommonDbCreateHelper {
    /*Default*/
    /*Util*/
    private CommonDbUpdateHelper dbOpenHelper;
    private SQLiteDatabase database;
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    public CommonDbCreateHelper(@NonNull Context context, @NonNull String databaseName) {
        dbOpenHelper = new CommonDbUpdateHelper(context, databaseName);
        database = dbOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public CommonDbUpdateHelper getDbOpenHelper() {
        return dbOpenHelper;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}
