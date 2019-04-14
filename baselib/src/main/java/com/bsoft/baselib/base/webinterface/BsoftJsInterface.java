package com.bsoft.baselib.base.webinterface;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;


import com.bsoft.baselib.log.LogUtil;
import com.bsoft.baselib.util.NotProguard;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


@NotProguard
public class BsoftJsInterface {
    /*Default*/
    /*Util*/
    private CoreWebInterface coreWebInterface;
    private ObservableEmitter<InterfacePost> emitter;

    public BsoftJsInterface(CoreWebInterface webInterface) {
        this.coreWebInterface = webInterface;
        Observable
                .create(new ObservableOnSubscribe<InterfacePost>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<InterfacePost> e) throws Exception {
                        emitter = e;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InterfacePost>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull InterfacePost incepterPost) {
                        if (TextUtils.equals(incepterPost.getMode(),"setNativeContainer")) {
                            coreWebInterface.setNativeContainer(incepterPost.getParam());
                        } else if (TextUtils.equals(incepterPost.getMode(),"closeWebView")) {
                            coreWebInterface.closeWebView();
                        } else if (TextUtils.equals(incepterPost.getMode(),"endRefreshData")) {
                            coreWebInterface.endRefresh();
                        } else if (TextUtils.equals(incepterPost.getMode(),"endLoadMoreData")) {
                            coreWebInterface.endLoadMoreData(incepterPost.getParam());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //********************** Js - Android ******************
    @JavascriptInterface
    public void setNativeContainer(String json) {
        InterfacePost post = new InterfacePost();
        post.setMode("setNativeContainer");
        post.setParam(json);
        emitter.onNext(post);
    }

    @JavascriptInterface
    public void closeWebView() {
        InterfacePost post = new InterfacePost();
        post.setMode("closeWebView");
        emitter.onNext(post);
    }

    @JavascriptInterface
    public void endLoadMoreData(String json) {
        InterfacePost post = new InterfacePost();
        post.setMode("endLoadMoreData");
        post.setParam(json);
        emitter.onNext(post);
    }

    @JavascriptInterface
    public void endRefreshData() {
        InterfacePost post = new InterfacePost();
        post.setMode("endRefreshData");
        emitter.onNext(post);
    }

    //********************** Android - Js ******************
    public static String javascriptTitleBtnClick(String id) {
        StringBuilder builder = new StringBuilder();
        builder.append("onTitleButtonClick")
                .append("(\"")
                .append(id)
                .append("\")");
        LogUtil.d("BsoftJsInterface;javascriptTitleBtnClick=" + builder.toString());
        return builder.toString();
    }

    public static String javascriptBeginRefresh() {
        return "beginRefresh()";
    }

    public static String javascriptLoadMoreData() {
        return "loadMoreData()";
    }
}
