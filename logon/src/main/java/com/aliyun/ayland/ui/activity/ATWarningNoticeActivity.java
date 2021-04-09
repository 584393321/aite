package com.aliyun.ayland.ui.activity;

import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;

import org.json.JSONException;

public class ATWarningNoticeActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private TextView tvContent;
    private ATHouseBean mATHouseBean;
    private String iotId;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_warning_notice;
    }

    @Override
    protected void findView() {
        tvContent = findViewById(R.id.tv_content);
        findViewById(R.id.button).setOnClickListener(v -> setSensorDeploy());
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void setSensorDeploy() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("deployType", 0);
        jsonObject.put("iotId", iotId);
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_SETSENSORDEPLOY, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        iotId = getIntent().getStringExtra("iotId");
        tvContent.setText(getIntent().getStringExtra("content"));
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_SETSENSORDEPLOY:
                        showToast(getString(R.string.at_removal_success));
                        finish();
                        break;
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
