package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSensorDeploymentBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.interfaces.ATOnEPLItemClickListener;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATSensorSecurityEPLAdapter;
import com.aliyun.ayland.widget.ATCommentExpandableListView;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.List;

public class ATSensorSecurityActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATSensorSecurityEPLAdapter mATSensorSecurityEPLAdapter;
    private Dialog dialog;
    private Context mContent;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private ATCommentExpandableListView expandableListView;
    private ATHouseBean mATHouseBean;
    private List<ATSensorDeploymentBean> mATSensorDeploymentBeanList;
    private int group_position, child_position, deployType;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_sensor_security;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        expandableListView = findViewById(R.id.expandableListView);
        mContent = this;
        init();
        initDialog();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        showBaseProgressDlg();
        getSensorDeployment();
    }

    private void getSensorDeployment() {
        //获取所有传感器布防
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETSENSORDEPLOYMENT, jsonObject);
    }

    private void setSensorDeploy() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("deployType", deployType);
        jsonObject.put("iotId", mATSensorDeploymentBeanList.get(group_position).getData().get(child_position).getIotId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_SETSENSORDEPLOY, jsonObject);
    }

    private void setAllSensorDeploy() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("deployType", deployType);
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_SETALLSENSORDEPLOY, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        titlebar.setTitle(getString(R.string.at_sensor_security));
        titlebar.setSendText(getString(R.string.at_a_key_processing));
        titlebar.setPublishClickListener(() -> dialog.show());

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            getSensorDeployment();
        });

        mATSensorSecurityEPLAdapter = new ATSensorSecurityEPLAdapter(this);
        mATSensorSecurityEPLAdapter.setOnItemClickListener(new ATOnEPLItemClickListener() {
            @Override
            public void onItemClick(int groupPosition) {
                startActivity(new Intent(mContent, ATDiscoveryDeviceActivity.class));
            }

            @Override
            public void onItemClick(int groupPosition, int childPosition) {
                if (expandableListView.isGroupExpanded(groupPosition)) {
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    expandableListView.expandGroup(groupPosition);
                }
            }

            @Override
            public void onItemClick(int groupPosition, int childPosition, int status) {
                group_position = groupPosition;
                child_position = childPosition;
                deployType = status;
                setSensorDeploy();
            }
        });
        expandableListView.setAdapter(mATSensorSecurityEPLAdapter);
        expandableListView.setOnGroupClickListener((expandableListView, v, i, l) -> true);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETSENSORDEPLOYMENT:
                        mATSensorDeploymentBeanList = gson.fromJson(jsonResult.getString("sensors"), new TypeToken<List<ATSensorDeploymentBean>>() {
                        }.getType());
                        mATSensorSecurityEPLAdapter.setList(mATSensorDeploymentBeanList);
                        for (int i = 0; i < mATSensorDeploymentBeanList.size(); i++) {
                            expandableListView.expandGroup(i);
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_SETSENSORDEPLOY:
                        mATSensorDeploymentBeanList.get(group_position).getData().get(child_position).setDeployType(deployType);
                        mATSensorSecurityEPLAdapter.setList(mATSensorDeploymentBeanList);
                        showToast(deployType == 0 ? getString(R.string.at_removal_success) : getString(R.string.at_protection_success));
                        break;
                    case ATConstants.Config.SERVER_URL_SETALLSENSORDEPLOY:
                        dialog.dismiss();
                        showBaseProgressDlg();
                        getSensorDeployment();
                        showToast(deployType == 0 ? getString(R.string.at_a_key_removal_success) : getString(R.string.at_a_key_protection_success));
                        break;
//                    case ATConstants.Config.SERVER_URL_UPDATEENVIRONMENTRECOMMENDSCENEINFO:
//                        recommendSceneStatus = recommendSceneStatus == 0 ? 1 : 0;
//                        button.setBackground(recommendSceneStatus == 0 ? getResources().getDrawable(R.drawable.shape_66px_e85d3e_to_ef6b49) :
//                                getResources().getDrawable(R.drawable.selector_66px_fcd6abf8e8c1_f7de92f9b49c));
//                        button.setText(recommendSceneStatus == 0 ? getString(R.string.stop_scene) : getString(R.string.start_scene));
//                        showToast(getString(R.string.operate_success));
//                        break;
//                    case ATConstants.Config.SERVER_URL_UPDATEENVIRONMENTRECOMMENDSCENEDEVICEINFO:
//                        mSceneActionBeanList.get(current_groupPosition).getDeviceList().get(current_childPosition).setDeviceStatus(deviceStatus);
//                        mRecommendSceneEPLAdapter.setList(mSceneActionBeanList);
//                        showToast(getString(R.string.operate_success));
//                        break;
//                    case ATConstants.Config.SERVER_URL_GETENVIRONMENTRECOMMENDSCENEINFO:
//                        mSceneActionBeanList = gson.fromJson(jsonResult.getJSONObject("data").getString("sceneActions"), new TypeToken<List<ATSceneActionBean>>() {
//                        }.getType());
//                        getId = jsonResult.getJSONObject("data").getString("id");
//                        mRecommendSceneEPLAdapter.setList(mSceneActionBeanList);
//                        for (int i = 0; i < mSceneActionBeanList.size(); i++) {
//                            expandableListView.expandGroup(i);
//                        }
//                        break;
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

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_a_key_processing, null, false);
        view.findViewById(R.id.tv_protection).setOnClickListener(v -> {
            deployType = 1;
            setAllSensorDeploy();
        });
        view.findViewById(R.id.tv_removal).setOnClickListener(v -> {
            deployType = 0;
            setAllSensorDeploy();
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
    }
}