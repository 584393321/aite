package com.aliyun.ayland.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.aliyun.ayland.contract.ATDeviceBindContract;
import com.aliyun.ayland.data.ATDevice;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.interfaces.ATOnBindDeviceCompletedListener;
import com.aliyun.ayland.model.ATDeviceBindModel;
import com.aliyun.ayland.utils.ATDeviceBindBusiness;

import java.io.IOException;

/**
 * Created by fr on 2018/1/31.
 */

public class ATDeviceBindPresenter implements ATDeviceBindContract.Presenter {
    private final ATDevice mATDevice;
    private final ATDeviceBindBusiness business;
    private ATDeviceBindContract.View mView;
    private ATDeviceBindModel mModel;
    private Context context;

    @Override
    public void install(Context context) {
        this.context = context;
    }

    @Override
    public void uninstall() {

    }

    public ATDeviceBindPresenter(@NonNull ATDeviceBindBusiness bindBusiness,
                                 @NonNull ATDevice ATDevice,
                                 @NonNull ATDeviceBindContract.View view) {
        this.mATDevice = ATDevice;
        business = bindBusiness;
        this.mView = view;
        mModel = new ATDeviceBindModel(this);
    }

    @Override
    public ATDevice device() {
        return mATDevice;
    }

    @Override
    public void bindDevice() {
        mView.showLoading();
        business.bindDevice(mATDevice, new ATOnBindDeviceCompletedListener() {
            @Override
            public void onSuccess(String iotId) {
                mView.bindSucceed(mATDevice.pk, iotId);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("modelonFailed: ", e.getMessage());
                mView.hideLoading();
                mView.bindFailed(e);
            }

            @Override
            public void onFailed(int code, String message, String localizedMsg) {
                mView.hideLoading();
                Log.e("modelonFailed: ", code + message + localizedMsg);
                if (TextUtils.isEmpty(localizedMsg)) {
                    mView.bindFailed(new IOException(message));
                } else {
                    mView.bindFailed(new IOException(localizedMsg));
                }
            }
        });
    }

    @Override
    public void request(String url, JSONObject jsonObject) {
        if (!TextUtils.isEmpty(ATGlobalApplication.getOpenId()))
            mModel.request(url, jsonObject);
    }

    @Override
    public void requestResult(String result, String url) {
        if (!TextUtils.isEmpty(result))
            mView.requestResult(result, url);
        else
            ToastUtils.toast(context, "请求超时");
    }
}
