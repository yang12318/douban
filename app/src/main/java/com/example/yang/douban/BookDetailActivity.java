package com.example.yang.douban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yang.douban.Adapter.BookReviewAdapter;
import com.example.yang.douban.Adapter.MyReviewAdapter;
import com.example.yang.douban.Bean.BookReview;
import com.example.yang.douban.Bean.MyReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDetailActivity extends AppCompatActivity {

    int bookId = 0;
    int collected = 0, gooded = 0;
    private List<BookReview> mReviewList;
    private RecyclerView recyclerView;
    private ImageView iv_book_head;
    private ImageButton iv_star, iv_good, ib_book_back,ib_send;
    private TextView tv_bookname, tv_bookauthor, tv_bookconcern, tv_summary;
    private LinearLayout ll_send;
    private EditText et_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        iv_book_head = (ImageView) findViewById(R.id.iv_book_head);
        ib_book_back = (ImageButton) findViewById(R.id.ib_book_back);
        iv_star = (ImageButton) findViewById(R.id.iv_star);
        iv_good = (ImageButton) findViewById(R.id.iv_good);
        tv_bookauthor = (TextView) findViewById(R.id.tv_bookauthor);
        tv_bookconcern = (TextView) findViewById(R.id.tv_bookconcern);
        tv_bookname = (TextView) findViewById(R.id.tv_bookname);
        tv_summary = (TextView) findViewById(R.id.tv_summary);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        ib_send = (ImageButton) findViewById(R.id.ib_send);
        et_send = (EditText) findViewById(R.id.et_send);
        Intent intent = getIntent();
        bookId = intent.getIntExtra("id", 0);
        if(bookId == 0) {
            showToast("出现错误：id=0");
        }
        final FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/details/?type=book&id="+String.valueOf(bookId));
        String response = flowerHttp.get();
        int id = 0, good_num = 0;
        String name = null, author = null, publisher = null, text = null, src = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            author = jsonObject.getString("author");
            publisher = jsonObject.getString("publisher");
            good_num = jsonObject.getInt("like_num");
            text = jsonObject.getString("text");
            src = "http://118.25.40.220/" + jsonObject.getString("src");
            collected = jsonObject.getInt("collected");
            gooded = jsonObject.getInt("gooded");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_bookname.setText(name);
        tv_summary.setText("简介："+"\n"+text);
        tv_bookconcern.setText("出版社："+publisher);
        tv_bookauthor.setText("作者："+author);
        Glide.with(this).load(src).into(iv_book_head);
        if(collected == 1) {
            Glide.with(this).load(R.drawable.star2).into(iv_star);
        }
        else {
            Glide.with(this).load(R.drawable.star1).into(iv_star);
        }
        if(gooded == 1) {
            Glide.with(this).load(R.drawable.like2).into(iv_good);
        }
        else {
            Glide.with(this).load(R.drawable.like1).into(iv_good);
        }
        initView();
        initData();
        initAdapter();
        iv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gooded == 1) {
                    FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/toCancelGood/");
                    Map<String, Object> map = new HashMap<>();
                    map.put("bookId", bookId);
                    String response = flowerHttp1.post(map);
                    int result = 10;
                    try {
                        result = new JSONObject(response).getInt("rsNum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(result == 1) {
                        showToast("取消点赞成功");
                        Glide.with(BookDetailActivity.this).load(R.drawable.like1).into(iv_good);
                        gooded = 0;
                    }
                    else if(result == 0) {
                        showToast("未知错误");
                        return;
                    }
                    else if(result == -1) {
                        showToast("您没有对该本图书进行点赞");
                        Glide.with(BookDetailActivity.this).load(R.drawable.like1).into(iv_good);
                        gooded = 0;
                        return;
                    }
                    else if(result == 10) {
                        showToast("未返回数据");
                        return;
                    }
                }
                else {
                    FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/toGood/");
                    Map<String, Object> map = new HashMap<>();
                    map.put("bookId", bookId);
                    String response = flowerHttp1.post(map);
                    int result = 10;
                    try {
                        result = new JSONObject(response).getInt("rsNum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(result == 1) {
                        showToast("点赞成功");
                        Glide.with(BookDetailActivity.this).load(R.drawable.like2).into(iv_good);
                        gooded = 1;
                    }
                    else if(result == 0) {
                        showToast("未知错误");
                        return;
                    }
                    else if(result == -1) {
                        showToast("您已经点赞过该本图书");
                        Glide.with(BookDetailActivity.this).load(R.drawable.like2).into(iv_good);
                        gooded = 1;
                        return;
                    }
                }
            }
        });
        iv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(collected == 1) {
                   FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/toCancelCollect/");
                   Map<String, Object> map = new HashMap<>();
                   map.put("id", bookId);
                   map.put("type", "books");
                   String response = flowerHttp1.post(map);
                   int result = 10;
                   try {
                       result = new JSONObject(response).getInt("rsNum");
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   if(result == 1) {
                       showToast("取消收藏成功");
                       Glide.with(BookDetailActivity.this).load(R.drawable.star1).into(iv_star);
                       collected = 0;
                       return;
                   }
                   else if(result == 0) {
                       showToast("未知错误");
                       return;
                   }
                   else if(result == -2) {
                       showToast("您没有收藏这本图书");
                       collected = 0;
                       Glide.with(BookDetailActivity.this).load(R.drawable.star1).into(iv_star);
                       return;
                   }
               }
               else {
                   FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/toCollect/");
                   Map<String, Object> map = new HashMap<>();
                   map.put("id", bookId);
                   map.put("type", "book");
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
                       Glide.with(BookDetailActivity.this).load(R.drawable.star2).into(iv_star);
                   }
                   else if(result == 0) {
                       showToast("未知错误");
                       return;
                   }
                   else if(result == -2) {
                       showToast("您已经收藏过该本图书");
                       collected = 1;
                       Glide.with(BookDetailActivity.this).load(R.drawable.star2).into(iv_star);
                       return;
                   }
               }
            }
        });
        ib_book_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_send.getText().toString();
                if(s.equals(null) || s.length() <=0) {
                    showToast("您还没有评论！");
                    return;
                }
                else {
                    FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/toComment/");
                    Map<String, Object> map = new HashMap<>();
                    map.put("bookId", bookId);
                    map.put("text", s);
                    String response = flowerHttp1.post(map);
                    int result = 10;
                    try {
                        result = new JSONObject(response).getInt("rsNum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(result == 1) {
                        showToast("评论成功");
                        initData();
                        initAdapter();
                        et_send.setText("");
                        hideOneInputMethod(BookDetailActivity.this, et_send);
                        return;
                    }
                    else if(result == 0){
                        showToast("未知错误");
                        return;
                    }
                    else if(result == 10) {
                        showToast("未返回数据");
                        return;
                    }
                }
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_book_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration();
    }

    private void initAdapter() {
        final BaseQuickAdapter adapter = new BookReviewAdapter(R.layout.item_book_review, mReviewList);
        //firstAdapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        mReviewList = new ArrayList<>();
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getComments/?type=book&id="+String.valueOf(bookId));
        String response = flowerHttp.get();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            //jsonArray = new JSONObject(response).getJSONArray("");
            for (int i = 0; i < jsonArray.length(); i++) {
                BookReview bookReview = new BookReview();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                bookReview.setCommenterId(jsonObject.getInt("commenterId"));
                bookReview.setCommenterName(jsonObject.getString("commenterName"));
                bookReview.setId(jsonObject.getInt("id"));
                bookReview.setPub_time(jsonObject.getString("pub_time"));
                bookReview.setText(jsonObject.getString("text"));
                bookReview.setSrc("http://118.25.40.220/"+jsonObject.getString("src"));
                mReviewList.add(bookReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public static void hideOneInputMethod(Activity act, View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
