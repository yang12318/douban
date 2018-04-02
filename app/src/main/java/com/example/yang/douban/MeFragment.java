package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by youxihouzainali on 2018/4/2.
 */

public class MeFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "MeFragment";
    protected View mView;
    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_me, container, false);

        return mView;
    }
}
