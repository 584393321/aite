package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATMonitorRecordBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATAllRecordRVAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.List;

public class ATSaveRecordFragment extends ATBaseFragment implements ATMainContract.View {
    private ATAllRecordRVAdapter mATAllRecordRVAdapter;
    private ATMainContract.Presenter mPresenter;
    private int startNum = 0;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_monitor_record;
    }

    @Override
    protected void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        getMonitorRecord();
    }

    private void getMonitorRecord() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageSize", "10");
        jsonObject.put("startNum", startNum);
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETMONITORRECORD, jsonObject);
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mATAllRecordRVAdapter = new ATAllRecordRVAdapter(getActivity());
        recyclerView.setAdapter(mATAllRecordRVAdapter);

        smartRefreshLayout.setNoMoreData(true);
        smartRefreshLayout.setEnableAutoLoadMore(false);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                startNum += 10;
                getMonitorRecord();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                startNum = 0;
                smartRefreshLayout.setNoMoreData(false);
                getMonitorRecord();
            }
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_GETMONITORRECORD.equals(url)) {
                    List<ATMonitorRecordBean> familyAppointRecordBeans = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATMonitorRecordBean>>() {
                    }.getType());
                    if (familyAppointRecordBeans.size() == 0) {
                        smartRefreshLayout.setNoMoreData(true);
                    } else {
                        smartRefreshLayout.setNoMoreData(false);
                    }
                    mATAllRecordRVAdapter.setList(familyAppointRecordBeans, startNum);
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}