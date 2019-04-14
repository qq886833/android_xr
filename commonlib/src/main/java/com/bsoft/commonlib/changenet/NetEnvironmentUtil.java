package com.bsoft.commonlib.changenet;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.shapref.CoreSharpref;
import com.bsoft.commonlib.net.NetConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by 83990 on 2018/2/8.
 */

public class NetEnvironmentUtil {

    /**
     * 初始化网络环境
     *
     * @param context
     * @return
     */
    public static boolean initConstans(Context context) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }

        NetAddressVo vo = getCurEnvironment(context);
        if (vo != null) {
            initCore(vo);
            NetConstants.httpApiUrl = vo.getHttpApiUrl();
            NetConstants.httpDownloadUrl = vo.getHttpDownloadUrl();
            NetConstants.httpImgUrl = vo.getHttpImgUrl();
            return true;
        } else {
            return false;
        }
    }

    private static void initCore(NetAddressVo vo) {
        if (TextUtils.isEmpty(CoreConstant.httpApiUrl)) {
            CoreConstant.httpApiUrl = vo.getHttpApiUrl();
        }
        if (TextUtils.isEmpty(CoreConstant.httpDownloadUrl)) {
            CoreConstant.httpDownloadUrl = vo.getHttpDownloadUrl();
        }
        if (TextUtils.isEmpty(CoreConstant.httpImgUrl)) {
            CoreConstant.httpImgUrl = vo.getHttpImgUrl();
        }
    }


    /**
     * 获取网络环境参数List
     *
     * @param context
     * @return
     */
    public static ArrayList<NetAddressVo> getNetEnvironments(Context context) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open("yjhealth_netConfigs");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer stringBuffer = new StringBuffer();
            String str = null;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }

            if (!TextUtils.isEmpty(stringBuffer)) {
                Gson gson = new Gson();
                ArrayList<NetAddressVo> netBeans = gson.fromJson(stringBuffer.toString()
                        , new TypeToken<ArrayList<NetAddressVo>>() {
                        }.getType());
                if (netBeans != null && !netBeans.isEmpty()) {
                    for (NetAddressVo vo : netBeans) {
                        if (isManual(vo.getEnvironment())) {
                            vo.setHttpApiUrl(CoreSharpref.getInstance().getNetApiUrl(vo.getHttpApiUrl()));
                        }
                    }
                    return netBeans;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取当前网络环境参数
     *
     * @param context
     * @return
     */
    public static NetAddressVo getCurEnvironment(Context context) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }
        String enviroment = CoreConstant.ENVIRONMENT;
        if (CoreConstant.isDebug) {
            enviroment = CoreSharpref.getInstance().getNetEnviroment(CoreConstant.ENVIRONMENT);
        }

        ArrayList<NetAddressVo> vos = getNetEnvironments(context);
        if (vos != null) {
            for (NetAddressVo vo : vos) {
                if (TextUtils.equals(vo.getEnvironment(), enviroment)) {
                    if (isManual(enviroment)) {
                        vo.setHttpApiUrl(CoreSharpref.getInstance().getNetApiUrl(vo.getHttpApiUrl()));
                    }
                    return vo;
                }
            }
        }

        return null;
    }

    /**
     * 设置当前网络环境
     *
     * @param context
     * @param enviroment
     * @return
     */
    public static boolean setEnvironment(Context context, String enviroment) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }

        if (TextUtils.isEmpty(enviroment)) {
            return false;
        }

        ArrayList<NetAddressVo> vos = getNetEnvironments(context);
        if (vos != null) {
            for (NetAddressVo vo : vos) {
                if (TextUtils.equals(vo.getEnvironment(), enviroment)) {
                    CoreSharpref.getInstance().setNetEnviroment(enviroment);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 设置手动的HttpApiUrl
     *
     * @param context
     * @param url
     * @return
     */
    public static boolean setManualHttpUrl(Context context, String url) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }

        if (TextUtils.isEmpty(url)) {
            return false;
        }
        CoreSharpref.getInstance().setNetApiUrl(url);
        return true;
    }

    /**
     * 判断是否是手动模式
     *
     * @param enviromnet
     * @return
     */
    public static boolean isManual(String enviromnet) {
        if (TextUtils.equals(enviromnet, "shoudong")) {
            return true;
        } else {
            return false;
        }
    }
}
