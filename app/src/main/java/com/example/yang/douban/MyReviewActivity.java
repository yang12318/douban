package com.example.yang.douban;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.douban.Adapter.MyGoodBookAdapter;
import com.example.yang.douban.Adapter.MyReviewAdapter;
import com.example.yang.douban.Bean.Book;
import com.example.yang.douban.Bean.MyReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReviewActivity extends AppCompatActivity {

    private List<MyReview> mReviewList;
    private RecyclerView recyclerView;
    private ImageButton ib_back;
    private EasyRefreshLayout easyRefreshLayout;
    private MyReviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);
        ib_back = (ImageButton) findViewById(R.id.ib_myreview_back);
        adapter = new MyReviewAdapter(R.layout.item_my_review, mReviewList);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
        adapter.setNewData(mReviewList);
        initAdapter();
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.emptylist);
        adapter.setHeaderFooterEmpty(true, true);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_my_review);
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
                        adapter.setNewData(mReviewList);
                        easyRefreshLayout.refreshComplete();
                    }
                }, 500);
            }
        });
        //recyclerView.addItemDecoration();
    }

    private void initAdapter() {
        //firstAdapter.openLoadAnimation();
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyReview myReview = mReviewList.get(position);
                int id = myReview.getId();
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/toDelComment/");
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
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
                    showToast("未找到该评论");
                    return;
                }
                else if(result == 1) {
                    showToast("删除成功");
                    initData();
                    adapter.setNewData(mReviewList);
                    initAdapter();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        MainApplication application = MainApplication.getInstance();
        Map<String, Integer> mapParam = application.mInfoMap;
        int id = -10;
        for(Map.Entry<String, Integer> item_map:mapParam.entrySet()) {
            if(item_map.getKey().equals("id"))
                id = item_map.getValue();
        }
        if(id == -10) {
            showToast("全局内存中保存的信息为空");
        }
        mReviewList = new ArrayList<>();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getComments/?type=user&id="+String.valueOf(id));
        String response = flowerHttp.get();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            //jsonArray = new JSONObject(response).getJSONArray("");
            for (int i = 0; i < jsonArray.length(); i++) {
                MyReview myReview = new MyReview();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                myReview.setBookId(jsonObject.getInt("bookId"));
                myReview.setBookName(jsonObject.getString("bookName"));
                myReview.setId(jsonObject.getInt("id"));
                myReview.setPub_time(jsonObject.getString("pub_time"));
                myReview.setText(jsonObject.getString("text"));
                mReviewList.add(myReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
