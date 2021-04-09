package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATEventInteger2;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATEquipmentControlRVAdapter;
import com.aliyun.ayland.ui.adapter.ATEquipmentRVAdapter;
import com.aliyun.ayland.widget.popup.ATHomePopup;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ATEquipmentActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATEquipmentControlRVAdapter mATEquipmentControlRVAdapter;
    private ATEquipmentRVAdapter mATEquipmentRVAdapter;
    private List<List<ATDeviceBean>> mDeviceBeanList = new ArrayList<>();
    private List<ATRoomBean1> mRoomNameList = new ArrayList<>();
    private boolean room, device;
    private ATHouseBean mATHouseBean;
    private String mAllDeviceData;
    private int current = 0, specs = 0, current_position = 0;
    private ATDeviceBean current_device;
    private ATHomePopup mATHomePopup;
    private ATMyTitleBar titlebar;
    private LinearLayout llEmpty;
    private RecyclerView rvEquipment, rvControl;
    private SmartRefreshLayout smartRefreshLayout;
    private TextView tvAddDevice;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_equipment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger2 ATEventInteger2) {
        if ("ATEquipmentActivity".equals(ATEventInteger2.getClazz())) {
            current_position = ATEventInteger2.getPosition();
            current_device = mDeviceBeanList.get(current).get(current_position);
            specs = ATEventInteger2.getSpecs();
            showBaseProgressDlg();
            control();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger ATEventInteger) {
        if ("ATEquipmentActivity".equals(ATEventInteger.getClazz())) {
            current = ATEventInteger.getPosition();
            mATEquipmentRVAdapter.setLists(mDeviceBeanList.get(current));
            if (mDeviceBeanList.get(ATEventInteger.getPosition()).size() == 0) {
                llEmpty.setVisibility(View.VISIBLE);
            } else {
                llEmpty.setVisibility(View.GONE);
            }
        } else if ("EquipmentActivity1".equals(ATEventInteger.getClazz())) {
            startActivity(new Intent(this, ATManageRoomActivity.class).putExtra("allDevice", mAllDeviceData)
                    .putExtra("mRoomNameList", (Serializable) mRoomNameList).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        rvEquipment = findViewById(R.id.rv_equipment);
        rvControl = findViewById(R.id.rv_control);
        llEmpty = findViewById(R.id.ll_empty);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        tvAddDevice = findViewById(R.id.tv_add_device);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void request() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (mATHouseBean == null)
            return;
        findOrderRoom();
        houseDevice();
    }

    private void control() {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONArray commands = new JSONArray();
        JSONObject command = new JSONObject();
        JSONObject data = new JSONObject();
        data.put(current_device.getAttributes().get(0).getAttribute(), specs);
        command.put("data", data);
        command.put("type", "SET_PROPERTIES");
        commands.add(command);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetId", current_device.getIotId());
        jsonObject.put("operator", operator);
        jsonObject.put("commands", commands);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_CONTROL, jsonObject);
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

    private void houseDevice() {
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
        mATHomePopup = new ATHomePopup(this);
        mATEquipmentControlRVAdapter = new ATEquipmentControlRVAdapter(this);
        rvControl.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rvControl.setAdapter(mATEquipmentControlRVAdapter);
        mATEquipmentRVAdapter = new ATEquipmentRVAdapter(this);
        rvEquipment.setLayoutManager(new GridLayoutManager(this, 2));
        rvEquipment.setAdapter(mATEquipmentRVAdapter);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            request();
        });
        titlebar.setRightBtTextImage(R.drawable.at_ioc_tianjia);
        titlebar.setRightClickListener(() -> mATHomePopup.showPopupWindow(titlebar.getRightButton()));
        tvAddDevice.setOnClickListener(view -> startActivity(new Intent(this, ATDiscoveryDeviceActivity.class)));
    }

    private void requestComplete() {
        if (room && device) {
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
            mDeviceBeanList.clear();
            List<ATDeviceBean> ATDeviceBeanList;
            ATGlobalApplication.setAllDeviceData(mAllDeviceData);
            JSONObject mAllDevice = JSON.parseObject(mAllDeviceData);
            for (int i = 0; i < mRoomNameList.size(); i++) {
                if (mAllDevice.containsKey(mRoomNameList.get(i).getIotSpaceId())) {
                    ATDeviceBeanList = gson.fromJson(mAllDevice.getString(mRoomNameList.get(i).getIotSpaceId()), new TypeToken<List<ATDeviceBean>>() {
                    }.getType());
                    ATDeviceBeanList.add(new ATDeviceBean());
                    mDeviceBeanList.add(ATDeviceBeanList);
                } else {
                    ATDeviceBeanList = new ArrayList<>();
                    mDeviceBeanList.add(ATDeviceBeanList);
                }
            }
            if (mDeviceBeanList.size() <= current)
                current = 0;
            mATEquipmentRVAdapter.setLists(mDeviceBeanList.get(current));
            if (mDeviceBeanList.get(current).size() == 0) {
                llEmpty.setVisibility(View.VISIBLE);
            } else {
                llEmpty.setVisibility(View.GONE);
            }
            mATEquipmentControlRVAdapter.setSelectItem(current);
            mATEquipmentControlRVAdapter.setLists(mRoomNameList);
            room = false;
            device = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
            showBaseProgressDlg();
            request();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_CONTROL:
                        closeBaseProgressDlg();
                        if (result.contains("success")) {
                            showToast(getString(R.string.at_operate_success));
                            mDeviceBeanList.get(current).get(current_position).getAttributes().get(0).setValue(String.valueOf(specs));
                            mATEquipmentRVAdapter.setCheck(mDeviceBeanList.get(current), current_position);
                        } else {
                            request();
                        }
                        break;
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
                }
            } else {
                closeBaseProgressDlg();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
