package com.bsoft.baselib.base.activity.baseWeb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.bsoft.baselib.R;
import com.bsoft.baselib.base.activity.BaseWebActivity;


public class SimpleWebActivity extends BaseWebActivity {
    /*Default*/
    /*Util*/
    /*Flag*/
    private String url;
    private String title;
    /*View*/

    public static void appStart(Context context, String url, String title) {
        Intent web = new Intent(context, SimpleWebActivity.class);
        web.putExtra("url", url);
        web.putExtra("title", title);
        context.startActivity(web);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_core_activity_simple_web);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        initLayout();

        if (!TextUtils.isEmpty(title)) {
            baseCoreToolbar.setVisibility(View.VISIBLE);
            baseCoreTvTitle.setText(title);
        } else {
            baseCoreToolbar.setVisibility(View.GONE);
        }

        load(url);
    }

    @Override
    protected void addJavascriptInterface(WebView webView) {
    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    @Override
    protected void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    @Override
    protected void onPageFinished(WebView view, String url) {
    }
}
