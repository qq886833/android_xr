package com.bsoft.paylib.event;

import com.tencent.mm.opensdk.modelbase.BaseResp;

import org.greenrobot.eventbus.EventBus;

public class EventUtil {
    public static void post(BaseResp event) {
        EventBus.getDefault().post(event);
    }
}
