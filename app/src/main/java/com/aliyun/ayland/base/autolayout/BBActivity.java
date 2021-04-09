package com.aliyun.ayland.base.autolayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aliyun.ayland.interfaces.CallResultBack;

public class BBActivity extends AutoLayoutActivity implements CallResultBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onResultCallBack(int resultValue) {
        // TODO Auto-generated method stub
    }
}
