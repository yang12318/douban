package com.example.yang.douban;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APITestActivity extends AppCompatActivity {

    private Button btn_login, btn_post, btn_get;
    private EditText et_cookie, et_url, et_response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitest);

        btn_login = (Button) findViewById(R.id.btn_login);
        et_cookie = (EditText) findViewById(R.id.et_cookie);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_post = (Button)findViewById(R.id.btn_post);
        et_url = (EditText) findViewById(R.id.et_url);
        et_response = (EditText) findViewById(R.id.et_response);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/login/");
                Map<String, Object> map = new HashMap<>();
                map.put("type", "email");
                map.put("text", "20001@qq.com");
                map.put("pwd", "password");
                String s = flowerHttp.firstPost(map);
                int result = 0;
                try {
                    result = new JSONObject(s).getInt("rsNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 0) {
                    showToast("未知错误");
                    return;
                } else if (result == -1) {
                    showToast("账号不存在");
                    return;
                } else if (result == -2) {
                    showToast("密码错误");
                    return;
                } else {
                    showToast("登录成功");
                    String cookie = null;
                    FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/getCsrf/");
                    String csrf = flowerHttp1.get();
                    SharedPreferences mShared;
                    mShared = MainApplication.getContext().getSharedPreferences("share", MODE_PRIVATE);
                    Map<String, Object> mapParam = (Map<String, Object>) mShared.getAll();
                    for (Map.Entry<String, Object> item_map : mapParam.entrySet()) {
                        String key = item_map.getKey();
                        Object value = item_map.getValue();
                        if(key.equals("Cookie")) {
                            cookie = value.toString();
                        }
                    }
                    SharedPreferences.Editor editor = mShared.edit();
                    editor.putString("csrfmiddlewaretoken", csrf);
                    editor.putString("Cookie", cookie);
                    editor.commit();
                }

            }
        });

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/test2/");
                String response = flowerHttp.get();
                et_response.setText(response);
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowerHttp flowerHttp  = new FlowerHttp("http://118.25.40.220/api/test2/");
                Map<String, Object> map = new HashMap<>();
                String s = flowerHttp.post(map);
                et_response.setText(s);
            }
        });
    }
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et_cookie.setText(response);
            }
        });
    }
}
