package com.bsoft.commonlib.net;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.net.base.BaseObserver;
import com.bsoft.baselib.net.base.BaseObserver2;
import com.bsoft.baselib.net.base.CoreResponse;
import com.bsoft.baselib.net.beans.NullResponse;
import com.bsoft.baselib.net.post.NetPostUtil;
import com.trello.rxlifecycle2.LifecycleProvider;


import java.util.ArrayList;

import androidx.collection.ArrayMap;

public class CommonPostManager {
    private static class NetPostUtilHolder {
        private static final NetPostUtil INSTANCE = new NetPostUtil(CoreAppInit.getApplication(),
                NetConstants.httpApiUrl,new HeaderInterceptor());
    }

    public static NetPostUtil getInstance() {
        return NetPostUtilHolder.INSTANCE;
    }

    //********************************  post return null**********************************
    public static void post(LifecycleProvider provider, String url, ArrayMap<String, String> heads, Object body,
                            BaseObserver<NullResponse> observer) {
        getInstance().post(provider, url, heads, body, observer);
    }

    //********************************  post **********************************
    public static <T> void post(LifecycleProvider provider, String url,
                                ArrayMap<String, String> heads, Object body,
                                Class<T> clazz, BaseObserver<T> observer) {
        getInstance().post(provider, url, heads, body, clazz, observer);
    }

    public static <T> void post(LifecycleProvider provider, ArrayMap<String, String> heads, Object body,
                                Class<T> clazz, BaseObserver<T> observer) {
        getInstance().post(provider, "*.jsonRequest", heads, body, clazz, observer);
    }

    //********************************  post 2**********************************
    public static <T extends CoreResponse> void post(LifecycleProvider provider, String url,
                                                     ArrayMap<String, String> heads, Object body,
                                                     Class<T> clazz, BaseObserver2<T> observer) {
        getInstance().post(provider, url, heads, body, clazz, observer);
    }

    //******************************* postList *********************************************
    public static <T> void postList(LifecycleProvider provider, String url,
                                    ArrayMap<String, String> heads, Object body,
                                    Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        getInstance().postList(provider, url, heads, body, clazz, observer);
    }

    public static <T> void postList(LifecycleProvider provider, ArrayMap<String, String> heads, Object body,
                                    Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        getInstance().postList(provider, "*.jsonRequest", heads, body, clazz, observer);
    }

}
