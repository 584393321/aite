package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSipListBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATChangeHouseRVAdapter;
import com.aliyun.ayland.widget.voip.VoipManager;
import com.anthouse.xuhui.R;
import com.evideo.voip.sdk.EVVoipException;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATChangeHouseActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATChangeHouseRVAdapter mATChangeHouseRVAdapter;
    private List<ATHouseBean> mHouseAllList = new ArrayList<>();
    private List<ATHouseBean> mHouseList = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout llCurrent, llOther;
    private TextView tvHouseName, tvAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_change_house;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        llCurrent = findViewById(R.id.ll_current);
        llOther = findViewById(R.id.ll_other);
        tvHouseName = findViewById(R.id.tv_house_name);
        tvAddress = findViewById(R.id.tv_address);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        smartRefreshLayout.autoRefresh();
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

    private void setPresent() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", ATGlobalApplication.getAccount());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_SETPRESENT, jsonObject);
    }

    private void findUserHouse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", ATGlobalApplication.getAccount());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDUSERHOUSE, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATChangeHouseRVAdapter = new ATChangeHouseRVAdapter(this);
        recyclerView.setAdapter(mATChangeHouseRVAdapter);
        mATChangeHouseRVAdapter.setOnItemClickListener((view, position) -> {
            mATHouseBean = mHouseList.get(position);
            list();
            setPresent();
        });
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            findUserHouse();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_SETPRESENT:
                        showToast(getString(R.string.at_change_house_success));
                        llCurrent.setVisibility(View.VISIBLE);
                        tvHouseName.setText(mATHouseBean.getName());
                        tvAddress.setText(String.format("%s%s%s", mATHouseBean.getProvince(), mATHouseBean.getCity(), mATHouseBean.getArea()));

                        ATGlobalApplication.setHouse(mATHouseBean.toString());
                        ATGlobalApplication.setHouseState(mATHouseBean.getHouseState());
                        mHouseList.clear();
                        if (mATHouseBean.getBuildingCode() != null)
                            for (ATHouseBean ATHouseBean : mHouseAllList) {
                                if (!mATHouseBean.getBuildingCode().equals(ATHouseBean.getBuildingCode())) {
                                    mHouseList.add(ATHouseBean);
                                }
                            }
                        if (mHouseList.size() == 0) {
                            llOther.setVisibility(View.GONE);
                        } else {
                            llOther.setVisibility(View.VISIBLE);
                        }
                        mATChangeHouseRVAdapter.setList(mHouseList);
                        break;
                    case ATConstants.Config.SERVER_URL_LIST:
                        VoipManager.getInstance().logout();
                        List<ATSipListBean> atSipListBean = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATSipListBean>>() {
                        }.getType());
                        if (atSipListBean.size() > 0) {
                            ATSipListBean sipListBean = atSipListBean.get(0);
                            if (sipListBean.getStatus() == 1)
                                try {
                                    VoipManager.getInstance().login(sipListBean.getSipNumber(), sipListBean.getSipPassword()
                                            , sipListBean.getSipHost(), sipListBean.getSipPort());
                                } catch (EVVoipException e) {
                                    e.printStackTrace();
                                }
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_FINDUSERHOUSE:
                        mHouseAllList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATHouseBean>>() {
                        }.getType());
                        if (mATHouseBean == null || TextUtils.isEmpty(mATHouseBean.getBuildingCode())) {
                            //当前无选择房屋
                            llCurrent.setVisibility(View.GONE);
                            return;
                        } else {
                            //有房屋
                            llCurrent.setVisibility(View.VISIBLE);
                        }
                        mHouseList.clear();
                        for (ATHouseBean ATHouseBean : mHouseAllList) {
                            if (!mATHouseBean.getBuildingCode().equals(ATHouseBean.getBuildingCode())) {
                                mHouseList.add(ATHouseBean);
                            } else {
                                tvHouseName.setText(ATHouseBean.getName());
                                tvAddress.setText(String.format("%s%s%s", ATHouseBean.getProvince(), ATHouseBean.getCity(), ATHouseBean.getArea()));
                            }
                        }
                        if (mHouseList.size() == 0) {
                            llOther.setVisibility(View.GONE);
                        } else {
                            llOther.setVisibility(View.VISIBLE);
                        }
                        mATChangeHouseRVAdapter.setList(mHouseList);
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishLoadMore();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}