package com.bsoft.baselib.util.image;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/6/22.
 */

public class ImageUrlUtil {
    public static String getUrl(String url, String id) {
        if (TextUtils.isEmpty(id) || "0".equals(id)) {
            return url + id;
        }
        return null;
    }
}
