package com.example.yang.douban;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by youxihouzainali on 2018/4/3.
 */

public class CallableThreadGet implements Callable<String> {
    private String url;

    public CallableThreadGet(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        SharedPreferences mShared;
        mShared = MyApplication.getContext().getSharedPreferences("share", MODE_PRIVATE);
        String csrftoken = null;
        String sessionID = null;
        Map<String, Object> mapParam = (Map<String, Object>) mShared.getAll();
        for (Map.Entry<String, Object> item_map : mapParam.entrySet()) {
            String key = item_map.getKey();
            Object value = item_map.getValue();
            if(key.equals("csrftoken")) {
                csrftoken = value.toString();
            }
            else if(key.equals("sessionId")) {
                sessionID = value.toString();
            }
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("csrftoken", csrftoken)
                .header("sessionID", sessionID)
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        return responseData;
    }
}
