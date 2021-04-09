package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

public class ATOldYoungCareActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATMyTitleBar titlebar;
    private TextView tvFunctionSet, tvFindLocation, tvOldYoungAlone, tvPersonnelRecord, tvPublicMonitoring;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_old_young_care;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        tvFunctionSet = findViewById(R.id.tv_function_set);
        tvFindLocation = findViewById(R.id.tv_find_location);
        tvOldYoungAlone = findViewById(R.id.tv_old_young_alone);
        tvPersonnelRecord = findViewById(R.id.tv_personnel_record);
        tvPublicMonitoring = findViewById(R.id.tv_public_monitoring);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
//        findCaringRecord();
    }

    private void findCaringRecord() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
//        jsonObject.put("villageId", mATHouseBean.getVillageId());
////        jsonObject.put("buildingCode", "100006");
////        jsonObject.put("villageId", "100000");
//        jsonObject.put("startNum", startNum);
//        jsonObject.put("pageSize", 20);
//        mPresenter.request(ATConstants.Config.SERVER_URL_FINDCARINGRECORD, jsonObject);
    }
//
//    private void queryLiveStreaming() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("iotId",mIotId);
//        mPresenter.request(ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING, jsonObject);
//    }

    private void init() {
        titlebar.setTitle(getString(R.string.at_old_young_care));

        tvFindLocation.setOnClickListener(view -> startActivity(new Intent(this, ATOldYoungCareFindLocationActivity.class)));

        tvOldYoungAlone.setOnClickListener(view -> startActivity(new Intent(this, ATOldYoungCareRecordActivity.class)
                .putExtra("typeRecord", "oldyoung")));

        tvPersonnelRecord.setOnClickListener(view -> startActivity(new Intent(this, ATOldYoungCareRecordActivity.class)
                .putExtra("typeRecord", "personnel")));

        tvPublicMonitoring.setOnClickListener(view -> startActivity(new Intent(this, ATOldYoungCareRecordActivity.class)
                .putExtra("typeRecord", "monitor")));

        tvFunctionSet.setOnClickListener(view -> startActivity(new Intent(this, ATCareFunctionSetActivity.class)));
//        ATSystemStatusBarUtils.init(this, true);
//        ATAutoUtils.auto(collapsingToolbarLayout);
//        ATAutoUtils.auto(appBarLayout);
//        ATAutoUtils.auto(mainContent);
//        titlebar.getBackground().setAlpha(0);
//        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
//            titlebar.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange()));
//            if (titlebar.getBackground().getAlpha() == 0) {
//                titlebar.getTvTitle().setTextColor(getResources().getColor(R.color.transparent));
//            } else if (titlebar.getBackground().getAlpha() == 255) {
//                titlebar.getTvTitle().setTextColor(getResources().getColor(R.color._333333));
//            }
//        });
//
//        btnOpenFunction.setOnClickListener(view -> {
//            startActivity(new Intent(this, ATCareFunctionSetActivity.class));
//        });
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mATCaringRecordRVAdapter = new ATCaringRecordRVAdapter(this);
//        recyclerView.setAdapter(mATCaringRecordRVAdapter);
//        mATCaringRecordRVAdapter.setOnItemClickListener(position -> {
//            mIotId = mATCaringRecordBeanList.get(position).getIotId();
//            queryLiveStreaming();
//        });
//
////        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
////            refreshLayout.finishRefresh(2000);
////            findCaringRecord();
////        });
//
//        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000);
//                startNum += 20;
//                findCaringRecord();
//            }
//
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh(2000);
//                startNum = 0;
//                findCaringRecord();
//            }
//        });
    }

    @Override
    public void requestResult(String result, String url) {
//        try {
//            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
//            if ("200".equals(jsonResult.getString("code"))) {
//                switch (url) {
//                    case ATConstants.Config.SERVER_URL_FINDCARINGRECORD:
//                        mATCaringRecordBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATCaringRecordBean>>() {
//                        }.getType());
//                        mATCaringRecordRVAdapter.setLists(mATCaringRecordBeanList, startNum);
//                        if(mATCaringRecordBeanList.size() > 0) {
//                            llEmpty.setVisibility(View.GONE);
//                        }
//                        break;
//                    case ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING:
//                        String path = null;
//                        path = jsonResult.getJSONObject("data").getString("path");
////                        path = jsonResult.getString("data");
//                        startActivity(new Intent(this, ATIntelligentMonitorActivity.class)
//                                .putExtra("path",path)
//                                .putExtra("iotId", mIotId));
//                        break;
//                }
//            } else {
//                showToast(jsonResult.getString("message"));
//            }
//            closeBaseProgressDlg();
//            smartRefreshLayout.finishRefresh();
//            smartRefreshLayout.finishLoadMore();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
