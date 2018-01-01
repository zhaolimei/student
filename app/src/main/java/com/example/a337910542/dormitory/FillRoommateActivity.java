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
import android.widget.TextView;
import android.widget.Toast;

import com.example.a337910542.dormitory.bean.SelectionResult;
import com.example.a337910542.dormitory.util.HttpCallbackListener;
import com.example.a337910542.dormitory.util.HttpUtil;
import com.example.a337910542.dormitory.util.JsonUtil;

public class FillRoommateActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SELECTION_RESULT = 1;
    private EditText stuid1, code1, stuid2, code2, stuid3, code3;
    private Button commitButton;
    private TextView stuid,stuname,stugender,targetBuildingNum,sameroom,stuidt1, vcodet1, stuidt2, vcodet2, stuidt3, vcodet3;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECTION_RESULT:
                    operateSelectionResult((SelectionResult)(msg.obj));
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_roommate);
        initView();
        title title = new title(getWindow().getDecorView());
        title.back.setOnClickListener(this);

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        Log.d("几人",pref.getString("number",""));
        stuid.setText(pref.getString("stuid",""));
        stuname.setText(pref.getString("stuname",""));
        stugender.setText(pref.getString("stugender",""));
        targetBuildingNum.setText(pref.getString("building","")+"号楼");


        Integer number = Integer.parseInt(pref.getString("number","0"));
        switch(number) {
            case 1:
                sameroom.setVisibility(View.INVISIBLE);
                stuid1.setVisibility(View.INVISIBLE);
                code1.setVisibility(View.INVISIBLE);
                stuidt1.setVisibility(View.INVISIBLE);
                vcodet1.setVisibility(View.INVISIBLE);
                stuid2.setVisibility(View.INVISIBLE);
                code2.setVisibility(View.INVISIBLE);
                stuid3.setVisibility(View.INVISIBLE);
                code3.setVisibility(View.INVISIBLE);
                stuidt2.setVisibility(View.INVISIBLE);
                vcodet2.setVisibility(View.INVISIBLE);
                stuidt3.setVisibility(View.INVISIBLE);
                vcodet3.setVisibility(View.INVISIBLE);

            case 2:
                stuid2.setVisibility(View.INVISIBLE);
                code2.setVisibility(View.INVISIBLE);
                stuid3.setVisibility(View.INVISIBLE);
                code3.setVisibility(View.INVISIBLE);
                stuidt2.setVisibility(View.INVISIBLE);
                vcodet2.setVisibility(View.INVISIBLE);
                stuidt3.setVisibility(View.INVISIBLE);
                vcodet3.setVisibility(View.INVISIBLE);
            case 3:
                stuid3.setVisibility(View.INVISIBLE);
                code3.setVisibility(View.INVISIBLE);
                stuidt3.setVisibility(View.INVISIBLE);
                vcodet3.setVisibility(View.INVISIBLE);
        }
        commitButton.setOnClickListener(this);
    }

    void initView() {
        sameroom=(TextView)findViewById(R.id.sameroom);
        stuid=(TextView)findViewById(R.id.stuid);
        stuname=(TextView)findViewById(R.id.stuname);
        stugender=(TextView)findViewById(R.id.stugender);
        targetBuildingNum=(TextView)findViewById(R.id.targetBuildingNum);
        commitButton = (Button)findViewById(R.id.commitInformation);
        stuid1 = (EditText) findViewById(R.id.first_stuid);
        code1 =(EditText) findViewById(R.id.code1);
        stuid2 = (EditText) findViewById(R.id.second_stuid);
        code2 = (EditText) findViewById(R.id.code2);
        stuid3 = (EditText) findViewById(R.id.third_stuid);
        code3 = (EditText) findViewById(R.id.code3);
        stuidt1 =(TextView) findViewById(R.id.stuid_text1);
        vcodet1 = (TextView) findViewById(R.id.vcode_text1);
        stuidt2  = (TextView) findViewById(R.id.stuid_text2);
        vcodet2 = (TextView) findViewById(R.id.vcode_text2);
        stuidt3 = (TextView) findViewById(R.id.stuid_text3);
        vcodet3 = (TextView) findViewById(R.id.vcode_text3);
    }

    private void operateSelectionResult(SelectionResult selectionResult){
        if(selectionResult.getErrcode().equals("0")){
            Toast.makeText(this,"选择成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, PersonalActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this,"选择失败，请重新选择", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, DormitoryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.commitInformation){
            SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
            Integer number = Integer.parseInt(pref.getString("number",""));
            boolean bool = true;
            EditText[] el = new EditText[]{stuid1, code1, stuid2, code2, stuid3, code3};
            for(int i=0; i<number-1; i++){
                if(el[i].getText().toString().equals("")){
                    Toast.makeText(this,"信息不能为空", Toast.LENGTH_LONG).show();
                    bool = false;
                }
            }

            if(bool == true){
                String data = "num="+number+"&stuid="+pref.getString("stuid","");
                for(int i=0; i<2*number-2; i+=2){
                    data = data + "&stu"+i+"id="+el[i].getText().toString()+"&v"+i+"code="+el[i+1].getText().toString();
                }
                data = data + "&buildingNo=" + pref.getString("building","");
                final String postdata = new String(data);
                final String url = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
                HttpUtil.postSelection(url, postdata, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        SelectionResult selectionResult = JsonUtil.parseSelectionResult(response);
                        if(selectionResult != null){
                            Message msg = new Message();
                            msg.what = SELECTION_RESULT;
                            msg.obj = selectionResult;
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
        else if(v.getId() == R.id.back_login){
            Intent intentBack = new Intent(this, MainActivity.class);
            startActivity(intentBack);
        }

    }
}
