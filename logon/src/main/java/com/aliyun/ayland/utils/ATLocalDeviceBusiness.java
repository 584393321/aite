package com.aliyun.ayland.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.aliyun.ayland.data.ATLocalDevice;
import com.aliyun.ayland.interfaces.ATOnDeviceFilterCompletedListener;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 本地设备过滤逻辑的封装
 *
 * @author guikong on 18/4/19.
 */
public class ATLocalDeviceBusiness {
    private static final String TAG = "ATLocalDeviceBusiness";

    private List<ATLocalDevice> discoveredDevices;
    private List<ATLocalDevice> filteredDevices;

    private ATOnDeviceFilterCompletedListener listener;

    public ATLocalDeviceBusiness(ATOnDeviceFilterCompletedListener listener) {
        this.listener = listener;
        discoveredDevices = new LinkedList<>();
        filteredDevices = new LinkedList<>();
    }

    @SuppressWarnings("unused")
    public List<ATLocalDevice> getFilteredDevices() {
        return new ArrayList<>(filteredDevices);
    }

    @SuppressWarnings("unused")
    public List<ATLocalDevice> getDiscoveredDevices() {
        return new ArrayList<>(discoveredDevices);
    }

    public void reset() {
        discoveredDevices.clear();
        filteredDevices.clear();
    }

    public void add(List<ATLocalDevice> atLocalDevices) {
        if (null == atLocalDevices || atLocalDevices.isEmpty()) {
            return;
        }

        // remove duplicated data
        List<ATLocalDevice> availableDevice = new ArrayList<>(atLocalDevices.size());

        for (ATLocalDevice ATLocalDevice : atLocalDevices) {
            boolean invalid = false;

            for (ATLocalDevice discoveredDevice : discoveredDevices) {
                if (TextUtils.equals(ATLocalDevice.deviceName, discoveredDevice.deviceName)
                        && TextUtils.equals(ATLocalDevice.productKey, discoveredDevice.productKey)) {
                    invalid = true;
                    break;
                }
            }

            if (!invalid) {
                availableDevice.add(ATLocalDevice);
            }
        }

        if (availableDevice.isEmpty()) {
            return;
        }

        // cache devices
        discoveredDevices.addAll(availableDevice);

        // do filter
        filterDevice(availableDevice);
    }

    /**
     * 过滤本地设备
     *
     * @param ATLocalDevices 本地设备，未区分已配网/待配网
     */
    private void filterDevice(List<ATLocalDevice> ATLocalDevices) {
        discoveredDevices.addAll(ATLocalDevices);

        List<ATLocalDevice> ATLocalDevicesNeedBind = new ArrayList<>();
        List<ATLocalDevice> ATLocalDevicesNeedEnroll = new ArrayList<>();

        for (ATLocalDevice ATLocalDevice : ATLocalDevices) {
            if (ATLocalDevice.NEED_BIND.equalsIgnoreCase(ATLocalDevice.deviceStatus)) {
                ATLocalDevicesNeedBind.add(ATLocalDevice);
            } else if (ATLocalDevice.NEED_CONNECT.equalsIgnoreCase(ATLocalDevice.deviceStatus)) {
                ATLocalDevicesNeedEnroll.add(ATLocalDevice);
            }
        }

        if (!ATLocalDevicesNeedEnroll.isEmpty()) {
            filterLocalDeviceNeedEnroll(ATLocalDevicesNeedEnroll);
        }

        if (!ATLocalDevicesNeedBind.isEmpty()) {
            filterLocalDeviceNeedBind(ATLocalDevices);
        }
    }

    /**
     * 过滤待配网设备
     *
     * @param ATLocalDevices 未配网的设备
     */
    private void filterLocalDeviceNeedEnroll(List<ATLocalDevice> ATLocalDevices) {
        filteredDevices.addAll(ATLocalDevices);

        if (null != listener) {
            try {
                listener.onDeviceFilterCompleted(ATLocalDevices);
            } catch (Exception e) {
                ALog.e(TAG, "exception happens when call onDeviceFilterCompleted:");
                e.printStackTrace();
            }
        }
    }

    /**
     * 过滤已配网设备
     *
     * @param ATLocalDevices 已配网的设备
     */
    private void filterLocalDeviceNeedBind(final List<ATLocalDevice> ATLocalDevices) {
        List<Map<String, String>> devices = new ArrayList<>(ATLocalDevices.size());
        for (ATLocalDevice ATLocalDevice : ATLocalDevices) {
            Map<String, String> device = new HashMap<>(2);
            device.put("productKey", ATLocalDevice.productKey);
            device.put("deviceName", ATLocalDevice.deviceName);
            devices.add(device);
        }

        IoTRequest request = new IoTRequestBuilder()
                .setPath("/awss/enrollee/product/filter")
                .setApiVersion("1.0.2")
                .addParam("iotDevices", devices)
                .setAuthType("iotAuth")
                .build();

        new IoTAPIClientFactory().getClient().send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                Log.e("weonLocalDeviceFound: ", e.getLocalizedMessage()+"---" + e.getMessage());
                // nothing to do
            }

            @Override
            public void onResponse(final IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                Log.e("weonLocalDeviceFound: ", "---1");
                if (200 != ioTResponse.getCode()) {
                    return;
                }

                if (!(ioTResponse.getData() instanceof JSONArray)) {
                    return;
                }

                final List<ATLocalDevice> availableDevices = new ArrayList<>();
                List<ATLocalDevice> filteredATLocalDevices = new ArrayList<>();
                JSONArray items = (JSONArray) ioTResponse.getData();

                if (null != items
                        && items.length() > 0) {
                    String jsonStr = items.toString();
                    filteredATLocalDevices.addAll(JSON.parseArray(jsonStr, ATLocalDevice.class));
                }

                //append token & addDeviceFrom & deviceStatus
                for (ATLocalDevice ATLocalDevice : ATLocalDevices) {
                    boolean available = false;
                    for (ATLocalDevice filteredATLocalDevice : filteredATLocalDevices) {
                        if (TextUtils.equals(filteredATLocalDevice.productKey, ATLocalDevice.productKey)
                                && TextUtils.equals(filteredATLocalDevice.deviceName, ATLocalDevice.deviceName)) {
                            available = true;
                            ATLocalDevice.productName = filteredATLocalDevice.productName;
                            break;
                        }
                    }
                    if (available) {
                        availableDevices.add(ATLocalDevice);
                    }
                }

                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        filteredDevices.addAll(availableDevices);
                        if (null != listener) {
                            try {
                                listener.onDeviceFilterCompleted(availableDevices);
                            } catch (Exception e) {
                                ALog.e(TAG, "exception happens when call onDeviceFilterCompleted:");
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
}