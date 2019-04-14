package com.bsoft.baselib.net.interceptor;

import com.bsoft.baselib.log.LogUtil;
import com.bsoft.baselib.net.util.HeadUtil;
import com.bsoft.baselib.net.util.RequestIdPool;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by kai.chen on 2017/6/24.
 * Log
 */

public class NetLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        int requestId = RequestIdPool.getInstance().getRequestId();
        Request request = chain.request();

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer responseJson = new StringBuffer();

        stringBuffer.append("\n" + request.method());
        stringBuffer.append("\nrequestId=" + requestId);
        stringBuffer.append("\nurl=" + request.url());

        Headers requestHeaders = request.headers();
        for (int i = 0, count = requestHeaders.size(); i < count; i++) {
            String name = requestHeaders.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                stringBuffer.append("\n" + name + ": " + requestHeaders.value(i));
            }
        }

        if (hasRequestBody && !HeadUtil.bodyEncoded(request.headers())) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = HeadUtil.UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(HeadUtil.UTF8);
            }
            if (HeadUtil.isPlaintext(buffer)) {
                stringBuffer.append("\nrequest=" + buffer.readString(charset));
            }
        }

        LogUtil.d("NetLogInterceptor" + stringBuffer.toString());

        long startNs = System.nanoTime();
        //********************Response*************************
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        stringBuffer.setLength(0);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        stringBuffer.append("\nrequestId=" + requestId);
        stringBuffer.append("\ncode=" + response.code());
        stringBuffer.append("\nmessage=" + response.message());
        stringBuffer.append("\ntime=" + tookMs + "ms");

        Headers responseHeaders = response.headers();
        for (int i = 0, count = responseHeaders.size(); i < count; i++) {
            String name = responseHeaders.name(i);
            if ("Date".equalsIgnoreCase(name)) {
                stringBuffer.append("\n" + name + ": " + responseHeaders.value(i));
            }
        }

        if (HttpHeaders.hasBody(response) && !HeadUtil.bodyEncoded(response.headers())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = HeadUtil.UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(HeadUtil.UTF8);
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                    LogUtil.d("NetLogInterceptor" + stringBuffer.toString());
                    return response;
                }
            }

            if (HeadUtil.isPlaintext(buffer) && contentLength != 0) {
                stringBuffer.append("\nresponse=" + buffer.clone().readString(charset));
                responseJson.append(buffer.clone().readString(charset));
            }
        }

        LogUtil.d("NetLogInterceptor" + stringBuffer.toString());
        LogUtil.json(responseJson.toString());

        return response;
    }
}
