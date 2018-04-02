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

public class HotBooksFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "HotBooksFragment";
    protected View mView;
    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_hotbooks, container, false);

        String desc = String.format("我是%s页面，来自%s",
                "分类", getArguments().getString("tag"));
        TextView tv_second = (TextView) mView.findViewById(R.id.tv_second);
        tv_second.setText(desc);

        return mView;
    }
}
