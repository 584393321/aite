package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

public class ATPublicSecurityMainActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private RelativeLayout rlPublicMonitoring, rlPublicFire;
    private TextView tvMonitoringNumber, tvMonitoringPlace;
    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_public_security_main;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        rlPublicMonitoring = findViewById(R.id.rl_public_monitoring);
        rlPublicFire = findViewById(R.id.rl_public_fire);
        tvMonitoringNumber = findViewById(R.id.tv_monitoring_number);
        tvMonitoringPlace = findViewById(R.id.tv_monitoring_place);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getPublicCamera();
    }

    private void getPublicCamera() {
//        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETPUBLICCAMERA, jsonObject);
    }

    private void init() {
        rlPublicMonitoring.setOnClickListener(view -> startActivity(new Intent(this, ATPublicSecurityActivity.class).putExtra("typePublic", "公区监控")));

        rlPublicFire.setOnClickListener(view -> {
            showToast(getString(R.string.at_service_development));
//            Intent intent = new Intent(this, ATPublicSecurityActivity.class).putExtra("typePublic", "公区消防");
//            startActivity(intent);
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_GETPUBLICCAMERA.equals(url)) {
                    tvMonitoringNumber.setText(jsonResult.getString("deviceNum"));
                    tvMonitoringPlace.setText(jsonResult.getString("addressNum"));
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