package com.example.yang.douban.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.douban.Bean.Article;
import com.example.yang.douban.Bean.Book;
import com.example.yang.douban.R;

import java.util.List;

/**
 * Created by youxihouzainali on 2018/3/31.
 */

public class CollectionArticlesAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {
    public CollectionArticlesAdapter(int layoutResId, List<Article> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.collectionArticles_Title, item.getTitle());
        helper.setText(R.id.collectionArticles_Text, item.getAuthor());
        helper.setText(R.id.collectionArticles_Author, item.getAuthor());
        helper.setText(R.id.collectionArticles_Click_Num, item.getClick_num() + "点击");
        Glide.with(mContext).load(item.getSrc()).into((ImageView) helper.getView(R.id.iv_collectarticlehead));
        helper.addOnClickListener(R.id.ib_article_collection_delete);
    }
}
