package com.bsoft.commonlib.web.config;


import com.bsoft.baselib.core.CoreVo;

public class NativeRouteConfig extends CoreVo {
    //路由name
    private String name;
    //参数
    private String params;
    //回调JS的事件名
    private String callback;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
