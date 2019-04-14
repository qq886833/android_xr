package com.bsoft.baselib.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;


import com.bsoft.baselib.R;
import com.bsoft.baselib.thirdpart.statusbarutil.StatusBarUtil;
import com.bsoft.baselib.widget.recyclerview.RecyclerViewUtil;
import com.bsoft.baselib.widget.recyclerview.adapter.CoreListAdapter;
import com.bsoft.baselib.widget.recyclerview.adapter.ItemViewDelegate;
import com.bsoft.baselib.widget.titlebar.config.StatusTitleConfig;
import com.bsoft.baselib.widget.titlebar.config.TitleButtonConfig;

import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class BsoftToolbar extends FrameLayout {
    /*Default*/
    /*Util*/
    private Activity baseActivity;
    private OnBsoftToolbarLisntener lisntener;
    private TitleButtonDelegate titleButtonDelegate;
    private CoreListAdapter<TitleButtonConfig> titleBtnAdapter;
    /*Flag*/
    private StatusTitleConfig titleConfig;
    /*View*/
    private View mainView;
    protected Toolbar coreBsoftToolbar;
    protected TextView bsoftToolbarTvTitle;
    protected View bsoftToolbarBottomLine;
    private RecyclerView bsoftToolbarRecyclerView;

    public interface OnBsoftToolbarLisntener {
        void onItemClick(TitleButtonConfig config);

        void onBack();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BsoftToolbar(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BsoftToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BsoftToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BsoftToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setLisntener(OnBsoftToolbarLisntener lisntener) {
        this.lisntener = lisntener;
    }

    public StatusTitleConfig getTitleConfig() {
        return titleConfig;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context) {
        if (context instanceof Activity) {
            baseActivity = (Activity) context;
        }

        mainView = LayoutInflater.from(context).inflate(R.layout.base_core_bsofttoolbar, null);
        addView(mainView);

        initToolBar();
        initTitleButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        //Toolbar
        bsoftToolbarTvTitle = findViewById(R.id.bsoftToolbarTvTitle);
        bsoftToolbarBottomLine = findViewById(R.id.bsoftToolbarBottomLine);
        coreBsoftToolbar = findViewById(R.id.coreBsoftToolbar);
        if (coreBsoftToolbar != null) {
            coreBsoftToolbar.setTitle("");
            coreBsoftToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lisntener != null) {
                        lisntener.onBack();
                    }
                }
            });
        }
    }

    private void initTitleButton() {
        if (baseActivity == null) {
            return;
        }
        bsoftToolbarRecyclerView = findViewById(R.id.bsoftToolbarRecyclerView);
        if (bsoftToolbarRecyclerView != null) {
            RecyclerViewUtil.initLinearH(baseActivity, bsoftToolbarRecyclerView, R.color.base_core_transparent, R.dimen.dp_10);
            titleButtonDelegate = new TitleButtonDelegate();
            titleButtonDelegate.setOnItemClickListener(new ItemViewDelegate.OnItemClickListener<TitleButtonConfig>() {
                @Override
                public void onItemClick(@NonNull ArrayList<TitleButtonConfig> datas, int position) {
                    TitleButtonConfig response = datas.get(position);
                    if (lisntener != null) {
                        lisntener.onItemClick(response);
                    }
                }

                @Override
                public boolean onItemLongClick(@NonNull ArrayList<TitleButtonConfig> datas, int position) {
                    return true;
                }

                @Override
                public void onItemViewClick(@NonNull View view, @NonNull ArrayList<TitleButtonConfig> datas, int position) {
                }

                @Override
                public boolean onItemViewLongClick(@NonNull View view, @NonNull ArrayList<TitleButtonConfig> datas, int position) {
                    return false;
                }
            });
            titleBtnAdapter = new CoreListAdapter<>(baseActivity, titleButtonDelegate);
            bsoftToolbarRecyclerView.setAdapter(titleBtnAdapter);
        }
    }
    
    public void setBarBackgroundColor(@ColorInt int color){
        coreBsoftToolbar.setBackgroundColor(color);
        if(color == Color.TRANSPARENT){
            bsoftToolbarBottomLine.setVisibility(GONE);
        }else if(color == Color.WHITE){
            bsoftToolbarBottomLine.setVisibility(VISIBLE);
        }
    }
    
    public void setBarBottomLineVisibility(boolean visibility){
        bsoftToolbarBottomLine.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setStatusBar() {
        if (baseActivity == null) {
            return;
        }

        //状态栏模式
        if (titleConfig != null) {
            if (titleConfig.statusTitleBarMode == StatusTitleConfig.STATUS_TITLE_BAR_MODE_LIGHT) {
                StatusBarUtil.setLightMode(baseActivity);
            } else {
                StatusBarUtil.setDarkMode(baseActivity);
            }
        } else {
            if (getResources().getInteger(R.integer.base_core_statusTitleBarMode) == StatusTitleConfig.STATUS_TITLE_BAR_MODE_LIGHT) {
                StatusBarUtil.setLightMode(baseActivity);
            } else {
                StatusBarUtil.setDarkMode(baseActivity);
            }
        }

        //状态栏颜色
        if (titleConfig != null && !TextUtils.isEmpty(titleConfig.statusTitleBarColor)) {
            try {
                StatusBarUtil.setColor(baseActivity, Color.parseColor(titleConfig.statusTitleBarColor), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (titleConfig != null && titleConfig.statusTitleBarColorLocal != 0) {
            StatusBarUtil.setColor(baseActivity, getResources().getColor(titleConfig.statusTitleBarColorLocal), 0);
        } else {
            StatusBarUtil.setColor(baseActivity, getResources().getColor(R.color.base_core_statustitlebar), 0);
        }
    }

    /**
     * 设置title和button
     *
     * @param config
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusTitle(StatusTitleConfig config) {
        if (config == null) {
            return;
        }
        this.titleConfig = config;

        if (coreBsoftToolbar != null) {
            if (titleConfig.titleBarShowState == StatusTitleConfig.TITLE_BAR_SHOW_STATE_HIDE) {
                coreBsoftToolbar.setVisibility(View.GONE);
            } else {
                coreBsoftToolbar.setVisibility(View.VISIBLE);

                //标题栏颜色
                if (!TextUtils.isEmpty(titleConfig.statusTitleBarColor)) {
                    try {
                        setBarBackgroundColor(Color.parseColor(titleConfig.statusTitleBarColor));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (titleConfig.statusTitleBarColorLocal != 0) {
                    setBarBackgroundColor(getResources().getColor(titleConfig.statusTitleBarColorLocal));
                } else {
                    setBarBackgroundColor(getResources().getColor(R.color.base_core_statustitlebar));
                }

                //返回按钮
                if (config.statusTitleBarMode == StatusTitleConfig.STATUS_TITLE_BAR_MODE_DARK) {
                    coreBsoftToolbar.setNavigationIcon(R.mipmap.base_core_ic_back_white);
                } else {
                    coreBsoftToolbar.setNavigationIcon(R.mipmap.base_core_ic_back_gray);
                }
            }
        }

        //title
        if (bsoftToolbarTvTitle != null && titleConfig.titleBarShowState == StatusTitleConfig.TITLE_BAR_SHOW_STATE_SHOW) {
            bsoftToolbarTvTitle.setText(titleConfig.title);

            //title颜色 titleColor > titleColorLocal > 默认
            if (!TextUtils.isEmpty(titleConfig.titleColor)) {
                try {
                    bsoftToolbarTvTitle.setTextColor(Color.parseColor(titleConfig.titleColor));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (titleConfig.titleColorLocal != 0) {
                bsoftToolbarTvTitle.setTextColor(getResources().getColor(titleConfig.titleColorLocal));
            } else {
                if (config.statusTitleBarMode == StatusTitleConfig.STATUS_TITLE_BAR_MODE_DARK) {
                    bsoftToolbarTvTitle.setTextColor(getResources().getColor(R.color.base_core_white));
                } else {
                    bsoftToolbarTvTitle.setTextColor(getResources().getColor(R.color.base_core_text_main));
                }
            }
        }

        setStatusBar();

        setTitleButton(config.titleBtn);
    }

    /**
     * 设置title
     *
     * @param title
     */
    public void setTitle(String title) {
        if (bsoftToolbarTvTitle == null) {
            return;
        }

        bsoftToolbarTvTitle.setText(title);
        if (titleConfig != null) {
            titleConfig.title = title;
        }
    }

    /**
     * 设置title
     *
     * @param configs
     */
    public void setTitleButton(ArrayList<TitleButtonConfig> configs) {
        int statusTitleBarMode = getResources().getInteger(R.integer.base_core_statusTitleBarMode);
        if (titleConfig != null) {
            titleConfig.titleBtn = configs;
            statusTitleBarMode = titleConfig.statusTitleBarMode;
        }

        if (titleBtnAdapter != null && titleButtonDelegate != null) {
            titleButtonDelegate.setStatusTitleBarMode(statusTitleBarMode);
            if (configs != null && !configs.isEmpty()) {
                titleBtnAdapter.setData(configs);
            } else {
                titleBtnAdapter.clear();
            }
        }
    }
}
