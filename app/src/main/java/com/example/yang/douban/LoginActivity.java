package com.example.yang.douban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_login;
    private EditText et_user, et_password;
    private TextView tv_register, tv_forget;
    private ImageView iv_user, iv_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_register = (TextView) findViewById(R.id.tv_register);
        et_password = (EditText) findViewById(R.id.et_passwordInput);
        et_user = (EditText) findViewById(R.id.et_usernameInput);
        iv_user = (ImageView) findViewById(R.id.iv_delUsername);
        iv_password = (ImageView) findViewById(R.id.iv_delPassword);
        et_user.addTextChangedListener(new JumpTextWatcher(et_user, et_password));
        btn_login.setOnClickListener(this);
        iv_password.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {
            String user = et_user.getText().toString();
            String password = et_password.getText().toString();
            if (user.length() <= 0 || user == null) {
                showToast("未填写手机号或邮箱");
                return;
            }
            if(!isEmail(user) || user.length() > 50) {
                showToast("邮箱格式非法，请检查");
                return;
            }
            //检查手机号、邮箱格式
            if (password.length() <= 0 || password == null) {
                showToast("未填写密码");
                return;
            }
            if(password.length() <6 || password.length() > 18) {
                showToast("密码不合法，请检查后重新输入");
                return;
            }
            showToast("登录成功");
            //向服务器发送信息
        }
        else if(v.getId() == R.id.tv_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.tv_forget) {
            Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.iv_delPassword) {
            et_password.setText(null);
        }
        else if(v.getId() == R.id.iv_delUsername) {
            et_user.setText(null);
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

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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