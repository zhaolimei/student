package com.example.a337910542.dormitory.util;

/**
 * Created by a337910542 on 2017/11/24.
 */

public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);
}
