package com.bsoft.commonlib.util;

import android.content.Context;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

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

    /**
     * 创建保存的文件夹
     */
    public static void createDirDirectory(String downloadPath) {
        File dirDirectory = new File(downloadPath);
        if (!dirDirectory.exists()) {
            dirDirectory.mkdirs();
        }
    }

    /**
     * 创建一个随机读写
     */
    public static RandomAccessFile createRAFile(String downloadPath, String fileName) {
        //断点读写
        try {
            return new RandomAccessFile(createFile(downloadPath, fileName), "rwd");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建一个文件
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return 文件
     */
    public static File createFile(String downloadPath, String fileName) {
        return new File(downloadPath, fileName);
    }

    /**
     * 查看一个文件是否存在
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return true | false
     */
    public static boolean fileExists(String downloadPath, String fileName) {
        return new File(downloadPath, fileName).exists();
    }

    /**
     * 删除一个文件
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return true | false
     */
    public static boolean delete(String downloadPath, String fileName) {
        return new File(downloadPath, fileName).delete();
    }
}
