package com.aliyun.ayland.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aliyun.ayland.global.ATGlobalApplication;

/**
 * Created by Administrator on 2018/6/12.
 */

public class ATKeepAliveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ATGlobalApplication.getAccount() != null && ATGlobalApplication.getAccount().length() > 0
                && ATGlobalApplication.getPassword() != null && ATGlobalApplication.getPassword().length() > 0) {
            context.startService(new Intent(context, ATSocketServer.class));
        }
    }
}