package com.aliyun.ayland.utils;

import com.aliyun.ayland.interfaces.CallBack;
import com.aliyun.ayland.interfaces.ILoginCallBack;

public class CallbackUtil {
    private static CallBack mCallBack;

    public static void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public static void doCallBackMethod(String authCode){
        mCallBack.auth(authCode);
    }
}