package com.aliyun.ayland.base.autolayout;

import android.os.Bundle;

import com.aliyun.ayland.interfaces.ATCallResultBack;

public class ATBBActivity extends ATAutoLayoutActivity implements ATCallResultBack {

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
