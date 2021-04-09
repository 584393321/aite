package com.aliyun.ayland.model;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.BaseModel;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.presenter.LoginPresenter;
import com.aliyun.ayland.utils.RxUtils;

import java.io.IOException;

import at.smarthome.AT_Aes;
import at.smarthome.HttpUtils2;


public class LoginModel extends BaseModel<LoginPresenter> {
    public static final String TAG = "MainModel";
    private String accessToken = "";

    public LoginModel(LoginPresenter mPresenter) {
        super(mPresenter);
    }

    @Override
    public void onMainEvent(int what, Object event) {
        switch (what) {
            case Constants.EventType.GET_TOKEN_SUCCESS:

                break;
        }
    }

    @Override
    public void onThreadEvent(int what, Object event) {

    }

    public void request(String url, JSONObject jsonObject) {
        RxUtils.singleTaskOnThread(() -> {
            try {
                if(Constants.Config.SERVER_URL_GETACCESSTOKEN.equals(url)){
                    getToken();
                    return;
                }
                Log.d(TAG, String.format(Constants.Config.SERVER_BASE_URL, url) + jsonObject.toString());
                String result = AT_Aes.setEncodeByKey(jsonObject.toString(), Constants.Config.AESPWD);
                result = HttpUtils2.doHttpPost(String.format(Constants.Config.SERVER_BASE_URL, url), result);
                if (result.length() > 0) {
                    result = AT_Aes.getDecodeByKey(result, Constants.Config.AESPWD);
                    if (result != null)
                        result = result.replace("\"data\":{}", "\"data\":[]");
                    if (result != null && result.length() > 4000) {
                        for (int i = 0; i < result.length(); i += 4000) {
                            if (i + 4000 < result.length())
                                Log.i(TAG + url, result.substring(i, i + 4000));
                            else
                                Log.i(TAG + url, result.substring(i, result.length()));
                        }
                    } else {
                        Log.d(TAG, url + result);
                    }
                    mPresenter.requestResult(result, url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}