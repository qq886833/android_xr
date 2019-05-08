package com.bsoft.commonlib.net;
import android.content.Context;

import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.net.base.BaseObserver;
import com.bsoft.baselib.net.base.BaseObserver2;
import com.bsoft.baselib.net.base.CoreResponse;
import com.bsoft.baselib.net.beans.NullResponse;
import com.bsoft.baselib.net.post.NetPostUtil;
import com.trello.rxlifecycle2.LifecycleProvider;


import java.util.ArrayList;

import androidx.collection.ArrayMap;
import okhttp3.Interceptor;

public class CommonPostManager extends NetPostUtil {

    private static class Holder {
        private static final CommonPostManager INSTANCE = new CommonPostManager(CoreAppInit.getApplication(),
                NetConstants.httpApiUrl, new HeaderInterceptor());
    }

    public static CommonPostManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 构造方法
     *
     * @param context      context
     * @param baseUrl      baseUrl
     * @param interceptors
     */
    public CommonPostManager(Context context, String baseUrl, Interceptor... interceptors) {
        super(context, baseUrl, interceptors);
    }

    public <T> void post(LifecycleProvider provider, ArrayMap<String, String> heads, Object body,
                         Class<T> clazz, BaseObserver<T> observer) {
        post(provider, "*.jsonRequest", heads, body, clazz, observer);
    }

    public <T> void postList(LifecycleProvider provider, ArrayMap<String, String> heads, Object body,
                             Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        postList(provider, "*.jsonRequest", heads, body, clazz, observer);
    }


}
