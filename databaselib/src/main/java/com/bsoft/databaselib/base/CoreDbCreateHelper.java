package com.bsoft.databaselib.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bsoft.databaselib.entity.DaoMaster;
import com.bsoft.databaselib.entity.DaoSession;

import androidx.annotation.NonNull;


/**
 * Created by chenkai on 2018/6/5.
 */

public class CoreDbCreateHelper {
    /*Default*/
    /*Util*/
    private CoreDbUpdateHelper dbOpenHelper;
    private SQLiteDatabase database;
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    public CoreDbCreateHelper(@NonNull Context context, @NonNull String databaseName) {
        dbOpenHelper = new CoreDbUpdateHelper(context, databaseName);
        database = dbOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public CoreDbUpdateHelper getDbOpenHelper() {
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
