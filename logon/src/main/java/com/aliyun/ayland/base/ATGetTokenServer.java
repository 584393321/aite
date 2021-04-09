package com.aliyun.ayland.base;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.rxbus.ATEventType;
import com.aliyun.ayland.rxbus.ATRxBus;
import com.aliyun.ayland.rxbus.ATRxEvent;

import org.json.JSONException;

import java.io.IOException;

import at.smarthome.AT_Aes;
import at.smarthome.HttpUtils2;

import static com.aliyun.ayland.global.ATConstants.EventType.GET_TOKEN_SUCCESS;

public class ATGetTokenServer {
    private volatile static ATGetTokenServer instance = null;
    private volatile static boolean isRequest;

    public static ATGetTokenServer getInstance() {
        if (instance == null) {
            synchronized (ATGetTokenServer.class) {
                if (instance == null) {
                    isRequest = false;
                    instance = new ATGetTokenServer();
                }
            }
        }
        return instance;
    }

    public void getToken() {
        if (isRequest)
            return;
        isRequest = true;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", AT_Aes.setEncodeByKey(ATGlobalApplication.getAccount(), ATConstants.Config.AESPWD));
        jsonObject.put("password", AT_Aes.setEncodeByKey(ATGlobalApplication.getPassword(), ATConstants.Config.AESPWD));
        jsonObject.put("customerCode", AT_Aes.setEncodeByKey(ATConstants.Config.CUSTOMER_CODE, ATConstants.Config.AESPWD));
        String result;
        try {
            result = HttpUtils2.doHttpPost(String.format(ATConstants.Config.SERVER_BASE_URL, ATConstants.Config.SERVER_URL_LOGIN), jsonObject.toString());
            if (result.length() > 0) {
                result = AT_Aes.getDecodeByKey(result, ATConstants.Config.AESPWD);
                org.json.JSONObject jsonResult = new org.json.JSONObject(result);
                ATGlobalApplication.setLoginBeanStr(jsonResult.getString("result"));
                ATRxBus.getDefault().post(new ATRxEvent(ATEventType.REC_ALL, ATEventType.THREAD_UI,
                        GET_TOKEN_SUCCESS, jsonResult.getString("code")));
            }
            isRequest = false;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}