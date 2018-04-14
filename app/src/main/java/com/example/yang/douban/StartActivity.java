package com.example.yang.douban;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

public class StartActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000; // 两秒后进入系统
    private TextView tv_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        tv_start = (TextView) findViewById(R.id.tv_start);
        String old_date = null;
        boolean flag = false;
        int id = 0;
        SharedPreferences mShared;
        final Intent mainIntent = new Intent();
        mShared = MainApplication.getContext().getSharedPreferences("share", MODE_PRIVATE);
        Map<String, Object> mapParam = (Map<String, Object>) mShared.getAll();
        for (Map.Entry<String, Object> item_map : mapParam.entrySet()) {
            String key = item_map.getKey();
            Object value = item_map.getValue();
            if(key.equals("Date")) {
                old_date = value.toString();
            }
            else if(key.equals("flag")) {
                flag = Boolean.parseBoolean(value.toString());
            }
            else if(key.equals("id")) {
                id = Integer.parseInt(value.toString());
            }
        }
        Log.d("StartActivity", "asff"+old_date);
        if(old_date == null || old_date.length() <= 0) {
            tv_start.setText("欢迎使用！");
            mainIntent.setClass(StartActivity.this, LoginActivity.class);
        }
        else if(DateUtil.getDeltaDate(old_date) <= 5 && flag == true) {
            MainApplication application = MainApplication.getInstance();
            application.mInfoMap.put("id", id);
            mainIntent.setClass(StartActivity.this, MainActivity.class);
        }
        else if(DateUtil.getDeltaDate(old_date) > 5 || flag == false) {
            mainIntent.setClass(StartActivity.this, LoginActivity.class);
        }
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                StartActivity.this.startActivity(mainIntent);
                StartActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}