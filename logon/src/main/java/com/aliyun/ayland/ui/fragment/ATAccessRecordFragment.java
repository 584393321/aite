package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATAccessRecordBean;
import com.aliyun.ayland.data.ATAccessRecordHumanBean;
import com.aliyun.ayland.data.ATEventAccessRecord;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATAccessRecordHumanRVAdapter;
import com.aliyun.ayland.ui.adapter.ATAccessRecordRVAdapter;
import com.aliyun.ayland.widget.popup.ATAccessRecordPopup;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.List;

public class ATAccessRecordFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private int pageNum = 0;
    private String approach, endTime, startTime;
    private ATAccessRecordRVAdapter mATAccessRecordRVAdapter;
    private ATAccessRecordHumanRVAdapter mATAccessRecordHumanRVAdapter;
    private ATAccessRecordPopup mATAccessRecordPopup;
    private int pageNum1 = 2;
    private boolean ifWalk;
    private RecyclerView recyclerView, recyclerViewHuman;
    private SmartRefreshLayout smartRefreshLayout;
    private RadioGroup rgCarWalk;
    private RelativeLayout rlSelect;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_access_record;
    }

    @Override
    protected void findView(View view) {
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewHuman = view.findViewById(R.id.recyclerView_human);
        rlSelect = view.findViewById(R.id.rl_select);
        rgCarWalk = view.findViewById(R.id.rg_car_walk);
        init();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger ATEventInteger) {
        if ("ATAccessRecordActivity".equals(ATEventInteger.getClazz())) {
            ifWalk = R.id.rb_walk == ATEventInteger.getPosition();
            if (ifWalk) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewHuman.setVisibility(View.VISIBLE);
            } else {
                recyclerViewHuman.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventAccessRecord ATEventAccessRecord) {
        if ("ATAccessRecordActivity".equals(ATEventAccessRecord.getClazz())) {
            approach = ATEventAccessRecord.getApproach();
            startTime = ATEventAccessRecord.getStartTime();
            endTime = ATEventAccessRecord.getEndTime();
            pageNum = 0;
            smartRefreshLayout.setNoMoreData(false);
            if (ifWalk) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewHuman.setVisibility(View.VISIBLE);
                findHumnRecord();
            } else {
                recyclerViewHuman.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                findCarParkRecord();
            }
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        findCarParkRecord();
        findHumnRecord();
    }

    private void findCarParkRecord() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("pageNum", pageNum);
        if (!TextUtils.isEmpty(approach))
            jsonObject.put("approach", approach);
        if (!TextUtils.isEmpty(startTime) && !getString(R.string.at_please_choose).equals(startTime))
            jsonObject.put("startTime", startTime);
        if (!TextUtils.isEmpty(endTime) && !getString(R.string.at_please_choose).equals(endTime))
            jsonObject.put("endTime", endTime);
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDCARPARKRECORD, jsonObject);
    }

    private void findHumnRecord() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        if (!TextUtils.isEmpty(startTime) && !getString(R.string.at_please_choose).equals(startTime))
            jsonObject.put("startTime", startTime);
        if (!TextUtils.isEmpty(endTime) && !getString(R.string.at_please_choose).equals(endTime))
            jsonObject.put("endTime", endTime);
        mPresenter.request(ATConstants.Config.SERVER_URL_QUERYVISITORRECORD, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        //下拉弹框
        mATAccessRecordPopup = new ATAccessRecordPopup(getActivity());
        rgCarWalk.setOnCheckedChangeListener((radioGroup1, i) -> {
            ifWalk = R.id.rb_walk == i;
            if (ifWalk) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewHuman.setVisibility(View.VISIBLE);
            } else {
                recyclerViewHuman.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        rlSelect.setOnClickListener(view -> mATAccessRecordPopup.showPopupWindow());
//        rlSelect.setOnClickListener(view -> mATAccessRecordPopup.showPopupWindow1(view));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mATAccessRecordRVAdapter = new ATAccessRecordRVAdapter(getActivity());
        recyclerView.setAdapter(mATAccessRecordRVAdapter);
        recyclerViewHuman.setLayoutManager(new LinearLayoutManager(getActivity()));
        mATAccessRecordHumanRVAdapter = new ATAccessRecordHumanRVAdapter(getActivity());
        recyclerViewHuman.setAdapter(mATAccessRecordHumanRVAdapter);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                pageNum += 7;
                pageNum1 += 7;
                if (ifWalk)
                    findHumnRecord();
                else
                    findCarParkRecord();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                pageNum = 0;
                pageNum1 = 0;
                smartRefreshLayout.setNoMoreData(false);
                if (ifWalk)
                    findHumnRecord();
                else
                    findCarParkRecord();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDCARPARKRECORD:
                        List<ATAccessRecordBean> accessRecordBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATAccessRecordBean>>() {
                        }.getType());
                        if (accessRecordBeanList != null) {
                            mATAccessRecordRVAdapter.setLists(accessRecordBeanList, pageNum);
                            if (accessRecordBeanList.size() < 7) {
                                smartRefreshLayout.setNoMoreData(true);
                            }
                        } else {
                            showToast(jsonResult.getString("message"));
                        }
                        break;

                    case ATConstants.Config.SERVER_URL_QUERYVISITORRECORD:
                        List<ATAccessRecordHumanBean> ATAccessRecordHumanBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATAccessRecordHumanBean>>() {
                        }.getType());
                        if (ATAccessRecordHumanBeanList != null) {
                            mATAccessRecordHumanRVAdapter.setLists(ATAccessRecordHumanBeanList, pageNum1);
                            if (ATAccessRecordHumanBeanList.size() < 7) {
                                smartRefreshLayout.setNoMoreData(true);
                            }
                        } else {
                            showToast(jsonResult.getString("message"));
                        }
                        break;
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