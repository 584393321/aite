package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;

import org.json.JSONException;

public class ATDeviceManageActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private TextView tvUnbind, tvMy, tvShared, tvAccepted;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_device_manage;
    }

    @Override
    protected void findView() {
        tvUnbind = findViewById(R.id.tv_unbind);
        tvMy = findViewById(R.id.tv_my);
        tvShared = findViewById(R.id.tv_shared);
        tvAccepted = findViewById(R.id.tv_accepted);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void getFace() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userType", "OPEN");
        jsonObject.put("userId", ATGlobalApplication.getOpenId());
        jsonObject.put("imageFormat", "URL");
        mPresenter.request(ATConstants.Config.SERVER_URL_GETFACE, jsonObject);
    }

    private void init() {
        tvUnbind.setOnClickListener(view -> startActivity(new Intent(this, ATDeviceManageToActivity.class).putExtra("type", 1)));
        tvMy.setOnClickListener(view -> startActivity(new Intent(this, ATDeviceManageToActivity.class).putExtra("type", 2)));
        tvShared.setOnClickListener(view -> startActivity(new Intent(this, ATDeviceManageToActivity.class).putExtra("type", 3)));
        tvAccepted.setOnClickListener(view -> startActivity(new Intent(this, ATDeviceManageToActivity.class).putExtra("type", 4)));
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FACEVILLAGELIST:
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}