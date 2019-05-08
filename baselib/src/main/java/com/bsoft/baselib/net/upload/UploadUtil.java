package com.bsoft.baselib.net.upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.bsoft.baselib.R;
import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.net.base.CoreResponse;
import com.bsoft.baselib.net.init.NetConfig;
import com.bsoft.baselib.net.interceptor.HeaderInterceptor;
import com.bsoft.baselib.net.interceptor.NetLogInterceptor;
import com.bsoft.baselib.net.util.SSLTools;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


public class UploadUtil {
    private Retrofit retrofit;

    public UploadUtil(Context context, String baseUrl, Interceptor... interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .writeTimeout(NetConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                //Head
                .addInterceptor(new HeaderInterceptor());

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpClientBuilder.addInterceptor(interceptor);
            }
        }

        //Log
        if (CoreConstant.isDebug && NetConfig.curLogType == NetConfig.LOG_NATIVE) {
            okHttpClientBuilder.addNetworkInterceptor(
                    new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY));
        } else if (CoreConstant.isDebug) {
            okHttpClientBuilder.addInterceptor(new NetLogInterceptor());
        }

        //ssl
        InputStream[] certificates = null;
        if (context.getString(R.string.base_core_net_url_mp).equals(baseUrl)) {
            certificates = new InputStream[]{context.getResources().openRawResource(R.raw.common_core_network_mp)};
        }
        SSLTools.SSLParams sslParams = SSLTools.getSslSocketFactory(certificates, null, null);
        if (sslParams != null) {
            okHttpClientBuilder.sslSocketFactory(sslParams.sslSocketFactory, sslParams.x509TrustManager);
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 用于自定义Api
     *
     * @param api api 接口
     * @param <T> T
     * @return api 实体类
     */
    public <T> T create(Class<T> api) {
        return retrofit.create(api);
    }

    /**
     * @param provider
     * @param url
     * @param heads
     * @param params
     * @param filePaths
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressLint("CheckResult")
    public <T extends CoreResponse> Observable<Object> upload(LifecycleProvider provider, String url,
                                                              ArrayMap<String, String> heads, ArrayMap<String, String> params, ArrayList<String> filePaths, final Class<T> clazz) {
        final Subject<Object> subject = PublishSubject.create();

        ArrayMap<String, RequestBody> requestBodys = new ArrayMap<>();
        if (filePaths != null && !filePaths.isEmpty()) {
            long totalSize = 0;
            Uploading uploading = new Uploading();

            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(i));
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                try {
                    totalSize = totalSize + requestBody.contentLength();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, uploading, subject);
                requestBodys.put("file\"; filename=\"" + file.getName(), progressRequestBody);
            }

            uploading.setTotalSize(totalSize);
        }

        ArrayMap<String, RequestBody> map = new ArrayMap<>();
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    map.put(key, RequestBody.create(MediaType.parse("form-data"), params.get(key)));
                }
            }
        }

        Observable<T> observable = create(UpLoadApi.class)
                .upload(url, heads, map, requestBodys)
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String s) {
                        return JSON.parseObject(s, clazz);
                    }
                })
                .subscribeOn(Schedulers.io());

        if (provider instanceof LifecycleProvider) {
            if (provider instanceof Activity) {
                observable.compose(RxLifecycle.bindUntilEvent(provider.lifecycle(), ActivityEvent.DESTROY));
            } else if (provider instanceof Fragment) {
                observable.compose(RxLifecycle.bindUntilEvent(provider.lifecycle(), FragmentEvent.DESTROY));
            }
        }

        observable.subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                subject.onSubscribe(d);
            }

            @Override
            public void onNext(T value) {
                subject.onNext(value);
            }

            @Override
            public void onError(Throwable e) {
                subject.onError(e);
            }

            @Override
            public void onComplete() {
                subject.onComplete();
            }
        });


        return subject;
    }

}
