package com.bsoft.baselib.net.post;

import androidx.collection.ArrayMap;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by chenkai on 2017/5/10.
 */

public interface ApiService {
    @POST
    Observable<String> post(@Url String url, @HeaderMap ArrayMap<String, String> headers,
                            @Body Object request);
}
