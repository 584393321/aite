package com.aliyun.ayland.ui.activity;

import android.text.TextUtils;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATLinkageLogBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATLinkageLogEPLAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageLogActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATLinkageLogEPLAdapter mLogEPLAdapter;
    private String currentDate = "";
    private List<ATLinkageLogBean> mTempLinkageLogList = new ArrayList<>();
    private List<ATLinkageLogBean> mAllATLinkageLogBeanList = new ArrayList<>();
    private List<List<ATLinkageLogBean>> mAllLinkageLogList = new ArrayList<>();
    private int pageStart = 0;
    private SmartRefreshLayout smartRefreshLayout;
    private ExpandableListView expandableListView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_log;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        expandableListView = findViewById(R.id.expandableListView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        getUserSceneRecord();
    }

    public void getUserSceneRecord() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("pageStart", pageStart);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETUSERSCENERECORD, jsonObject);
    }

    private void init() {
        mLogEPLAdapter = new ATLinkageLogEPLAdapter(this);
        expandableListView.setAdapter(mLogEPLAdapter);
        expandableListView.setOnGroupClickListener((expandableListView, v, i, l) -> true);

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                pageStart += 20;
                getUserSceneRecord();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                pageStart = 0;
                getUserSceneRecord();
            }
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETUSERSCENERECORD:
                        List<ATLinkageLogBean> mLinkageLogList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATLinkageLogBean>>() {
                        }.getType());
                        mAllATLinkageLogBeanList.clear();
                        if (pageStart == 0)
                            mAllLinkageLogList.clear();
                        mAllATLinkageLogBeanList.addAll(mLinkageLogList);
                        mTempLinkageLogList = new ArrayList<>();
                        if (mLinkageLogList.size() == 0) {
                            smartRefreshLayout.setNoMoreData(true);
                            smartRefreshLayout.setEnableLoadMore(false);
                        } else {
                            smartRefreshLayout.setNoMoreData(false);
                            smartRefreshLayout.setEnableLoadMore(true);
                        }
                        for (int i = 0; i < mAllATLinkageLogBeanList.size(); i++) {
                            mTempLinkageLogList.add(mAllATLinkageLogBeanList.get(i));
                            if (TextUtils.isEmpty(mAllATLinkageLogBeanList.get(i).getCreateTime()) || i == mAllATLinkageLogBeanList.size() - 1
                                    || !mAllATLinkageLogBeanList.get(i).getCreateTime().split(" ")[0].equals(mAllATLinkageLogBeanList.get(i + 1).getCreateTime().split(" ")[0])) {
                                mAllLinkageLogList.add(mTempLinkageLogList);
                                mTempLinkageLogList = new ArrayList<>();
                            }
                        }
                        mLogEPLAdapter.setList(mAllLinkageLogList);
                        for (int i = 0; i < mAllLinkageLogList.size(); i++) {
                            expandableListView.expandGroup(i);
                        }
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            smartRefreshLayout.finishLoadMore();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}