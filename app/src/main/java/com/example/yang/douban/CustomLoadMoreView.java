package com.example.yang.douban;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * Created by youxihouzainali on 2018/4/11.
 */

public class CustomLoadMoreView extends LoadMoreView {

    @Override
    public boolean isLoadEndGone() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.quick_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}
