package com.bsoft.commonlib.web;

public interface CommonWebInterface{
    void pushNativeRoute(String json);

    void notifyTokenInvalid();

    void openWebView(String json);
}
