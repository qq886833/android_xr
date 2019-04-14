package com.bsoft.baselib.base.activity;

import android.os.Bundle;

import com.bsoft.baselib.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;


/**
 * Created by chenkai on 2018/6/21.
 */

public abstract class BaseListActivity extends BaseActivity {
    /*Default*/
    protected static final int FIRST_PAGE = 1;
    /*Util*/
    /*Flag*/
    private boolean isEmpty = true;
    protected int pageNo = FIRST_PAGE;
    protected int pageSize = 20;

    private boolean refreshEnable = true;
    private boolean loadMoreEnable = true;
    /*View*/
    protected RefreshLayout baseCoreRefreshLayout;

    protected abstract void onLoadMoreView();

    protected void onListRefresh() {
        onRefreshView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        baseCoreRefreshLayout = findViewById(R.id.baseCoreRefreshLayout);

        if (baseCoreRefreshLayout != null) {
            baseCoreRefreshLayout.setEnableLoadMore(false);
            baseCoreRefreshLayout.setEnableRefresh(false);

            baseCoreRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    pageNo = FIRST_PAGE;
                    baseCoreRefreshLayout.setNoMoreData(false);
                    onListRefresh();
                }
            });
            baseCoreRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    pageNo++;
                    onLoadMoreView();
                }
            });
        }
    }

    public void setRefreshEnable(boolean enable) {
        if (baseCoreRefreshLayout != null) {
            refreshEnable = enable;
            baseCoreRefreshLayout.setEnableRefresh(enable);
        }
    }

    public void setLoadMoreEnable(boolean enable) {
        if (baseCoreRefreshLayout != null) {
            loadMoreEnable = enable;
            baseCoreRefreshLayout.setEnableLoadMore(enable);
        }
    }

    public boolean isRefreshEnable() {
        return refreshEnable;
    }

    public boolean isLoadMoreEnable() {
        return loadMoreEnable;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    /**
     * no more data
     */
    public void finishLoadMoreWithNoMoreData() {
        if (baseCoreRefreshLayout != null) {
            baseCoreRefreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    /**
     * restore
     * 所有状态下显示
     */
    @Override
    public void restoreView() {
        super.restoreView();
        isEmpty = false;

        if (baseCoreRefreshLayout != null) {
            baseCoreRefreshLayout.finishRefresh(true);
            baseCoreRefreshLayout.finishLoadMore(true);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }
        }
    }

    /**
     * loading
     * 在下拉刷新和加载更多状态下不显示
     * 显示的时候不可下拉刷新加载更多
     */
    @Override
    public void showLoadingView() {
        showLoadingView(0, null);
    }

    @Override
    public void showLoadingView(@DrawableRes int resourceId, String text) {
        if (baseCoreRefreshLayout == null) {
            super.showLoadingView(resourceId, text);
            return;
        }
        if (baseCoreRefreshLayout.getState() != RefreshState.Refreshing
                && baseCoreRefreshLayout.getState() != RefreshState.Loading) {
            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(false);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(false);
            }

            if (isEmpty) {
                super.showLoadingView(resourceId, text);
            }
        }
    }

    /**
     * empty
     * 进入加载和下拉刷新状态下显示
     * 显示的时候不可加载更多
     */
    @Override
    public void showEmptyView() {
        showEmptyView(0, null);
    }

    @Override
    public void showEmptyView(@DrawableRes int resourceId, String text) {
        if (baseCoreRefreshLayout == null) {
            super.showEmptyView(resourceId, text);
            return;
        }
        if (refreshEnable) {
            baseCoreRefreshLayout.setEnableRefresh(true);
        }

        if (baseCoreRefreshLayout.getState() != RefreshState.Loading) {
            isEmpty = true;

            baseCoreRefreshLayout.finishRefresh(true);
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(false);
            }

            super.showEmptyView(resourceId, text);
        } else {
            baseCoreRefreshLayout.finishLoadMore(true);
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }
            finishLoadMoreWithNoMoreData();
        }
    }


    /**
     * error 单图片
     * 数据为空时显示，显示时不可下拉刷新不可加载更多（数据为空时不能下拉刷新的）
     *
     * @param errorType
     */
    @Override
    public void showErrorViewSimple(String errorType, String errorMsg) {
        showErrorViewSimple(errorType, errorMsg, 0, null);
    }

    @Override
    public void showErrorViewSimple(String errorType, String errorMsg, @DrawableRes int defRes, String defText) {
        if (baseCoreRefreshLayout == null) {
            super.showErrorViewSimple(errorType, errorMsg, defRes, defText);
            return;
        }
        if (isEmpty) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(false);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(false);
            }

            super.showErrorViewSimple(errorType, errorMsg, defRes, defText);
        } else if (baseCoreRefreshLayout.getState() != RefreshState.Loading) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        } else {
            pageNo--;

            baseCoreRefreshLayout.finishLoadMore(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        }
    }

    @Override
    public void showErrorViewSimpleReplace(String errorType, String errorMsg, @DrawableRes int resourceId, String text) {
        if (baseCoreRefreshLayout == null) {
            super.showErrorViewSimpleReplace(errorType, errorMsg, resourceId, text);
            return;
        }
        if (isEmpty) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(false);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(false);
            }

            super.showErrorViewSimpleReplace(errorType, errorMsg, resourceId, text);
        } else if (baseCoreRefreshLayout.getState() != RefreshState.Loading) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        } else {
            pageNo--;

            baseCoreRefreshLayout.finishLoadMore(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        }
    }

    /**
     * error 点击重试
     *
     * @param errorType
     * @param errorMsg
     */
    @Override
    public void showErrorView(String errorType, String errorMsg) {
        showErrorView(errorType, errorMsg, 0, null);
    }

    @Override
    public void showErrorView(String errorType, String errorMsg, @DrawableRes int defRes, String defText) {
        if (baseCoreRefreshLayout == null) {
            super.showErrorView(errorType, errorMsg, defRes, defText);
            return;
        }
        if (isEmpty) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(false);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(false);
            }

            super.showErrorView(errorType, errorMsg, defRes, defText);
        } else if (baseCoreRefreshLayout.getState() != RefreshState.Loading) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        } else {
            pageNo--;

            baseCoreRefreshLayout.finishLoadMore(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        }
    }

    @Override
    public void showErrorViewReplace(String errorType, String errorMsg, @DrawableRes int resourceId, String text) {
        if (baseCoreRefreshLayout == null) {
            super.showErrorViewReplace(errorType, errorMsg, resourceId, text);
            return;
        }
        if (isEmpty) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(false);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(false);
            }

            super.showErrorViewReplace(errorType, errorMsg, resourceId, text);
        } else if (baseCoreRefreshLayout.getState() != RefreshState.Loading) {
            baseCoreRefreshLayout.finishRefresh(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        } else {
            pageNo--;

            baseCoreRefreshLayout.finishLoadMore(false);

            if (refreshEnable) {
                baseCoreRefreshLayout.setEnableRefresh(true);
            }
            if (loadMoreEnable) {
                baseCoreRefreshLayout.setEnableLoadMore(true);
            }

            showErrorToast(errorType, errorMsg);
        }
    }
}
