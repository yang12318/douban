package com.example.yang.douban;

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

/**
 * Created by youxihouzainali on 2018/3/25.
 */


public class CallableThread2 implements Callable<String> {

    private String url;
    private Map<String, Object> map;

    public CallableThread2(String url, Map<String, Object> map) {
        this.url = url;
        this.map = map;
    }

    @Override
    public String call() throws Exception {
        final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
        Request request = new Request.Builder()
                .url("http://118.25.40.220/api/getCsrf")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        Headers headers = response.headers();
        HttpUrl url1 = request.url();
        List<Cookie> cookies = Cookie.parseAll(url1, headers);
        if (cookies != null) {
            okHttpClient.cookieJar().saveFromResponse(url1, cookies);
        }
        String csrf = response.body().string();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("csrfmiddlewaretoken", csrf);
        Set<String> sets = map.keySet();
        for (String set : sets) {
            formBody.add(set, String.valueOf(map.get(set)));
        }
        RequestBody requestBody = formBody.build();
        StringBuilder cookieStr = new StringBuilder();
        List<Cookie> cookies1 = okHttpClient.cookieJar().loadForRequest(request.url());
        for(Cookie cookie : cookies1){
            cookieStr.append(cookie.name()).append("=").append(cookie.value()+";");
        }
        Request request2 = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Cookie", cookieStr.toString())
                .build();
        Response response2 = okHttpClient.newCall(request2).execute();
        String responseData = response2.body().string();
        return responseData;
    }
}
