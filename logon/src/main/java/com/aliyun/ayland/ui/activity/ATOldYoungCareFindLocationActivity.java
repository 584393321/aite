package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATFamilyMemberCareBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATOldYoungCareFindLocationAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATOldYoungCareFindLocationActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATOldYoungCareFindLocationAdapter mATOldYoungCareFindLocationAdapter;
    private List<ATFamilyMemberCareBean> mATFamilyMemberCareBeanList = new ArrayList<>();
    //    private List<ATFamilyMemberCareBean> mATFamilyMemberCareBeanReallyList = new ArrayList<>();
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout llContent;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_old_young_care_find_location;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        llContent = findViewById(R.id.ll_content);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findFamilyMemberForCare();
    }

    private void findFamilyMemberForCare() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDFAMILYCHILDANDOLD, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATOldYoungCareFindLocationAdapter = new ATOldYoungCareFindLocationAdapter(this);
        recyclerView.setAdapter(mATOldYoungCareFindLocationAdapter);
        mATOldYoungCareFindLocationAdapter.setOnItemClickListener((view, position) ->
                startActivity(new Intent(this, ATOldYoungCareFindLocationDetailsActivity.class)
                        .putExtra("personCode", mATFamilyMemberCareBeanList.get(position).getPersonCode())));

        titlebar.setSendText(getString(R.string.at_add));
        titlebar.setPublishClickListener(() -> startActivity(new Intent(this, ATCareFunctionSetActivity.class)));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> findFamilyMemberForCare());
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_FINDFAMILYCHILDANDOLD.equals(url)) {
                    mATFamilyMemberCareBeanList = gson.fromJson(jsonResult.getString("members"), new TypeToken<List<ATFamilyMemberCareBean>>() {
                    }.getType());
//                        mATFamilyMemberCareBeanReallyList.clear();
                    llContent.setVisibility(mATFamilyMemberCareBeanList.size() == 0 ? View.VISIBLE : View.GONE);
//                        for (int i = 0; i < mATFamilyMemberCareBeanList.size(); i++) {
//                            if (mATFamilyMemberCareBeanList.get(i).getIfEnable() == 1) {
//                                mATFamilyMemberCareBeanReallyList.add(mATFamilyMemberCareBeanList.get(i));
//                            }
//                        }
                    mATOldYoungCareFindLocationAdapter.setLists(mATFamilyMemberCareBeanList);
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}