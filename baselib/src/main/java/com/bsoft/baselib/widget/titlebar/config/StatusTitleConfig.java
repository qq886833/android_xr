package com.bsoft.baselib.widget.titlebar.config;

import com.bsoft.baselib.core.CoreVo;

import java.util.ArrayList;

import androidx.annotation.ColorRes;

public class StatusTitleConfig extends CoreVo {
    public static final int TITLE_BAR_SHOW_STATE_HIDE = 0;
    public static final int TITLE_BAR_SHOW_STATE_SHOW = 1;

    public static final int STATUS_TITLE_BAR_MODE_LIGHT = 0;
    public static final int STATUS_TITLE_BAR_MODE_DARK = 1;

    public String title;

    /**
     * 标题栏是否显示
     * 0 - 不显示；
     * 1 - 显示（默认)
     */
    public int titleBarShowState = TITLE_BAR_SHOW_STATE_SHOW;

    /**
     * 状态栏、标题栏模式
     * 0 - 亮色（字黑）
     * 1 - 暗色(字白，默认)
     */
    public int statusTitleBarMode = STATUS_TITLE_BAR_MODE_DARK;

    /**
     * 状态栏、标题栏颜色，#343434
     * 例：#343434
     * 默认主题色
     * 优先
     */
    public String statusTitleBarColor;
    /**
     * 状态栏、标题栏颜色，本地资源文件
     * 默认主题色
     */
    @ColorRes
    public int statusTitleBarColorLocal;


    /**
     * title颜色
     * 例：#343434
     * 亮色模式默认黑色；暗色模式默认白色
     * 优先
     */
    public String titleColor;
    /**
     * title颜色，本地资源文件
     * 亮色模式默认黑色；暗色模式默认白色
     * 优先
     */
    @ColorRes
    public int titleColorLocal;

    /**
     * 标题栏自定义按钮
     */
    public ArrayList<TitleButtonConfig> titleBtn;
}
