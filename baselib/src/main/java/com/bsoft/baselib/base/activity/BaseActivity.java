package com.bsoft.baselib.base.activity;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.bsoft.baselib.R;
import com.bsoft.baselib.core.CoreActivity;
import com.bsoft.baselib.net.util.NetworkErrorUtil;
import com.bsoft.baselib.thirdpart.statusbarutil.StatusBarUtil;
import com.bsoft.baselib.util.ToastUtil;
import com.bsoft.baselib.widget.dialog.LoadingDialog;
import com.bsoft.baselib.widget.loadview.LoadViewHelper;
import com.bsoft.baselib.widget.titlebar.config.StatusTitleConfig;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;


/**
 * Created by chenkai on 2018/6/1.
 */

public abstract class BaseActivity extends CoreActivity {
    /*Util*/
    //可能为空，使用注意判断
    protected LoadViewHelper loadViewHelper;
    /*View*/
    protected LoadingDialog loadingDialog;
    protected Toolbar baseCoreToolbar;
    protected TextView baseCoreTvTitle;


    protected abstract void onRefreshView();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //loadingDialog
        dismissLoadingDialog();
    }

    protected void initLayout() {
        initToolBar();
        intiLoadView();
    }

    protected void initToolBar() {
        //Toolbar
        baseCoreTvTitle = findViewById(R.id.baseCoreTvTitle);
        baseCoreToolbar = findViewById(R.id.baseCoreToolbar);
        if (baseCoreToolbar != null) {
            baseCoreToolbar.setTitle("");
            setSupportActionBar(baseCoreToolbar);
            baseCoreToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void intiLoadView() {
        View base = findViewById(R.id.baseCoreLoadLay);
        if (base != null) {
            loadViewHelper = new LoadViewHelper(this, base);
            loadViewHelper.setErrorClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefreshView();
                }
            });
        }
    }

    protected void setStatusBar() {
        if (getResources().getInteger(R.integer.base_core_statusTitleBarMode) == StatusTitleConfig.STATUS_TITLE_BAR_MODE_LIGHT) {
            StatusBarUtil.setLightMode(this);
        } else {
            StatusBarUtil.setDarkMode(this);
        }
        StatusBarUtil.setColor(this, getResources().getColor(R.color.base_core_statustitlebar), 0);
    }

    //******************************** baseCoreLoadLay *************************************************
    public boolean isViewReplace() {
        if (loadViewHelper == null) {
            return false;
        }
        return loadViewHelper.isRellace();
    }

    //***************** restore **************
    public void restoreView() {
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.restore();
    }

    //***************** loading **************
    public void showLoadingView() {
        showLoadingView(0, null);
    }

    public void showLoadingView(@DrawableRes int resourceId, String text) {
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showLoading(resourceId, text);
    }

    //***************** empty **************
    public void showEmptyView() {
        showEmptyView(0, null);
    }

    public void showEmptyView(@DrawableRes int resourceId, String text) {
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showEmpty(resourceId, text);
    }

    //***************** error 单图片 **************
    public void showErrorViewSimple(String errorType, String errorMsg) {
        showErrorViewSimple(errorType, errorMsg, 0, null);
    }

    public void showErrorViewSimple(String errorType, String errorMsg, @DrawableRes int defRes, String defText) {
        if (TextUtils.equals(errorType, NetworkErrorUtil.ERROR_TYPE_SHOW)) {
            ToastUtil.toast(errorMsg);
        }
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showErrorSimple(errorType, defRes, defText);
    }

    public void showErrorViewSimpleReplace(String errorType, String errorMsg, @DrawableRes int resourceId, String text) {
        if (TextUtils.equals(errorType, NetworkErrorUtil.ERROR_TYPE_SHOW)) {
            ToastUtil.toast(errorMsg);
        }
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showErrorSimple(resourceId, text);
    }

    //***************** error 点击重试 **************
    public void showErrorView(String errorType, String errorMsg) {
        showErrorView(errorType, errorMsg, 0, null);
    }

    public void showErrorView(String errorType, String errorMsg, @DrawableRes int defRes, String defText) {
        if (TextUtils.equals(errorType, NetworkErrorUtil.ERROR_TYPE_SHOW)) {
            ToastUtil.toast(errorMsg);
        }
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showError(errorType, defRes, defText);
    }

    public void showErrorViewReplace(String errorType, String errorMsg, @DrawableRes int resourceId, String text) {
        if (TextUtils.equals(errorType, NetworkErrorUtil.ERROR_TYPE_SHOW)) {
            ToastUtil.toast(errorMsg);
        }
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showError(resourceId, text);
    }

    //***************** loadingDialog **************
    public void showLoadingDialog() {
        showLoadingDialog(0, null);
    }

    public void showLoadingDialog(@DrawableRes int resourceId, String msg) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance(resourceId, msg);
        }
        loadingDialog.show(baseActivity, "loading");
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    //***************** show error toast **************
    public void showErrorToast(String errorType, String errorMsg) {
        if (TextUtils.equals(errorType, NetworkErrorUtil.ERROR_TYPE_SHOW)) {
            ToastUtil.toast(errorMsg);
        } else {
            ToastUtil.toast(LoadViewHelper.getMsgIdForError(errorType, 0));
        }
    }
}
