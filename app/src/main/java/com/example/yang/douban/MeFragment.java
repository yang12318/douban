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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by youxihouzainali on 2018/4/2.
 */

public class MeFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "MeFragment";
    protected View mView;
    protected Context mContext;
    private Button btn_exit;
    private CircleImageView iv_image_head;
    private TextView tv_revise, tv_nickname;
    private LinearLayout ll_collect_article, ll_collect_book, ll_good, ll_book;

    @Override
    public void onResume() {
        super.onResume();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getInfo/");
        Map<String, Object> map = new HashMap<>();
        String response = flowerHttp.post(map);
        int rsNum = 10;
        String src = null, nickname = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            nickname = jsonObject.getString("username");
            src = "http://118.25.40.220/" + jsonObject.getString("src");
            jsonObject = jsonArray.getJSONObject(1);
            rsNum = jsonObject.getInt("rsNum");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(rsNum == 0) {
            showToast("出现未知错误");
        }
        else if(rsNum == -1) {
            showToast("用户不存在");
        }
        else if(rsNum == 1) {
            tv_nickname.setText(nickname);
            Glide.with(this).load(src).into(iv_image_head);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        tv_revise = (TextView) mView.findViewById(R.id.tv_text_revise);
        iv_image_head = (CircleImageView) mView.findViewById(R.id.iv_image_head);
        ll_good = (LinearLayout) mView.findViewById(R.id.ll_good);
        //ll_article = (LinearLayout) mView.findViewById(R.id.ll_article);
        ll_book = (LinearLayout) mView.findViewById(R.id.ll_book);
        ll_collect_article = (LinearLayout) mView.findViewById(R.id.ll_collect_article);
        ll_collect_book = (LinearLayout) mView.findViewById(R.id.ll_collect_book);
        tv_nickname = (TextView) mView.findViewById(R.id.tv_nickname);
        btn_exit = (Button) mView.findViewById(R.id.exit);
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getInfo/");
        Map<String, Object> map = new HashMap<>();
        String response = flowerHttp.post(map);
        int rsNum = 10;
        String src = null, nickname = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            nickname = jsonObject.getString("username");
            src = "http://118.25.40.220/" + jsonObject.getString("src");
            jsonObject = jsonArray.getJSONObject(1);
            rsNum = jsonObject.getInt("rsNum");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(rsNum == 0) {
            showToast("出现未知错误");
        }
        else if(rsNum == -1) {
            showToast("用户不存在");
        }
        else if(rsNum == 1) {
            tv_nickname.setText(nickname);
            Glide.with(this).load(src).into(iv_image_head);
        }
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
