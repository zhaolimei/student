package com.example.a337910542.dormitory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a337910542.dormitory.bean.DormitoryInformation;
import com.example.a337910542.dormitory.util.HttpCallbackListener;
import com.example.a337910542.dormitory.util.HttpUtil;
import com.example.a337910542.dormitory.util.JsonUtil;


public class DormitoryActivity extends AppCompatActivity implements View.OnClickListener{


    private Button button5;
    private Button button13;
    private Button button14;
    private Button button8;
    private Button button9;
    private static  final int DORMITORY_INFORMATION  = 1;


    //处理子线程返回的个人信息
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DORMITORY_INFORMATION:
                    updateInformation((DormitoryInformation)(msg.obj));
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory);
        initView();
        button5.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button13.setOnClickListener(this);
        button14.setOnClickListener(this);
        ToolBar toolBar = new ToolBar(getWindow().getDecorView());
        toolBar.back.setOnClickListener(this);
        toolBar.personalInformation.setOnClickListener(this);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String gender = pref.getString("gender", "");

        //get请求
        final String informationUrl = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender="+gender;
        Log.d("userGender",informationUrl);
        queryInformation(informationUrl);
    }

    // 初始化信息
    void initView(){
        button5 = (Button) findViewById(R.id.fifth_floor);
        button13 = (Button)findViewById(R.id.thirteenth_floor);
        button14 = (Button)findViewById(R.id.fourteenth_floor);
        button8 = (Button)findViewById(R.id.eighth_floor);
        button9 = (Button)findViewById(R.id.nineth_floor);
    }

    //更新信息

    void updateInformation(DormitoryInformation dormitoryInformation){
        button5.setText(dormitoryInformation.getFifthNumber());
        button13.setText(dormitoryInformation.getThirteenthNumber());
        button14.setText(dormitoryInformation.getFourteenthNumber());
        button8.setText(dormitoryInformation.getEighthNumber());
        button9.setText(dormitoryInformation.getNinethNumber());
    }

    //向服务器请求查询并返回信息
    void queryInformation(final String url){
        HttpUtil.queryFrom(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //将返回结果解析成java对象
                DormitoryInformation dormitoryInformation= JsonUtil.parseDormitoryInformation(response);
                //如果结果不为空，则将子线程的结果发送给主线程处理
                if(dormitoryInformation != null){
                    Message msg = new Message();
                    msg.what = DORMITORY_INFORMATION;
                    msg.obj = dormitoryInformation;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DormitoryActivity.this, SelectionActivity.class);
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        switch (v.getId()) {
            case R.id.fifth_floor:
                if(Integer.parseInt(button5.getText().toString()) > 0){
                    editor.putString("building","5");
                    editor.commit();
                    startActivity(intent);
                }
                break;
            case R.id.thirteenth_floor:

                if(Integer.parseInt(button13.getText().toString())>0){
                    editor.putString("building", "13");
                    editor.commit();
                    startActivity(intent);
                }
                break;
            case R.id.fourteenth_floor:
                if(Integer.parseInt(button14.getText().toString()) > 0){
                    editor.putString("building", "14");
                    editor.commit();
                    startActivity(intent);
                }
                break;
            case R.id.eighth_floor:
                if(Integer.parseInt(button8.getText().toString()) > 0){
                    editor.putString("building", "8");
                    editor.commit();
                    startActivity(intent);
                }
                break;
            case R.id.nineth_floor:
                if(Integer.parseInt(button9.getText().toString()) >0 ){
                    editor.putString("building","9");
                    editor.commit();
                    startActivity(intent);
                }
                break;
            case R.id.back_login:
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
                break;
            case R.id.personal_information:
                Intent toPersonal = new Intent(this, PersonalActivity.class);
                startActivity(toPersonal);
                break;
        }
    }
}
