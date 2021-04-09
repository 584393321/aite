package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATPublicCameraBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATPublicSecurityCameraRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.List;

public class ATPublicSecurityActivity extends ATBaseActivity implements ATMainContract.View{
    private ATMainPresenter mPresenter;
    private ATPublicSecurityCameraRVAdapter mATPublicSecurityCameraRVAdapter;
    private String mIotId, productKey;
    private List<ATPublicCameraBean> mATPublicCameraBeanList;
    private String typePublic;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_public_security;
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
        if (typePublic.equals("公区监控")) {
            getPublicCamera();
        }else if (typePublic.equals("公区消防")) {

        }
    }

    private void getPublicCamera() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETPUBLICCAMERA, jsonObject);
    }

    private void queryLiveStreaming() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iotId",mIotId);
        mPresenter.request(ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING, jsonObject);
    }

    private void init() {
        typePublic = getIntent().getStringExtra("typePublic");
        titlebar.setTitle(typePublic);
        if (getString(R.string.at_public_monitor1).equals(typePublic)) {
            smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
                refreshLayout.finishRefresh(2000);
                getPublicCamera();
            });

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATPublicSecurityCameraRVAdapter = new ATPublicSecurityCameraRVAdapter(this);
        recyclerView.setAdapter(mATPublicSecurityCameraRVAdapter);

        mATPublicSecurityCameraRVAdapter.setOnItemClickListener(position -> {
            mIotId = mATPublicCameraBeanList.get(position).getIotId();
            productKey = mATPublicCameraBeanList.get(position).getProductKey();
            queryLiveStreaming();
        });
//        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
//            refreshLayout.finishRefresh(2000);
//            getPublicCamera();
//        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETPUBLICCAMERA:
                        mATPublicCameraBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATPublicCameraBean>>() {
                        }.getType());
                        mATPublicSecurityCameraRVAdapter.setLists(mATPublicCameraBeanList);
//                        if(mATPublicSecurityCameraBeanList.size() >= 0) {
//                            llEmpty.setVisibility(View.GONE);
//                        }
                        break;
                    case ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING:
                        String path = jsonResult.getJSONObject("data").getString("path");
//                        path = jsonResult.getString("data");
                        startActivity(new Intent(this, ATIntelligentMonitorActivity.class)
                        .putExtra("productKey", productKey)
                        .putExtra("path",path)
                        .putExtra("iotId", mIotId));
                        break;
                }
            } else {
                if(jsonResult.getString("errorMessage").equals("device offline")) {
                    showToast("设备已离线");
                }else {
                    showToast(jsonResult.getString("message"));
                }
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
