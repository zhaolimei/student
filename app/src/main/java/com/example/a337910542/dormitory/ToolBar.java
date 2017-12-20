package com.example.a337910542.dormitory;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.a337910542.dormitory.R;

/**
 * Created by xiaozhang on 2017/12/13.
 */

public class ToolBar {
    public Button back;
    public ImageView personalInformation;

    public ToolBar(View v){
        back = (Button) v.findViewById(R.id.back_login);
        personalInformation = (ImageView) v.findViewById(R.id.personal_information);
    }
}
