package com.bsoft.baselib.widget.loadview;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsoft.baselib.R;
import com.bsoft.baselib.net.util.NetworkErrorUtil;
import com.bsoft.baselib.util.EffectUtil;
import com.bsoft.baselib.util.image.GlideUtil;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 */
public class LoadViewHelper {
    /*Default*/
    /*Util*/
    private IVaryViewHelper helper;
    private Context context;
    private Object objectContext;
    /*Flag*/
    private OnClickListener clickListener;
    private boolean isRellace;

    public LoadViewHelper(Activity activity, View view) {
        this(new VaryViewHelper(view));
        this.context = activity;
        this.objectContext = activity;
    }

    public LoadViewHelper(FragmentActivity activity, View view) {
        this(new VaryViewHelper(view));
        this.context = activity;
        this.objectContext = activity;
    }

    public LoadViewHelper(Fragment fragment, View view) {
        this(new VaryViewHelper(view));
        this.context = fragment.getActivity();
        this.objectContext = fragment;
    }

    private LoadViewHelper(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    /**
     * 恢复
     */
    public void restore() {
        isRellace = false;
        helper.restoreView();
    }

    public boolean isRellace() {
        return isRellace;
    }

    public void setRellace(boolean rellace) {
        isRellace = rellace;
    }

    //************************ 自定义 *************************

    /**
     * 显示自定义
     *
     * @param view
     */
    public void showView(@NonNull View view) {
        if (context instanceof Activity
                && ((Activity) context).isDestroyed()) {
            return;
        }
        isRellace = true;
        helper.showLayout(view);
    }

    /**
     * 显示自定义
     *
     * @param layId
     */
    public void showViewById(int layId) {
        if (context instanceof Activity
                && ((Activity) context).isDestroyed()) {
            return;
        }
        isRellace = true;
        View layout = helper.inflate(layId);
        helper.showLayout(layout);
    }

    //************************ loading *************************

    /**
     * 默认加载
     */
    public void showLoading() {
        showLoading(0, null, 0);
    }

    /**
     * @param size
     */
    public void showLoading(int size) {
        showLoading(0, null, size);
    }

    /**
     * @param resourceId
     */
    public void showLoading(@DrawableRes int resourceId, String text) {
        showLoading(resourceId, text, 0);
    }

    /**
     * @param size
     */
    public void showLoading(@DrawableRes int resourceId, String text, int size) {
        if (context instanceof Activity
                && ((Activity) context).isDestroyed()) {
            return;
        }
        isRellace = true;
        View layout = helper.inflate(R.layout.base_core_loadhelper_loading);
        ImageView imageView = layout.findViewById(R.id.ivLoading);
        TextView tvMsg = layout.findViewById(R.id.tvMsg);

        if (size != 0) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = size;
            params.height = size;
            imageView.setLayoutParams(params);
        }
        if (resourceId != 0) {
            GlideUtil.getInstance(objectContext).load(resourceId).into(imageView);
        } else {
            GlideUtil.getInstance(objectContext).load(R.drawable.base_core_loading).into(imageView);
        }

        if (!TextUtils.isEmpty(text)) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(text);
        } else {
            tvMsg.setVisibility(View.GONE);
        }
        helper.showLayout(layout);
    }

    //************************ empty *************************

    /**
     * 默认空
     */
    public void showEmpty() {
        showEmpty(0, null, 0);
    }

    /**
     * 设置大小
     *
     * @param size
     */
    public void showEmpty(int size) {
        showEmpty(0, null, size);
    }

    /**
     * @param text
     */
    public void showEmpty(@DrawableRes int resourceId, String text) {
        showEmpty(resourceId, text, 0);
    }


    public void showEmpty(@DrawableRes int resourceId, String text, int size) {
        if (context instanceof Activity
                && ((Activity) context).isDestroyed()) {
            return;
        }
        isRellace = true;
        View layout = helper.inflate(R.layout.base_core_loadhelper_empty);
        ImageView ivEmpty = layout.findViewById(R.id.ivEmpty);
        TextView tvMsg = layout.findViewById(R.id.tvMsg);

        if (size != 0) {
            ViewGroup.LayoutParams params = ivEmpty.getLayoutParams();
            params.width = size;
            params.height = size;
            ivEmpty.setLayoutParams(params);
        }

        if (TextUtils.isEmpty(text)) {
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(text);
        }

        if (resourceId != 0) {
            GlideUtil.getInstance(objectContext).load(resourceId).into(ivEmpty);
        } else {
            GlideUtil.getInstance(objectContext).load(R.mipmap.base_core_ic_loadview_empty).into(ivEmpty);
        }

        helper.showLayout(layout);
    }

    //************************ error 单图片,文字*************************

    /**
     * @param type
     */
    public void showErrorSimple(String type) {
        showErrorSimple(getImgIdForError(type, 0), context.getString(getMsgIdForError(type, 0)), 0);
    }

    /**
     * @param type
     */
    public void showErrorSimple(String type, int size) {
        showErrorSimple(getImgIdForError(type, 0), null, size);
    }

    /**
     * @param defRes
     */
    public void showErrorSimple(String type, @DrawableRes int defRes, String defText) {
        showErrorSimple(getImgIdForError(type, defRes), getMsgIdForError(context, type, defText), 0);
    }

    /**
     * @param defRes
     */
    public void showErrorSimple(String type, @DrawableRes int defRes, String defText, int size) {
        showErrorSimple(getImgIdForError(type, defRes), getMsgIdForError(context, type, defText), size);
    }

    /**
     * @param resourceId
     */
    public void showErrorSimple(@DrawableRes int resourceId, String text) {
        showErrorSimple(resourceId, text, 0);
    }

    /**
     * 设置图片、大小
     *
     * @param resourceId
     * @param size
     */
    public void showErrorSimple(@DrawableRes int resourceId, String text, int size) {
        if (context instanceof Activity
                && ((Activity) context).isDestroyed()) {
            return;
        }
        isRellace = true;
        View layout = helper.inflate(R.layout.base_core_loadhelper_error_simple);
        ImageView ivError = layout.findViewById(R.id.ivError);
        TextView tvMsg = layout.findViewById(R.id.tvMsg);

        if (size != 0) {
            ViewGroup.LayoutParams params = ivError.getLayoutParams();
            params.width = size;
            params.height = size;
            ivError.setLayoutParams(params);
        }

        if (TextUtils.isEmpty(text)) {
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(text);
        }

        if (resourceId != 0) {
            GlideUtil.getInstance(objectContext).load(resourceId).into(ivError);
        } else {
            GlideUtil.getInstance(objectContext).load(R.mipmap.base_core_ic_loadview_net_error).into(ivError);
        }

        helper.showLayout(layout);
    }

    //************************ error 文字、点击重试*************************

    public void setErrorClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * 默认 error
     *
     * @param type
     */
    public void showError(String type) {
        showError(getImgIdForError(type, 0), context.getString(getMsgIdForError(type, 0)));
    }

    /**
     * error
     *
     * @param type
     */
    public void showError(String type, @DrawableRes int defRes, @NonNull String defText) {
        showError(getImgIdForError(type, defRes), getMsgIdForError(context, type, defText));
    }

    /**
     * @param resourceId
     * @param text
     */
    public void showError(@DrawableRes int resourceId, @NonNull String text) {
        if (context instanceof Activity
                && ((Activity) context).isDestroyed()) {
            return;
        }
        isRellace = true;
        View layout = helper.inflate(R.layout.base_core_loadhelper_error);
        ImageView image = layout.findViewById(R.id.image);
        TextView tvMsg = layout.findViewById(R.id.tvMsg);
        TextView tvRetry = layout.findViewById(R.id.tvRetry);

        if (resourceId != 0) {
            GlideUtil.getInstance(objectContext).load(resourceId).into(image);
        } else {
            GlideUtil.getInstance(objectContext).load(R.mipmap.base_core_ic_loadview_net_error).into(image);
        }

        if (TextUtils.isEmpty(text)) {
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(text);
        }

        if (clickListener != null) {
            EffectUtil.addClickEffect(tvRetry);
            tvRetry.setOnClickListener(clickListener);
        }

        helper.showLayout(layout);
    }

    public static String getMsgIdForError(Context context, String type, String def) {
        int resourceId = R.string.base_core_net_error;
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case NetworkErrorUtil.ERROR_TYPE_TIMEOUT:
                    resourceId = R.string.base_core_net_timeout;
                    break;
                case NetworkErrorUtil.ERROR_TYPE_OFFLINE:
                    resourceId = R.string.base_core_net_offline;
                    break;
                default:
                    resourceId = R.string.base_core_net_error;
            }
        }

        if (resourceId == R.string.base_core_net_error && def != null) {
            return def;
        } else {
            return context.getString(resourceId);
        }
    }

    public static int getImgIdForError(String type, @DrawableRes int def) {
        if (TextUtils.isEmpty(type)) {
            return def != 0 ? def : R.mipmap.base_core_ic_loadview_net_error;
        }
        switch (type) {
            case NetworkErrorUtil.ERROR_TYPE_TIMEOUT:
                return R.mipmap.base_core_ic_loadview_net_timeout;
            case NetworkErrorUtil.ERROR_TYPE_OFFLINE:
                return R.mipmap.base_core_ic_loadview_net_offline;
            default:
                return def != 0 ? def : R.mipmap.base_core_ic_loadview_net_error;
        }
    }

    public static int getMsgIdForError(String type, @StringRes int def) {
        if (TextUtils.isEmpty(type)) {
            return def != 0 ? def : R.string.base_core_net_error;
        }
        switch (type) {
            case NetworkErrorUtil.ERROR_TYPE_TIMEOUT:
                return R.string.base_core_net_timeout;
            case NetworkErrorUtil.ERROR_TYPE_OFFLINE:
                return R.string.base_core_net_offline;
            default:
                return def != 0 ? def : R.string.base_core_net_error;
        }
    }
}
