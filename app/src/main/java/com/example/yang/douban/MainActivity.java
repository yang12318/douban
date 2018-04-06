package com.example.yang.douban;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();
        bundle.putString("tag", TAG);
        mTabHost  = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.addTab(getTabView(R.string.menu_first, R.drawable.tab_first_selector),
                HotArticlesFragment.class, bundle);
        mTabHost.addTab(getTabView(R.string.menu_second, R.drawable.tab_second_selector),
                HotBooksFragment.class, bundle);
        mTabHost.addTab(getTabView(R.string.menu_third, R.drawable.tab_third_selector),
                MeFragment.class, bundle);
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    private TabHost.TabSpec getTabView(int textId, int imgId) {
        String text = getResources().getString(textId);
        Drawable drawable = getResources().getDrawable(imgId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        View item_tabbar = getLayoutInflater().inflate(R.layout.item_tabbar, null);
        TextView tv_item = (TextView) item_tabbar.findViewById(R.id.tv_item_tabbar);
        tv_item.setText(text);
        tv_item.setCompoundDrawables(null, drawable, null, null);
        TabHost.TabSpec spec = mTabHost.newTabSpec(text).setIndicator(item_tabbar);
        return spec;
    }
}
