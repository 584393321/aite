package com.aliyun.iot.aep.sdk.submodule;

import android.app.Application;

import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;

import java.util.Map;

/**
 * Created by jumo on 2018/7/3.
 */

public class OpenAccountBoneGlue extends SimpleSDKDelegateImp {

    @Override
    public int init(Application application, SDKConfigure sdkConfigure, Map<String, String> map) {
        return 0;
    }
}
