package com.example.yang.douban.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.douban.Bean.MyReview;
import com.example.yang.douban.R;

import java.util.List;

/**
 * Created by youxihouzainali on 2018/4/7.
 */

public class MyReviewAdapter extends BaseQuickAdapter<MyReview, BaseViewHolder> {
    public MyReviewAdapter(int layoutResId, List<MyReview> list) {
        super(layoutResId, list);
    }

    protected void convert(BaseViewHolder helper, MyReview item) {
        helper.setText(R.id.myReview_bookName, item.getBookName());
        helper.setText(R.id.myReview_pubTime, item.getPub_time());
        helper.setText(R.id.myReview_text, item.getText());
        helper.addOnClickListener(R.id.btn_review_delete);
    }
}
