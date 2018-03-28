package com.example.yang.douban;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by youxihouzainali on 2018/3/25.
 */

public class FlowerHttp {
    String url;
    String responseData = "";

    public FlowerHttp(String url) {
        this.url = url;
    }

    public String post(final Map<String, Object> map) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Callable<String> callable = new CallableThread2(url, map);
        Future future = executorService.submit(callable);
        try {
            responseData = future.get().toString();
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "等待";
    }

    public String get() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Callable<String> callable = new CallableThread1(url);
        Future future = executorService.submit(callable);
        try {
            responseData = future.get().toString();
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "等待";
    }

    public String getResponseData() {
        return responseData;
    }

}
