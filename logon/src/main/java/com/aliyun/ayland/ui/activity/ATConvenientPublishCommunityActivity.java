package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATConvenientLifeCommunityBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATCommunityPublishCanseeRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATConvenientPublishCommunityActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private List<ATConvenientLifeCommunityBean> mConvenientLifeMineCommunityList = new ArrayList<>();
    private ATCommunityPublishCanseeRVAdapter mATCommunityPublishCanseeRVAdapter;
    private ATConvenientLifeCommunityBean mATConvenientLifeCommunityBean;
    private ATMyTitleBar titlebar;
    private RecyclerView recyclerView;
    private TextView tvName;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_convenient_publish_cansee;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        recyclerView = findViewById(R.id.recyclerView);
        tvName = findViewById(R.id.tv_name);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getMyCommunityList();
    }

    private void getMyCommunityList() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETMYCOMMUNITYLIST, jsonObject);
    }

    private void init() {
        mATConvenientLifeCommunityBean = getIntent().getParcelableExtra("ATConvenientLifeCommunityBean");
        if (mATConvenientLifeCommunityBean != null) {
            tvName.setText(mATConvenientLifeCommunityBean.getCommunityName());
        } else {
            tvName.setText(getString(R.string.at_square));
        }
        titlebar.setSendText(getString(R.string.at_sure1));
        titlebar.setPublishClickListener(() -> {
            ArrayList<ATConvenientLifeCommunityBean> selectCommunityList = new ArrayList<>();
            for (Integer integer : mATCommunityPublishCanseeRVAdapter.getCheckSet()) {
                selectCommunityList.add(mConvenientLifeMineCommunityList.get(integer));
            }                    Log.e("onActivityResult: ", selectCommunityList.size()+"--1");

            setResult(RESULT_OK, new Intent().putParcelableArrayListExtra("selectCommunityList", selectCommunityList));
            finish();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATCommunityPublishCanseeRVAdapter = new ATCommunityPublishCanseeRVAdapter(this);
        recyclerView.setAdapter(mATCommunityPublishCanseeRVAdapter);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_GETMYCOMMUNITYLIST.equals(url)) {
                    List<ATConvenientLifeCommunityBean> convenientLifeMineCommunityList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATConvenientLifeCommunityBean>>() {
                    }.getType());
                    if (mATConvenientLifeCommunityBean.getId() != 1) {
                        ATConvenientLifeCommunityBean ATConvenientLifeCommunityBean = new ATConvenientLifeCommunityBean();
                        ATConvenientLifeCommunityBean.setId(1);
                        ATConvenientLifeCommunityBean.setCommunityName(getString(R.string.at_square));
                        mConvenientLifeMineCommunityList.add(ATConvenientLifeCommunityBean);
                        for (ATConvenientLifeCommunityBean lifeCommunityBean : convenientLifeMineCommunityList) {
                            if (lifeCommunityBean.getId() != mATConvenientLifeCommunityBean.getId()) {
                                mConvenientLifeMineCommunityList.add(lifeCommunityBean);
                            }
                        }
                    } else {
                        mConvenientLifeMineCommunityList.addAll(convenientLifeMineCommunityList);
                    }
                    mATCommunityPublishCanseeRVAdapter.setList(mConvenientLifeMineCommunityList);
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