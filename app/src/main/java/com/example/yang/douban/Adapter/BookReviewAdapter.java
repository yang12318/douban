package com.example.yang.douban.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.douban.Bean.BookReview;
import com.example.yang.douban.Bean.MyReview;
import com.example.yang.douban.R;

import java.util.List;

/**
 * Created by youxihouzainali on 2018/4/7.
 */

public class BookReviewAdapter extends BaseQuickAdapter<BookReview, BaseViewHolder> {
    public BookReviewAdapter(int layoutResId, List<BookReview> list) {
        super(layoutResId, list);
    }

    protected void convert(BaseViewHolder helper, BookReview item) {
        helper.setText(R.id.bookdetail_Name, item.getCommenterName());
        helper.setText(R.id.bookdetail_Time, item.getPub_time());
        helper.setText(R.id.bookdetail_Text, item.getText());
    }
}
