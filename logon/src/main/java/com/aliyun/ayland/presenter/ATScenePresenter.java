package com.aliyun.ayland.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.aliyun.ayland.contract.ATSceneContract;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.model.ATSceneModel;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

/**
 * Created by fr on 2018/1/31.
 */

public class ATScenePresenter implements ATSceneContract.Presenter {
    private ATSceneContract.View mView;
    private ATSceneModel mModel;
    private Context context;

    public ATScenePresenter(ATSceneContract.View view) {
        this.mView = view;
        mModel = new ATSceneModel(this);
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
            mModel.request(url, jsonObject, null);
    }

    @Override
    public void request(String url, JSONObject jsonObject, Object o) {
        mModel.request(url, jsonObject, o);
    }

    @Override
    public void requestResult(String result, String url, Object o) {
        if (!TextUtils.isEmpty(result))
            ThreadPool.MainThreadHandler.getInstance().post(() -> mView.requestResult(result, url, o));
        else
            ToastUtils.toast(context, "请求超时");
    }
}