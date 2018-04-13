package com.example.yang.douban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleDetailActivity extends AppCompatActivity {

    int articleId = 0;
    int collected = 0;
    private ImageButton ib_star, ib_back;
    private TextView tv_title, tv_author, tv_time;
    private TextView wv;
    private CircleImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Intent intent = getIntent();
        articleId = intent.getIntExtra("id", 0);
        showToast(String.valueOf(articleId));
        if(articleId == 0) {
            showToast("出现错误：id=0");
        }
        ib_star = (ImageButton) findViewById(R.id.ib_article_star);
        ib_back = (ImageButton) findViewById(R.id.ib_article_back);
        tv_title = (TextView) findViewById(R.id.tv_articletitle);
        tv_author = (TextView) findViewById(R.id.tv_articleauthor);
        tv_time = (TextView) findViewById(R.id.tv_articletime);
        wv = (TextView) findViewById(R.id.wv_article);
        iv = (CircleImageView) findViewById(R.id.iv_articlehead);
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/details/?type=article&id="+String.valueOf(articleId));
        String response = flowerHttp.get();
        int id = 0, good_num = 0, click_num = 0;
        String title = null, author = null, pub_time = null, text = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            id = jsonObject.getInt("id");
            title = jsonObject.getString("title");
            author = jsonObject.getString("author");
            pub_time = jsonObject.getString("pub_time");
            click_num = jsonObject.getInt("click_num");
            text = jsonObject.getString("text");
            collected = jsonObject.getInt("collected");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_title.setText(title);
        tv_author.setText(author);
        tv_time.setText(pub_time);
        wv.setText(Html.fromHtml(text));
        if(collected == 0) {
            Glide.with(ArticleDetailActivity.this).load(R.drawable.star1).into(ib_star);
        }
        else {
            Glide.with(ArticleDetailActivity.this).load(R.drawable.star2).into(ib_star);
        }
        ib_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collected == 0) {
                    FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/toCollect/");
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", articleId);
                    map.put("type", "article");
                    String response = flowerHttp1.post(map);
                    int result = 10;
                    try {
                        result = new JSONObject(response).getInt("rsNum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(result == 1) {
                        showToast("收藏成功");
                        collected = 1;
                        Glide.with(ArticleDetailActivity.this).load(R.drawable.star2).into(ib_star);
                    }
                    else if(result == 0) {
                        showToast("未知错误");
                        return;
                    }
                    else if(result == -2) {
                        showToast("您已经收藏过该篇文章");
                        collected = 1;
                        Glide.with(ArticleDetailActivity.this).load(R.drawable.star2).into(ib_star);
                        return;
                    }
                }
                else {
                    FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/toCancelCollect/");
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", articleId);
                    map.put("type", "articles");
                    String response = flowerHttp1.post(map);
                    int result = -10;
                    try {
                        result = new JSONObject(response).getInt("rsNum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(result == 1) {
                        showToast("取消收藏成功");
                        Glide.with(ArticleDetailActivity.this).load(R.drawable.star1).into(ib_star);
                        collected = 0;
                        return;
                    }
                    else if(result == 0) {
                        showToast("未知错误");
                        return;
                    }
                    else if(result == -2) {
                        showToast("您没有收藏这篇文章");
                        collected = 0;
                        Glide.with(ArticleDetailActivity.this).load(R.drawable.star1).into(ib_star);
                        return;
                    }
                }
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
