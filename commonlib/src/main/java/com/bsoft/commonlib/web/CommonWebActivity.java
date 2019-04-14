package com.bsoft.commonlib.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bsoft.baselib.base.activity.baseWeb.BaseWebActivity;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.util.DataConverUtil;
import com.bsoft.commonlib.R;
import com.bsoft.commonlib.arouter.CommonArouterGroup;
import com.bsoft.commonlib.init.BaseAppInit;
import com.bsoft.commonlib.net.NetConstants;
import com.bsoft.commonlib.shapref.AccountSharpref;
import com.bsoft.commonlib.web.config.NativeRouteConfig;
import com.bsoft.commonlib.web.config.OpenWebViewConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import androidx.annotation.Nullable;

@Route(path = CommonArouterGroup.COMMON_WEB_ACTIVITY)
public class CommonWebActivity extends BaseWebActivity implements CommonWebInterface {
    /*Default*/
    public static final int REQUEST_CODE = 78;

    public static final String URL = "url";
    public static final String MAIN_URL_SUB = "index.html#";
    /*Util*/
    /*Flag*/
    private String path;
    private String param;
    protected String url;

    public static void appStart(String url) {
        CommonArouterGroup.getArouter(CommonArouterGroup.COMMON_WEB_ACTIVITY)
                .withString(URL, url)
                .greenChannel()
                .navigation();
    }

    public static void appStart(String path, String param) {
        CommonArouterGroup.getArouter(CommonArouterGroup.COMMON_WEB_ACTIVITY)
                .withString(CommonArouterGroup.PATH, path)
                .withString(CommonArouterGroup.PARAM, param)
                .greenChannel()
                .navigation();
    }

    @Override
    protected void addJavascriptInterface(WebView webView) {
        webView.addJavascriptInterface(new CommonJsInterface(this), "bsoftJsInterface");
    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    @Override
    protected void onPageStarted(WebView view, String url, Bitmap favicon) {
        showLoadingView();
    }

    @Override
    protected void onPageFinished(WebView view, String url) {
        if (needRestore) {
            restoreView();
        }
    }

    protected int getContentViewId() {
        return R.layout.common_activity_web;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        parseIntent();
        initLayout();

        loadUrl();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            String callback = data.getStringExtra(CommonArouterGroup.CALLBACK);
            String param = data.getStringExtra(CommonArouterGroup.PARAM);
            evaluateJavascript(CommonJsInterface.javascriptCallback(callback, param));
        }
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        url = intent.getStringExtra(URL);
        path = intent.getStringExtra(CommonArouterGroup.PATH);
        param = intent.getStringExtra(CommonArouterGroup.PARAM);
    }

    private void loadUrl() {
        if (!TextUtils.isEmpty(url)) {
            load(url);
        } else if (!TextUtils.isEmpty(path)) {
            StringBuilder builder = new StringBuilder();
            builder.append(NetConstants.httpApiUrl + MAIN_URL_SUB).append(path);
            if (!TextUtils.isEmpty(param)) {
                builder.append(param);
            }
            load(builder.toString());
        }
    }

    public static String getCommonParam(boolean hasQuery) {
        StringBuilder builder = new StringBuilder();
        try {
            if (hasQuery) {
                builder.append("&productCode=").append(URLEncoder.encode(CoreAppInit.getApplication().getString(R.string.common_product_name), "utf-8"));
            } else {
                builder.append("?productCode=").append(URLEncoder.encode(CoreAppInit.getApplication().getString(R.string.common_product_name), "utf-8"));
            }

            String token = AccountSharpref.getInstance().getToken();
            if (!TextUtils.isEmpty(token)) {
                builder.append("&accessToken=").append(URLEncoder.encode(token, "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    //********************** Js - Android ******************
    @Override
    public void pushNativeRoute(String json) {
        NativeRouteConfig config = DataConverUtil.convertAtoB(json, NativeRouteConfig.class);
        if (config == null) {
            return;
        }

        if (TextUtils.isEmpty(config.getCallback())) {
            CommonArouterGroup.getArouter(config.getName())
                    .withString(CommonArouterGroup.PARAM, config.getParams())
                    .withString(CommonArouterGroup.CALLBACK, config.getCallback())
                    .navigation(baseActivity, REQUEST_CODE);
        } else {
            CommonArouterGroup.getArouter(config.getName())
                    .withString(CommonArouterGroup.PARAM, config.getParams())
                    .navigation();
        }
    }

    @Override
    public void notifyTokenInvalid() {
        if (BaseAppInit.getInstance().getListener() != null) {
            BaseAppInit.getInstance().getListener().tokenError();
        }
    }

    @Override
    public void openWebView(String json) {
       OpenWebViewConfig config = DataConverUtil.convertAtoB(json, OpenWebViewConfig.class);
        if (config == null) {
            return;
        }
        CommonArouterGroup.getArouter(CommonArouterGroup.COMMON_WEB_ACTIVITY)
                .withString(CommonArouterGroup.PATH, config.getPath())
                .withString(CommonArouterGroup.PARAM, config.getParams())
                .navigation();
    }
}
