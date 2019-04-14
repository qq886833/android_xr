package com.bsoft.commonlib.init;

import android.os.Bundle;

public interface InitListener {
    /**
     * 需要登录时回调
     *
     * @param path  需要跳转的路由地址
     * @param param 参数
     */
    void needLogin(String path, Bundle param);

    /**
     * token失效
     */
    void tokenError();

    /**
     * 未完善信息
     */
    void userInfoError(String path, Bundle param);
}
