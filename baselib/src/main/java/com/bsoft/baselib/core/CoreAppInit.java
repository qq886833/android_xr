package com.bsoft.baselib.core;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bsoft.baselib.MyEventBusIndex;
import com.bsoft.baselib.R;
import com.bsoft.baselib.constant.CoreConstant;
import com.bsoft.baselib.log.CoreLogTag;
import com.bsoft.baselib.log.LogUtil;
import com.bsoft.baselib.shapref.CoreSharpref;
import com.bsoft.baselib.widget.refreshlayout.DynamicTimeFormat;
import com.facebook.drawee.backends.pipeline.Fresco;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by chenkai on 2018/6/19.
 */

public class CoreAppInit {
    private static Application application;
    private boolean isInit;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.base_core_bg, R.color.base_core_text_sub);
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static class CoreAppInitHolder {
        private static final CoreAppInit INSTANCE = new CoreAppInit();
    }

    public static CoreAppInit getInstance() {
        return CoreAppInit.CoreAppInitHolder.INSTANCE;
    }

    public static Application getApplication() {
        return application;
    }

    public boolean isInit() {
        return isInit;
    }

    /**
     * 如果已经init，则取消
     *
     * @param app
     */
    public void init(Application app) {
        if (!isInit) {
            forceInit(app);
            isInit = true;
        }
    }

    /**
     * 强制init
     *
     * @param app
     */
    public void forceInit(Application app) {
        application = app;
        CoreConstant.isDebug = CoreSharpref.getInstance().getDebug();
        LogUtil.init();

        //TODO
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        Fresco.initialize(app);
        QMUISwipeBackActivityManager.init(app);

        Log.d(CoreLogTag.TAG, "yunxin CoreConstants.isDebug=" + CoreConstant.isDebug);

        isInit = true;
    }
}
