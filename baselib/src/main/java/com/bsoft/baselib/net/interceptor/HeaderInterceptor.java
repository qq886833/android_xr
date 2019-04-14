package com.bsoft.baselib.net.interceptor;



import com.bsoft.baselib.R;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.net.util.HeadUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kai.chen on 2017/6/24.
 * 统一添加head
 */

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        Headers headers = request.headers();
        if (headers == null || (!headers.names().contains("B-Product-Code"))) {
            requestBuilder.addHeader("B-Product-Code", CoreAppInit.getApplication().getString(R.string.base_product_name));
        }
        requestBuilder.addHeader("User-Agent", HeadUtil.getUserAgent());

        request = requestBuilder.build();
        //********************Response*************************
        return chain.proceed(request);
    }


}
