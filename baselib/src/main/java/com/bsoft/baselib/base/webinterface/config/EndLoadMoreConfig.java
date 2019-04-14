package com.bsoft.baselib.base.webinterface.config;


import com.bsoft.baselib.core.CoreVo;

public class EndLoadMoreConfig extends CoreVo {
    public static final int INFINISHED = 0;
    public static final int FINISHED = 1;

    private int finished = INFINISHED;

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
