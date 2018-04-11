package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.douban.Adapter.HotArticlesAdapter;
import com.example.yang.douban.Adapter.HotBooksAdapter;
import com.example.yang.douban.Bean.Article;
import com.example.yang.douban.Bean.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youxihouzainali on 2018/4/2.
 */

public class HotArticlesFragment extends android.support.v4.app.Fragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HotArticlesFragment";
    protected View mView;
    protected Context mContext;
    private List<Article> mArticleList;
    private HotArticlesAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_hotarticles, container, false);
        initView();
        initData();
        initAdapter();
        return mView;
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_hotarticles);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //recyclerView.addItemDecoration();
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        adapter = new HotArticlesAdapter(R.layout.item_hot_articles, mArticleList);
        //firstAdapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this, recyclerView);
        //View headView = getLayoutInflater().inflate(R.layout.top_view, (ViewGroup) recyclerView.getParent(), false);
        //adapter.addHeaderView(headView);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, ArticleDetailActivity.class);
                int id = mArticleList.get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        mArticleList = new ArrayList<>();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getHotText/?type=articles&page="+String.valueOf(page));
        String response = flowerHttp.get();
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
                article.setGood_num(jsonObject.getInt("like_num"));
                article.setText(jsonObject.getString("text"));
                mArticleList.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        adapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mArticleList.clear();
                initData();
                adapter.setNewData(mArticleList);
                adapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onLoadMoreRequested() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getHotText/?type=articles&page="+String.valueOf(page));
                String response = flowerHttp.get();
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
                        article.setGood_num(jsonObject.getInt("like_num"));
                        article.setText(jsonObject.getString("text"));
                        mArticleList.add(article);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.setNewData(mArticleList);
                adapter.loadMoreComplete();
            }
        }, 2000);
    }
}
