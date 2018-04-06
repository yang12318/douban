package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by youxihouzainali on 2018/4/2.
 */

public class MeFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "MeFragment";
    protected View mView;
    protected Context mContext;
    private Button btn_test;
    private TextView tv_revise;
    private LinearLayout ll_collect_article, ll_collect_book, ll_book, ll_article;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        tv_revise = (TextView) mView.findViewById(R.id.tv_text_revise);
        btn_test = (Button) mView.findViewById(R.id.exit);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), APITestActivity.class);
                startActivity(intent);
            }
        });
        tv_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReviseActivity.class);
                startActivity(intent);
            }
        });
        return mView;
    }
}
