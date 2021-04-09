package com.aliyun.ayland.utils;

import com.aliyun.ayland.interfaces.ATCallBack;
import com.aliyun.ayland.interfaces.ATMyOALoginCallBack;

public class ATCallbackUtil {
    private static ATCallBack sMATCallBack;

    public static void setCallBack(ATCallBack ATCallBack) {
        sMATCallBack = ATCallBack;
    }

    public static void doCallBackMethod(String authCode){
        sMATCallBack.auth(authCode);
    }
}