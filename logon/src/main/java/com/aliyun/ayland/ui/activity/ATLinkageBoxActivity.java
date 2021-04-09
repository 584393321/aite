package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATAllAppointmentBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATLinkageBoxRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.List;

public class ATLinkageBoxActivity extends ATBaseActivity implements ATMainContract.View {
    public static final int REQUEST_CODE_LINKAGE_BOX = 0x1001;
    private ATMainPresenter mPresenter;
    private ATLinkageBoxRVAdapter mATLinkageBoxRVAdapter;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_recycleview_sml;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getAllAppointment();
    }

    private void getAllAppointment() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETALLAPPOINTMENT, jsonObject);
    }

    private void init() {
        titlebar.setTitle(getString(R.string.at_colorful_box_appoint));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATLinkageBoxRVAdapter = new ATLinkageBoxRVAdapter(this);
        recyclerView.setAdapter(mATLinkageBoxRVAdapter);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            getAllAppointment();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETALLAPPOINTMENT:
                        List<ATAllAppointmentBean> allAppointmentList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATAllAppointmentBean>>() {
                        }.getType());
                        mATLinkageBoxRVAdapter.setLists(allAppointmentList);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_LINKAGE_BOX) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}