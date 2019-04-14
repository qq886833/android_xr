package com.bsoft.baselib.net.base;

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
 * Created by kai.chen on 2017/6/22.
 */

public abstract class BaseObserver<T> implements Observer<CoreResponse<T>> {

    public abstract void onHandlePrePare(Disposable d);

    protected abstract void onHandleSuccess(T value);

    protected abstract void onHandleError(String errorType, String msg);

    protected abstract void onHandleComplete();

    protected BaseObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onHandlePrePare(d);
    }

    @Override
    public void onNext(CoreResponse<T> value) {
        if (value.isSuccess()) {
            onHandleSuccess(value.getDetails());
        } else {
            if (CoreConstant.isDebug) {
                Toast.makeText(CoreAppInit.getApplication(),
                        CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_server) + ":code=" + value.getCode() + ";msg=" + value.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
            onHandleError(value.getCode(), value.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException = NetworkErrorUtil.getErrorTypeByThrow(e);
        if (NetConfig.curLogType == NetConfig.LOG_MINE
                && CoreConstant.isDebug) {
            LogUtil.e("BaseGetObserver;onError;type="
                    + apiException.getCode() + ";msg=" + apiException.getMsg(), e);
        }
        if (CoreConstant.isDebug) {
            Toast.makeText(CoreAppInit.getApplication(),
                    CoreAppInit.getApplication().getText(R.string.base_core_net_error_local) + ":type=" + apiException.getCode() + ";msg=" + apiException.getMsg(),
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
