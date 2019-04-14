package com.bsoft.baselib.base.activity.baseWeb;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bsoft.baselib.R;
import com.bsoft.baselib.base.activity.BaseListActivity;
import com.bsoft.baselib.base.webinterface.BsoftJsInterface;
import com.bsoft.baselib.base.webinterface.CoreWebInterface;
import com.bsoft.baselib.base.webinterface.config.EndLoadMoreConfig;
import com.bsoft.baselib.base.webinterface.config.NativeContainerConfig;
import com.bsoft.baselib.base.webinterface.config.WebRefreshConfig;
import com.bsoft.baselib.log.LogUtil;
import com.bsoft.baselib.net.util.NetworkErrorUtil;
import com.bsoft.baselib.util.DataConverUtil;
import com.bsoft.baselib.widget.titlebar.BsoftToolbar;
import com.bsoft.baselib.widget.titlebar.config.TitleButtonConfig;

import androidx.annotation.NonNull;


/**
 * Created by chenkai on 2018/7/2.
 */

public abstract class BaseWebActivity extends BaseListActivity implements CoreWebInterface {
    /*Default*/
    private final static int TIME_OUT = 20000;
    /*Util*/
    private Handler handler = new Handler();
    /*Flag*/
    protected boolean needRestore = false;
    /*View*/
    protected WebView baseCoreWebview;
    protected BsoftToolbar baseCoreBosftToolbar;

    protected abstract void addJavascriptInterface(WebView webView);

    protected abstract boolean shouldOverrideUrlLoading(WebView webView, String url);

    protected abstract void onPageStarted(WebView view, String url, Bitmap favicon);

    protected abstract void onPageFinished(WebView view, String url);

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (baseCoreWebview != null
                && !isViewReplace()
                && (keyCode == KeyEvent.KEYCODE_BACK) && baseCoreWebview.canGoBack()) {
            baseCoreWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        //Toolbar
        if (baseCoreToolbar != null) {
            baseCoreToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        }
    }

    @Override
    protected void setStatusBar() {
        if (baseCoreBosftToolbar != null) {
            baseCoreBosftToolbar.setStatusBar();
        } else {
            super.setStatusBar();
        }
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        baseCoreBosftToolbar = findViewById(R.id.baseCoreBosftToolbar);
        if (baseCoreBosftToolbar != null) {
            baseCoreBosftToolbar.setLisntener(new BsoftToolbar.OnBsoftToolbarLisntener() {
                @Override
                public void onItemClick(TitleButtonConfig config) {
                    titleBtnClick(config);
                }

                @Override
                public void onBack() {
                    back();
                }
            });
        }

        baseCoreWebview = findViewById(R.id.baseCoreWebview);
        if (baseCoreWebview != null) {
            baseCoreWebview.getSettings().setJavaScriptEnabled(true);
            baseCoreWebview.getSettings().setDomStorageEnabled(true);
            baseCoreWebview.getSettings().setBlockNetworkImage(false);//解决图片不显示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                baseCoreWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            baseCoreWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            baseCoreWebview.requestFocus();
            baseCoreWebview.setWebChromeClient(webChromeClient);
            baseCoreWebview.setWebViewClient(webViewClient);
            baseCoreWebview.addJavascriptInterface(new BsoftJsInterface(this), "bsoftJsInterface");
            addJavascriptInterface(baseCoreWebview);
        }

        //默认不可用
        setRefreshEnable(false);
        setLoadMoreEnable(false);
    }

    @Override
    protected void onListRefresh() {
        evaluateJavascript(BsoftJsInterface.javascriptBeginRefresh());
    }

    @Override
    protected void onLoadMoreView() {
        evaluateJavascript(BsoftJsInterface.javascriptLoadMoreData());
    }

    @Override
    protected void onRefreshView() {
        if (baseCoreWebview != null) {
            baseCoreWebview.reload();
        }
    }

    @Override
    public void restoreView() {
        super.restoreView();
        //webview情况下，一直为empty
        setEmpty(true);
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            }
            Uri uri = Uri.parse(url);
            if (uri == null) {
                return false;
            }

            if (BaseWebActivity.this.shouldOverrideUrlLoading(view, url)) {
                return true;
            }
            // 表重写此方法明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            needRestore = true;
            handler.removeCallbacks(timeoutRunnable);
            handler.postDelayed(timeoutRunnable, TIME_OUT);
            BaseWebActivity.this.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (needRestore) {
                restoreView();
            }
            handler.removeCallbacks(timeoutRunnable);
            BaseWebActivity.this.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            int errorCode = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean isMain = request.isForMainFrame();
                errorCode = error.getErrorCode();
                // 断网 6.0之后
                if (isMain
                        && (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT)) {
                    needRestore = false;
                    viewError(NetworkErrorUtil.ERROR_TYPE_OFFLINE, getString(R.string.base_core_net_error_type_offline));
                }
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // 断网 6.0之前
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    needRestore = false;
                    viewError(NetworkErrorUtil.ERROR_TYPE_OFFLINE, getString(R.string.base_core_net_error_type_offline));
                }
            }
        }
    };

    protected void initRefresh(WebRefreshConfig config) {
        if (config == null) {
            return;
        }
        setRefreshEnable(config.getHeader() == WebRefreshConfig.ENABLE);
        setLoadMoreEnable(config.getFooter() == WebRefreshConfig.ENABLE);
    }

    public void back() {
        if (baseCoreWebview != null
                && !isViewReplace()
                && baseCoreWebview.canGoBack()) {
            baseCoreWebview.goBack();
        } else {
            onBackPressed();
        }
    }

    public void load(@NonNull String url) {
        if (baseCoreWebview == null) {
            return;
        }
        LogUtil.d("BaseWebActivity;load;url=" + url);
        if (!TextUtils.isEmpty(url)) {
            restoreView();
            baseCoreWebview.loadUrl(url);
        }
    }

    private void viewError(String errorType, String msg) {
        showErrorView(errorType, msg);
    }

    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            if (baseCoreWebview != null
                    && baseCoreWebview.getProgress() < 100) {
                viewError(NetworkErrorUtil.ERROR_TYPE_TIMEOUT, getString(R.string.base_core_net_error_type_timeout));
            }
        }
    };

    //********************** Js - Android ******************
    @Override
    public void setNativeContainer(String json) {
        LogUtil.d("BaseWebActivity;setNativeContainer;json=" + json);
        NativeContainerConfig nativeContainerConfig = DataConverUtil.convertAtoB(json, NativeContainerConfig.class);
        if (nativeContainerConfig != null) {
            if (baseCoreBosftToolbar != null) {
                baseCoreBosftToolbar.setStatusTitle(nativeContainerConfig.getHeader());
            }
            initRefresh(nativeContainerConfig.getRefresh());
        }

    }

    @Override
    public void closeWebView() {
        onBackPressed();
    }

    @Override
    public void endRefresh() {
        restoreView();
    }

    @Override
    public void endLoadMoreData(String json) {
        EndLoadMoreConfig config = DataConverUtil.convertAtoB(json, EndLoadMoreConfig.class);
        restoreView();
        if (config != null && config.getFinished() == EndLoadMoreConfig.FINISHED) {
            finishLoadMoreWithNoMoreData();
        }
    }

    //********************** Android - Js ******************
    protected void evaluateJavascript(String script) {
        if (baseCoreWebview == null
                || TextUtils.isEmpty(script)) {
            return;
        }
        baseCoreWebview.evaluateJavascript(script, null);
    }

    private void titleBtnClick(TitleButtonConfig config) {
        if (config == null) {
            return;
        }

        if (config.processMode == TitleButtonConfig.MODE_WEB) {
            evaluateJavascript(BsoftJsInterface.javascriptTitleBtnClick(config.id));
        }
    }
}
