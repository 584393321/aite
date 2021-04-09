package com.aliyun.ayland.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.contract.MainContract;
import com.aliyun.ayland.model.LoginModel;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

/**
 * Created by fr on 2018/1/31.
 */

public class LoginPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private LoginModel mModel;

    public LoginPresenter(MainContract.View view) {
        this.mView = view;
        mModel = new LoginModel(this);
    }

    @Override
    public void install(Context context) {
        mModel.install(context);
    }

    @Override
    public void uninstall() {
        mModel.uninstall();
    }

    @Override
    public void request(String url, JSONObject jsonObject) {
        mModel.request(url, jsonObject);
    }

    @Override
    public void requestResult(String result, String url) {
        ThreadPool.MainThreadHandler.getInstance().post(() -> {
            mView.requestResult(result, url);
        });
    }
}
