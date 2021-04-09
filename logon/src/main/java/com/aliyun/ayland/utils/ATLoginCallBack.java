package com.aliyun.ayland.utils;

import com.aliyun.ayland.interfaces.ATILoginCallBack;

public class ATLoginCallBack {
    private ATILoginCallBack mILoginCallBack;

    public ATLoginCallBack(ATILoginCallBack mILoginCallBack) {
        this.mILoginCallBack = mILoginCallBack;
    }

    public void onSuccess() {
        mILoginCallBack.onLoginSuccess();
    }

    public void onFailure(int code, String error) {
        mILoginCallBack.onLoginFailed(code, error);
    }
}