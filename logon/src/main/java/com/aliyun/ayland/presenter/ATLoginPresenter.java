package com.aliyun.ayland.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.model.ATLoginModel;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

/**
 * Created by fr on 2018/1/31.
 */

public class ATLoginPresenter implements ATMainContract.Presenter {
    private ATMainContract.View mView;
    private ATLoginModel mModel;
    private Context context;

    public ATLoginPresenter(ATMainContract.View view) {
        this.mView = view;
        mModel = new ATLoginModel(this);
    }

    @Override
    public void install(Context context) {
        this.context = context;
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
            if (!TextUtils.isEmpty(result))
                mView.requestResult(result, url);
            else
                ToastUtils.toast(context, "请求超时");
        });
    }
}
