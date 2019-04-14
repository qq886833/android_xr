package com.bsoft.baselib.net.post;
import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.net.base.BaseObserver;
import com.bsoft.baselib.net.base.BaseObserver2;
import com.bsoft.baselib.net.base.CoreResponse;
import com.bsoft.baselib.net.beans.NullResponse;
import com.trello.rxlifecycle2.LifecycleProvider;


import java.util.ArrayList;

import androidx.collection.ArrayMap;

public class PostManager {

    private static class NetPostUtilHolder {
        private static final NetPostUtil INSTANCE = new NetPostUtil(CoreAppInit.getApplication(),
                CoreConstant.httpApiUrl);
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
    public static <T> void post(LifecycleProvider lifecycleProvider, String url,
                                ArrayMap<String, String> heads, Object body,
                                final Class<T> clazz, BaseObserver<T> observer) {

        getInstance().post(lifecycleProvider, url, heads, body, clazz, observer);
    }

    //********************************  post 2**********************************
    public static <T extends CoreResponse> void post(LifecycleProvider lifecycleProvider, String url,
                                                     ArrayMap<String, String> heads, Object body,
                                                     final Class<T> clazz, BaseObserver2<T> observer) {
        getInstance().post(lifecycleProvider, url, heads, body, clazz, observer);
    }


    //******************************* postList *********************************************
    public static <T> void postList(LifecycleProvider lifecycleProvider, String url,
                                    ArrayMap<String, String> heads, Object body,
                                    final Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        getInstance().postList(lifecycleProvider, url, heads, body, clazz, observer);
    }
}
