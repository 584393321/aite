package com.aliyun.iot.aep.sdk.init;

import android.app.Application;

import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.iot.security.identifypersion.IPManager;

import java.util.Map;

public class PersonIdentityDelegate extends SimpleSDKDelegateImp {

    @Override
    public int init(Application application, SDKConfigure sdkConfigure, Map<String, String> map) {
        IPManager iPManager = new IPManager();
        iPManager.init(application);
        return 0;
    }
}
