package com.aliyun.iot.aep.sdk.init;

import android.app.Application;

import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import com.aliyun.iot.push.PushManager;

import java.util.Map;

/**
 * Created by xingwei on 2018/7/2.
 */
public class PushSDKDelegate extends SimpleSDKDelegateImp {

    @Override
    public int init(Application application, SDKConfigure sdkConfigure, Map<String, String> map) {
        String securityIndex = map.get("securityIndex");
        PushManager.getInstance().init(application, securityIndex);

        boolean enableXiaomi = sdkConfigure.opts.optBoolean("enable_xiaomi");
        boolean enableHuawei = sdkConfigure.opts.optBoolean("enable_huawei");

        if (enableXiaomi) {
            String appId = sdkConfigure.opts.optString("xiaomi_appid");
            String appKey = sdkConfigure.opts.optString("xiaomi_appkey");

            PushManager.getInstance().initMiPush(application, appId, appKey);
        }

        if (enableHuawei) {
            PushManager.getInstance().initHuaweiPush(application);
        }
        return 0;
    }
}
