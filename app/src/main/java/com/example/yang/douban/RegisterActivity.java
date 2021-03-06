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
import android.widget.ImageButton;
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
    private ImageButton ib_back;
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
        et_username.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_password.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_nickname.setOnFocusChangeListener(new userOnFocusChanageListener());
        iv_username = (ImageView) findViewById(R.id.iv_delUser) ;
        iv_nickname = (ImageView) findViewById(R.id.iv_delName);
        iv_password = (ImageView) findViewById(R.id.iv_delPass);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        btn_next.setOnClickListener(this);
        iv_password.setOnClickListener(this);
        iv_nickname.setOnClickListener(this);
        iv_username.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_username.getText().toString();
                if(str1.length()>0){
                    iv_username.setVisibility(View.VISIBLE);
                } else {
                    iv_username.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_password.getText().toString();
                if(str1.length()>0){
                    iv_password.setVisibility(View.VISIBLE);
                } else {
                    iv_password.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_nickname.getText().toString();
                if(str1.length()>0){
                    iv_nickname.setVisibility(View.VISIBLE);
                } else {
                    iv_nickname.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/checkExist/");
            Map<String, Object> map = new HashMap<>();
            map.put("type", "email");
            map.put("text", user);
            String response = flowerHttp.firstPost(map);
            int result = -10;
            try {
                result = new JSONObject(response).getInt("rsNum");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result == 0) {
                Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                intent.putExtra("user", user);
                intent.putExtra("password", password);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                //进入发送验证码页面
            }
            else if(result == 1) {
                showToast("该邮箱已被注册");
                return;
            }
            else if(result == -10){
                showToast("服务器未响应");
                return;
            }
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
        else if(v.getId() == R.id.ib_back) {
            finish();
        }
    }
    private class userOnFocusChanageListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if ((v.getId() == et_username.getId()) & (et_username.getText().length() > 0)) {
                iv_username.setVisibility(View.VISIBLE);
            } else iv_username.setVisibility(View.INVISIBLE);
            if ((v.getId() == et_password.getId()) & (et_password.getText().length() > 0)) {
                iv_password.setVisibility(View.VISIBLE);
            } else iv_password.setVisibility(View.INVISIBLE);
            if ((v.getId() == et_nickname.getId()) & (et_nickname.getText().length() > 0)) {
                iv_nickname.setVisibility(View.VISIBLE);
            } else iv_nickname.setVisibility(View.INVISIBLE);
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
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(s);
        boolean isMatched = matcher.matches();
        return isMatched;
    }
}
