package com.aliyun.ayland.global;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.aliyun.ayland.data.LoginBean;
import com.aliyun.ayland.utils.ACache;
import com.aliyun.ayland.utils.PreferencesUtils;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.espressif.android.EspTouchApp;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.plugins.RxJavaPlugins;

public class ATApplication extends MultiDexApplication {
    private static ATApplication sInstance;
    private static String sPkVersion;   //包版本
    private static ACache sCache;
    private static String account,access_token, password, house, all_device_data, all_scene_icon, loginBeanStr;
    private static Gson gson = new Gson();
    private static LoginBean loginBean;
    private ATGlobalApplication moduleApplication;
    private EspTouchApp moduleApplication1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        moduleApplication = getModuleApplicationInstance(this);
        try {
            //通过反射调用moduleApplication的attach方法
            Method method = Application.class.getDeclaredMethod("attach", Context.class);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(moduleApplication, getBaseContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        moduleApplication1 = getModuleApplicationInstance1(this);
        try {
            //通过反射调用moduleApplication1的attach方法
            Method method1 = Application.class.getDeclaredMethod("attach", Context.class);
            if (method1 != null) {
                method1.setAccessible(true);
                method1.invoke(moduleApplication1, getBaseContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!getPackageName().equals(getCurrentProcessName())) {
            return;
        }
        sInstance = this;

        RxJavaPlugins.setErrorHandler(e -> {
        });
        sCache = ACache.get(this);

        SDKManager.init(this);

        if (moduleApplication != null) {
            moduleApplication.onCreate();
        }

        if (moduleApplication1 != null) {
            moduleApplication1.onCreate();
        }
    }

    private String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }


    private ATGlobalApplication getModuleApplicationInstance(Context paramContext) {
        try {
            if (moduleApplication == null) {
                ClassLoader classLoader = paramContext.getClassLoader();
                if (classLoader != null) {
                    Class<?> mClass = classLoader.loadClass(ATGlobalApplication.class.getName());
                    if (mClass != null)
                        moduleApplication = (ATGlobalApplication) mClass.newInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moduleApplication;
    }

    private EspTouchApp getModuleApplicationInstance1(Context paramContext) {
        try {
            if (moduleApplication1 == null) {
                ClassLoader classLoader = paramContext.getClassLoader();
                if (classLoader != null) {
                    Class<?> mClass = classLoader.loadClass(EspTouchApp.class.getName());
                    if (mClass != null)
                        moduleApplication1 = (EspTouchApp) mClass.newInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moduleApplication1;
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

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

    public static ATApplication getInstance() {
        return sInstance;
    }

    public static String getIoTToken() {
        return IoTCredentialManageImpl.getInstance(sInstance).getIoTToken();
    }

    public static String getAccessToken() {
        if (!TextUtils.isEmpty(access_token)) {
            return access_token;
        } else {
            access_token = PreferencesUtils.getString(ATApplication.getContext(), "access_token", "");
        }
        return access_token;
    }

    public static String getAccount() {
        if (!TextUtils.isEmpty(account)) {
            return account;
        } else {
            account = PreferencesUtils.getString(ATApplication.getContext(), "account", "");
        }
        return account;
    }

    public static String getPassword() {
        if (!TextUtils.isEmpty(password)) {
            return password;
        } else {
            password = PreferencesUtils.getString(ATApplication.getContext(), "tempPass", "");
        }
        return password;
    }

    public static String getAllDeviceData() {
        if (!TextUtils.isEmpty(all_device_data)) {
            return all_device_data;
        } else {
            all_device_data = PreferencesUtils.getString(ATApplication.getContext(), "all_device_data", "");
        }
        return all_device_data;
    }

    public static String getAllSceneIcon() {
        if (!TextUtils.isEmpty(all_scene_icon)) {
            return all_scene_icon;
        } else {
            all_scene_icon = PreferencesUtils.getString(ATApplication.getContext(), "all_scene_icon", "");
        }
        return all_scene_icon;
    }

    public static String getOpenId() {
        if (loginBean == null) {
            loginBean = gson.fromJson(ATApplication.getLoginBeanStr(), LoginBean.class);
        }
        return loginBean.getOpenid();
    }

    public static boolean isRead() {
        return PreferencesUtils.getBoolean(ATApplication.getContext(), "read", false);
    }

    public static boolean isFamilyRead() {
        return PreferencesUtils.getBoolean(ATApplication.getContext(), "family_read", false);
    }

    public static boolean isAgree() {
        return PreferencesUtils.getBoolean(ATApplication.getContext(), "isAgree", false);
    }

    public static String getHouse() {
        if (!TextUtils.isEmpty(house)) {
            return house;
        } else {
            house = PreferencesUtils.getString(ATApplication.getContext(), "house", "");
        }
        return house;
    }

    public static String getLoginBeanStr() {
        if (TextUtils.isEmpty(loginBeanStr)) {
            loginBeanStr = PreferencesUtils.getString(ATApplication.getContext(), getAccount()+"_login", "");
        }
        return loginBeanStr;
    }

    public static LoginBean getLoginBean() {
        if (loginBean == null) {
            loginBean = gson.fromJson(ATApplication.getLoginBeanStr(), LoginBean.class);
        }
        return loginBean;
    }

    public static void setAllSceneIcon(String mAllSceneIcon) {
        all_scene_icon = mAllSceneIcon;
        PreferencesUtils.putString(ATApplication.getContext(), "all_scene_icon", mAllSceneIcon);
    }

    public static void setAccessToken(String accessToken) {
        access_token = accessToken;
        PreferencesUtils.putString(ATApplication.getContext(), "access_token", accessToken);
    }

    public static void setAccount(String mAccount) {
        account = mAccount;
        PreferencesUtils.putString(ATApplication.getContext(), "account", mAccount);
    }

    public static void setPassword(String mPassword) {
        password = mPassword;
        PreferencesUtils.putString(ATApplication.getContext(), "tempPass", mPassword);
    }

    public static void setAllDeviceData(String mAllDeviceData) {
        all_device_data = mAllDeviceData;
        PreferencesUtils.putString(ATApplication.getContext(), "all_device_data", mAllDeviceData);
    }

    public static void setRead(boolean read) {
        PreferencesUtils.putBoolean(ATApplication.getContext(), "read", read);
    }

    public static void setFamilyRead(boolean read) {
        PreferencesUtils.putBoolean(ATApplication.getContext(), "family_read", read);
    }

    public static void setHouse(String mHouse) {
        house = mHouse;
        PreferencesUtils.putString(ATApplication.getContext(), "house", mHouse);
    }

    public static void setLoginBeanStr(String mLoginBeanStr) {
        loginBeanStr = mLoginBeanStr;
        loginBean = gson.fromJson(loginBeanStr, LoginBean.class);
        PreferencesUtils.putString(ATApplication.getContext(), getAccount() +"_login", mLoginBeanStr);
    }

   public static void setAgree(boolean is_agree) {
       PreferencesUtils.putBoolean(ATApplication.getContext(), "isAgree", is_agree);
    }

    public static void setNull() {
        PreferencesUtils.putString(ATApplication.getContext(), "house", "");
        PreferencesUtils.putBoolean(ATApplication.getContext(), "read", false);
        PreferencesUtils.putString(ATApplication.getContext(), "all_device_data", "");
        PreferencesUtils.putString(ATApplication.getContext(), "account", "");
        PreferencesUtils.putString(ATApplication.getContext(), "all_scene_icon", "");
        PreferencesUtils.putBoolean(ATApplication.getContext(), "isAgree", false);
        PreferencesUtils.putBoolean(ATApplication.getContext(), "read", false);
    }
}