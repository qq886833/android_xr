package com.bsoft.commonlib.database.base;

import android.content.Context;
import android.util.Log;


import com.bsoft.baselib.log.CoreLogTag;
import com.bsoft.commonlib.database.entity.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by chenkai on 2018/3/13.
 */

public class CommonDbUpdateHelper extends DaoMaster.OpenHelper {
    private String name;

    public CommonDbUpdateHelper(Context context, String name) {
        super(context, name);
        this.name = name;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.d(CoreLogTag.TAG, "commonLib;CoreDbUpdateHelper;onUpgrade;db=" + name + ";oldVersion=" + oldVersion
                + ";newVersion=" + newVersion);
        CommonMigrationUtil.getInstance().migrate(db);
    }
}
