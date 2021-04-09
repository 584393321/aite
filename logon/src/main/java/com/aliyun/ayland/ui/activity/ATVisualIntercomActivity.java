package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.ATAutoAppBarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoCollapsingToolbarLayout;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSipListBean;
import com.aliyun.ayland.data.ATVisualIntercomRecordBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATVisualIntercomRVAdapter;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.aliyun.ayland.widget.voip.VoipManager;
import com.aliyun.ayland.widget.zxing.common.ATConstant;
import com.anthouse.xuhui.R;
import com.evideo.voip.sdk.EVVoipException;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.List;

public class ATVisualIntercomActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATVisualIntercomRVAdapter mATVisualIntercomRVAdapter;
    private ATSwitchButton switchview;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private ATMyTitleBar titlebar;
    private ATSipListBean mAtSipListBean;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_visual_intercom;
    }

    @Override
    protected void findView() {
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        titlebar = findViewById(R.id.titlebar);
        switchview = findViewById(R.id.switchview);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        list();
    }

    private void list() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 0);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        jsonObject.put("targetId", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_LIST, jsonObject);
    }

    private void setOpenOrClose() {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        String url = switchview.isChecked() ? ATConstants.Config.SERVER_URL_CLOSE : ATConstants.Config.SERVER_URL_OPEN;
        mPresenter.request(url, jsonObject);
    }

    private void log() {
        if (mATHouseBean == null)
            return;
        if (mAtSipListBean == null) {
            list();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        jsonObject.put("sipNumber", mAtSipListBean.getSipNumber());
        mPresenter.request(ATConstants.Config.SERVER_URL_LOG, jsonObject);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            titlebar.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange()));
        });

        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATVisualIntercomRVAdapter = new ATVisualIntercomRVAdapter(this);
        recyclerView.setAdapter(mATVisualIntercomRVAdapter);
        switchview.setOnTouchListener((view, motionEvent) -> {
            setOpenOrClose();
            return false;
        });
        smartRefreshLayout.setNoMoreData(true);
        smartRefreshLayout.setEnableAutoLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            log();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_LIST:
                        List<ATSipListBean> atSipListBean = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATSipListBean>>() {
                        }.getType());
                        if (atSipListBean.size() > 0) {
                            mAtSipListBean = atSipListBean.get(0);
                            log();
                            switchview.setChecked(mAtSipListBean.getStatus() == 1);
                            if (mAtSipListBean.getStatus() == 1) {
                                try {
                                    VoipManager.getInstance().login(mAtSipListBean.getSipNumber(), mAtSipListBean.getSipPassword(),
                                            mAtSipListBean.getSipHost(), mAtSipListBean.getSipPort());
                                } catch (EVVoipException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_OPEN:
                        switchview.setChecked(true);
                        if (mAtSipListBean != null)
                            try {
                                VoipManager.getInstance().login(mAtSipListBean.getSipNumber(), mAtSipListBean.getSipPassword(), mAtSipListBean.getSipHost(), mAtSipListBean.getSipPort());
                            } catch (EVVoipException e) {
                                e.printStackTrace();
                            }
                        break;
                    case ATConstants.Config.SERVER_URL_CLOSE:
                        switchview.setChecked(false);
                        VoipManager.getInstance().logout();
                        break;
                    case ATConstants.Config.SERVER_URL_LOG:
                        List<ATVisualIntercomRecordBean> ATVisualIntercomRecordBeans = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATVisualIntercomRecordBean>>() {
                        }.getType());
                        if (ATVisualIntercomRecordBeans.size() == 0) {
                            smartRefreshLayout.setNoMoreData(true);
                        } else {
                            smartRefreshLayout.setNoMoreData(false);
                        }
                        mATVisualIntercomRVAdapter.setList(ATVisualIntercomRecordBeans);
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