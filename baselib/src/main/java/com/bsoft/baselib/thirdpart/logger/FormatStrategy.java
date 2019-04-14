package com.bsoft.baselib.thirdpart.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Used to determine how messages should be printed or saved.
 */
public interface FormatStrategy {

  void log(int priority, @Nullable String tag, @NonNull String message);
}
