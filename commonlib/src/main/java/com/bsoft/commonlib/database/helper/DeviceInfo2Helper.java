package com.bsoft.commonlib.database.helper;



import com.bsoft.commonlib.database.base.CommonDbManager;
import com.bsoft.commonlib.database.entity.DaoSession;
import com.bsoft.commonlib.database.entity.DeviceInfo2;
import com.bsoft.commonlib.database.entity.DeviceInfo2Dao;

import java.util.List;

/**
 * Created by chenkai on 2018/3/9.
 */

public class DeviceInfo2Helper {

    /**
     * 获取设备信息
     *
     * @return
     */
    public static DeviceInfo2 getInstallInfo() {
        DeviceInfo2Dao dao = CommonDbManager.getInstance().getDaoSession().getDeviceInfo2Dao();
        List<DeviceInfo2> infos = dao.queryBuilder().list();

        if (!infos.isEmpty()) {
            return infos.get(0);
        }

        return null;
    }

    /**
     * 存入设备信息
     *
     * @param info
     * @return
     */
    public static long insert(DeviceInfo2 info) {
        DaoSession daoSession = CommonDbManager.getInstance().getDaoSession();
        daoSession.deleteAll(DeviceInfo2.class);
        return daoSession.insertOrReplace(info);
    }

}
