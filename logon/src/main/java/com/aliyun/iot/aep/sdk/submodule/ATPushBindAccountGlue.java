package com.aliyun.iot.aep.sdk.submodule;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.push.PushManager;

import java.util.Map;



/**
 * Created by xingwei on 2018/7/2.
 */

public class ATPushBindAccountGlue extends SimpleSDKDelegateImp {

    @Override
    public int init(Application app, SDKConfigure sdkConfigure, Map<String, String> map) {

        // bind user id
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginBusiness.LOGIN_CHANGE_ACTION);
        LocalBroadcastManager lb = LocalBroadcastManager.getInstance(app);
        lb.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (LoginBusiness.isLogin()) {
                    PushManager.getInstance().bindUser();
                } else {
                    PushManager.getInstance().unbindUser();
                }
            }
        }, intentFilter);
        return 0;
    }
}
