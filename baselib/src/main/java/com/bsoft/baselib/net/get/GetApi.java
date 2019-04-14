package com.bsoft.baselib.net.get;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by chenkai on 2017/5/10.
 */

public interface GetApi {
    @GET
    Observable<String> get(@Url String url, @HeaderMap Map<String, String> headers,
                           @QueryMap Map<String, Object> params);
}
