package com.aliyun.ayland.contract;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBasePresenter;
import com.aliyun.ayland.base.ATBaseView;
import com.aliyun.ayland.data.ATDevice;

/**
 * Created by fr on 2018/5/8.
 */

public interface ATDeviceBindContract {
    interface Presenter extends ATBasePresenter {
        void bindDevice();
        void request(String url, JSONObject jsonObject);
        void requestResult(String result, String url);
        ATDevice device();
//        void getDeviceToken(String productKey, String deviceName);
//        void getDeviceSuccess(String token);
//        void getDeviceFailed(String reason);
    }

    interface View extends ATBaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void bindSucceed(String productKey, String iotId);
        void bindFailed(Exception e);
        void requestResult(String result, String url);
    }
}
