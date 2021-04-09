package com.aliyun.ayland.base;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.rxbus.EventType;
import com.aliyun.ayland.rxbus.RxBus;
import com.aliyun.ayland.rxbus.RxEvent;

import org.json.JSONException;

import java.io.IOException;

import at.smarthome.AT_Aes;
import at.smarthome.HttpUtils2;

import static com.aliyun.ayland.global.Constants.EventType.GET_TOKEN_SUCCESS;

public class GetTokenServer {
    private volatile static GetTokenServer instance = null;
    private volatile static boolean isRequest;

    public static GetTokenServer getInstance() {
        if (instance == null) {
            synchronized (GetTokenServer.class) {
                if (instance == null) {
                    isRequest = false;
                    instance = new GetTokenServer();
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
        jsonObject.put("username", ATApplication.getAccount());
        String result = AT_Aes.setEncodeByKey(jsonObject.toString(), Constants.Config.AESPWD);
        try {
            result = HttpUtils2.doHttpPost(String.format(Constants.Config.SERVER_BASE_URL, Constants.Config.SERVER_URL_GETACCESSTOKEN), result);
            if (result.length() > 0) {
                result = AT_Aes.getDecodeByKey(result, Constants.Config.AESPWD);
                org.json.JSONObject jsonResult = new org.json.JSONObject(result);
                if ("200".equals(jsonResult.getString("code"))) {
                    ATApplication.setAccessToken(jsonResult.getString("access_token"));
                }
                RxBus.getDefault().post(new RxEvent(EventType.REC_ALL, EventType.THREAD_UI,
                        GET_TOKEN_SUCCESS, jsonResult.getString("code")));
            }
            isRequest = false;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
