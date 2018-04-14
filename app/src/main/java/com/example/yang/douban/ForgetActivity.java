package com.example.yang.douban;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnFocusChangeListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private Button btn_finish;
    private EditText et_email, et_newCode, et_code;
    private CountDownTimerButton btn_code;
    private ImageView iv_email,iv_code,iv_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        imageButton = (ImageButton) findViewById(R.id.ib_forget_back);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_code = (CountDownTimerButton) findViewById(R.id.btn_code);
        iv_email = (ImageView) findViewById(R.id.iv_delEmail);
        iv_code = (ImageView) findViewById(R.id.iv_del_forgetcode);
        iv_new = (ImageView) findViewById(R.id.iv_delNew);
        et_code = (EditText) findViewById(R.id.et_code);
        et_email = (EditText) findViewById(R.id.et_email);
        et_newCode = (EditText) findViewById(R.id.et_newcode);
        et_email.addTextChangedListener(new JumpTextWatcher(et_email, et_code));
        et_code.addTextChangedListener(new JumpTextWatcher(et_code, et_newCode));
        et_newCode.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_code.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_email.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_email.getText().toString();
                if(str1.length()>0){
                    iv_email.setVisibility(View.VISIBLE);
                } else {
                    iv_email.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_code.getText().toString();
                if(str1.length()>0){
                    iv_code.setVisibility(View.VISIBLE);
                } else {
                    iv_code.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_newCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_newCode.getText().toString();
                if(str1.length()>0){
                    iv_new.setVisibility(View.VISIBLE);
                } else {
                    iv_new.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = null, newPass = null, code = null;
                email = et_email.getText().toString();
                newPass = et_newCode.getText().toString();
                code = et_code.getText().toString();
                if(email == null || email.length() <= 0) {
                    showToast("您未填写邮箱");
                    return;
                }
                if(! isEmail(email) || email.length() > 50) {
                    showToast("邮箱格式非法，请检查");
                    return;
                }
                if(newPass == null || newPass.length() <= 0) {
                    showToast("您还未填写新密码");
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
                if(code == null || code.length() <= 0) {
                    showToast("您还未填写验证码");
                    return;
                }
                FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getBackPwd/");
                Map<String, Object> map = new HashMap<>();
                map.put("type", "email");
                map.put("text", email);
                map.put("code", code);
                map.put("pwd", newPass);
                String response = flowerHttp.firstPost(map);
                int result = -10;
                try {
                    result = new JSONObject(response).getInt("rsNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result == 1) {
                    showToast("修改密码成功，请登录");
                    Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else if(result == 0) {
                    showToast("未知错误");
                    return;
                }
                else if(result == -1) {
                    showToast("验证码错误");
                    return;
                }
                else if(result == -10) {
                    showToast("服务器未响应");
                    return;
                }
            }
        });
    }
    private class userOnFocusChanageListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if ((v.getId() == et_email.getId()) & (et_email.getText().length() > 0)) {
                iv_email.setVisibility(View.VISIBLE);
            } else iv_email.setVisibility(View.INVISIBLE);
            if ((v.getId() == et_code.getId()) & (et_code.getText().length() > 0)) {
                iv_code.setVisibility(View.VISIBLE);
            } else iv_code.setVisibility(View.INVISIBLE);
            if ((v.getId() == et_newCode.getId()) & (et_newCode.getText().length() > 0)) {
                iv_new.setVisibility(View.VISIBLE);
            } else iv_new.setVisibility(View.INVISIBLE);
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
    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void sendCode() {
        String email = null;
        email = et_email.getText().toString();
        if(email.length() <= 0 || email == null) {
            showToast("您还未填写邮箱");
            return;
        }
        if(! isEmail(email) || email.length() > 50) {
            showToast("邮箱格式非法，请检查");
            return;
        }
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/checkExist/");
        Map<String, Object> map = new HashMap<>();
        map.put("type", "email");
        map.put("text", email);
        String response = flowerHttp.firstPost(map);
        int result = -10;
        try {
            result = new JSONObject(response).getInt("rsNum");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(result == 0) {
            showToast("该邮箱未被注册");
            return;
        }
        else if(result == 1) {
            FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/getCode/");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("type", "email");
            map1.put("getType", "getBackPwd");
            map1.put("text", email);
            String response1 = flowerHttp1.firstPost(map1);
            int result1 = -10;
            try {
                result1 = new JSONObject(response1).getInt("rsNum");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result1 == 0) {
                showToast("未知错误");
                return;
            }
            else if(result1 == 1) {
                showToast("已向邮箱发送验证码");
                return;
            }
            else if(result1 == -10){
                showToast("服务器未响应");
                return;
            }
        }
        else if(result == -10){
            showToast("服务器未响应");
            return;
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
