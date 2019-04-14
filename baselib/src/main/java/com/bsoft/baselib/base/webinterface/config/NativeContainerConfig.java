package com.bsoft.baselib.base.webinterface.config;


import com.bsoft.baselib.core.CoreVo;
import com.bsoft.baselib.widget.titlebar.config.StatusTitleConfig;

public class NativeContainerConfig extends CoreVo {
    private StatusTitleConfig header;
    private WebRefreshConfig refresh;

    public StatusTitleConfig getHeader() {
        return header;
    }

    public void setHeader(StatusTitleConfig header) {
        this.header = header;
    }

    public WebRefreshConfig getRefresh() {
        return refresh;
    }

    public void setRefresh(WebRefreshConfig refresh) {
        this.refresh = refresh;
    }
}
