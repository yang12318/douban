package com.example.yang.douban;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.Calendar;

import com.bumptech.glide.Glide;
import com.example.yang.douban.Bean.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ReviseActivity extends AppCompatActivity {

    public static final int CHOOSE_PHOTO = 2;
    private LinearLayout ll_revise_head, ll_revise_pass, ll_revise_birth, ll_revise_location;
    private LinearLayout ll_revise_gender;
    private ImageView iv_head;
    private String imagepath = null;
    private TextView tv_birth, tv_location, tv_email, tv_gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);
        ll_revise_head = (LinearLayout) findViewById(R.id.ll_revise_head);
        ll_revise_pass = (LinearLayout) findViewById(R.id.ll_revise_pass);
        ll_revise_location = (LinearLayout) findViewById(R.id.ll_revise_location);
        ll_revise_birth = (LinearLayout) findViewById(R.id.ll_revise_birth);
        ll_revise_gender = (LinearLayout) findViewById(R.id.ll_revise_gender);
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
        ll_revise_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_sex();
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
                if (ContextCompat.checkSelfPermission(ReviseActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.
                        PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ReviseActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(ReviseActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                else {
                    showToast("您拒绝了我们的权限请求");
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if(Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    }
                    else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagepath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagepath = getImagePath(uri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagepath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagepath = uri.getPath();
            }
            displayImage();
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagepath = getImagePath(uri, null);
        displayImage();
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage() {
        if(imagepath != null) {
            //Glide.with(this).load(imagepath).into(iv1);
            Glide.with(this).load(imagepath).into(iv_head);
        }
        else {
            showToast("没有找到图片");
        }
    }

    public void change_sex(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //定义一个AlertDialog
        String[] strarr = {"男","女","保密"};
        builder.setItems(strarr, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
                String sex = "保密";
                if (arg1 == 0) {
                    sex = "男";
                }else if(arg1 == 1){
                    sex = "女";
                }
                else {
                    sex = "保密";
                }
                tv_gender.setText(sex);
                FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/changeInfo/");
                Map<String, Object> map = new HashMap<>();
                map.put("birthday", tv_birth.getText().toString());
                map.put("gender", sex);
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
        });
        builder.show();
    }
}
