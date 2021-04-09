package com.aliyun.ayland.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.contract.ATDeviceBindContract;
import com.aliyun.ayland.data.ATDeviceListBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.utils.ATToastUtils;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iot.link.ui.component.simpleLoadview.LinkSimpleLoadView;
import com.anthouse.xuhui.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author sinyuk
 * @date 2018/7/2
 */
public class ATDeviceBindFragment extends Fragment implements ATDeviceBindContract.View {
    private String productKey, iotId;
    private Gson gson = new Gson();
    private boolean sub_bind_success = false;
    private List<ATDeviceListBean> mDeviceList = new ArrayList<>();
    private int request = 0, bind_status = -1;
    private ATDeviceBindContract.Presenter mPresenter;
    private String mMessage;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                request++;
                if (bind_status != -1 && sub_bind_success && request == mDeviceList.size()) {
                    ThreadPool.MainThreadHandler.getInstance().post(() -> {
                        if (bind_status == 0) {
                            ATToastUtils.shortShow(mMessage);
                        } else if (bind_status == 1) {
                            ATToastUtils.shortShow(getString(R.string.at_bind_success));
                        }
                        getActivity().finish();
                    });
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.at_fragment_device_bind, container, false);
    }

    private LinkSimpleLoadView loadView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.deviceadd_device_bind_start_btn)
                .setOnClickListener(v -> mPresenter.bindDevice());
        loadView = view.findViewById(R.id.deviceadd_device_bind_link_simple_load_view);
        loadView.setLoadViewLoacation(1);
        loadView.setTipViewLoacation(1);
        loadView.hide();
    }

    public void setPresenter(ATDeviceBindContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        String loading = getString(R.string.at_deviceadd_loading);
        loadView.showLoading(loading);
    }

    @Override
    public void hideLoading() {
        loadView.hide();
    }

    @Override
    public void bindSucceed(String productKey, String iotId) {
        Log.e("bindSucceed: ", iotId + "---" + productKey);
        if ("a1vR7BrA01U".equals(productKey) || "a1HUlYJhpwZ".equals(productKey) || "a1BygYCG3Db".equals(productKey)) {
            queryDeviceSubList(iotId);
        }else {
            sub_bind_success = true;
        }
        //绑定设备与房屋
        this.productKey = productKey;
        this.iotId = iotId;

        bindDevbuilding(iotId);
    }

    private void bindDevbuilding(String iotId) {
        ATHouseBean ATHouseBean;
        if (TextUtils.isEmpty(ATGlobalApplication.getHouse()))
            return;
        ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONArray iotIdArr = new JSONArray();
        iotIdArr.add(iotId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceId", ATHouseBean.getIotSpaceId());
        jsonObject.put("rootSpaceId", ATHouseBean.getRootSpaceId());
        jsonObject.put("iotIdList", iotIdArr);
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_BINDDEVBUILDING, jsonObject);
    }

    @Override
    public void bindFailed(Exception e) {
        ATToastUtils.shortShow(e.getMessage());
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_BINDDEVBUILDING.equals(url)) {//                        Intent intent = new Intent();
//                        intent.setAction("UP_INDEX_FRAGMENT_ACTION");
//                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("productKey", productKey);
//                        bundle.putString("iotId", iotId);
//                        Router.getInstance().toUrl(getActivity(), "link://router/" + productKey, bundle);
                    Log.e("bindSucceed: ", bind_status + "---");
                    ThreadPool.MainThreadHandler.getInstance().post(() -> {
                        if (bind_status == -1) {
                            bind_status = 1;
                            if (sub_bind_success) {
                                ATToastUtils.shortShow(getString(R.string.at_bind_success));
                                getActivity().finish();
                            }
                        } else {
                            sub_bind_success = true;
                            handler.sendEmptyMessage(1);
                        }
                    });
                }
            } else {
                if (bind_status == -1) {
                    bind_status = 0;
                    mMessage = jsonResult.getString("message");
                    if (sub_bind_success) {
                        ATToastUtils.shortShow(mMessage);
                        getActivity().finish();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindZigbeeDeviceSub(ATDeviceListBean deviceBean) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("productKey", deviceBean.getProductKey());
        maps.put("deviceName", deviceBean.getDeviceName());
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/awss/subdevice/bind")
                .setApiVersion("1.0.2")
                .setAuthType("iotAuth")
                .setParams(maps);
        IoTRequest request = builder.build();
        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                bindDevbuilding(deviceBean.getIotId());
            }
        });
    }

    private void queryDeviceSubList(String iotId) {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject pageQuery = new JSONObject();
        pageQuery.put("pageNo", 1);
        pageQuery.put("pageSize", 100);
        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath("/home/paas/device/sub/list")
                .setApiVersion("1.0.0")
                .setAuthType("iotAuth")
                .addParam("iotId", iotId)
                .addParam("pageQuery", pageQuery)
                .addParam("operator", operator);
        IoTRequest request = builder.build();
        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                Log.e("bindSucceed: ", Objects.requireNonNull(e.getMessage()));
                ThreadPool.MainThreadHandler.getInstance().post(() -> {
                    try {
                        sub_bind_success = true;
                        if (bind_status == 0) {
                            ATToastUtils.shortShow(mMessage);
                            getActivity().finish();
                        } else if (bind_status == 1) {
                            ATToastUtils.shortShow(getString(R.string.at_bind_success));
                            getActivity().finish();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                Log.e("bindSucceed1: ", ioTResponse.getCode()+"---");
                if (200 == ioTResponse.getCode()) {
                    try {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(ioTResponse.getData().toString());
                        mDeviceList = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<ATDeviceListBean>>() {
                        }.getType());
                        Log.e("bindSucceed: ", mDeviceList.toString()+"---");
                        if (mDeviceList.size() > 0) {
                            for (ATDeviceListBean ATDeviceListBean : mDeviceList) {
                                bindZigbeeDeviceSub(ATDeviceListBean);
                            }
                        } else {
                            ThreadPool.MainThreadHandler.getInstance().post(() -> {
                                try {
                                    sub_bind_success = true;
                                    if (bind_status == 0) {
                                        ATToastUtils.shortShow(mMessage);
                                        getActivity().finish();
                                    } else if (bind_status == 1) {
                                        ATToastUtils.shortShow(getString(R.string.at_bind_success));
                                        getActivity().finish();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}