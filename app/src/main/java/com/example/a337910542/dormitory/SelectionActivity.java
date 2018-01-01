package com.example.a337910542.dormitory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String s = pref.getString("building","");
        Log.d("几号楼", s);
        initView();
        title title = new title(getWindow().getDecorView());
        title.back.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    void initView(){
        button1 =(Button) findViewById(R.id.one_person);
        button2 =(Button) findViewById(R.id.two_person);
        button3 = (Button) findViewById(R.id.three_person);
        button4 = (Button) findViewById(R.id.four_person);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        switch(v.getId()){
            case R.id.one_person:
                editor.putString("number","1");
                editor.commit();
                Log.d("单人选","单人");
                Intent intent1 = new Intent(SelectionActivity.this, DormitoryActivity.class);
                startActivity(intent1);
                break;
            case R.id.two_person:
                editor.putString("number", "2");
                editor.commit();
                Intent intent2 = new Intent(SelectionActivity.this, DormitoryActivity.class);
                Log.d("两人选","两人");
                startActivity(intent2);
                break;
            case R.id.three_person:
                editor.putString("number", "3");
                editor.commit();
                Intent intent3 = new Intent(SelectionActivity.this, DormitoryActivity.class);
                startActivity(intent3);
                break;
            case R.id.four_person:
                editor.putString("number", "4");
                editor.commit();
                Intent intent4 = new Intent(SelectionActivity.this, DormitoryActivity.class);
                startActivity(intent4);
                break;
            case R.id.back_login:
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
                break;
            default:
                break;

        }
    }
}
