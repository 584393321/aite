package com.aliyun.ayland.interfaces;

import com.aliyun.ayland.data.ATLocalDevice;

import java.util.List;

/**
 * @author guikong on 18/4/19.
 */
public interface ATOnDeviceFilterCompletedListener {
    void onDeviceFilterCompleted(List<ATLocalDevice> ATLocalDevices);
}