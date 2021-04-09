package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATCaringRecordBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATPublicCameraBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATCaringRecordRVAdapter;
import com.aliyun.ayland.ui.adapter.ATPublicSecurityCameraRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATOldYoungCareRecordActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATCaringRecordRVAdapter mATCaringRecordRVAdapter;
    private List<ATPublicCameraBean> mATPublicCameraBeanList = new ArrayList<>();
    private int startNum = 0;
    private String mIotId, productKey ="", typeRecord;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout llEmpty;
    private TextView tvNoData;
    private Button btnOpenFunction;
    private ATPublicSecurityCameraRVAdapter mATPublicSecurityCameraRVAdapter;
    private List<ATCaringRecordBean> mATCaringRecordBeanList= new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_old_young_care_record;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        llEmpty = findViewById(R.id.ll_empty);
        btnOpenFunction = findViewById(R.id.btn_open_function);
        tvNoData = findViewById(R.id.tv_no_data);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        request();
    }

    private void request() {
        showBaseProgressDlg();
        switch (typeRecord) {
            case "oldyoung":
                findCaringRecord();
                break;
            case "personnel":
                findOutAloneRecord();
                break;
            case "monitor":
                getPublicCamera();
                break;
        }
    }

    private void findOutAloneRecord() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("startNum", startNum);
        jsonObject.put("pageSize", 20);
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDOUTALONERECORD, jsonObject);
    }

    private void findCaringRecord() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("startNum", startNum);
        jsonObject.put("pageSize", 20);
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDCARINGRECORD, jsonObject);
    }

    private void queryLiveStreaming() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iotId",mIotId);
        mPresenter.request(ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING, jsonObject);
    }

    private void getPublicCamera() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETPUBLICCAMERA, jsonObject);
    }

    private void init () {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        typeRecord = getIntent().getStringExtra("typeRecord");
        switch (typeRecord) {
            case "oldyoung":
                titlebar.setTitle(getString(R.string.at_travel_reminder_for_the_elderly_or_children));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                mATCaringRecordRVAdapter = new ATCaringRecordRVAdapter(this, typeRecord);
                recyclerView.setAdapter(mATCaringRecordRVAdapter);
                mATCaringRecordRVAdapter.setOnItemClickListener(position -> {
                    startActivity(new Intent(this, ATOldYoungCareFindLocationDetailsActivity.class)
                            .putExtra("type", 1)
                            .putExtra("ATCaringRecordBean", mATCaringRecordBeanList.get(position)));
                });
                smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshLayout) {
                        refreshLayout.finishLoadMore(2000);
                        startNum += 20;
                        request();
                    }

                    @Override
                    public void onRefresh(RefreshLayout refreshLayout) {
                        refreshLayout.finishRefresh(2000);
                        startNum = 0;
                        request();
                    }
                });
                break;
            case "monitor":
                titlebar.setTitle(getString(R.string.at_village_share_video));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                mATPublicSecurityCameraRVAdapter = new ATPublicSecurityCameraRVAdapter(this);
                recyclerView.setAdapter(mATPublicSecurityCameraRVAdapter);
                mATPublicSecurityCameraRVAdapter.setOnItemClickListener(position -> {
                    mIotId = mATPublicCameraBeanList.get(position).getIotId();
                    productKey = mATPublicCameraBeanList.get(position).getProductKey();
                    queryLiveStreaming();
                });
                smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshLayout) {
                        refreshLayout.finishLoadMore(2000);
                        startNum += 20;
                        request();
                    }

                    @Override
                    public void onRefresh(RefreshLayout refreshLayout) {
                        refreshLayout.finishRefresh(2000);
                        startNum = 0;
                        request();
                    }
                });
                break;
            case "personnel":
                titlebar.setTitle(getString(R.string.at_travel_record));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                mATCaringRecordRVAdapter = new ATCaringRecordRVAdapter(this, typeRecord);
                recyclerView.setAdapter(mATCaringRecordRVAdapter);
                mATCaringRecordRVAdapter.setOnItemClickListener(position -> {
                    startActivity(new Intent(this, ATOldYoungCareFindLocationDetailsActivity.class)
                            .putExtra("type", 2)
                            .putExtra("ATCaringRecordBean", mATCaringRecordBeanList.get(position)));
                });
                smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshLayout) {
                        refreshLayout.finishLoadMore(2000);
                        startNum += 20;
                        request();
                    }

                    @Override
                    public void onRefresh(RefreshLayout refreshLayout) {
                        refreshLayout.finishRefresh(2000);
                        startNum = 0;
                        request();
                    }
                });
                break;
        }

        btnOpenFunction.setOnClickListener(view -> startActivity(new Intent(this, ATCareFunctionSetActivity.class)));
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDOUTALONERECORD:
                        List<ATCaringRecordBean> aTCaringRecordBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATCaringRecordBean>>() {
                        }.getType());
                        if(startNum == 0)
                            mATCaringRecordBeanList.clear();
                        mATCaringRecordBeanList.addAll(aTCaringRecordBeanList);
                        mATCaringRecordRVAdapter.setLists(mATCaringRecordBeanList);
                        if(mATCaringRecordBeanList.size() <= 0){
                            llEmpty.setVisibility(View.VISIBLE);
                            if(0 == jsonResult.getInt("careType")){
                                tvNoData.setText(getString(R.string.at_no_record2));
                                btnOpenFunction.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setText(getString(R.string.at_no_record1));
                                btnOpenFunction.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_FINDCARINGRECORD:
                        List<ATCaringRecordBean> aTCaringRecordBeanList1 = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATCaringRecordBean>>() {
                        }.getType());
                        if(startNum == 0)
                            mATCaringRecordBeanList.clear();
                        mATCaringRecordBeanList.addAll(aTCaringRecordBeanList1);
                        mATCaringRecordRVAdapter.setLists(mATCaringRecordBeanList);
                        if(mATCaringRecordBeanList.size() <= 0){
                            llEmpty.setVisibility(View.VISIBLE);
                            if(0 == jsonResult.getInt("careType")){
                                tvNoData.setText(getString(R.string.at_no_record2));
                                btnOpenFunction.setVisibility(View.VISIBLE);
                            }else {
                                tvNoData.setText(getString(R.string.at_no_record1));
                                btnOpenFunction.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING:
                        String path = jsonResult.getJSONObject("data").getString("path");
//                        path = jsonResult.getString("data");
                        startActivity(new Intent(this, ATIntelligentMonitorActivity.class)
                                .putExtra("productKey", productKey)
                                .putExtra("path",path)
                                .putExtra("iotId", mIotId));
                        break;
                    case ATConstants.Config.SERVER_URL_GETPUBLICCAMERA:
                        mATPublicCameraBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATPublicCameraBean>>() {
                        }.getType());
                        mATPublicSecurityCameraRVAdapter.setLists(mATPublicCameraBeanList);
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadMore();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}