package com.example.yang.douban;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_next;
    private EditText et_username, et_password, et_nickname;
    private ImageView iv_username, iv_password, iv_nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_next = (Button) findViewById(R.id.btn_next);
        et_nickname = (EditText) findViewById(R.id.et_nameInput);
        et_password = (EditText) findViewById(R.id.et_passInput);
        et_username = (EditText) findViewById(R.id.et_userInput);
        et_username.addTextChangedListener(new JumpTextWatcher(et_username, et_password));
        et_password.addTextChangedListener(new JumpTextWatcher(et_password, et_nickname));
        iv_username = (ImageView) findViewById(R.id.iv_delUser) ;
        iv_nickname = (ImageView) findViewById(R.id.iv_delName);
        iv_password = (ImageView) findViewById(R.id.iv_delPass);
        btn_next.setOnClickListener(this);
        iv_password.setOnClickListener(this);
        iv_nickname.setOnClickListener(this);
        iv_username.setOnClickListener(this);

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_next) {
            String user = et_username.getText().toString();
            String password = et_password.getText().toString();
            String nickname = et_nickname.getText().toString();
            if(user.length() <= 0 || user == null) {
                showToast("您的邮箱未填写！");
                return;
            }
            if(! isEmail(user) || user.length() > 50) {
                showToast("邮箱格式非法，请检查");
                return;
            }
            if(password.length() <= 0 || password == null) {
                showToast("您的密码未填写!");
                return;
            }
            if(password.length() < 6) {
                showToast("密码长度过短，请换用更复杂的密码");
                return;
            }
            if(password.length() > 18) {
                showToast("密码长度过长");
                return;
            }
            if(nickname.length() <= 0 || nickname == null) {
                showToast("您的昵称未填写!");
                return;
            }
            if(nickname.length() > 18) {
                showToast("您的昵称长度过长");
                return;
            }
            /*FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/inter");
            Map<String, Object> map = new HashMap<>();
            String flag = flowerHttp.firstPost(map);
            if(Integer.parseInt(flag) == 1) {
                showToast("该邮箱号已被注册");
                return;
            }
            flowerHttp.setUrl("http://118.25.40.220/api/registe/");*/
            FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/registe/");
            Map<String, Object> map = new HashMap<>();
            map.put("type", "email");
            map.put("text", user);
            map.put("username", nickname);
            map.put("pwd", password);
            String response = flowerHttp.firstPost(map);
            int result = -10;
            try {
                result = new JSONObject(response).getInt("rsNum");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result == 0) {
                showToast("昵称已存在");
                return;
            }
            else if(result == -1) {
                showToast("该邮箱已被注册");
                return;
            }
            else if(result == -10){
                showToast("发生未知错误");
                return;
            }
            else {
                showToast("注册成功");
            }
            /*Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
            intent.putExtra("user", user);
            intent.putExtra("password", password);
            intent.putExtra("nickname", nickname);
            startActivity(intent);
            */
            //进入发送验证码页面
        }
        else if(v.getId() == R.id.iv_delUser) {
            et_username.setText(null);
        }
        else if (v.getId() == R.id.iv_delPass) {
            et_password.setText(null);
        }
        else if(v.getId() == R.id.iv_delName) {
            et_nickname.setText(null);
        }
    }


    private class JumpTextWatcher implements TextWatcher {
        private EditText mThisView = null;
        private View mNextView = null;

        public JumpTextWatcher(EditText vThis, View vNext) {
            super();
            mThisView = vThis;
            if(vNext != null) {
                mNextView = vNext;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if(str.indexOf("\r") >= 0 || str.indexOf("\n") >= 0) {      //发现输入回车或换行
                mThisView.setText(str.replace("\r", "").replace("\n", ""));
                if(mNextView != null) {
                    mNextView.requestFocus();
                    if(mNextView instanceof EditText) {         //让光标自动移动到编辑框的文本末尾
                        EditText et = (EditText) mNextView;
                        et.setSelection(et.getText().length());
                    }
                }
            }
        }
    }

    public boolean isEmail(String s) {
        if(! (s.contains(".com") && s.contains("@")))
            return false;
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z_]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(s);
        boolean isMatched = matcher.matches();
        return isMatched;
    }
}
