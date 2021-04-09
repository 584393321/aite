package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATFamilyMemberCareBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATFamilyMemberCareRVAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATCareFunctionSetActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATFamilyMemberCareRVAdapter mATFamilyMemberCareRVAdapter;
    private List<ATFamilyMemberCareBean> mATFamilyMemberCareBeanList = new ArrayList<>();
    private int position,enable,personType;
    private String personCode;
    private Dialog mDialog;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_care_function_set;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        findFamilyMemberForCare();
    }

    private void findFamilyMemberForCare() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDFAMILYMEMBERFORCARE, jsonObject);
    }

    private void setMonitoring() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("enable", enable);
        jsonObject.put("personType", personType);
        jsonObject.put("villageId",  mATHouseBean.getVillageId());
        jsonObject.put("personCode", personCode);
        mPresenter.request(ATConstants.Config.SERVER_URL_SETMONITORING, jsonObject);
    }

    private void init() {
        initDialog();
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATFamilyMemberCareRVAdapter = new ATFamilyMemberCareRVAdapter(this);
        recyclerView.setAdapter(mATFamilyMemberCareRVAdapter);
        mATFamilyMemberCareRVAdapter.setOnItemClickListener((view, enable, position) -> {
            this.enable = (int) enable;
            this.position = position;
            personType = mATFamilyMemberCareBeanList.get(position).getPersonType();
            personCode = mATFamilyMemberCareBeanList.get(position).getPersonCode();
            setMonitoring();
        });

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findFamilyMemberForCare();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            closeBaseProgressDlg();
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDFAMILYMEMBERFORCARE:
                        mATFamilyMemberCareBeanList = gson.fromJson(jsonResult.getString("members"), new TypeToken<List<ATFamilyMemberCareBean>>() {
                        }.getType());
                        mATFamilyMemberCareRVAdapter.setLists(mATFamilyMemberCareBeanList);
                        break;
                    case ATConstants.Config.SERVER_URL_SETMONITORING:
                        mATFamilyMemberCareBeanList.get(position).setIfEnable(enable);
                        mATFamilyMemberCareRVAdapter.setEnable(enable, position);
                        showToast(getString(R.string.at_operate_success));
                        break;
                }
            } else {
                if (jsonResult.getString("message").equals("请先添加人脸")) {
                    mDialog.show();
                } else {
                    showToast(jsonResult.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        mDialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_add_face, null, false);
        Button btnAddFace = view.findViewById(R.id.btn_add_face);
        btnAddFace.setOnClickListener(view1 -> {
            startActivity(new Intent(this, ATUserFaceActivity.class)
                    .putExtra("openId", mATFamilyMemberCareBeanList.get(position).getPersonCode())
                    .putExtra("personCode", mATFamilyMemberCareBeanList.get(position).getPersonCode()));
            mDialog.dismiss();
        });
        mDialog.setContentView(view);
    }
}