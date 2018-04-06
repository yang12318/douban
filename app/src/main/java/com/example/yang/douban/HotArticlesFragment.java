package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HotArticlesFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "HotArticlesFragment";
    protected View mView;
    protected Context mContext;
    private List<Article> mArticleList;
    private RecyclerView recyclerView;

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
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        final BaseQuickAdapter adapter = new HotArticlesAdapter(R.layout.item_hot_articles, mArticleList);
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
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getHotText/?type=articles&page=1");
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
                article.setGood_num(jsonObject.getInt("good_num"));
                article.setText(jsonObject.getString("text"));
                mArticleList.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
