package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by youxihouzainali on 2018/4/2.
 */

public class HotArticlesFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "HotArticlesFragment";
    protected View mView;
    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_hotarticles, container, false);

        String desc = String.format("我是%s页面，来自%s",
                "首页", getArguments().getString("tag"));
        TextView tv_first = (TextView) mView.findViewById(R.id.tv_first);
        tv_first.setText(desc);

        return mView;
    }
}
