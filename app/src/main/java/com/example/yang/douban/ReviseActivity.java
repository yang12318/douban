package com.example.yang.douban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.Calendar;

import com.example.yang.douban.Bean.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ReviseActivity extends AppCompatActivity {

    private LinearLayout ll_revise_head, ll_revise_pass, ll_revise_birth, ll_revise_location;
    private ImageView iv_head;
    private TextView tv_birth, tv_location, tv_email, tv_gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);
        ll_revise_head = (LinearLayout) findViewById(R.id.ll_revise_head);
        ll_revise_pass = (LinearLayout) findViewById(R.id.ll_revise_pass);
        ll_revise_location = (LinearLayout) findViewById(R.id.ll_revise_location);
        ll_revise_birth = (LinearLayout) findViewById(R.id.ll_revise_birth);
        tv_birth = (TextView) findViewById(R.id.tv_birth);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getInfo/");
        Map<String, Object> map = new HashMap<>();
        String response = flowerHttp.post(map);
        int rsNum = 0;
        String birthday = null, gender = null, address = null, email = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            birthday = jsonObject.getString("birthday");
            gender = jsonObject.getString("gender");
            address = jsonObject.getString("address");
            email = jsonObject.getString("email");
            jsonObject = jsonArray.getJSONObject(1);
            rsNum = jsonObject.getInt("rsNum");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(rsNum == 0) {
            showToast("出现未知错误");
        }
        else if(rsNum == -1) {
            showToast("用户不存在");
        }
        else if(rsNum == 1) {
            tv_birth.setText(birthday);
            tv_location.setText(address);
            tv_email.setText(email);
            tv_gender.setText(gender);
        }
        ll_revise_birth.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                String birthday = null;
                new BirthActivity(ReviseActivity.this, 0, new BirthActivity.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker DatePicker, int Year, int MonthOfYear,
                                          int DayOfMonth) {
                        String birthday = String.format("%d-%d-%d", Year, MonthOfYear + 1,DayOfMonth);
                        tv_birth.setText(birthday);
                        FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/changeInfo/");
                        Map<String, Object> map = new HashMap<>();
                        map.put("birthday", birthday);
                        map.put("gender", tv_gender.getText().toString());
                        map.put("address", tv_location.getText().toString());
                        String response = flowerHttp1.post(map);
                        int result = 0;
                        try {
                            result = new JSONObject(response).getInt("rsNum");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(result == 1) {
                            showToast("修改成功");
                        }
                        else if(result == 0) {
                            showToast("未知错误");
                            return;
                        }
                        else if(result == -1) {
                            showToast("用户不存在");
                            return;
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();

            }
        });
        ll_revise_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ll_revise_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviseActivity.this, ChangeCodeActivity.class);
                startActivity(intent);
            }
        });
        ll_revise_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(ReviseActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
