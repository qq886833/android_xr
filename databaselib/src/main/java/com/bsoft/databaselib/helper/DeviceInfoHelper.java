package com.bsoft.databaselib.helper;


import com.bsoft.databaselib.base.CoreDbManager;
import com.bsoft.databaselib.entity.DaoSession;
import com.bsoft.databaselib.entity.DeviceInfo;
import com.bsoft.databaselib.entity.DeviceInfoDao;

import java.util.List;

/**
 * Created by chenkai on 2018/3/9.
 */

public class DeviceInfoHelper {

    /**
     * 获取设备信息
     *
     * @return
     */
    public static DeviceInfo getInstallInfo() {
        DeviceInfoDao dao = CoreDbManager.getInstance().getDaoSession().getDeviceInfoDao();
        List<DeviceInfo> infos = dao.queryBuilder().list();

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
    public static long insert(DeviceInfo info) {
        DaoSession daoSession = CoreDbManager.getInstance().getDaoSession();
        daoSession.deleteAll(DeviceInfo.class);
        return daoSession.insertOrReplace(info);
    }

}
