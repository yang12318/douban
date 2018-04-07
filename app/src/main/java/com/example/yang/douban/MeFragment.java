package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by youxihouzainali on 2018/4/2.
 */

public class MeFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "MeFragment";
    protected View mView;
    protected Context mContext;
    private Button btn_exit;
    private TextView tv_revise;
    private LinearLayout ll_collect_article, ll_collect_book, ll_good, ll_book;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        tv_revise = (TextView) mView.findViewById(R.id.tv_text_revise);
        ll_good = (LinearLayout) mView.findViewById(R.id.ll_good);
        //ll_article = (LinearLayout) mView.findViewById(R.id.ll_article);
        ll_book = (LinearLayout) mView.findViewById(R.id.ll_book);
        ll_collect_article = (LinearLayout) mView.findViewById(R.id.ll_collect_article);
        ll_collect_book = (LinearLayout) mView.findViewById(R.id.ll_collect_book);
        btn_exit = (Button) mView.findViewById(R.id.exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/logout/");
                Map<String, Object> map = new HashMap<>();
                String response = flowerHttp.post(map);
                int result = 0;
                try {
                    result = new JSONObject(response).getInt("rsNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 1) {
                    showToast("注销成功");
                    SharedPreferences sharedPreferences;
                    sharedPreferences = mContext.getSharedPreferences("share", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().commit();
                    Intent intent =  new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                } else if (result == 0) {
                    showToast("未知错误");
                    return;
                }
            }
        });
        tv_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReviseActivity.class);
                startActivity(intent);
            }
        });
        ll_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyGoodBookActivity.class);
                startActivity(intent);
            }
        });
        /*ll_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyOwnArticleActivity.class);
                startActivity(intent);
            }
        });*/
        ll_collect_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleCollectionActivity.class);
                startActivity(intent);
            }
        });
        ll_collect_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BookCollectionActivity.class);
                startActivity(intent);
            }
        });
        ll_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyReviewActivity.class);
                startActivity(intent);
            }
        });
        return mView;
    }

    private void showToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }
}
