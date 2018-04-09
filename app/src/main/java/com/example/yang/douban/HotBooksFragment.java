package com.example.yang.douban;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_hotbooks, container, false);
        initView();
        initData();
        initAdapter();
        return mView;
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_hotbooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //recyclerView.addItemDecoration();
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        final BaseQuickAdapter adapter = new HotBooksAdapter(R.layout.item_hot_books, mBookList);
        //firstAdapter.openLoadAnimation();
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
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getHotText/?type=books&page=1");
        String response = flowerHttp.get();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            //jsonArray = new JSONObject(response).getJSONArray("");
            for(int i = 0; i < jsonArray.length(); i++) {
                Book book = new Book();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                book.setAuthor(jsonObject.getString("author"));
                book.setGood_num(jsonObject.getInt("good_num"));
                book.setName(jsonObject.getString("name"));
                book.setPublisher(jsonObject.getString("publisher"));
                book.setText(jsonObject.getString("text"));
                book.setImage("http://118.25.40.220/" + jsonObject.getString("src"));
                book.setId(jsonObject.getInt("id"));
                mBookList.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
