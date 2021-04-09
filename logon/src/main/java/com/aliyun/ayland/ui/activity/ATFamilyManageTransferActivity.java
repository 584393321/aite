package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATFamilyMenberBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATFamilyManageTransferRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyManageTransferActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean houseBean;
    private ATFamilyManageTransferRVAdapter mFamilyManageTransferRVAdapter;
    private ATMyTitleBar titlebar;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_family_manage_transfer;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void transferAdminAuthority(String memberPersonCode) {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", houseBean.getBuildingCode());
        jsonObject.put("memberPersonCode", memberPersonCode);
        jsonObject.put("villageId", houseBean.getVillageId());
        jsonObject.put("operatorCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_TRANSFERADMINAUTHORITY, jsonObject);
    }

    private void init() {
        ArrayList<ATFamilyMenberBean> otherMemberList = getIntent().getParcelableArrayListExtra("mOtherMemberList");
        List<ATFamilyMenberBean> mOtherMenberList = new ArrayList<>();
        for (ATFamilyMenberBean familyMemberBean : otherMemberList) {
            if (!"104".equals(familyMemberBean.getHouseholdtype()))
                mOtherMenberList.add(familyMemberBean);
        }
        houseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFamilyManageTransferRVAdapter = new ATFamilyManageTransferRVAdapter(this, mOtherMenberList);
        recyclerView.setAdapter(mFamilyManageTransferRVAdapter);

        titlebar.setSendText(getString(R.string.at_sure1));
        titlebar.setPublishClickListener(() -> {
            if(mFamilyManageTransferRVAdapter.getCheck_position() == -1){
                showToast(getString(R.string.at_transfer_family_manage_to));
            }else {
                transferAdminAuthority(mOtherMenberList.get(mFamilyManageTransferRVAdapter.getCheck_position()).getPersonCode());
            }
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_TRANSFERADMINAUTHORITY.equals(url)) {
                    showToast(getString(R.string.at_transfer_family_manage_success));
                    finish();
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}