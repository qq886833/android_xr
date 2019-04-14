package com.bsoft.baselib.log;

import android.text.TextUtils;

import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.thirdpart.logger.AndroidLogAdapter;
import com.bsoft.baselib.thirdpart.logger.FormatStrategy;
import com.bsoft.baselib.thirdpart.logger.PrettyFormatStrategy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LogAdapter extends AndroidLogAdapter {
    private final FormatStrategy formatStrategy;

    public LogAdapter() {
        this.formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("log")
                .build();
    }

    public LogAdapter(@NonNull FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        return CoreConstant.isDebug && !TextUtils.isEmpty(tag) && tag.contains(CoreLogTag.TAG);
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        formatStrategy.log(priority, tag, message);
    }
}
