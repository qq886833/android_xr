package com.bsoft.baselib.base.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bsoft.baselib.R;
import com.bsoft.baselib.core.CoreFragment;
import com.bsoft.baselib.net.util.NetworkErrorUtil;
import com.bsoft.baselib.util.ToastUtil;
import com.bsoft.baselib.widget.dialog.LoadingDialog;
import com.bsoft.baselib.widget.loadview.LoadViewHelper;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;


/**
 * Created by chenkai on 2018/6/20.
 */

public abstract class BaseFragment extends CoreFragment {
    /*Default*/
    /*Util*/
    protected LoadViewHelper loadViewHelper;
    /*Flag*/
    private boolean isCreated = false;////界面是否加载完成
    private boolean isVisibleToUser = false;//是否可见
    private boolean lazyLoaded;//数据是否加载过了
    private boolean viewpagerLoad = false;//是否是viewpaper加载
    /*View*/
    protected LoadingDialog loadingDialog;

    protected Toolbar baseCoreToolbar;
    protected TextView baseCoreTvTitle;

    protected abstract void onRefreshView();
    /**
     * 需要懒加载使用
     */
    protected void startHint() {
    }

    /**
     * 需要懒加载使用
     */
    protected void endHint() {
    }

    /*不要重写这个方法*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreated = true;

        if (!viewpagerLoad) {
            startHint();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        this.viewpagerLoad = true;
        if (!isCreated) {
            return;
        }

        if (isVisibleToUser) {
            startHint();
        } else {
            endHint();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.isVisibleToUser = !hidden;
        if (!isCreated) {
            return;
        }

        if (isVisibleToUser) {
            startHint();
        } else {
            endHint();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }

    protected void initLayout() {
        initToolBar();
        intiLoadView();
    }

    private void intiLoadView() {
        View base = mainView.findViewById(R.id.baseCoreLoadLay);
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

    protected void initToolBar() {
        //Toolbar
        baseCoreTvTitle = mainView.findViewById(R.id.baseCoreTvTitle);
        baseCoreToolbar = mainView.findViewById(R.id.baseCoreToolbar);
        if (baseCoreToolbar != null) {
            baseCoreToolbar.setTitle("");
            baseCoreToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    public boolean isLazyLoaded() {
        return lazyLoaded;
    }

    public void setLazyLoaded(boolean lazyLoaded) {
        this.lazyLoaded = lazyLoaded;
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
        showLoadingView(0, null, 0);
    }

    public void showLoadingView(int size) {
        showLoadingView(0, null, size);
    }

    public void showLoadingView(@DrawableRes int resourceId, String text) {
        showLoadingView(resourceId, text, 0);
    }

    public void showLoadingView(@DrawableRes int resourceId, String text, int size) {
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showLoading(resourceId, text, size);
    }

    //***************** empty **************
    public void showEmptyView() {
        showEmptyView(0, null, 0);
    }

    public void showEmptyView(int size) {
        showEmptyView(0, null, size);
    }

    public void showEmptyView(@DrawableRes int resourceId, String text) {
        showEmptyView(resourceId, text, 0);
    }

    public void showEmptyView(@DrawableRes int resourceId, String text, int size) {
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showEmpty(resourceId, text, size);
    }

    //***************** error 单图片 **************
    public void showErrorViewSimple(String errorType, String errorMsg) {
        showErrorViewSimple(errorType, errorMsg, 0, null, 0);
    }

    public void showErrorViewSimple(String errorType, String errorMsg, int size) {
        showErrorViewSimple(errorType, errorMsg, 0, null, size);
    }

    public void showErrorViewSimple(String errorType, String errorMsg, @DrawableRes int defRes, String defText) {
        showErrorViewSimple(errorType, errorMsg, defRes, defText, 0);
    }

    public void showErrorViewSimple(String errorType, String errorMsg, @DrawableRes int defRes, String defText, int size) {
        if (TextUtils.equals(errorType, NetworkErrorUtil.ERROR_TYPE_SHOW)) {
            ToastUtil.toast(errorMsg);
        }
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showErrorSimple(errorType, defRes, defText, size);
    }

    public void showErrorViewSimpleReplace(String errorType, String errorMsg, @DrawableRes int resourceId, String text) {
        showErrorViewSimpleReplace(errorType, errorMsg, resourceId, text, 0);
    }

    public void showErrorViewSimpleReplace(String errorType, String errorMsg, @DrawableRes int resourceId, String text, int size) {
        if (TextUtils.equals(errorType, NetworkErrorUtil.ERROR_TYPE_SHOW)) {
            ToastUtil.toast(errorMsg);
        }
        if (loadViewHelper == null) {
            return;
        }
        loadViewHelper.showErrorSimple(resourceId, text, size);
    }

    //***************** error 点击重试 **************
    public void showErrorView(String errorType, String errorMsg) {
        showErrorView(errorType, null, 0, null);
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
