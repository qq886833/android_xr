package com.bsoft.baselib.net.download;

import android.annotation.SuppressLint;
import android.app.Activity;


import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class DownloadManager {

    private static class DownloadUtilHolder {
        private static final DownloadUtil INSTANCE = new DownloadUtil(CoreAppInit.getApplication(),
                CoreConstant.httpDownloadUrl);
    }

    public static DownloadUtil getInstance() {
        return DownloadManager.DownloadUtilHolder.INSTANCE;
    }

    /**
     * 默认普通模式
     *
     * @param provider
     * @param url
     * @param filePath
     * @param observer
     */
    @SuppressLint("CheckResult")
    public static void download(LifecycleProvider provider, @NonNull String url, @NonNull String filePath,
                                BaseDownLoadObserver observer) {
        Observable observable = getInstance()
                .download(provider, url, filePath);

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
