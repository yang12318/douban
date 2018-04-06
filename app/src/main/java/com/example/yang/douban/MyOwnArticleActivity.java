package com.example.yang.douban;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.douban.Adapter.HotArticlesAdapter;
import com.example.yang.douban.Adapter.MyOwnArticleAdapter;
import com.example.yang.douban.Bean.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOwnArticleActivity extends AppCompatActivity {

    private List<Article> mArticleList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_own_article);
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_myownarticles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration();
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        final BaseQuickAdapter adapter = new MyOwnArticleAdapter(R.layout.item_hot_articles, mArticleList);
        //firstAdapter.openLoadAnimation();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //mDetailList.get(position).setTitle("更新--"+position);
                adapter.setNewData(mArticleList);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        mArticleList = new ArrayList<>();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getMyGoodBook/");
        Map<String, Object> map = new HashMap<>();
        String response = flowerHttp.post(map);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            //jsonArray = new JSONObject(response).getJSONArray("");
            for(int i = 0; i < jsonArray.length(); i++) {
                Article article = new Article();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                article.setId(jsonObject.getInt("id"));
                article.setTitle(jsonObject.getString("title"));
                article.setAuthor(jsonObject.getString("author"));
                article.setPub_time(jsonObject.getString("pub_time"));
                article.setClick_num(jsonObject.getInt("click_num"));
                article.setGood_num(jsonObject.getInt("good_num"));
                article.setText(jsonObject.getString("text"));
                mArticleList.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
