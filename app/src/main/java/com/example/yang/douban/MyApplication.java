package com.example.yang.douban;

import android.app.Application;
import android.content.Context;

/**
 * Created by youxihouzainali on 2018/4/3.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
