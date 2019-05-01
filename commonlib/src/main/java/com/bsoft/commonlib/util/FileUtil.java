package com.bsoft.commonlib.util;

import android.content.Context;


import java.io.File;

import androidx.annotation.NonNull;

/**
 * Created by chenkai on 2018/6/26.
 */

public class FileUtil {
    public static String storeDir, storeImageUrl;

    /**
     * 获取存储空间地址
     *
     * @return
     */
    public static String getStoreDir(@NonNull Context context) {
        if (null == storeDir) {
//            File f = Environment.getExternalStorageDirectory();
            File f = context.getExternalFilesDir("");
//            File f = this.getExternalCacheDir();
            if (null != f) {
                StringBuffer path = new StringBuffer();
                path.append(f.getPath()).append(File.separator).append("hcn").append(File.separator);
                File dir = new File(path.toString());
                if (!dir.exists()) {
                    dir.mkdir();
                }
                storeDir = path.toString();
            }
        }
        return storeDir;
    }

    public static String getCacheDir(@NonNull Context context, String dirStr) {
        File f = context.getExternalCacheDir();
        if (null != f) {
            StringBuffer path = new StringBuffer();
            path.append(f.getPath()).append(File.separator).append("hcn")
                    .append(File.separator).append(dirStr);
            File dir = new File(path.toString());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return path.toString();
        }
        return null;
    }

    /**
     * 获取图片保存目录
     *
     * @return
     */
    public String getStoreImageDir(@NonNull Context context) {
        if (null == storeImageUrl) {
            if (null == getStoreDir(context)) {
                return null;
            } else {
                storeImageUrl = new StringBuffer(getStoreDir(context)).append(
                        "image").append(File.separator).toString();
            }
            File dir = new File(storeImageUrl);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return storeImageUrl;
    }
}
