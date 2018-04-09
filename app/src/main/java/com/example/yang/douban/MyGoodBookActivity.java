package com.example.yang.douban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.douban.Adapter.MyGoodBookAdapter;
import com.example.yang.douban.Bean.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyGoodBookActivity extends AppCompatActivity {

    private List<Book> mBookList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_good_book);
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_my_good_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration();
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        final BaseQuickAdapter adapter = new MyGoodBookAdapter(R.layout.item_my_good_books, mBookList);
        //firstAdapter.openLoadAnimation();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyGoodBookActivity.this, BookDetailActivity.class);
                int id = mBookList.get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Book book = mBookList.get(position);
                int id = book.getId();
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/toCancelGood/");
                Map<String, Object> map = new HashMap<>();
                map.put("bookId", id);
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
                else if(result == -1) {
                    showToast("您没有点赞该本图书");
                    return;
                }
                else if(result == 1) {
                    showToast("删除成功");
                    initData();
                    initAdapter();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        mBookList = new ArrayList<>();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getMyGoodBook/");
        Map<String, Object> map = new HashMap<>();
        String response = flowerHttp.post(map);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            //jsonArray = new JSONObject(response).getJSONArray("");
            for (int i = 0; i < jsonArray.length(); i++) {
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
    private void showToast(String s) {
        Toast.makeText(MyGoodBookActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
