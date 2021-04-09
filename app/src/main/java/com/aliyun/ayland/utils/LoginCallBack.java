package com.aliyun.ayland.utils;

import com.aliyun.ayland.interfaces.ILoginCallBack;

public class LoginCallBack {
    private ILoginCallBack mILoginCallBack;

    public LoginCallBack(ILoginCallBack mILoginCallBack) {
        this.mILoginCallBack = mILoginCallBack;
    }

    public void onSuccess() {
        mILoginCallBack.onLoginSuccess();
    }

    public void onFailure(int code, String error) {
        mILoginCallBack.onLoginFailed(code, error);
    }
}