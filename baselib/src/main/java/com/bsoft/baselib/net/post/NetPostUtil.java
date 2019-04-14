package com.bsoft.baselib.net.post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bsoft.baselib.R;
import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.net.base.BaseObserver;
import com.bsoft.baselib.net.base.BaseObserver2;
import com.bsoft.baselib.net.base.CoreResponse;
import com.bsoft.baselib.net.beans.NullResponse;
import com.bsoft.baselib.net.cache.CacheBuilder;
import com.bsoft.baselib.net.cache.CacheInterceptor;
import com.bsoft.baselib.net.init.NetConfig;
import com.bsoft.baselib.net.interceptor.HeaderInterceptor;
import com.bsoft.baselib.net.interceptor.NetLogInterceptor;
import com.bsoft.baselib.net.util.SSLTools;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


/**
 *
 */

public class NetPostUtil {
    /*Default*/
    /*Util*/
    private Retrofit retrofit;

    /**
     * 构造方法
     *
     * @param context context
     * @param baseUrl baseUrl
     */
    public NetPostUtil(Context context, String baseUrl, Interceptor... interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, NetConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS))
                .retryOnConnectionFailure(false)
                .connectTimeout(NetConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NetConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                //Head
                .addInterceptor(new HeaderInterceptor());

        //Cahce
        if (NetConfig.CACHE_ENABLE) {
            Cache cache = new CacheBuilder().build(context);
            okHttpClientBuilder.cache(cache)
                    .addInterceptor(new CacheInterceptor(context));
        }

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
            certificates = new InputStream[]{context.getResources().openRawResource(R.raw.yjhealth_core_network_mp)};
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
     * 动态代理创建 api
     *
     * @param api api 接口
     * @param <T> T
     * @return api 实体类
     */
    public <T> T create(Class<T> api) {
        return retrofit.create(api);
    }

    //********************************  post return null **********************************

    /**
     * baseUrl+url 或者 url
     * post 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param body
     * @param observer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    public <T> void post(LifecycleProvider provider, String url, ArrayMap<String, String> heads, Object body,
                         BaseObserver<NullResponse> observer) {

        Observable observable = create(ApiService.class)
                .post(url, heads, body)
                .map(new Function<String, NullResponse>() {
                    @Override
                    public NullResponse apply(String s) {
                        NullResponse response = new NullResponse();
                        response.setCode(CoreResponse.SUCCESS);
                        return response;
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

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //********************************  post **********************************

    /**
     * baseUrl+url 或者 url
     * post 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param body
     * @param observer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    public <T> void post(LifecycleProvider provider, String url, ArrayMap<String, String> heads, Object body,
                         final Class<T> clazz, BaseObserver<T> observer) {

        Observable observable = create(ApiService.class)
                .post(url, heads, body)
                .map(new Function<String, CoreResponse<T>>() {
                    @Override
                    public CoreResponse<T> apply(String s) throws Exception {
                        return convert(s, clazz);
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

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //********************************  post 2**********************************

    /**
     * baseUrl+url 或者 url
     * post 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param body
     * @param observer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    public <T extends CoreResponse> void post(LifecycleProvider provider, String url, ArrayMap<String, String> heads, Object body,
                                              final Class<T> clazz, BaseObserver2<T> observer) {

        Observable observable = create(ApiService.class)
                .post(url, heads, body)
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

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //******************************* postList *********************************************

    /**
     * baseUrl+url 或者 url
     * post list 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param body
     * @param observer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    public <T> void postList(LifecycleProvider provider, String url, ArrayMap<String, String> heads, Object body,
                             final Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        Observable observable = create(ApiService.class)
                .post(url, heads, body)
                .map(new Function<String, CoreResponse<ArrayList<T>>>() {
                    @Override
                    public CoreResponse<ArrayList<T>> apply(String s) throws Exception {
                        return convertList(s, clazz);
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

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    private <T> CoreResponse<T> convert(String s, Class<T> clazz) {
        CoreResponse<T> baseResponse = JSON.parseObject(s, new TypeReference<CoreResponse<T>>() {
        });

        String detail = JSON.toJSONString(baseResponse.getDetails());
        baseResponse.setDetails(JSON.parseObject(detail, clazz));

        return baseResponse;
    }

    private <T> CoreResponse<ArrayList<T>> convertList(String s, Class<T> clazz) {
        CoreResponse<ArrayList<T>> baseResponse = JSON.parseObject(s, new TypeReference<CoreResponse<ArrayList<T>>>() {
        });

        String detail = JSON.toJSONString(baseResponse.getDetails());
        ArrayList<T> arrayList = new ArrayList<>();
        List<T> list = JSON.parseArray(detail, clazz);
        if (list != null) {
            arrayList.addAll(list);
        }

        baseResponse.setDetails(arrayList);

        return baseResponse;
    }
}
