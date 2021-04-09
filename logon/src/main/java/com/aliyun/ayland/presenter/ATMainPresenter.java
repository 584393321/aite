package com.aliyun.ayland.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.model.ATMainModel;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

/**
 * Created by fr on 2018/1/31.
 */

public class ATMainPresenter implements ATMainContract.Presenter {
    private ATMainContract.View mView;
    private ATMainModel mModel;
    private Context context;

    public ATMainPresenter(ATMainContract.View view) {
        this.mView = view;
        mModel = new ATMainModel(this);
    }

    @Override
    public void install(Context context) {
        mModel.install(context);
        this.context = context;
    }

    @Override
    public void uninstall() {
        mModel.uninstall();
    }

    @Override
    public void request(String url, JSONObject jsonObject) {
        if (!TextUtils.isEmpty(ATGlobalApplication.getOpenId()))
            mModel.request(url, jsonObject);
    }

    @Override
    public void requestResult(String result, String url) {
        ThreadPool.MainThreadHandler.getInstance().post(() -> {
            if (!TextUtils.isEmpty(result))
                mView.requestResult(result, url);
            else
                ToastUtils.toast(context, "请求超时");
        });
    }
}
