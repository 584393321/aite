package com.aliyun.ayland.model;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseModel;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATLoginPresenter;
import com.aliyun.ayland.utils.ATRxUtils;

import java.io.IOException;

import at.smarthome.AT_Aes;
import at.smarthome.HttpUtils2;


public class ATLoginModel extends ATBaseModel<ATLoginPresenter> {
    public static final String TAG = "ATLoginModel";
    private String accessToken = "";

    public ATLoginModel(ATLoginPresenter mPresenter) {
        super(mPresenter);
    }

    @Override
    public void onMainEvent(int what, Object event) {
        switch (what) {
            case ATConstants.EventType.GET_TOKEN_SUCCESS:

                break;
        }
    }

    @Override
    public void onThreadEvent(int what, Object event) {

    }

    public void request(String url, JSONObject jsonObject) {
        ATRxUtils.singleTaskOnThread(() -> {
            try {
                Log.e(TAG, String.format(ATConstants.Config.SERVER_BASE_URL, url) + jsonObject.toString());
//                String result = AT_Aes.setEncodeByKey(jsonObject.toString(), ATConstants.Config.AESPWD);
                jsonObject.put("customerCode", ATConstants.Config.CUSTOMER_CODE);
                JSONObject mJsonObject = new JSONObject();
                for (String s : jsonObject.keySet()) {
                    if (TextUtils.isEmpty((String)jsonObject.get(s)))
                        mJsonObject.put(s, jsonObject.get(s));
                    else
                        mJsonObject.put(s, AT_Aes.setEncodeByKey((String) jsonObject.get(s), ATConstants.Config.AESPWD));
                }
                Log.e(TAG, String.format(ATConstants.Config.SERVER_BASE_URL, url) + mJsonObject.toString());
                String result = HttpUtils2.doHttpPost(String.format(ATConstants.Config.SERVER_BASE_URL, url), mJsonObject.toString());
                if ("401".equals(result)) {
                    mPresenter.requestResult("", url);
                }else if (result.length() > 0) {
                    result = AT_Aes.getDecodeByKey(result, ATConstants.Config.AESPWD);
                    if (result != null)
                        result = result.replace("\"data\":{}", "\"data\":[]");
                    if (result != null && result.length() > 4000) {
                        for (int i = 0; i < result.length(); i += 4000) {
                            if (i + 4000 < result.length())
                                Log.e(TAG + url, result.substring(i, i + 4000));
                            else
                                Log.e(TAG + url, result.substring(i, result.length()));
                        }
                    } else {
                        Log.e(TAG, url + result);
                    }
                    mPresenter.requestResult(result, url);
                } else
                    mPresenter.requestResult("", url);
            } catch (IOException e) {
                mPresenter.requestResult("", url);
                e.printStackTrace();
            }
        });
    }
}