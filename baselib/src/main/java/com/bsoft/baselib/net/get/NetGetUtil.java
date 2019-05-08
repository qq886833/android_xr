package com.bsoft.baselib.net.get;

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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

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

public class NetGetUtil {
    /*Default*/
    /*Util*/
    private Retrofit retrofit;

    /**
     * 构造方法
     *
     * @param context context
     * @param baseUrl baseUrl
     */
    public NetGetUtil(Context context, String baseUrl, Interceptor... interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, NetConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS))
                .retryOnConnectionFailure(false)
                .connectTimeout(NetConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NetConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                //Head
                .addInterceptor(new HeaderInterceptor());

        //Log
        if (CoreConstant.isDebug && NetConfig.curLogType == NetConfig.LOG_NATIVE) {
            okHttpClientBuilder.addNetworkInterceptor(
                    new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY));
        } else if (CoreConstant.isDebug) {
            okHttpClientBuilder.addInterceptor(new NetLogInterceptor());
        }

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

        //ssl
        InputStream[] certificates = null;
        if (context.getString(R.string.base_core_net_url_mp).equals(baseUrl)) {
            certificates = new InputStream[]{context.getResources().openRawResource(R.raw.common_core_network_mp)};
        }
        SSLTools.SSLParams sslParams = SSLTools.getSslSocketFactory(certificates, null, null);
        if (sslParams != null) {
            okHttpClientBuilder.sslSocketFactory(sslParams.sslSocketFactory, sslParams.x509TrustManager);
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
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

    //********************************  get return null **********************************

    /**
     * baseUrl+url 或者 url
     * get 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param params
     * @param observer
     */
    @SuppressLint("CheckResult")
    public void get(LifecycleProvider provider, String url, Map<String, String> heads, Map<String, Object> params,
                    BaseObserver<NullResponse> observer) {
        Observable observable = create(GetApi.class)
                .get(url, heads, params)
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

    //********************************  get **********************************

    /**
     * baseUrl+url 或者 url
     * get 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param params
     * @param observer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    public <T> void get(LifecycleProvider provider, String url, Map<String, String> heads, Map<String, Object> params,
                        final Class<T> clazz, BaseObserver<T> observer) {
        Observable observable = create(GetApi.class)
                .get(url, heads, params)
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

    //********************************  get 2**********************************

    /**
     * baseUrl+url 或者 url
     * post 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param params
     * @param observer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    public <T extends CoreResponse> void get(LifecycleProvider provider, String url, Map<String, String> heads, Map<String, Object> params,
                                             final Class<T> clazz, BaseObserver2<T> observer) {

        Observable observable = create(GetApi.class)
                .get(url, heads, params)
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

    //******************************* getList *********************************************

    /**
     * baseUrl+url 或者 url
     * get list 请求
     *
     * @param provider LifecycleProvider
     * @param heads
     * @param params
     * @param observer
     * @param <T>
     */
    @SuppressLint("CheckResult")
    public <T> void getList(LifecycleProvider provider, String url, Map<String, String> heads, Map<String, Object> params,
                            final Class<T> clazz, BaseObserver<ArrayList<T>> observer) {
        Observable observable = create(GetApi.class)
                .get(url, heads, params)
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
        CoreResponse<T> baseGetResponse = JSON.parseObject(s, new TypeReference<CoreResponse<T>>() {
        });

        String detail = JSON.toJSONString(baseGetResponse.getDetails());
        baseGetResponse.setDetails(JSON.parseObject(detail, clazz));

        return baseGetResponse;
    }

    private <T> CoreResponse<ArrayList<T>> convertList(String s, Class<T> clazz) {
        CoreResponse<ArrayList<T>> baseGetResponse = JSON.parseObject(s, new TypeReference<CoreResponse<ArrayList<T>>>() {
        });

        String detail = JSON.toJSONString(baseGetResponse.getDetails());
        ArrayList<T> arrayList = new ArrayList<>();
        List<T> list = JSON.parseArray(detail, clazz);
        if (list != null) {
            arrayList.addAll(list);
        }

        baseGetResponse.setDetails(arrayList);

        return baseGetResponse;
    }
}
