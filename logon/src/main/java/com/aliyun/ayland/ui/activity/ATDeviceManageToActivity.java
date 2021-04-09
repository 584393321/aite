package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDeviceManageBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATDeviceManageRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATDeviceManageToActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATDeviceManageRVAdapter mATDeviceManageRVAdapter;
    private int type, position;
    private List<ATDeviceManageBean> mDeviceManageList;
    private ATMyTitleBar titlebar;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_recycleview_sml;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        recyclerView = findViewById(R.id.recyclerView);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        smartRefreshLayout.autoRefresh();
    }

    private void shareDevice() {
        ATHouseBean ATHouseBean;
        if (TextUtils.isEmpty(ATGlobalApplication.getHouse()))
            return;
        ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        showBaseProgressDlg();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONArray iotIdArr = new JSONArray();
        iotIdArr.add(mDeviceManageList.get(position).getIotId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceId", ATHouseBean.getIotSpaceId());
        jsonObject.put("rootSpaceId", ATHouseBean.getRootSpaceId());
        jsonObject.put("iotIdList", iotIdArr);
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_BINDDEVBUILDING, jsonObject);
    }

    private void unsharedDevice() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        if (mDeviceManageList.get(position).getSharedUsers() != null && !ATGlobalApplication.getAccount().equals(mDeviceManageList.get(position).getSharedUsers().get(0).getUsername()))
            jsonObject.put("username", mDeviceManageList.get(position).getSharedUsers().get(0).getUsername());
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("iotId", mDeviceManageList.get(position).getIotId());
        mPresenter.request(ATConstants.Config.SERVER_URL_UNSHAREDDEVICE, jsonObject);
    }

    private void findDevices() {
        ATHouseBean ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (type == 2 && ATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("iotSpaceId", ATHouseBean.getIotSpaceId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDDEVICES, jsonObject);
    }

    private void init() {
        type = getIntent().getIntExtra("type", 1);
        switch (type) {
            case 1:
                titlebar.setTitle(getString(R.string.at_unbind_device));
                break;
            case 2:
                titlebar.setTitle(getString(R.string.at_my_device));
                titlebar.setSendText(getString(R.string.at_share));
                titlebar.setPublishClickListener(() -> {
                    if (getString(R.string.at_share).equals(titlebar.getSendText())) {
                        mATDeviceManageRVAdapter.setCheckable(true);
                        titlebar.setSendText(getString(R.string.at_next));
                    } else {
                        if (mATDeviceManageRVAdapter.getCheckSet().size() == 0) {
                            showToast(getString(R.string.at_please_choose_at_lease_one_device));
                        } else {
                            ArrayList<String> iotIdList = new ArrayList<>();
                            for (Integer integer : mATDeviceManageRVAdapter.getCheckSet()) {
                                iotIdList.add(mDeviceManageList.get(integer).getIotId());
                            }
                            startActivity(new Intent(this, ATDeviceManageMineToActivity.class).putExtra("iotIdList", iotIdList));
                        }
                    }
                });
                break;
            case 3:
                titlebar.setTitle(getString(R.string.at_shared_device));
                break;
            case 4:
                titlebar.setTitle(getString(R.string.at_accepted_device));
                break;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATDeviceManageRVAdapter = new ATDeviceManageRVAdapter(this, type);
        recyclerView.setAdapter(mATDeviceManageRVAdapter);
        mATDeviceManageRVAdapter.setOnItemClickListener((type, position) -> {
            this.position = position;
            if (type == 1)
                shareDevice();
            else
                unsharedDevice();
        });

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findDevices();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_BINDDEVBUILDING:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_bind_success));
                        mDeviceManageList.remove(position);
                        mATDeviceManageRVAdapter.remove(position);
                        mATDeviceManageRVAdapter.notifyItemRemoved(position);
                        mATDeviceManageRVAdapter.notifyItemRangeChanged(position, mDeviceManageList.size());
                        break;
                    case ATConstants.Config.SERVER_URL_UNSHAREDDEVICE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_unbind_success));
                        mDeviceManageList.remove(position);
                        mATDeviceManageRVAdapter.remove(position);
                        mATDeviceManageRVAdapter.notifyItemRemoved(position);
                        mATDeviceManageRVAdapter.notifyItemRangeChanged(position, mDeviceManageList.size());
                        break;
                    case ATConstants.Config.SERVER_URL_FINDDEVICES:
                        mDeviceManageList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDeviceManageBean>>() {
                        }.getType());
                        mATDeviceManageRVAdapter.setList(mDeviceManageList);
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            smartRefreshLayout.finishRefresh();
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}