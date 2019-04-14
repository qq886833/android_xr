package com.bsoft.baselib.widget.titlebar;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bsoft.baselib.R;
import com.bsoft.baselib.util.image.GlideUtil;
import com.bsoft.baselib.widget.recyclerview.adapter.ItemViewDelegate;
import com.bsoft.baselib.widget.titlebar.config.StatusTitleConfig;
import com.bsoft.baselib.widget.titlebar.config.TitleButtonConfig;

import java.util.ArrayList;

import androidx.annotation.NonNull;


/**
 * 标题栏动态button delegate
 */
public class TitleButtonDelegate extends ItemViewDelegate<TitleButtonConfig> {
    /*Default*/
    /*Util*/
    /*Flag*/
    //状态栏、标题栏模式 0 - 亮色（默认）；1 - 暗色
    private int statusTitleBarMode = StatusTitleConfig.STATUS_TITLE_BAR_MODE_LIGHT;
    /*View*/
    private ImageView ivIcon;
    private TextView tvName;

    public void setStatusTitleBarMode(int statusTitleBarMode) {
        this.statusTitleBarMode = statusTitleBarMode;
    }

    @Override
    public void onCreateViewHolder(@NonNull ViewGroup parent) {
        root = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_core_title_button, parent, false);
        ivIcon = root.findViewById(R.id.ivIcon);
        tvName = root.findViewById(R.id.tvName);
    }

    @Override
    public boolean isForViewType(@NonNull ArrayList<TitleButtonConfig> datas, int position) {
        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArrayList<TitleButtonConfig> datas, final int position) {
        TitleButtonConfig response = datas.get(position);

        if (response.showMode == TitleButtonConfig.SHOW_MODE_NAME) {
            ivIcon.setVisibility(View.GONE);
            showName(response);
        } else if (response.showMode == TitleButtonConfig.SHOW_MODE_ICON) {
            tvName.setVisibility(View.GONE);
            showIcon(response);
        } else if (response.showMode == TitleButtonConfig.SHOW_MODE_ALL) {
            showName(response);
            showIcon(response);
        } else {
            ivIcon.setVisibility(View.GONE);
            tvName.setVisibility(View.GONE);
        }
    }

    private void showName(TitleButtonConfig response) {
        tvName.setVisibility(View.VISIBLE);
        tvName.setText(response.name);

        //nameColor
        if (!TextUtils.isEmpty(response.nameColor)) {
            try {
                tvName.setTextColor(Color.parseColor(response.nameColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (response.nameColorLocal != 0) {
            tvName.setTextColor(activity.getResources().getColor(response.nameColorLocal));
        } else {
            if (statusTitleBarMode == StatusTitleConfig.STATUS_TITLE_BAR_MODE_DARK) {
                tvName.setTextColor(activity.getResources().getColor(R.color.base_core_white));
            } else {
                tvName.setTextColor(activity.getResources().getColor(R.color.base_core_text_main));
            }
        }
    }

    private void showIcon(TitleButtonConfig response) {
        ivIcon.setVisibility(View.VISIBLE);
        //优先 icon
        if (!TextUtils.isEmpty(response.icon)) {
            GlideUtil.loadImageByUrl(activity, ivIcon, response.icon, R.drawable.base_core_title_icon_def);
        } else if (response.iconLocal != 0) {
            ivIcon.setImageResource(response.iconLocal);
        }
    }
}
