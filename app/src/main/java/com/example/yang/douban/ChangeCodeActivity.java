package com.example.yang.douban;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeCodeActivity extends AppCompatActivity {

    private EditText et_old, et_new;
    private Button btn_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_code);
        et_old = (EditText) findViewById(R.id.et_old);
        et_new = (EditText) findViewById(R.id.et_new);
        btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = null, newPass = null;
                oldPass = et_old.getText().toString();
                newPass = et_new.getText().toString();
                if(oldPass == null || oldPass.length() <= 0) {
                    showToast("原密码未填写");
                    return;
                }
                if(newPass == null || newPass.length() <= 0) {
                    showToast("新密码未填写");
                    return;
                }
                if(newPass.length() < 6) {
                    showToast("密码长度过短，请换用更复杂的密码");
                    return;
                }
                if(newPass.length() > 18) {
                    showToast("密码长度过长");
                    return;
                }
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/changePwd/");
                Map<String, Object> map = new HashMap<>();
                map.put("oldPwd", oldPass);
                map.put("newPwd", newPass);
                String response = flowerHttp.post(map);
                int result = 0;
                try {
                    result = new JSONObject(response).getInt("rsNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 0) {
                    showToast("未知错误");
                    return;
                } else if (result == -1) {
                    showToast("原密码错误");
                    return;
                } else if(result == 1){
                    showToast("密码修改成功，请重新登录");
                    SharedPreferences sharedPreferences;
                    sharedPreferences = getSharedPreferences("share", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(ChangeCodeActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(ChangeCodeActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
