package com.example.yang.douban.Adapter;

import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.douban.Bean.Article;
import com.example.yang.douban.Bean.Book;
import com.example.yang.douban.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by youxihouzainali on 2018/3/31.
 */

public class HotArticlesAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {
    public HotArticlesAdapter(int layoutResId, List<Article> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.hotArticles_Title, item.getTitle());
        helper.setText(R.id.hotArticles_Text, Html.fromHtml(item.getText()));
        helper.setText(R.id.hotArticles_Author, item.getAuthor());
        helper.setText(R.id.hotArticles_Click_Num, item.getClick_num() + "点击");
        Glide.with(mContext).load(item.getSrc()).into((CircleImageView) helper.getView(R.id.iv_hotarticlehead));
    }
}
