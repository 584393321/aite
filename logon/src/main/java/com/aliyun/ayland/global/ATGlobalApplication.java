package com.aliyun.ayland.global;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.utils.ATACache;
import com.aliyun.ayland.utils.ATPreferencesUtils;
import com.aliyun.ayland.widget.voip.CacheManager;
import com.aliyun.ayland.widget.voip.VoipManager;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.evideo.voip.sdk.EVVoipConfig;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.plugins.RxJavaPlugins;

import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_BLURNESS;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_BRIGHTNESS;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_CROP_FACE_SIZE;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_HEAD_PITCH;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_HEAD_ROLL;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_HEAD_YAW;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_MIN_FACE_SIZE;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_NOT_FACE_THRESHOLD;
import static com.baidu.idl.face.platform.FaceEnvironment.VALUE_OCCLUSION;

public class ATGlobalApplication extends MultiDexApplication {
    private static ATGlobalApplication sInstance;
    private static String sPkVersion;   //包版本
    private static ATACache sCache;
    private static String account, password, access_token, house, houseState, all_device_data, all_scene_icon, loginBeanStr, mAvatar_url, authCode, mNick;
    private static Gson gson = new Gson();
    private static ATLoginBean sATLoginBean;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        // 开启网络请求日志
//        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
//            @Override
//            public void run() {
//                // 开启网络请求日志
//                IoTAPIClientImpl.getInstance().registerTracker(new ATTracker());
//                ALog.setLevel(ALog.LEVEL_VERBOSE);
//                // 开启长连接日志
//                com.aliyun.alink.linksdk.tools.ALog.setLevel(ALog.LEVEL_VERBOSE);
//            }
//        });

        RxJavaPlugins.setErrorHandler(e -> {
        });
        sCache = ATACache.get(this);

        SDKManager.init(this);

//        IoTAPIClientImpl.getInstance().registerTracker(new LogTracker());
        initFace();
        if (isMainProcess(this)) {
            ATWebSockIO.getInstance().setUpConnect();
        }

        VoipManager.getInstance().init(this);
        CacheManager.getInstance().init(this);
        EVVoipConfig.enableRingtone(false);
    }

    private boolean isMainProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info != null && info.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo pi : info) {
                if (pi.pid == pid && "com.anthouse.xuhui".equals(pi.processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initFace() {
        //初始化文件储存器
        FaceSDKManager.getInstance().initialize(this, ATConstants.FaceConfig.licenseID, ATConstants.FaceConfig.licenseFileName);
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        // 设置活体动作，通过设置list LivenessTypeEnum.Eye，LivenessTypeEnum.Mouth，LivenessTypeEnum.HeadUp，
        // LivenessTypeEnum.HeadDown，LivenessTypeEnum.HeadLeft, LivenessTypeEnum.HeadRight,
        // LivenessTypeEnum.HeadLeftOrRight
        List<LivenessTypeEnum> livenessList = new ArrayList<>();
//        livenessList.add(LivenessTypeEnum.Mouth);
//        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
        livenessList.add(LivenessTypeEnum.Eye);
//        livenessList.add(LivenessTypeEnum.HeadUp);
//        livenessList.add(LivenessTypeEnum.HeadDown);
//        livenessList.add(LivenessTypeEnum.HeadLeft);
//        livenessList.add(LivenessTypeEnum.HeadRight);
        config.setLivenessTypeList(livenessList);
        // 设置 活体动作是否随机 boolean
        config.setLivenessRandom(false);
        config.setLivenessRandomCount(1);
        // 模糊度范围 (0-1) 推荐小于0.7
        config.setBlurnessValue(VALUE_BLURNESS);
        // 光照范围 (0-1) 推荐大于40
        config.setBrightnessValue(VALUE_BRIGHTNESS);
        // 裁剪人脸大小
        config.setCropFaceValue(VALUE_CROP_FACE_SIZE);
        // 人脸yaw,pitch,row 角度，范围（-45，45），推荐-15-15
        config.setHeadPitchValue(VALUE_HEAD_PITCH);
        config.setHeadRollValue(VALUE_HEAD_ROLL);
        config.setHeadYawValue(VALUE_HEAD_YAW);
        // 最小检测人脸（在图片人脸能够被检测到最小值）80-200， 越小越耗性能，推荐120-200
        config.setMinFaceSize(VALUE_MIN_FACE_SIZE);
        // 人脸置信度（0-1）推荐大于0.6
        config.setNotFaceValue(VALUE_NOT_FACE_THRESHOLD);
        // 人脸遮挡范围 （0-1） 推荐小于0.5
        config.setOcclusionValue(VALUE_OCCLUSION);
        // 是否进行质量检测
        config.setCheckFaceQuality(true);
        // 人脸检测使用线程数
        config.setFaceDecodeNumberOfThreads(2);
        // 是否开启提示音
        config.setSound(false);
        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

    public static ATGlobalApplication getInstance() {
        return sInstance;
    }

    public static String getIoTToken() {
        return IoTCredentialManageImpl.getInstance(sInstance).getIoTToken();
    }

    public static String getAccessToken() {
        if (!TextUtils.isEmpty(access_token)) {
            return access_token;
        } else {
            access_token = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "access_token", "");
        }
        return access_token;
    }

    public static void setAccessToken(String accessToken) {
        access_token = accessToken;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "access_token", accessToken);
    }

    public static String getHid() {
        if (sATLoginBean == null) {
            sATLoginBean = gson.fromJson(ATGlobalApplication.getLoginBeanStr(), ATLoginBean.class);
        }
        return sATLoginBean == null ? "" : sATLoginBean.getOpenid();
    }

    public static String getOpenId() {
        if (sATLoginBean == null) {
            sATLoginBean = gson.fromJson(ATGlobalApplication.getLoginBeanStr(), ATLoginBean.class);
        }
        return sATLoginBean == null ? "" : sATLoginBean.getPersonCode();
    }

    public static String getAccount() {
        if (!TextUtils.isEmpty(account)) {
            return account;
        } else {
            account = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "account", "");
        }
        return account;
    }

    public static void setAccount(String mAccount) {
        account = mAccount;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "account", mAccount);
    }

    public static String getAuthCode() {
        if (!TextUtils.isEmpty(authCode)) {
            return authCode;
        } else {
            authCode = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "authCode", "");
        }
        return authCode;
    }

    public static void setAuthCode(String mAuthCode) {
        authCode = mAuthCode;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "authCode", mAuthCode);
    }

    public static String getPassword() {
        if (!TextUtils.isEmpty(password)) {
            return password;
        } else {
            password = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "tempPass", "");
        }
        return password;
    }

    public static void setPassword(String mPassword) {
        password = mPassword;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "tempPass", mPassword);
    }

    public static String getAllDeviceData() {
        if (!TextUtils.isEmpty(all_device_data)) {
            return all_device_data;
        } else {
            all_device_data = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "all_device_data", "");
        }
        return all_device_data;
    }

    public static void setAllDeviceData(String mAllDeviceData) {
        all_device_data = mAllDeviceData;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "all_device_data", mAllDeviceData);
    }

    public static String getAllSceneIcon() {
        if (!TextUtils.isEmpty(all_scene_icon)) {
            return all_scene_icon;
        } else {
            all_scene_icon = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "all_scene_icon", "");
        }
        return all_scene_icon;
    }

    public static void setAllSceneIcon(String mAllSceneIcon) {
        all_scene_icon = mAllSceneIcon;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "all_scene_icon", mAllSceneIcon);
    }

    public static boolean isRead() {
        return ATPreferencesUtils.getBoolean(ATGlobalApplication.getContext(), "read", false);
    }

    public static void setRead(boolean read) {
        ATPreferencesUtils.putBoolean(ATGlobalApplication.getContext(), "read", read);
    }

    public static boolean isFamilyRead() {
        return ATPreferencesUtils.getBoolean(ATGlobalApplication.getContext(), "family_read", false);
    }

    public static void setFamilyRead(boolean read) {
        ATPreferencesUtils.putBoolean(ATGlobalApplication.getContext(), "family_read", read);
    }

    public static boolean isKitchenRead() {
        return ATPreferencesUtils.getBoolean(ATGlobalApplication.getContext(), "kitchen_read", false);
    }

    public static void setKitchenRead(boolean read) {
        ATPreferencesUtils.putBoolean(ATGlobalApplication.getContext(), "kitchen_read", read);
    }

    public static boolean isAgree() {
        return ATPreferencesUtils.getBoolean(ATGlobalApplication.getContext(), "isAgree", false);
    }

    public static void setAgree(boolean is_agree) {
        ATPreferencesUtils.putBoolean(ATGlobalApplication.getContext(), "isAgree", is_agree);
    }

    public static String getHouse() {
        if (!TextUtils.isEmpty(house)) {
            return house;
        } else {
            house = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "house", "");
        }
        return house;
    }

    public static void setHouse(String mHouse) {
        house = mHouse;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "house", mHouse);
    }

    public static String getLoginBeanStr() {
        if (TextUtils.isEmpty(loginBeanStr)) {
            loginBeanStr = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), getAccount() + "_login", "");
        }
        return loginBeanStr;
    }

    public static void setLoginBeanStr(String mLoginBeanStr) {
        loginBeanStr = mLoginBeanStr;
        sATLoginBean = gson.fromJson(loginBeanStr, ATLoginBean.class);
        if (sATLoginBean != null) {
//            if (!TextUtils.isEmpty(mAvatar_url))
//                sATLoginBean.setAvatarUrl(mAvatar_url);
            access_token = sATLoginBean.getAccessToken();
            ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "access_token", access_token);
            if (!TextUtils.isEmpty(mNick))
                sATLoginBean.setNickName(mNick);
            ATPreferencesUtils.putString(ATGlobalApplication.getContext(), getAccount() + "_login", JSONObject.toJSONString(sATLoginBean));
            houseState = sATLoginBean.getHouseState();
            setHouseState(sATLoginBean.getHouseState());
        }
    }

    public static ATLoginBean getATLoginBean() {
        if (sATLoginBean == null) {
            sATLoginBean = gson.fromJson(ATGlobalApplication.getLoginBeanStr(), ATLoginBean.class) == null ? new ATLoginBean()
                    : gson.fromJson(ATGlobalApplication.getLoginBeanStr(), ATLoginBean.class);
        }
        return sATLoginBean;
    }

    public static void setHouseState(String mHouseState) {
        houseState = mHouseState;
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), getAccount() + "_houseState", mHouseState);
    }

    public static String getHouseState() {
        if (TextUtils.isEmpty(houseState)) {
            houseState = ATPreferencesUtils.getString(ATGlobalApplication.getContext(), getAccount() + "_houseState", "");
        }
        return houseState;
    }

    public static void setNull() {
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "house", "");
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "all_device_data", "");
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "account", "");
        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "all_scene_icon", "");
        ATPreferencesUtils.putBoolean(ATGlobalApplication.getContext(), "isAgree", false);
        ATPreferencesUtils.putBoolean(ATGlobalApplication.getContext(), "read", false);
        VoipManager.getInstance().logout();
        setAllDeviceData("");
        setHouseState("");
        setAccessToken("");
    }
}