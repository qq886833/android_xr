package com.bsoft.commonlib.web;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;


import com.bsoft.baselib.base.webinterface.BsoftJsInterface;
import com.bsoft.baselib.base.webinterface.CoreWebInterface;
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
public class CommonJsInterface extends BsoftJsInterface {
    /*Default*/
    /*Util*/
    private CommonWebInterface commonWebInterface;
    private ObservableEmitter<InterfacePost> emitter;

    public CommonJsInterface(Object webInterface) {
        super((CoreWebInterface) webInterface);
        this.commonWebInterface = (CommonWebInterface) webInterface;
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
                        if (TextUtils.equals(incepterPost.getMode(),"pushNativeRoute")) {
                            commonWebInterface.pushNativeRoute(incepterPost.getParam());
                        } else if (TextUtils.equals(incepterPost.getMode(),"notifyTokenInvalid")) {
                            commonWebInterface.notifyTokenInvalid();
                        } else if (TextUtils.equals(incepterPost.getMode(),"openWebView")) {
                            commonWebInterface.openWebView(incepterPost.getParam());
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
    public void pushNativeRoute(String json) {
        LogUtil.d(json);
        InterfacePost post = new InterfacePost();
        post.setMode("pushNativeRoute");
        post.setParam(json);
        emitter.onNext(post);
    }

    @JavascriptInterface
    public void notifyTokenInvalid() {
        InterfacePost post = new InterfacePost();
        post.setMode("notifyTokenInvalid");
        emitter.onNext(post);
    }

    @JavascriptInterface
    public void openWebView(String json) {
        InterfacePost post = new InterfacePost();
        post.setMode("openWebView");
        post.setParam(json);
        emitter.onNext(post);
    }

    //********************** Android - Js ******************
    public static String javascriptCallback(String callback, String param) {
        StringBuilder builder = new StringBuilder();
        builder.append("callback")
                .append("(\"")
                .append(callback)
                .append("\",\"")
                .append(param)
                .append("\")");
        LogUtil.d("CommonJsInterface;javascriptPushData=" + builder.toString());
        return builder.toString();
    }
}
