package com.aliyun.ayland.interfaces;

/**
 * @author guikong on 18/4/12.
 */
public interface OnBindDeviceCompletedListener {
    void onSuccess(String iotId);

    void onFailed(Exception e);

    void onFailed(int code, String message, String localizedMsg);
}
