package com.aliyun.ayland.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aliyun.ayland.global.ATApplication;


/**
 * Created by Administrator on 2018/6/12.
 */

public class KeepAliveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ATApplication.getAccount() != null && ATApplication.getAccount().length() > 0
                && ATApplication.getPassword() != null && ATApplication.getPassword().length() > 0) {
            context.startService(new Intent(context, SocketServer.class));
        }
    }
}
