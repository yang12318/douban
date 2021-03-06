package com.example.yang.douban;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ChangeCodeActivity extends AppCompatActivity {

    private EditText et_old, et_new;
    private Button btn_change;
    private ImageButton ib_back;
    private ImageView iv_old,iv_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_code);
        et_old = (EditText) findViewById(R.id.et_old);
        et_new = (EditText) findViewById(R.id.et_new);
        et_old.addTextChangedListener(new JumpTextWatcher(et_old, et_new));
        et_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_old.getText().toString();
                if(str1.length()>0){
                    iv_old.setVisibility(View.VISIBLE);
                } else {
                    iv_old.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        et_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1 =  et_new.getText().toString();
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

        et_old.setOnFocusChangeListener(new userOnFocusChanageListener());
        et_new.setOnFocusChangeListener(new userOnFocusChanageListener());
        iv_old = (ImageView) findViewById(R.id.iv_delOld) ;
        iv_new = (ImageView) findViewById(R.id.iv_delNewPassword) ;
        ib_back = (ImageButton)  findViewById(R.id.ib_change_back);
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
                    Intent intent = new Intent(ChangeCodeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_old.setText(null);
            }
        });
        iv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_new.setText(null);
            }
        });
    }
    private class userOnFocusChanageListener implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if ((v.getId() == et_old.getId()) & (et_old.getText().length() > 0)) {
                iv_old.setVisibility(View.VISIBLE);
            } else iv_old.setVisibility(View.INVISIBLE);
            if ((v.getId() == et_new.getId()) & (et_new.getText().length() > 0)) {
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
            String str =  s.toString();
            if(str.indexOf("\r")  >= 0  || str.indexOf("\n") >= 0) {      //发现输入回车或换行
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
        Toast.makeText(ChangeCodeActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
