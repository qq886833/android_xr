package com.bsoft.baselib.net.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface DownloadApi {

    /**
     * 下载Api
     *
     * @param url 下载地址
     * @return 观察对象
     */
    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String url);
}
