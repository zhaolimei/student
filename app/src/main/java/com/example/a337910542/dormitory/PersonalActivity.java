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
import android.widget.TextView;
import android.widget.Toast;

import com.example.a337910542.dormitory.bean.StudentInformation;
import com.example.a337910542.dormitory.util.HttpCallbackListener;
import com.example.a337910542.dormitory.util.HttpUtil;
import com.example.a337910542.dormitory.util.JsonUtil;


public class PersonalActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView studentId;
    private TextView studentName;
    private TextView studentGender;
    private TextView studentVcode;
    private TextView studentRoom;
    private TextView studentBuilding;
    private TextView studentLocation;
    private TextView studentGrade;
    private Button searchDormitory;

    private static final int CHECK_INFORMATION = 1;

    //处理子线程返回的个人信息
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_INFORMATION:
                    updateInformation((StudentInformation)(msg.obj));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String uName = pref.getString("stuid","");
        Log.d("personal", uName);

        searchDormitory.setOnClickListener(this);
        title title = new title(getWindow().getDecorView());
        title.back.setOnClickListener(this);

        //get请求
        final String informationUrl = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+uName;
        queryInformation(informationUrl);
    }

    void initView(){
        studentId = (TextView) findViewById(R.id.stuid);
        studentName =(TextView)  findViewById(R.id.stuname);
        studentGender =(TextView)  findViewById(R.id.stugender);
        studentVcode =(TextView)  findViewById(R.id.stuvcode);
        studentRoom = (TextView) findViewById(R.id.sturoom);
        studentBuilding =(TextView)  findViewById(R.id.stubuilding);
        searchDormitory =(Button) findViewById(R.id.search_dormitory);
    }

    //向服务器请求查询并返回信息
    void queryInformation(final String url){
        HttpUtil.queryFrom(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //将返回结果解析成java对象
                StudentInformation studentInformation = JsonUtil.parseInformationJson(response);
                //如果结果不为空，则将子线程的结果发送给主线程处理
                if(studentInformation != null){
                    Message msg = new Message();
                    msg.what = CHECK_INFORMATION;
                    msg.obj = studentInformation;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    //更新信息
    void updateInformation(StudentInformation studentInformation){
        studentId.setText(studentInformation.getStuId());
        studentName.setText(studentInformation.getStuName());
        studentGender.setText(studentInformation.getStuGender());
        studentVcode.setText(studentInformation.getStuVcode());
        studentRoom.setText(studentInformation.getStuRoom());
        studentBuilding.setText(studentInformation.getStuBuilding());
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("name", studentInformation.getStuName());
        if(studentInformation.getStuGender().equals("男")){
            editor.putString("gender", "1");
        }else {
            editor.putString("gender", "2");
        }
        editor.putString("stuid",studentInformation.getStuId());//保存用户名 学号 性别信息
        editor.putString("stuname",studentInformation.getStuName());
        editor.putString("stugender",studentInformation.getStuGender());
        editor.putString("vcode", studentInformation.getStuVcode());
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        Log.d("办理","search");
        if(v.getId() == R.id.search_dormitory){

            if(studentBuilding.getText().equals("暂未办理入住"))
            {
                Intent intent = new Intent(PersonalActivity.this, SelectionActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(this, "已办理住宿", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.back_login){
            Intent intentBack = new Intent(this, MainActivity.class);
            startActivity(intentBack);
        }
    }
}
