package com.bsoft.baselib.base.webinterface;


import com.bsoft.baselib.core.CoreVo;

public class InterfacePost extends CoreVo {

    private String Mode;
    private String param;


    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }
}
