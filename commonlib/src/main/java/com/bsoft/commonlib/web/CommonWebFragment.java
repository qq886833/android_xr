package com.bsoft.commonlib.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bsoft.baselib.base.fragment.BaseWebFragment;
import com.bsoft.baselib.util.DataConverUtil;
import com.bsoft.commonlib.R;
import com.bsoft.commonlib.arouter.CommonArouterGroup;
import com.bsoft.commonlib.init.BaseAppInit;
import com.bsoft.commonlib.net.NetConstants;
import com.bsoft.commonlib.web.config.NativeRouteConfig;
import com.bsoft.commonlib.web.config.OpenWebViewConfig;

import androidx.annotation.Nullable;


@Route(path = CommonArouterGroup.COMMON_WEB_FRAGMENT)
public class CommonWebFragment extends BaseWebFragment implements CommonWebInterface {
    /*Default*/
    public static final int REQUEST_CODE = 79;

    public static final String URL = "url";
    public static final String MAIN_URL_SUB = "index.html#";
    /*Util*/
    /*Flag*/
    private String path;
    private String param;
    protected String url;

    public static CommonWebFragment newInstance(String url) {
        CommonWebFragment fragment = new CommonWebFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public static CommonWebFragment newInstance(String path, String param) {
        CommonWebFragment fragment = new CommonWebFragment();
        Bundle args = new Bundle();
        args.putString(CommonArouterGroup.PATH, path);
        args.putString(CommonArouterGroup.PARAM, param);
        fragment.setArguments(args);
        return fragment;
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
        return R.layout.common_fragment_web;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentViewId(), container, false);
    }

    @Override
    protected void viewCreated(View view, @Nullable Bundle savedInstanceState) {
        parseIntent();

        initLayout();
        loadUrl();
    }

    private void parseIntent() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        url = bundle.getString(URL);
        path = bundle.getString(CommonArouterGroup.PATH);
        param = bundle.getString(CommonArouterGroup.PARAM);
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

    /*需要activity 回调一下*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            String callback = data.getStringExtra(CommonArouterGroup.CALLBACK);
            String param = data.getStringExtra(CommonArouterGroup.PARAM);
            evaluateJavascript(CommonJsInterface.javascriptCallback(callback, param));
        }
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
