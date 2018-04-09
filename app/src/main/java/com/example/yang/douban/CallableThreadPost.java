package com.example.yang.douban;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by youxihouzainali on 2018/4/3.
 */

public class CallableThreadPost implements Callable<String> {
    private String url;
    private Map<String, Object> map;

    public CallableThreadPost(String url, Map<String, Object> map) {
        this.url = url;
        this.map = map;
    }

    @Override
    public String call() throws Exception {
        SharedPreferences mShared;
        mShared = MainApplication.getContext().getSharedPreferences("share", MODE_PRIVATE);
        String csrfmiddlewaretoken = null;
        String cookie = null;
        Map<String, Object> mapParam = (Map<String, Object>) mShared.getAll();
        for (Map.Entry<String, Object> item_map : mapParam.entrySet()) {
            String key = item_map.getKey();
            Object value = item_map.getValue();
            if(key.equals("Cookie")) {
                cookie = value.toString();
            }
            else if(key.equals("csrfmiddlewaretoken")) {
                csrfmiddlewaretoken = value.toString();
            }
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("csrfmiddlewaretoken", csrfmiddlewaretoken);
        Set<String> sets = map.keySet();
        for (String set : sets) {
            formBody.add(set, String.valueOf(map.get(set)));
        }
        RequestBody requestBody = formBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Cookie", cookie)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String responseData = response.body().string();
        return responseData;
    }

}
