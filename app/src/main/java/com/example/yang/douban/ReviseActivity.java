package com.example.yang.douban;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.bumptech.glide.Glide;
import com.example.yang.douban.Bean.Article;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.CityListSelectActivity;
import com.lljjcoder.style.citylist.bean.CityInfoBean;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.lljjcoder.style.citypickerview.CityPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Build.TYPE;

public class ReviseActivity extends AppCompatActivity {

    public static final int CHOOSE_PHOTO = 2;
    private LinearLayout ll_revise_head, ll_revise_pass, ll_revise_birth, ll_revise_location;
    private LinearLayout ll_revise_gender;
    private ImageView iv_head;
    private ImageButton ib_back;
    private String imagepath = null;
    private TextView tv_birth, tv_location, tv_email, tv_gender;
    CityPickerView mPicker = new CityPickerView();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);
        mPicker.init(this);
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
        ib_back = (ImageButton) findViewById(R.id.ib_revise_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FlowerHttp flowerHttp = new FlowerHttp("http://118.25.40.220/api/getInfo/");
        Map<String, Object> map = new HashMap<>();
        String response = flowerHttp.post(map);
        int rsNum = 10;
        String birthday = null, gender = null, address = null, email = null, src = null, username = null;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            birthday = jsonObject.getString("birthday");
            gender = jsonObject.getString("gender");
            if(gender.equals("M") || gender.equals("m"))
                gender = "男";
            else if(gender.equals("F") || gender.equals("f"))
                gender = "女";
            address = jsonObject.getString("address");
            email = jsonObject.getString("email");
            username = jsonObject.getString("username");
            src = "http://118.25.40.220" + jsonObject.getString("src");
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
            Glide.with(this).load(src).into(iv_head);
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
                CityConfig cityConfig = new CityConfig.Builder()
                        .title("选择城市")//标题
                        .titleTextSize(18)//标题文字大小
                        .titleTextColor("#585858")//标题文字颜  色
                        .titleBackgroundColor("#E9E9E9")//标题栏背景色
                        .confirTextColor("#585858")//确认按钮文字颜色
                        .confirmText("确定")//确认按钮文字
                        .confirmTextSize(16)//确认按钮文字大小
                        .cancelTextColor("#585858")//取消按钮文字颜色
                        .cancelText("取消")//取消按钮文字
                        .cancelTextSize(16)//取消按钮文字大小
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                        .showBackground(true)//是否显示半透明背景
                        .visibleItemsCount(7)//显示item的数量
                        .province("山东省")//默认显示的省份
                        .city("青岛市")//默认显示省份下面的城市
                        //.district("崂山区")//默认显示省市下面的区县数据
                        .provinceCyclic(false)//省份滚轮是否可以循环滚动
                        .cityCyclic(false)//城市滚轮是否可以循环滚动
                        .districtCyclic(false)//区县滚轮是否循环滚动
                        //.setCustomItemLayout(R.layout.item_city)//自定义item的布局
                        //.setCustomItemTextViewId(R.id.item_city_name_tv)//自定义item布局里面的textViewid
                        .drawShadows(false)//滚轮不显示模糊效果
                        .setLineColor("#03a9f4")//中间横线的颜色
                        .setLineHeigh(5)//中间横线的高度
                        .setShowGAT(true)//是否显示港澳台数据，默认不显示
                        .build();
                mPicker.setConfig(cityConfig);
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        String Province = null;
                        String City = null;
                        if (province != null) {
                            Province = province.getName();
                        }
                        if (city != null) {
                            City = city.getName();
                        }
                        if (district != null) {
                        }
                        String location = Province + "-" + City;
                        tv_location.setText(location);
                        FlowerHttp flowerHttp1 = new FlowerHttp("http://118.25.40.220/api/changeInfo/");
                        Map<String, Object> map = new HashMap<>();
                        map.put("birthday", tv_birth.getText().toString());
                        map.put("gender", tv_gender.getText().toString());
                        map.put("address", location);
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
                    @Override
                    public void onCancel() {
                        showToast("已取消");
                    }
                });
                mPicker.showCityPicker( );
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
        if(requestCode == CHOOSE_PHOTO) {
            if(resultCode == RESULT_OK) {
                //判断手机系统版本号
                if(Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                }
                else {
                    handleImageBeforeKitKat(data);
                }
            }
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
            File file = new File(imagepath);
            if (!file.exists()) {
                showToast("文件不存在");
            }
            else {
                SharedPreferences mShared;
                mShared = MainApplication.getContext().getSharedPreferences("share", MODE_PRIVATE);
                String csrfmiddlewaretoken = null;
                String cookie = null;
                Map<String, Object> mapParam = (Map<String, Object>) mShared.getAll();
                for (Map.Entry<String, Object> item_map : mapParam.entrySet()) {
                    String key = item_map.getKey();
                    Object value = item_map.getValue();
                    if(key.equals("Cookie")) {
                        cookie = value.toString();
                    }
                    else if(key.equals("csrfmiddlewaretoken")) {
                        csrfmiddlewaretoken = value.toString();
                    }
                }
                MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                if (file != null) {
                    // MediaType.parse() 里面是上传的文件类型。
                    RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                    String filename = file.getName();
                    // 参数分别为， 请求key ，文件名称 ， RequestBody
                    requestBody.addFormDataPart("image", filename, body);
                }
                requestBody.addFormDataPart("csrfmiddlewaretoken",csrfmiddlewaretoken);
                Request request = new Request.Builder()
                        .url("http://118.25.40.220/api/changeHeadImage/")
                        .header("Cookie", cookie)
                        .header("Content-Type", "multipart/form-data")
                        .post(requestBody.build()).build();
                // readTimeout("请求超时时间" , 时间单位);
                OkHttpClient okHttpClient = new OkHttpClient();
                Response response = null;
                String responseData = null;
                try {
                    //response = okHttpClient.newBuilder().readTimeout(20000, TimeUnit.MILLISECONDS).build().newCall(request).execute();
                    response = okHttpClient.newCall(request).execute();
                    responseData = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int result = 10;
                try {
                    result = new JSONObject(responseData).getInt("rsNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(result == 1) {
                    showToast("修改头像成功");
                }
                else if(result == 0) {
                    showToast("未知错误");
                }
                else if(result == -1) {
                    showToast("文件太大");
                }
                else if(result == -2) {
                    showToast("没有检测到登录");
                }
                else if(result == 10) {
                    showToast("服务器未响应");
                }
            }
            //showToast(imagepath);
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
