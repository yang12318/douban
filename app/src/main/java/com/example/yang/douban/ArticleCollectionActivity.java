package com.example.yang.douban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.douban.Adapter.CollectionArticlesAdapter;
import com.example.yang.douban.Adapter.HotArticlesAdapter;
import com.example.yang.douban.Bean.Article;
import com.example.yang.douban.Bean.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleCollectionActivity extends AppCompatActivity {

    private List<Article> mArticleList;
    private RecyclerView recyclerView;
    private ImageButton btn_back;
    private EasyRefreshLayout easyRefreshLayout;
    private BaseQuickAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_collection);
        adapter = new CollectionArticlesAdapter(R.layout.item_collection_articles, mArticleList);
        btn_back = (ImageButton) findViewById(R.id.ib_article_collection_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
        adapter.setNewData(mArticleList);
        initAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.emptylist);
        adapter.setHeaderFooterEmpty(true, true);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_collection_articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                initData();
                initAdapter();
                easyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
                    @Override
                    public void complete() {
                        adapter.setNewData(mArticleList);
                        easyRefreshLayout.refreshComplete();
                    }
                }, 500);
            }
        });
        //recyclerView.addItemDecoration();
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        //firstAdapter.openLoadAnimation();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ArticleCollectionActivity.this, ArticleDetailActivity.class);
                int id = mArticleList.get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Article article = mArticleList.get(position);
                int id = article.getId();
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/toCancelCollect/");
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("type", "articles");
                String response = flowerHttp.post(map);
                int result = 0;
                try {
                    result = new JSONObject(response).getInt("rsNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result == 0) {
                    showToast("未知错误");
                    return;
                }
                else if(result == -2) {
                    showToast("您没有收藏该本图书");
                    return;
                }
                else if(result == 1) {
                    showToast("取消收藏成功");
                    initData();
                    adapter.setNewData(mArticleList);
                    initAdapter();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        mArticleList = new ArrayList<>();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getCollectedInfo/");
        Map<String, Object> map = new HashMap<>();
        map.put("type", "articles");
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
                article.setText(jsonObject.getString("text"));
                article.setSrc("http://118.25.40.220/"+jsonObject.getString("authorSrc"));
                mArticleList.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String s) {
        Toast.makeText(ArticleCollectionActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
