package com.bsoft.commonlib.web.config;


import com.bsoft.baselib.core.CoreVo;

public class OpenWebViewConfig extends CoreVo {
    //路由path
    private String path;
    //参数
    private String params;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
