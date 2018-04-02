package com.example.yang.douban.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.douban.Bean.Article;
import com.example.yang.douban.R;

import java.util.List;

/**
 * Created by youxihouzainali on 2018/4/1.
 */

public class MyArticlesAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {
    public MyArticlesAdapter(int layoutResId, List<Article> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.hotArticles_Title, item.getTitle());
        helper.setText(R.id.hotArticles_Text, item.getAuthor());
        helper.setText(R.id.hotArticles_Click_Num, item.getClick_num() + "点击");
        helper.setText(R.id.hotArticles_Good_Num, item.getGood_num() + "赞同");
    }
}
