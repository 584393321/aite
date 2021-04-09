package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.BaseActivity;
import com.aliyun.ayland.contract.MainContract;
import com.aliyun.ayland.data.HouseBean;
import com.aliyun.ayland.data.VisitorReservateBean;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.interfaces.OnPopupItemClickListener;
import com.aliyun.ayland.presenter.MainPresenter;
import com.aliyun.ayland.ui.adapter.AccessRecordRVAdapter;
import com.aliyun.ayland.widget.popup.AccessRecordPopup;
import com.aliyun.ayland.widget.titlebar.MyTitleBar;
import com.anthouse.lgcs.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.List;

public class AccessRecordActivity extends BaseActivity implements MainContract.View {
    private MainPresenter mPresenter;
    private MyTitleBar titleBar;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private AccessRecordRVAdapter mAccessRecordRVAdapter;
    private AccessRecordPopup mAccessRecordPopup;
    private HouseBean mHouseBean;
    private LinearLayout llContent;
    private TextView tvNoData;
    private String actualStartTime, actualEndTime;
    private int pageNum, visitorStatus = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycleview_smr1;
    }

    @Override
    protected void findView() {
        titleBar = findViewById(R.id.titlebar);
        recyclerView = findViewById(R.id.recyclerView);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        llContent = findViewById(R.id.ll_content);
        tvNoData = findViewById(R.id.tv_no_data);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(this);
        mPresenter.install(this);

        if (!TextUtils.isEmpty(ATApplication.getHouse())) {
            mHouseBean = gson.fromJson(ATApplication.getHouse(), HouseBean.class);
            smartRefreshLayout.autoRefresh();
        }
    }

    private void findVisitorPage() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mHouseBean.getVillageId());
        jsonObject.put("pageNo", pageNum);
        jsonObject.put("pageSize", 10);
        jsonObject.put("personCode", ATApplication.getLoginBean().getPersonCode());
        if (visitorStatus != 0)
            jsonObject.put("visitorStatus", visitorStatus);
        if (!TextUtils.isEmpty(actualStartTime))
            jsonObject.put("actualStartTime", actualStartTime);
        if (!TextUtils.isEmpty(actualEndTime))
            jsonObject.put("actualEndTime", actualEndTime);
        mPresenter.request(Constants.Config.SERVER_URL_FINDVISITORPAGE, jsonObject);
    }

    private void init() {
        titleBar.setTitle(getString(R.string.at_visitor_record));
        titleBar.setSendText(getString(R.string.at_select));
        titleBar.setRightClickListener(() -> mAccessRecordPopup.showPopupWindow(titleBar));

        tvNoData.setText("暂无访客记录");
        //下拉弹框
        mAccessRecordPopup = new AccessRecordPopup(this, new OnPopupItemClickListener() {
            @Override
            public void onItemClick(int i1, int i2) {

            }

            @Override
            public void onItemClick(String s1, String s2, String s3) {
                pageNum = 0;
                visitorStatus = Integer.parseInt(s1);
                actualStartTime = s2;
                actualEndTime = s3;
                findVisitorPage();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAccessRecordRVAdapter = new AccessRecordRVAdapter(this);
        recyclerView.setAdapter(mAccessRecordRVAdapter);

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                pageNum++;
                findVisitorPage();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                pageNum = 0;
                smartRefreshLayout.setNoMoreData(false);
                findVisitorPage();
            }
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case Constants.Config.SERVER_URL_FINDVISITORPAGE:
                        List<VisitorReservateBean> visitorReservateList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<VisitorReservateBean>>() {
                        }.getType());
                        if (pageNum == 0 && visitorReservateList.size() == 0) {
                            llContent.setVisibility(View.VISIBLE);
                        } else {
                            llContent.setVisibility(View.GONE);
                        }
                        if (visitorReservateList.size() == 0)
                            smartRefreshLayout.setNoMoreData(true);
                        mAccessRecordRVAdapter.setLists(visitorReservateList, pageNum);
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadMore();
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}