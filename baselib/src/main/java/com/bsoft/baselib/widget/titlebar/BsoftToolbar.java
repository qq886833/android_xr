package com.bsoft.baselib.widget.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class BsoftToolbar extends FrameLayout {
    /*Default*/
    /*Util*/
    private Activity activity;
    private OnBsoftToolbarLisntener lisntener;
    private TitleButtonDelegate titleButtonDelegate;
    private CoreListAdapter<TitleButtonConfig> titleBtnAdapter;
    /*Flag*/
    private StatusTitleConfig titleConfig;
    private boolean curBottonLineVisibility;
    /*View*/
    protected Toolbar coreBsoftToolbar;
    protected TextView bsoftToolbarTvTitle;
    protected View bsoftToolbarBottomLine;
    private RecyclerView bsoftToolbarRecyclerView;

    /*attrs*/
    private boolean attrLineVisible;
    private Drawable attrDrawableBack;
    private String attrTitle;
    @ColorInt
    private int attrBackgroundColor;
    @ColorInt
    private int attrTitleColor;

    public interface OnBsoftToolbarLisntener {
        void onItemClick(TitleButtonConfig config);

        void onBack();

        void updateStatusBar();
    }

    public BsoftToolbar(Context context) {
        this(context, null);
    }

    public BsoftToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BsoftToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BsoftToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setLisntener(OnBsoftToolbarLisntener lisntener) {
        this.lisntener = lisntener;
    }

    public StatusTitleConfig getTitleConfig() {
        return titleConfig;
    }

    private void init(Context context, AttributeSet attrs) {
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        LayoutInflater.from(context).inflate(R.layout.base_core_bsofttoolbar, this);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BsoftToolbar);
        attrLineVisible = ta.getBoolean(R.styleable.BsoftToolbar_bsoft_toolbar_line_visible, true);
        attrDrawableBack = ta.getDrawable(R.styleable.BsoftToolbar_bsoft_toolbar_back_drawable);
        attrTitle = ta.getString(R.styleable.BsoftToolbar_bsoft_toolbar_title);
        attrTitleColor = ta.getColor(R.styleable.BsoftToolbar_bsoft_toolbar_title_color, Color.WHITE);
        attrBackgroundColor = ta.getColor(R.styleable.BsoftToolbar_bsoft_toolbar_background_color, Color.GRAY);
        ta.recycle();

        bsoftToolbarBottomLine = findViewById(R.id.bsoftToolbarBottomLine);
        setBarBottomLineVisibility(attrLineVisible);

        initToolBar();
        initTitleButton();
    }

    private void initToolBar() {
        //Toolbar
        bsoftToolbarTvTitle = findViewById(R.id.bsoftToolbarTvTitle);
        coreBsoftToolbar = findViewById(R.id.coreBsoftToolbar);
        coreBsoftToolbar.setTitle("");
        coreBsoftToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lisntener != null) {
                    lisntener.onBack();
                }
            }
        });

        setTitle(attrTitle);
        bsoftToolbarTvTitle.setTextColor(attrTitleColor);
        setToolBarBgColor(attrBackgroundColor);
        if (attrDrawableBack != null) {
            coreBsoftToolbar.setNavigationIcon(attrDrawableBack);
        }
    }

    private void initTitleButton() {
        if (activity == null) {
            return;
        }
        bsoftToolbarRecyclerView = findViewById(R.id.bsoftToolbarRecyclerView);

        RecyclerViewUtil.initLinearH(activity, bsoftToolbarRecyclerView, R.color.base_core_transparent, R.dimen.dp_10);
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
        titleBtnAdapter = new CoreListAdapter<>(activity, titleButtonDelegate);
        bsoftToolbarRecyclerView.setAdapter(titleBtnAdapter);

    }

    public void setTitle(String title) {
        bsoftToolbarTvTitle.setText(title);
        if (titleConfig != null) {
            titleConfig.title = title;
        }
    }

    public void setTitleColor(@ColorInt int color) {
        bsoftToolbarTvTitle.setTextColor(color);
        if (titleConfig != null) {
            titleConfig.titleColorLocal = color;
        }
    }

    public void setToolBarBgColor(@ColorInt int color) {
        coreBsoftToolbar.setBackgroundColor(color);
        if (titleConfig != null) {
            titleConfig.statusTitleBarColorLocal = color;
        }
        if (lisntener != null){
            lisntener.updateStatusBar();
        }
    }

    public void setBarBottomLineVisibility(boolean visibility) {
        curBottonLineVisibility = visibility;
        bsoftToolbarBottomLine.setVisibility(visibility ? VISIBLE : GONE);
        if (titleConfig != null) {
            titleConfig.bottomLineVisibility = visibility;
        }
    }

    public void setNavigationIcon(Drawable drawable) {
        coreBsoftToolbar.setNavigationIcon(drawable);
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        coreBsoftToolbar.setNavigationIcon(resId);
    }

    public void setToolbarVisibility(boolean visibility) {
        coreBsoftToolbar.setVisibility(visibility ? VISIBLE : GONE);
        if (!visibility) {
            setBarBottomLineVisibility(false);
        } else {
            setBarBottomLineVisibility(curBottonLineVisibility);
        }
        if (titleConfig != null) {
            titleConfig.titleBarShowState = visibility ?
                    StatusTitleConfig.TITLE_BAR_SHOW_STATE_SHOW : StatusTitleConfig.TITLE_BAR_SHOW_STATE_HIDE;
        }
    }

    public void setBarBackgroundColorWithBottomLine(@ColorInt int color) {
        setToolBarBgColor(color);
        if (color == Color.TRANSPARENT) {
            setBarBottomLineVisibility(false);
        } else if (color == Color.WHITE) {
            setBarBottomLineVisibility(true);
        }
    }

    public void setStatusBar() {
        if (activity == null) {
            return;
        }

        //状态栏模式
        if (titleConfig != null && titleConfig.statusTitleBarMode == StatusTitleConfig.STATUS_TITLE_BAR_MODE_LIGHT) {
            StatusBarUtil.setLightMode(activity);
        } else {
            StatusBarUtil.setDarkMode(activity);
        }

        //状态栏颜色
        if (titleConfig != null && !TextUtils.isEmpty(titleConfig.statusTitleBarColor)) {
            try {
                StatusBarUtil.setColor(activity, Color.parseColor(titleConfig.statusTitleBarColor), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (titleConfig != null && titleConfig.statusTitleBarColorLocal != 0) {
            StatusBarUtil.setColor(activity, getResources().getColor(titleConfig.statusTitleBarColorLocal), 0);
        } else {
            StatusBarUtil.setColor(activity, attrBackgroundColor, 0);
        }
    }

    /**
     * 设置title和button
     *
     * @param config
     */
    public void setStatusTitle(StatusTitleConfig config) {
        if (config == null) {
            return;
        }
        this.titleConfig = config;


        if (titleConfig.titleBarShowState == StatusTitleConfig.TITLE_BAR_SHOW_STATE_HIDE) {
            setToolbarVisibility(false);
        } else {
            setToolbarVisibility(true);

        }

        //标题栏颜色
        if (!TextUtils.isEmpty(titleConfig.statusTitleBarColor)) {
            try {
                coreBsoftToolbar.setBackgroundColor(Color.parseColor(titleConfig.statusTitleBarColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (titleConfig.statusTitleBarColorLocal != 0) {
            coreBsoftToolbar.setBackgroundColor(getResources().getColor(titleConfig.statusTitleBarColorLocal));
        } else {
            coreBsoftToolbar.setBackgroundColor(attrBackgroundColor);
        }

        //返回按钮
        if (config.statusTitleBarMode == StatusTitleConfig.STATUS_TITLE_BAR_MODE_DARK) {
            coreBsoftToolbar.setNavigationIcon(R.mipmap.base_core_ic_back_white);
        } else {
            coreBsoftToolbar.setNavigationIcon(R.mipmap.base_core_ic_back_gray);
        }

        //bottom line
        setBarBottomLineVisibility(config.bottomLineVisibility);

        //title
        setTitle(titleConfig.title);
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

        if (lisntener != null){
            lisntener.updateStatusBar();
        }

        //button
        if (titleButtonDelegate != null) {
            titleButtonDelegate.setStatusTitleBarMode(titleConfig.statusTitleBarMode);
        }
        setTitleButton(config.titleBtn);
    }

    /**
     * 设置title
     *
     * @param configs
     */
    public void setTitleButton(ArrayList<TitleButtonConfig> configs) {
        if (titleConfig != null) {
            titleConfig.titleBtn = configs;
        }

        if (titleBtnAdapter != null) {
            if (configs != null && !configs.isEmpty()) {
                titleBtnAdapter.setData(configs);
            } else {
                titleBtnAdapter.clear();
            }
        }
    }

    /**
     * add btn
     *
     * @param config
     */
    public void addButton(TitleButtonConfig config) {
        if (config == null) {
            return;
        }

        ArrayList<TitleButtonConfig> configs = new ArrayList<>();
        configs.add(config);
        if (titleConfig != null) {
            if (titleConfig.titleBtn == null) {
                titleConfig.titleBtn = configs;
            } else {
                titleConfig.titleBtn.addAll(configs);
            }
        }

        if (titleBtnAdapter != null) {
            if (titleBtnAdapter.isEmpty()) {
                titleBtnAdapter.setData(configs);
            } else {
                titleBtnAdapter.addData(configs);
            }
        }
    }


}
