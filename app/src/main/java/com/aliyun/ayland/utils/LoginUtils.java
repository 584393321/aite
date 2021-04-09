package com.aliyun.ayland.utils;

import android.text.TextUtils;

import com.aliyun.ayland.global.ATApplication;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;

public class LoginUtils {
    /**
     * 登录
     *
     * @param account password loginCallback
     */
    public static void login(String account, String password, LoginCallBack loginCallback) {
        if (LoginBusiness.isLogin() && account.equals(ATApplication.getAccount()) && !TextUtils.isEmpty(ATApplication.getLoginBeanStr())) {
            loginCallback.onSuccess();
        } else {
            LoginBusiness.login(new ILoginCallback() {
                @Override
                public void onLoginSuccess() {
                    loginCallback.onSuccess();
                }

                @Override
                public void onLoginFailed(int code, String error) {
                    loginCallback.onFailure(code, error);
                }
            });
        }
    }

    /**
     * 退出登录
     */
    public static void logout() {
        LoginBusiness.logout(new ILogoutCallback() {
            @Override
            public void onLogoutSuccess() {
                ATApplication.setAllDeviceData("");
                ATApplication.setAccount("");
                ATApplication.setLoginBeanStr("");
            }

            @Override
            public void onLogoutFailed(int code, String error) {
            }
        });
    }
}