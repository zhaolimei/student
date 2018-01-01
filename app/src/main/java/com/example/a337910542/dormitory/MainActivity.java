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
import android.widget.EditText;
import android.widget.Toast;

import com.example.a337910542.dormitory.bean.LoginResult;
import com.example.a337910542.dormitory.util.HttpCallbackListener;
import com.example.a337910542.dormitory.util.HttpUtil;
import com.example.a337910542.dormitory.util.JsonUtil;
import com.example.a337910542.dormitory.util.NetUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText userName, pass;
    private Button loginBtn;

    private static final int LOGIN_INFORMATION = 1;
    //处理登陆返回结果
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_INFORMATION:
                    ifIntent((LoginResult)msg.obj);
                    break;
                default:
                    break;
            }
        }

    };

    //执行登陆跳转
    void ifIntent(LoginResult loginResult){

        if(loginResult.getErrCode().equals("0"))
        {
                Toast.makeText(MainActivity.this,"登陆成功", Toast.LENGTH_LONG).show();
                String uName = userName.getText().toString();
                Log.d("wenben",uName);
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("stuid",uName);
                editor.commit();
                Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                intent.putExtra("userName",uName);
                startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this,"用户名或密码错误", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //检查网络状态
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this,"网络OK！", Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this,"网络挂了！", Toast.LENGTH_LONG).show();
        }

        userName = (EditText) findViewById(R.id.userId);
        pass = (EditText) findViewById(R.id.pass);
        loginBtn =(Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn){
            String uName = userName.getText().toString();
            String pwd = pass.getText().toString();
            //判断输入框是否有输入
            if(uName.equals("")){
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            }
            if(pwd.equals("")){
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            }
            //如果输入框都不为空
            if(!uName.equals("") && !pwd.equals("")){
                Log.d("dfs",uName+pwd);
                //将用户名和密码添加到链接里
                final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?username="+uName+"&password="+pwd;
                //向服务器发送请求
                HttpUtil.queryFrom(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        //将返回结果解析成java对象
                        LoginResult loginResult = JsonUtil.parseLoginJson(response);
                        Log.d("msg", loginResult.getErrMsg());
                        //如果结果不为空，则将子线程的结果发送给主线程处理
                        if(loginResult != null){
                            Message msg = new Message();
                            msg.what = LOGIN_INFORMATION;
                            msg.obj = loginResult;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        }
    }
}
