package com.bsoft.baselib.widget.titlebar.config;


import com.bsoft.baselib.core.CoreVo;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public class TitleButtonConfig extends CoreVo {
    /*Default*/
    public static final int SHOW_MODE_NAME = 1;
    public static final int SHOW_MODE_ICON = 2;
    public static final int SHOW_MODE_ALL = 3;

    public static final int MODE_WEB = 1;
    public static final int MODE_LOCAL = 2;

    public String id;

    /**
     * 图标
     * 优先
     * url
     */
    public String icon;

    /**
     * 图标
     * 本地资源文件
     */
    @DrawableRes
    public int iconLocal;

    public String name;

    /**
     * 按钮名称颜色
     * 本地资源文件
     * 亮色模式默认黑色；暗色模式默认白色
     */
    @ColorRes
    public int nameColorLocal;

    /**
     * 按钮名称颜色
     * 例：#343434
     * 本地资源文件
     * 亮色模式默认黑色；暗色模式默认白色
     * 优先
     */
    public String nameColor;

    /**
     * 显示模式
     * 0 - 显示name（默认）
     * 1 - 显示icon；
     * 2 - 都显示
     */
    public int showMode = SHOW_MODE_NAME;

    /**
     * btn不同level区分处理
     */
    public int processMode = MODE_WEB;
}
