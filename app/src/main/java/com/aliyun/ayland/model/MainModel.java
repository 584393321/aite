package com.aliyun.ayland.model;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.BaseModel;
import com.aliyun.ayland.data.JsonObjectBean;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.presenter.MainPresenter;
import com.aliyun.ayland.utils.RxUtils;

import java.io.IOException;
import java.util.Vector;

import at.smarthome.AT_Aes;
import at.smarthome.HttpUtils2;


public class MainModel extends BaseModel<MainPresenter> {
    public static final String TAG = "MainModel";
    private Vector<JsonObjectBean> serverCommands = new Vector<>();

    public MainModel(MainPresenter mPresenter) {
        super(mPresenter);
    }

    @Override
    public void onMainEvent(int what, Object event) {
        switch (what) {
            case Constants.EventType.GET_TOKEN_SUCCESS:
                if (serverCommands.size() > 0)
                    synchronized (serverCommands) {
                        if (serverCommands.size() > 0) {
                            request(serverCommands.get(0).getUrl(), serverCommands.get(0).getJsonObject());
                            serverCommands.remove(0);
                        }
                    }
                break;
        }
    }

    @Override
    public void onThreadEvent(int what, Object event) {

    }

    @Override
    public void uninstall() {
        super.uninstall();
        synchronized (serverCommands) {
            serverCommands.clear();
        }
    }

    public void request(String url, JSONObject jsonObject) {
        RxUtils.singleTaskOnThread(() -> {
            try {
                Log.d(TAG, url + jsonObject.toString());
                String result = AT_Aes.setEncodeByKey(jsonObject.toString(), Constants.Config.AESPWD);
                if(TextUtils.isEmpty(ATApplication.getAccessToken())){
                    serverCommands.add(new JsonObjectBean(url, jsonObject));
                    getToken();
                    return;
                }
                result = HttpUtils2.doHttpPostAccessToken(String.format(Constants.Config.SERVER_BASE_URL, url), result, ATApplication.getAccessToken());
                if ("401".equals(result)) {
                    serverCommands.add(new JsonObjectBean(url, jsonObject));
                    getToken();
                } else if (result.length() > 0) {
                    result = AT_Aes.getDecodeByKey(result, Constants.Config.AESPWD);
                    Log.e(TAG, String.format(Constants.Config.SERVER_BASE_URL, url) + result);
                    if (result == null)
                        return;
                    result = result.replace("\"data\":{}", "\"data\":[]");
                    mPresenter.requestResult(result, url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}