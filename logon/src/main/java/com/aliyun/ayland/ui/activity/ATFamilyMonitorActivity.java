package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATDeviceTslDataType;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.interfaces.ATOnEPLItemClickListener;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATFamilyMonitorRVAdapter;
import com.aliyun.ayland.widget.popup.ATListPopup;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyMonitorActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATFamilyMonitorRVAdapter mATFamilyMonitorRVAdapter;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout llNoData;
    private TextView tvAdd;
    private ATHouseBean mATHouseBean;
    private int position = 0, alarmSwitch = 0, motionDetectSensitivity = 0;
    private List<ATDeviceBean> mATDeviceBeanList;
    boolean isAlarmSwitch;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_family_monitor;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        llNoData = findViewById(R.id.ll_no_data);
        tvAdd = findViewById(R.id.tv_add);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getSafetyEquipment();
    }

    private void operatorCamera() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        if (isAlarmSwitch) {
            jsonObject.put("alarmSwitch", alarmSwitch);
        } else {
            jsonObject.put("motionDetectSensitivity", motionDetectSensitivity);
        }
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("iotId", mATDeviceBeanList.get(position).getIotId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_OPERATORCAMERA, jsonObject);
    }

    private void getSafetyEquipment() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETSAFETYEQUIPMENT, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        titlebar.setTitle(getString(R.string.at_family_monitor));
        titlebar.setSendText(getString(R.string.at_alarm_record));
        titlebar.setPublishClickListener(() -> startActivity(new Intent(this, ATAlarmRecordActivity.class)));

        ATListPopup aTListPopup = new ATListPopup(this);
        aTListPopup.setTitle(getString(R.string.at_choose_sensitivity));
        List<String> list = new ArrayList<>();
        list.add("最低档");
        list.add("低档");
        list.add("中档");
        list.add("高档");
        list.add("最高档");
        aTListPopup.setList(list);
        aTListPopup.setOnItemClickListener((view, o, position) -> {
            motionDetectSensitivity = position + 1;
            operatorCamera();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATFamilyMonitorRVAdapter = new ATFamilyMonitorRVAdapter(this);
        recyclerView.setAdapter(mATFamilyMonitorRVAdapter);
        mATFamilyMonitorRVAdapter.setOnItemClickListener(new ATOnEPLItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(ATFamilyMonitorActivity.this, ATIntelligentMonitorActivity.class)
                        .putExtra("productKey", mATDeviceBeanList.get(position).getProductKey())
                        .putExtra("iotId", mATDeviceBeanList.get(position).getIotId()));
            }

            @Override
            public void onItemClick(int position, int childPosition) {

            }

            @Override
            public void onItemClick(int groupPosition, int childPosition, int status) {
                position = groupPosition;
                switch (childPosition) {
                    case 0:
                        //报警开关
                        alarmSwitch = status;
                        isAlarmSwitch = true;
                        operatorCamera();
                        break;
                    case 1:
                        //移动检测开关
                        isAlarmSwitch = false;
                        motionDetectSensitivity = status;
                        operatorCamera();
                        break;
                    case 2:
                        //移动检测灵敏度
                        isAlarmSwitch = false;
                        motionDetectSensitivity = status;
                        aTListPopup.showPopupWindow();
                        break;
                }
            }
        });
        tvAdd.setOnClickListener(view -> startActivity(new Intent(this, ATDiscoveryDeviceActivity.class)));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            getSafetyEquipment();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDUSERCAMERA:
                        List<ATDeviceBean> aTDeviceBeanList = gson.fromJson(jsonResult.getString("deviceList"), new TypeToken<List<ATDeviceBean>>() {
                        }.getType());
                        if (aTDeviceBeanList.size() == 0) {
                            llNoData.setVisibility(View.VISIBLE);
                        } else {
                            llNoData.setVisibility(View.GONE);
                        }
                        mATFamilyMonitorRVAdapter.setLists(aTDeviceBeanList);
                        break;
                    case ATConstants.Config.SERVER_URL_OPERATORCAMERA:
                        if (mATDeviceBeanList.get(position).getAttributes() != null)
                            for (ATDeviceTslDataType atDeviceTslDataType : mATDeviceBeanList.get(position).getAttributes()) {
                            if (isAlarmSwitch) {
                                if ("AlarmSwitch".equals(atDeviceTslDataType.getAttribute())) {
                                    atDeviceTslDataType.setValue(String.valueOf(alarmSwitch));
                                    break;
                                }
                            } else{
                                if ("MotionDetectSensitivity".equals(atDeviceTslDataType.getAttribute())) {
                                    atDeviceTslDataType.setValue(String.valueOf(motionDetectSensitivity));
                                    break;
                                }
                            }
                        }
                        mATFamilyMonitorRVAdapter.setLists(mATDeviceBeanList);
                        showToast(getString(R.string.at_operate_success));
                        break;
                    case ATConstants.Config.SERVER_URL_GETSAFETYEQUIPMENT:
                        mATDeviceBeanList = gson.fromJson(jsonResult.getJSONObject("camera").getString("data"), new TypeToken<List<ATDeviceBean>>() {
                        }.getType());
                        if (mATDeviceBeanList.size() == 0) {
                            llNoData.setVisibility(View.VISIBLE);
                        } else {
                            llNoData.setVisibility(View.GONE);
                        }
                        mATFamilyMonitorRVAdapter.setLists(mATDeviceBeanList);
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}