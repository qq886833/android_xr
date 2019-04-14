package com.bsoft.baselib.net.get;

import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.net.base.BaseObserver;
import com.bsoft.baselib.net.base.BaseObserver2;
import com.bsoft.baselib.net.base.CoreResponse;
import com.bsoft.baselib.net.beans.NullResponse;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.ArrayList;
import java.util.Map;

public class GetManager {
    private static class NetGetUtilHolder {
        private static final NetGetUtil INSTANCE = new NetGetUtil(CoreAppInit.getApplication(), CoreConstant.httpApiUrl);
    }

    public static NetGetUtil getInstance() {
        return GetManager.NetGetUtilHolder.INSTANCE;
    }

    //********************************  get return null**********************************
    public static void get(LifecycleProvider lifecycleProvider, String url, Map<String, String> heads, Map<String, Object> params,
                           BaseObserver<NullResponse> observer) {
        getInstance().get(lifecycleProvider, url, heads, params, observer);
    }

    //********************************  get **********************************
    public static <T> void get(LifecycleProvider lifecycleProvider, String url,
                               Map<String, String> heads, Map<String, Object> params,
                               final Class<T> clazz, BaseObserver<T> observer) {
        getInstance().get(lifecycleProvider, url, heads, params, clazz, observer);
    }

    //********************************  get 2 **********************************
    public static <T extends CoreResponse> void get(LifecycleProvider lifecycleProvider, String url,
                                                    Map<String, String> heads, Map<String, Object> params,
                                                    final Class<T> clazz, BaseObserver2<T> observer) {
        getInstance().get(lifecycleProvider, url, heads, params, clazz, observer);
    }

    //******************************* getList *********************************************
    public static <T> void getList(LifecycleProvider lifecycleProvider, String url,
                                   Map<String, String> heads, Map<String, Object> params,
                                   final Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        getInstance().getList(lifecycleProvider, url, heads, params, clazz, observer);
    }
}
