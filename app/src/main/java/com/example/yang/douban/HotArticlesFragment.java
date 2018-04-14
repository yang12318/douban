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
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
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

public class HotArticlesFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "HotArticlesFragment";
    protected View mView;
    protected Context mContext;
    private List<Article> mArticleList;
    private HotArticlesAdapter adapter;
    private RecyclerView recyclerView;
    private EasyRefreshLayout easyRefreshLayout;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_hotarticles, container, false);
        adapter = new HotArticlesAdapter(R.layout.item_hot_articles, mArticleList);
        page = 1;
        initView();
        initData();
        adapter.setNewData(mArticleList);
        initAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.emptylist);
        adapter.setHeaderFooterEmpty(true, true);
        return mView;
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_hotarticles);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //recyclerView.addItemDecoration();
        easyRefreshLayout = (EasyRefreshLayout) mView.findViewById(R.id.easylayout);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getHotText/?type=articles&page="+String.valueOf(page));
                        String response = flowerHttp.get();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            int result = 10;
                            //jsonArray = new JSONObject(response).getJSONArray("");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            try {
                                result = jsonObject1.getInt("rsNum");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(result == 1) {
                                for(int i = 1; i < jsonArray.length(); i++) {
                                    Article article = new Article();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    article.setId(jsonObject.getInt("id"));
                                    article.setTitle(jsonObject.getString("title"));
                                    article.setAuthor(jsonObject.getString("author"));
                                    article.setPub_time(jsonObject.getString("pub_time"));
                                    article.setClick_num(jsonObject.getInt("click_num"));
                                    article.setText(jsonObject.getString("text"));
                                    article.setSrc("http://118.25.40.220/"+jsonObject.getString("src"));
                                    mArticleList.add(article);
                                }
                                easyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
                                    @Override
                                    public void complete() {
                                        adapter.setNewData(mArticleList);
                                        adapter.notifyDataSetChanged();
                                    }
                                }, 500);
                            }
                            else if(result == -1) {
                                //adapter.loadMoreEnd();
                                //flag = true;
                                easyRefreshLayout.closeLoadView();
                                if(page > 1)
                                    page--;
                                Toast.makeText(mContext, "已显示全部数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        mArticleList.clear();
                        initData();
                        adapter.setNewData(mArticleList);
                        easyRefreshLayout.refreshComplete();
                        Toast.makeText(mContext, "refresh success", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
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
            int result = 10;
            //jsonArray = new JSONObject(response).getJSONArray("");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            try {
                result = jsonObject1.getInt("rsNum");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result == 1) {
                for(int i = 1; i < jsonArray.length(); i++) {
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
            }
            else if(result == -1) {
                adapter.loadMoreEnd();
                Toast.makeText(mContext, "已加载全部数据", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
