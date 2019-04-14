package com.bsoft.baselib.net.util;

import android.net.ParseException;

import com.bsoft.baselib.R;
import com.bsoft.baselib.core.CoreAppInit;
import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;


/**
 * Created by chenkai on 2018/3/13.
 */

public class NetworkErrorUtil {
    //error
    public static final String ERROR_TYPE_UNKNOW = "-1";
    public static final String ERROR_TYPE_OFFLINE = "-2";
    public static final String ERROR_TYPE_UNCONNECT = "-3";
    public static final String ERROR_TYPE_TIMEOUT = "-4";
    public static final String ERROR_TYPE_SERVER = "-5";
    public static final String ERROR_TYPE_SSL = "-6";
    public static final String ERROR_PARSE = "-7";
    public static final String ERROR_TYPE_SHOW = "0";//必须显示的log

    /**
     * 解析网络错误类型
     *
     * @param e
     * @return
     */
    public static ApiException getErrorTypeByThrow(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            ex = new ApiException(e, ERROR_TYPE_UNCONNECT);
            ex.setMsg(CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_unconnect).toString());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex = new ApiException(e, ERROR_PARSE);
            ex.setMsg(CoreAppInit.getApplication().getText(R.string.base_core_net_error_parse).toString());
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR_TYPE_OFFLINE);
            ex.setMsg(CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_offline).toString());
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ERROR_TYPE_TIMEOUT);
            ex.setMsg(CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_timeout).toString());
            return ex;
        } else if (e instanceof SSLHandshakeException) {
            ex = new ApiException(e, ERROR_TYPE_SSL);
            ex.setMsg(CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_ssl).toString());
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(e, ERROR_TYPE_OFFLINE);
            ex.setMsg(CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_offline).toString());
            return ex;
        } else {
            ex = new ApiException(e, ERROR_TYPE_UNKNOW);
            ex.setMsg(CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_default).toString());
            return ex;
        }
    }
}
