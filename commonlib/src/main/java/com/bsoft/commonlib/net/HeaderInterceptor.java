package com.bsoft.commonlib.net;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.bsoft.baselib.net.util.HeadUtil;
import com.bsoft.commonlib.init.BaseAppInit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kai.chen on 2017/6/24.
 * 统一添加head
 */

public class HeaderInterceptor implements Interceptor {
    private ObservableEmitter<String> tokenErrorEmitter;

    @SuppressLint("CheckResult")
    public HeaderInterceptor() {
        //防止多网络请求连续发送
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                HeaderInterceptor.this.tokenErrorEmitter = emitter;
            }
        }).debounce(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String msg) throws Exception {
                        if (BaseAppInit.getInstance().getListener() != null) {
                            BaseAppInit.getInstance().getListener().tokenError();
                        }
                    }
                });
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        Headers headers = request.headers();
        if (headers != null) {
            String digest = HeadUtil.getSignature(request);
            if (!TextUtils.isEmpty(digest)) {
                requestBuilder.addHeader("X-Signature", digest);
            }
        }

        request = requestBuilder.build();
        //********************Response*************************
        Response response = chain.proceed(request);

        //token失效
        if (response.header("X-Auth-Failed-Code") != null) {
            tokenErrorEmitter.onNext("X-Auth-Failed-Code");
        }
        return response;
    }


}
