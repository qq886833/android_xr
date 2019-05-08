package com.bsoft.baselib.net.download;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.text.TextUtils;

import com.bsoft.baselib.R;
import com.bsoft.baselib.net.init.NetConfig;
import com.bsoft.baselib.net.interceptor.HeaderInterceptor;
import com.bsoft.baselib.net.util.SSLTools;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


public class DownloadUtil {
    private Retrofit retrofit;

    public DownloadUtil(Context context, String baseUrl, Interceptor... interceptors) {
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
     * 下载
     *
     * @param url      下载网络地址
     * @param filePath 本地存储路径
     * @return
     */
    @SuppressLint("CheckResult")
    public Observable<Downloading> download(LifecycleProvider provider, @NonNull String url, final String filePath) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url = null");
        }
        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("filePath = null");
        }
        final Subject<Downloading> subject = PublishSubject.create();
        final Disposable[] disposable = new Disposable[1];

        Observable<ResponseBody> observable = create(DownloadApi.class)
                .downloadFile(url)
                .subscribeOn(Schedulers.io());

        if (provider instanceof LifecycleProvider) {
            if (provider instanceof Activity) {
                observable.compose(RxLifecycle.bindUntilEvent(provider.lifecycle(), ActivityEvent.DESTROY));
            } else if (provider instanceof Fragment) {
                observable.compose(RxLifecycle.bindUntilEvent(provider.lifecycle(), FragmentEvent.DESTROY));
            }
        }

        observable.subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable[0] = d;
                subject.onSubscribe(d);
            }

            @Override
            public void onNext(ResponseBody value) {
                writeResponseBodyToDisk(disposable[0], value, filePath, subject);
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

    /**
     * 把响应体写入到磁盘
     *
     * @param body     响应体
     * @param filePath 文件路径
     * @param subject  subject
     * @return 是否写入成功
     */
    private void writeResponseBodyToDisk(@NonNull Disposable disposable, @NonNull ResponseBody body, @NonNull String filePath, @NonNull Subject<Downloading> subject) {
        File file = new File(filePath);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);

            byte[] fileReader = new byte[4096];

            long currentSize = 0;
            long totalSize = body.contentLength();
            Downloading downloading = new Downloading(totalSize, filePath);
            while (true) {
                if (!subject.hasObservers()) {
                    disposable.dispose();
                }
                int byteCount = inputStream.read(fileReader);
                if (byteCount == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, byteCount);
                currentSize += byteCount;
                downloading.set(currentSize, currentSize == totalSize);
                subject.onNext(downloading);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
