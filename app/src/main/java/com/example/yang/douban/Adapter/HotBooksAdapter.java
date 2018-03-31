package com.example.yang.douban.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.douban.Bean.Book;
import com.example.yang.douban.R;

import java.util.List;

/**
 * Created by youxihouzainali on 2018/3/31.
 */

public class HotBooksAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    public HotBooksAdapter(int layoutResId, List<Book> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Book item) {
        helper.setText(R.id.hotBooks_title, item.getName());
        helper.setText(R.id.hotBooks_author, item.getAuthor());
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.hotBooks_image));
    }
}
