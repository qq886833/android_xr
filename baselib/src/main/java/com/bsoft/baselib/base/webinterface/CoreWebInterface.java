package com.bsoft.baselib.base.webinterface;

public interface CoreWebInterface {
    void setNativeContainer(String json);

    void closeWebView();

    void endRefresh();

    void endLoadMoreData(String json);
}
