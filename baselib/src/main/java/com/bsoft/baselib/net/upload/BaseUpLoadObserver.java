package com.bsoft.baselib.net.upload;


import android.widget.Toast;


import com.bsoft.baselib.R;
import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.core.CoreAppInit;
import com.bsoft.baselib.log.LogUtil;
import com.bsoft.baselib.net.base.CoreResponse;
import com.bsoft.baselib.net.init.NetConfig;
import com.bsoft.baselib.net.util.ApiException;
import com.bsoft.baselib.net.util.NetworkErrorUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by kai.chen on 2017/7/3.
 */

public abstract class BaseUpLoadObserver<T extends CoreResponse> implements Observer<Object> {

    protected abstract void onHandlePrePare(Disposable d);

    protected abstract void onHandleUploading(Uploading value);

    protected abstract void onHandleSuccess(T value);

    protected abstract void onHandleError(String errorType, String msg);

    protected abstract void onHandleComplete();

    protected BaseUpLoadObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onHandlePrePare(d);
    }

    @Override
    public void onNext(Object value) {
        if (value instanceof Uploading) {
            onHandleUploading((Uploading) value);
        } else {
            T t = (T) value;
            if (t.isSuccess()) {
                onHandleSuccess(t);
            } else {
                if (CoreConstant.isDebug) {
                    Toast.makeText(CoreAppInit.getApplication(),
                            CoreAppInit.getApplication().getText(R.string.base_core_net_error_type_server) + ":code=" + t.getCode() + ";msg=" + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                onHandleError(t.getCode(), t.getMessage());
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException = NetworkErrorUtil.getErrorTypeByThrow(e);
        if (NetConfig.curLogType == NetConfig.LOG_MINE
                && CoreConstant.isDebug) {
            LogUtil.e("BaseUpLoadObserver;onError;type="
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
