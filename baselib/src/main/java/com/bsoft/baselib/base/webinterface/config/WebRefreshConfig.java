package com.bsoft.baselib.base.webinterface.config;


import com.bsoft.baselib.core.CoreVo;

public class WebRefreshConfig extends CoreVo {
    public static final int DISABLE = 0;
    public static final int ENABLE = 1;

    //是否支持下拉刷新 0-不支持 1-支持
    private int header = DISABLE;
    //是否支持上拉加载更多 0-不支持 1-支持
    private int footer = DISABLE;

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public int getFooter() {
        return footer;
    }

    public void setFooter(int footer) {
        this.footer = footer;
    }
}
