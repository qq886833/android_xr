package com.bsoft.commonlib.util;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsoft.baselib.util.EffectUtil;
import com.bsoft.commonlib.R;
import com.bsoft.commonlib.widget.LinearLineWrapLayout;

import androidx.annotation.DrawableRes;


public class CreateViewUtil {
    /**
     * 创建视图
     *
     * @param name
     * @return
     */
    public static View createAppView(Context context, String name, @DrawableRes int iconId, int laywidth, View.OnClickListener listener) {
        FrameLayout view = (FrameLayout) LayoutInflater.from(context).inflate(
                R.layout.common_item_createh_view, null);
        LinearLineWrapLayout.LayoutParams layoutParams = new LinearLineWrapLayout.LayoutParams(laywidth,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        TextView tvName = view.findViewById(R.id.tvName);
        ImageView ivIcon = view.findViewById(R.id.ivIcon);

        tvName.setText(name);
        ivIcon.setImageResource(iconId);

        if (listener != null) {
            EffectUtil.addClickEffect(view);
            view.setOnClickListener(listener);
        }

        return view;
    }
}
