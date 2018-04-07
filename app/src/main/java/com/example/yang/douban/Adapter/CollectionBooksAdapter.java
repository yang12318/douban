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

public class CollectionBooksAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {
    public CollectionBooksAdapter(int layoutResId, List<Book> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Book item) {
        helper.setText(R.id.collctionBooks_title, item.getName());
        helper.setText(R.id.collectionBooks_author, item.getAuthor());
        helper.addOnClickListener(R.id.collection_cancel);
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.collcetionBooks_image));
    }
}
