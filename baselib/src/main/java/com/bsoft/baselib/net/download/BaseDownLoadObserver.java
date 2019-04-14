package com.bsoft.baselib.net.download;


import android.widget.Toast;

import com.bsoft.baselib.R;
import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.log.LogUtil;
import com.bsoft.baselib.net.init.NetConfig;
import com.bsoft.baselib.net.util.ApiException;
import com.bsoft.baselib.net.util.NetworkErrorUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by kai.chen on 2017/7/3.
 */

public abstract class BaseDownLoadObserver implements Observer<Downloading> {

    protected abstract void onHandlePrePare(Disposable d);

    protected abstract void onHandleDownloading(Downloading value);

    protected abstract void onHandleSuccess(Downloading value);

    protected abstract void onHandleError(String errorType, String msg);

    protected abstract void onHandleComplete();

    protected BaseDownLoadObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onHandlePrePare(d);
    }

    @Override
    public void onNext(Downloading value) {
        if (value.isDone()) {
            onHandleSuccess(value);
        } else {
            onHandleDownloading(value);
        }
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException = NetworkErrorUtil.getErrorTypeByThrow(e);
        if (NetConfig.curLogType == NetConfig.LOG_MINE
                && CoreConstant.isDebug) {
            LogUtil.e("BaseDownLoadObserver;onError;type="
                    + apiException.getCode() + ";msg=" + apiException.getMsg(), e);
        }
        if (CoreConstant.isDebug) {
            Toast.makeText(CoreAppInit.getApplication(),
                    CoreAppInit.getApplication().getString(R.string.base_core_net_error_local) + ":type=" + apiException.getCode() + ";msg=" + apiException.getMsg(),
                    Toast.LENGTH_LONG).show();
        }
        onHandleError(apiException.getCode(), apiException.getMsg());
        onHandleComplete();
    }

    @Override
    public void onComplete() {
        onHandleComplete();
    }
}
