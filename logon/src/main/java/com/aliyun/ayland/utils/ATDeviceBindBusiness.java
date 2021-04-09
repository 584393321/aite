package com.aliyun.ayland.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.api.discovery.IOnDeviceTokenGetListener;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.ayland.data.ATDevice;
import com.aliyun.ayland.data.ATRoom;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.interfaces.ATOnBindDeviceCompletedListener;
import com.aliyun.ayland.interfaces.ATOnGetRoomListListener;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 设备绑定业务的封装
 * <br/>
 * 支持以下设备类型绑定: <br/>
 * 1. WiFi/以太网 类型
 * 2. GPRS 类型
 * 3. Zigbee 子设备入网（无需绑定）
 * 4. BLE 设备入网
 *
 * @author guikong on 18/4/8.
 */
public class ATDeviceBindBusiness {

    private static final String TAG = "ATDeviceBindBusiness";

    private static final int QUREY_STATUS_NONE = 0;
    private static final int QUREY_STATUS_DOING = 1;
    private static final int QUREY_STATUS_SUCCESS = 2;
    private static final int QUREY_STATUS_FAILED = 3;

    private int qureyStatus = QUREY_STATUS_NONE;

    private static final int BIND_STATUS_NONE = 10;
    private static final int BIND_STATUS_DOING = 11;
    private static final int BIND_STATUS_SUCCESS = 12;
    private static final int BIND_STATUS_FAILED = 13;
    private int bindStatus = BIND_STATUS_NONE;
    private Gson gson = new Gson();

    private ATDevice mATDevice;

    private String groupId;

    private ATOnBindDeviceCompletedListener mATOnBindDeviceCompletedListener;

    public ATDeviceBindBusiness setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * 查询产品信息
     */
    public void queryProductInfo(final ATDevice ATDevice) {
        if (null == ATDevice) {
            throw new IllegalArgumentException("ATDevice can not be null");
        }
        qureyStatus = QUREY_STATUS_DOING;//正在查询
        IoTRequest request = new IoTRequestBuilder()
                .setPath("/thing/detailInfo/queryProductInfoByProductKey")
                .setApiVersion("1.1.1")
                .addParam("productKey", ATDevice.pk)
                .setAuthType("iotAuth")
                .build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                //查询失败
                qureyStatus = QUREY_STATUS_FAILED;
                bindStatus = BIND_STATUS_FAILED;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mATOnBindDeviceCompletedListener != null) {
                                mATOnBindDeviceCompletedListener.onFailed(e);
                            }

                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                            ex.printStackTrace();
                        }
                        mATOnBindDeviceCompletedListener = null;
                    }
                });
            }

            @Override
            public void onResponse(final IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                if (200 != ioTResponse.getCode() || !(ioTResponse.getData() instanceof JSONObject)) {
                    qureyStatus = QUREY_STATUS_FAILED;//查询失败
                    bindStatus = BIND_STATUS_FAILED;
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mATOnBindDeviceCompletedListener != null) {
                                    mATOnBindDeviceCompletedListener.onFailed(ioTResponse.getCode(), ioTResponse.getMessage(), ioTResponse.getLocalizedMsg());
                                }
                            } catch (Exception ex) {
                                ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                                ex.printStackTrace();
                            }
                            mATOnBindDeviceCompletedListener = null;
                        }
                    });
                    return;
                }
                String netType;

                JSONObject data = (JSONObject) ioTResponse.getData();
                netType = data.optString("netType");
                ATDevice.netType = netType;

                // WiFi and ethernet is same
                if ("NET_WIFI".equalsIgnoreCase(netType)
                        || "NET_ETHERNET".equalsIgnoreCase(netType)) {
                    bindWithWiFi(ATDevice);
                } else if ("NET_CELLULAR".equalsIgnoreCase(netType)
                        || "NET_ZIGBEE".equalsIgnoreCase(netType)
                        || "NET_OTHER".equalsIgnoreCase(netType)
                        || "NET_BT".equalsIgnoreCase(netType)) {
                    qureyStatus = QUREY_STATUS_SUCCESS;//查询成功
                    ATDevice cloneATDevice = new ATDevice();
                    cloneATDevice.pk = ATDevice.pk;
                    cloneATDevice.dn = ATDevice.dn;
                    cloneATDevice.netType = ATDevice.netType;
                    cloneATDevice.roomId = ATDevice.roomId;
                    mATDevice = cloneATDevice;
                    if (bindStatus == BIND_STATUS_DOING) {//如果已经点击了绑定按钮
                        bindDeviceInternal(mATOnBindDeviceCompletedListener);
                    }
                } else {
                    bindStatus = BIND_STATUS_FAILED;
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (mATOnBindDeviceCompletedListener != null) {
                                    mATOnBindDeviceCompletedListener.onFailed(new IllegalArgumentException("unsupported net type"));
                                }
                            } catch (Exception ex) {
                                ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                                ex.printStackTrace();
                            }
                            mATOnBindDeviceCompletedListener = null;
                        }
                    });
                }
            }
        });
    }

    //绑定设备
    private void bindDeviceInternal(final ATOnBindDeviceCompletedListener listener) {
        String path = getPathByDevice(mATDevice);
        if (TextUtils.isEmpty(path)) {
            listener.onFailed(new UnsupportedOperationException("ble bind is not support at present@" + mATDevice.toString()));
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("productKey", mATDevice.pk);
        maps.put("deviceName", mATDevice.dn);
        if (!TextUtils.isEmpty(mATDevice.token)) {
            maps.put("token", mATDevice.token);
        }
//        if (!TextUtils.isEmpty(mATDevice.roomId)) {
//            List<String> groupIds = new ArrayList<>(1);
//            groupIds.add(mATDevice.roomId);
//            maps.put("groupIds", groupIds);
//        } else {
//            listener.onFailed(new UnsupportedOperationException("请先选择房间"));
//        }

        Log.e("bindDeviceInternal: ", mATDevice.toString());
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath(path)
                .setApiVersion("/awss/token/user/bind".equals(path) ? "1.0.3" : "1.0.2")
                .setAuthType("iotAuth")
                .setParams(maps);

        IoTRequest request = builder.build();

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                ALog.d(TAG, "onFailure");
                bindStatus = BIND_STATUS_FAILED;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onFailed(e);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                ALog.d(TAG, "onResponse bindWithWiFi ok");
                if ((200 != ioTResponse.getCode() && !"目标设备已经绑定过".equals(ioTResponse.getMessage())) || !(ioTResponse.getData() instanceof String)) {
                    bindStatus = BIND_STATUS_FAILED;
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onFailed(ioTResponse.getCode(), ioTResponse.getMessage(), ioTResponse.getLocalizedMsg());
                            } catch (Exception ex) {
                                ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                                ex.printStackTrace();
                            }
                        }
                    });
                    return;
                }
                final String iotId = (String) ioTResponse.getData();
                bindStatus = BIND_STATUS_SUCCESS;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onSuccess(iotId);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onSuccess", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void bindDevice(ATDevice ATDevice, final ATOnBindDeviceCompletedListener listener) {
        if (bindStatus == BIND_STATUS_DOING) {
            listener.onFailed(new IllegalStateException("bindStatus = BIND_STATUS_DOING"));
            return;
        }
        bindStatus = BIND_STATUS_DOING;
        if (qureyStatus == QUREY_STATUS_SUCCESS) {
            //产品信息查询已经完成
            bindDeviceInternal(listener);
        } else if (qureyStatus == QUREY_STATUS_DOING) {
            //正在查询
            mATOnBindDeviceCompletedListener = listener;
        } else {//未查询或查询失败
            mATOnBindDeviceCompletedListener = listener;
            queryProductInfo(ATDevice);
        }
    }


    private void bindWithWiFi(final ATDevice ATDevice) {
        ALog.d(TAG, "bindWithWiFi");
        qureyStatus = QUREY_STATUS_DOING;//查询中。。。。
        final AtomicBoolean handled = new AtomicBoolean(false);
        LocalDeviceMgr.getInstance().getDeviceToken(ATGlobalApplication.getContext(), ATDevice.pk, ATDevice.dn, 60 * 1000, 5 * 1000, new IOnDeviceTokenGetListener() {
            @Override
            public void onSuccess(String token) {
                ALog.e(TAG, "getDeviceToken onSuccess token = " + token);
                if (handled.get()) {
                    return;
                }
                handled.set(true);

                qureyStatus = QUREY_STATUS_SUCCESS;//查询成功
                ATDevice cloneATDevice = new ATDevice();
                cloneATDevice.pk = ATDevice.pk;
                cloneATDevice.dn = ATDevice.dn;
                cloneATDevice.netType = ATDevice.netType;
                cloneATDevice.token = token;
                cloneATDevice.roomId = ATDevice.roomId;
                mATDevice = cloneATDevice;
                if (bindStatus == BIND_STATUS_DOING) {
                    bindDeviceInternal(mATOnBindDeviceCompletedListener);
                }
            }

            @Override
            public void onFail(final String s) {
                ALog.e(TAG, "getDeviceToken onFail s = " + s);
                qureyStatus = QUREY_STATUS_FAILED;//查询失败
                bindStatus = BIND_STATUS_FAILED;
                if (handled.get()) {
                    return;
                }

                handled.set(true);

                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mATOnBindDeviceCompletedListener != null) {
                                mATOnBindDeviceCompletedListener.onFailed(new RuntimeException(s));
                            }

                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                            ex.printStackTrace();
                        }
                        mATOnBindDeviceCompletedListener = null;
                    }
                });
            }
        });
    }


    private String getPathByDevice(ATDevice ATDevice) {
        String netType = ATDevice.netType.toUpperCase();
        switch (netType) {
            case "NET_WIFI":
            case "NET_ETHERNET":
                return "/awss/token/user/bind";
            case "NET_CELLULAR":
                return "/awss/gprs/user/bind";
            case "NET_ZIGBEE":
            case "NET_OTHER":
                return "/awss/subdevice/bind";
            case "NET_BT":
            default:
                return null;

        }
    }

    private static final int SUCCEED_CODE = 200;

    /**
     * Gets room list.
     */
    public void getRoomList(int page, @NonNull final ATOnGetRoomListListener listener) {
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/homelink/room/list")
                .setApiVersion("1.0.0")
                .setAuthType("iotAuth")
                .addParam("pageNo", page)
                .addParam("pageSize", 20)
                .addParam("houseId", groupId);

        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(builder.build(), new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null) {
                            listener.onFailed(e);
                        } else {
//                            com.taobao.accs.utl.ALog.e(TAG, "unknown error");
                        }
                    }
                });
            }

            @Override
            public void onResponse(final IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                if (!(ioTResponse.getData() instanceof JSONObject)) {
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailed(ioTResponse);
                        }
                    });
                    return;
                }

                if (ioTResponse.getCode() != SUCCEED_CODE || ioTResponse.getData() == null) {
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailed(ioTResponse);
                        }
                    });

                } else {
                    try {
                        final int total;
                        final List<ATRoom> ATRooms = new ArrayList<>();
                        JSONObject data = (JSONObject) ioTResponse.getData();
                        total = data.optInt("total");
                        JSONArray items = data.optJSONArray("data");
                        if (null != items && items.length() > 0) {
                            String jsonStr = items.toString();
                            ATRooms.addAll(JSON.parseArray(jsonStr, ATRoom.class));
                        }

                        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onSucceed(total, ATRooms);
                            }
                        });
                    } catch (Exception e) {
                        listener.onFailed(e);
                    }
                }
            }
        });
    }
}