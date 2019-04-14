package com.bsoft.baselib.net.upload;

import androidx.collection.ArrayMap;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;


interface UpLoadApi {
    /**
     * Upload
     *
     * @return
     */
    @Multipart
    @POST
    Observable<String> upload(@Url String url, @HeaderMap ArrayMap<String, String> headers,
                              @PartMap ArrayMap<String, RequestBody> params, @PartMap ArrayMap<String, RequestBody> files);
}
