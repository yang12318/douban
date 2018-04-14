package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.douban.Adapter.HotBooksAdapter;
import com.example.yang.douban.Bean.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youxihouzainali on 2018/4/2.
 */

public class HotBooksFragment extends android.support.v4.app.Fragment{
    private static final String TAG = "HotBooksFragment";
    protected View mView;
    protected Context mContext;
    private List<Book> mBookList;
    private RecyclerView recyclerView;
    private HotBooksAdapter adapter;
    private EasyRefreshLayout easyRefreshLayout;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_hotbooks, container, false);
        adapter = new HotBooksAdapter(R.layout.item_hot_books, mBookList);
        page = 1;
        initView();
        initData();
        adapter.setNewData(mBookList);
        initAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.emptylist);
        adapter.setHeaderFooterEmpty(true, true);
        return mView;
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_hotbooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        easyRefreshLayout = (EasyRefreshLayout) mView.findViewById(R.id.easylayout);
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getHotText/?type=books&page="+String.valueOf(page));
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
                                    Book book = new Book();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    book.setAuthor(jsonObject.getString("author"));
                                    book.setGood_num(jsonObject.getInt("like_num"));
                                    book.setName(jsonObject.getString("name"));
                                    book.setPublisher(jsonObject.getString("publisher"));
                                    book.setText(jsonObject.getString("text"));
                                    book.setImage("http://118.25.40.220/" + jsonObject.getString("src"));
                                    book.setId(jsonObject.getInt("id"));
                                    mBookList.add(book);
                                }
                                easyRefreshLayout.loadMoreComplete(new EasyRefreshLayout.Event() {
                                    @Override
                                    public void complete() {
                                        adapter.setNewData(mBookList);
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
                        mBookList.clear();
                        initData();
                        adapter.setNewData(mBookList);
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
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                int id = mBookList.get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        mBookList = new ArrayList<>();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getHotText/?type=books&page="+String.valueOf(page));
        String response = flowerHttp.get();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            int result = 10;
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            try {
                result = jsonObject1.getInt("rsNum");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result == 1) {
                for(int i = 1; i < jsonArray.length(); i++) {
                    Book book = new Book();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    book.setAuthor(jsonObject.getString("author"));
                    book.setGood_num(jsonObject.getInt("like_num"));
                    book.setName(jsonObject.getString("name"));
                    book.setPublisher(jsonObject.getString("publisher"));
                    book.setText(jsonObject.getString("text"));
                    book.setImage("http://118.25.40.220/" + jsonObject.getString("src"));
                    book.setId(jsonObject.getInt("id"));
                    mBookList.add(book);
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
