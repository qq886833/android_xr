package com.bsoft.databaselib.base;

import android.content.Context;
import android.util.Log;

import com.bsoft.baselib.log.CoreLogTag;
import com.bsoft.databaselib.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by chenkai on 2018/3/13.
 */

public class CoreDbUpdateHelper extends DaoMaster.OpenHelper {
    private String name;

    public CoreDbUpdateHelper(Context context, String name) {
        super(context, name);
        this.name = name;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d(CoreLogTag.TAG, "CoreDbUpdateHelper;onUpgrade;db=" + name + ";oldVersion=" + oldVersion
                + ";newVersion=" + newVersion);
        CoreMigrationUtil.getInstance().migrate(db);
    }
}
