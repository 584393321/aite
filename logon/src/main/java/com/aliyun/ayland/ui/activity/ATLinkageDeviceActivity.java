package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.data.ATTcaDeviceBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATHomeControlRVAdapter;
import com.aliyun.ayland.ui.adapter.ATHomeInnerRightRVAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


import static com.aliyun.ayland.ui.activity.ATLinkageAddActivity.REQUEST_CODE_ADD_CONDITION;

public class ATLinkageDeviceActivity extends ATBaseActivity implements ATMainContract.View {
    private ATHomeControlRVAdapter mATHomeControlRVAdapter;
    private ATHomeInnerRightRVAdapter mHomeDeviceRVAdapter;
    private ATMainContract.Presenter mPresenter;
    private ATHouseBean mATHouseBean;
    private boolean room, device, tca_device;
    private List<ATRoomBean1> mRoomNameList = new ArrayList<>();
    private List<String> mAllTcaDeviceIotId = new ArrayList<>();
    private String mAllDeviceData;
    private List<List<ATDeviceBean>> mDeviceBeanList = new ArrayList<>();
    private int flowType;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rvControl, rvDevice;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_device;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        rvControl = findViewById(R.id.rv_control);
        rvDevice = findViewById(R.id.rv_device);
        init();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger ATEventInteger) {
        if ("ATLinkageDeviceActivity".equals(ATEventInteger.getClazz())) {
            mHomeDeviceRVAdapter.setLists(mDeviceBeanList.get(ATEventInteger.getPosition()));
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        request();
    }

    private void request() {
        if (TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
            return;
        }
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        showBaseProgressDlg();
        getUserTcaList();
        findOrderRoom();
        getHouseDevice();
    }

    private void getUserTcaList() {
        tca_device = false;
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject target = new JSONObject();
        target.put("hid", ATGlobalApplication.getOpenId());
        target.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("target", target);
        jsonObject.put("flowType", flowType);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETUSERTCALIST, jsonObject);
    }

    private void findOrderRoom() {
        room = false;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        ATLoginBean ATLoginBean = ATGlobalApplication.getATLoginBean();
        jsonObject.put("personCode", ATLoginBean.getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDORDERROOM, jsonObject);
    }

    private void getHouseDevice() {
        device = false;
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("iotSpaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("username", ATGlobalApplication.getAccount());
        mPresenter.request(ATConstants.Config.SERVER_URL_HOUSEDEVICE, jsonObject);
    }

    private void init() {
        flowType = getIntent().getIntExtra("flowType", 1);

        mATHomeControlRVAdapter = new ATHomeControlRVAdapter(this);
        rvControl.setLayoutManager(new LinearLayoutManager(this));
        rvControl.setAdapter(mATHomeControlRVAdapter);

        mHomeDeviceRVAdapter = new ATHomeInnerRightRVAdapter(this);
        rvDevice.setLayoutManager(new GridLayoutManager(this, 2));
        rvDevice.setHasFixedSize(true);
        rvDevice.setAdapter(mHomeDeviceRVAdapter);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> request());
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:
                        mAllDeviceData = jsonResult.getString("data");
                        device = true;
                        requestComplete();
                        break;
                    case ATConstants.Config.SERVER_URL_FINDORDERROOM:
                        mRoomNameList.clear();
                        ATRoomBean1 roomBean = new ATRoomBean1();
                        roomBean.setType("all");
                        roomBean.setIotSpaceId("all");
                        roomBean.setName("全部设备");
                        mRoomNameList.add(roomBean);
                        List<ATRoomBean1> roomNameList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATRoomBean1>>() {
                        }.getType());
                        if (roomNameList.size() > 0)
                            mRoomNameList.addAll(roomNameList);
                        room = true;
                        requestComplete();
                        break;
                    case ATConstants.Config.SERVER_URL_GETUSERTCALIST:
                        List<ATTcaDeviceBean> tcaDeviceList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATTcaDeviceBean>>() {
                        }.getType());
                        for (ATTcaDeviceBean ATTcaDeviceBean : tcaDeviceList) {
                            mAllTcaDeviceIotId.add(ATTcaDeviceBean.getIotId());
                        }
                        tca_device = true;
                        requestComplete();
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
                closeBaseProgressDlg();
                smartRefreshLayout.finishRefresh();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestComplete() {
        if (room && device && tca_device) {
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
            mDeviceBeanList.clear();
            List<ATDeviceBean> ATDeviceBeanList;
            JSONObject mAllDevice = JSON.parseObject(mAllDeviceData);
            for (int i = 0; i < mRoomNameList.size(); i++) {
                if (mAllDevice.containsKey(mRoomNameList.get(i).getIotSpaceId())) {
                    ATDeviceBeanList = gson.fromJson(mAllDevice.getString(mRoomNameList.get(i).getIotSpaceId()), new TypeToken<List<ATDeviceBean>>() {
                    }.getType());
                    for (int j = 0; j < ATDeviceBeanList.size(); j++) {
                        if (!mAllTcaDeviceIotId.contains(ATDeviceBeanList.get(j).getIotId())) {
                            ATDeviceBeanList.remove(j);
                            j--;
                        }
                    }
                    mDeviceBeanList.add(ATDeviceBeanList);
                } else {
                    ATDeviceBeanList = new ArrayList<>();
                    mDeviceBeanList.add(ATDeviceBeanList);
                }
            }

            runOnUiThread(() -> {
                mATHomeControlRVAdapter.setSelectItem(0);
                mATHomeControlRVAdapter.setLists(mRoomNameList, mAllDeviceData);
                mHomeDeviceRVAdapter.setLists(mDeviceBeanList.get(0));
            });

            room = false;
            device = false;
            tca_device = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_CONDITION) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}