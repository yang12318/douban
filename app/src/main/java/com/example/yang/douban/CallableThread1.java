package com.example.yang.douban;

import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by youxihouzainali on 2018/3/25.
 */

public class CallableThread1 implements Callable<String> {

    private String url;

    public CallableThread1(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        return responseData;
    }
}
