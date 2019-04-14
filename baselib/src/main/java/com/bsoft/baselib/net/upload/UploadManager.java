package com.bsoft.baselib.net.upload;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.net.base.CoreResponse;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class UploadManager {

    private static class UploadUtilHolder {
        private static final UploadUtil INSTANCE = new UploadUtil(CoreAppInit.getApplication(),
                CoreConstant.httpApiUrl);
    }

    public static UploadUtil getInstance() {
        return UploadManager.UploadUtilHolder.INSTANCE;
    }

    @SuppressLint("CheckResult")
    public static <T extends CoreResponse> void upload(LifecycleProvider provider, @NonNull String url,
                                                       ArrayMap<String, String> heads, ArrayMap<String, String> params,
                                                       ArrayList<String> filePaths, Class<T> clazz,
                                                       BaseUpLoadObserver<T> observer) {
        Observable observable = getInstance().upload(provider, url, heads, params, filePaths, clazz);

        if (provider instanceof LifecycleProvider) {
            if (provider instanceof Activity) {
                observable.compose(RxLifecycle.bindUntilEvent(provider.lifecycle(), ActivityEvent.DESTROY));
            } else if (provider instanceof Fragment) {
                observable.compose(RxLifecycle.bindUntilEvent(provider.lifecycle(), FragmentEvent.DESTROY));
            }
        }

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
