package com.bsoft.commonlib.util.image;


import com.bsoft.baselib.util.image.GlideUtil2;
import com.bsoft.commonlib.net.NetConstants;

public class WiseCommonGlideUtil extends GlideUtil2 {

    public static class Holder {
        private static final WiseCommonGlideUtil INSTANCE = new WiseCommonGlideUtil();
    }

    public static WiseCommonGlideUtil getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    protected String getImgUrl() {
        return NetConstants.httpImgUrl;
    }
}
