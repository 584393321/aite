package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDoorAlarmRecordBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATSmartLockSecurityRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATSmartLockSecurityActivity extends ATBaseActivity implements ATMainContract.View{
    private ATMainPresenter mPresenter;
    private ATSmartLockSecurityRVAdapter mATSmartLockSecurityRVAdapter;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private ATHouseBean mATHouseBean;
    private int startNum = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_smart_lock_security;
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

        findSmartDoorAlarmRecord();
    }

    private void findSmartDoorAlarmRecord() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("startNum", startNum);
        jsonObject.put("pageSize", 10);
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDSMARTDOORALARMRECORD, jsonObject);
    }

    private void init () {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        titlebar.setTitle(getString(R.string.at_lock_security));
        mATSmartLockSecurityRVAdapter = new ATSmartLockSecurityRVAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mATSmartLockSecurityRVAdapter);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                startNum += 10;
                findSmartDoorAlarmRecord();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                startNum = 0;
                findSmartDoorAlarmRecord();
            }
        });

    }
    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDSMARTDOORALARMRECORD:
                        List<ATDoorAlarmRecordBean> ATDoorAlarmRecordBeans = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDoorAlarmRecordBean>>() {
                        }.getType());
                        if (ATDoorAlarmRecordBeans.size() < 10) {
                            smartRefreshLayout.setNoMoreData(true);
                        } else {
                            smartRefreshLayout.setNoMoreData(false);
                        }
                       mATSmartLockSecurityRVAdapter.setList(ATDoorAlarmRecordBeans, startNum);
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