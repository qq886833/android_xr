package com.bsoft.commonlib.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

public class HtmlCompatUtil {
    @NonNull
    public static Spanned fromHtml(@NonNull String source, int flags) {
        return Build.VERSION.SDK_INT >= 24 ? HtmlCompat.fromHtml(source, flags) : Html.fromHtml(source);
    }
    @NonNull
    public static Spanned fromHtml(@NonNull String source) {
        return Build.VERSION.SDK_INT >= 24 ? HtmlCompat.fromHtml(source, Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(source);
    }
}
