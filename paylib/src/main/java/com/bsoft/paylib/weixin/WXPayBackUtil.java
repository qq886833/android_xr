package com.bsoft.paylib.weixin;

import com.bsoft.paylib.event.EventUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

public class WXPayBackUtil {
    public static boolean wxPayBackEnable = false;
    public static void onResp(BaseResp resp) {
        //微信支付
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX
                && wxPayBackEnable) {
            EventUtil.post(resp);
        }
    }
}
