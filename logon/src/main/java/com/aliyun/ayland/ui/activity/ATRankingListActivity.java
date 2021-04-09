package com.aliyun.ayland.ui.activity;

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
import com.aliyun.ayland.data.ATRankingListBean;
import com.aliyun.ayland.data.ATRankingListBean.RankListBean;
import com.aliyun.ayland.data.ATRankingListBean.UserRankInfoBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATRankingListRvAdapter;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.anthouse.xuhui.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.List;

public class ATRankingListActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATRankingListRvAdapter mATRankingListRvAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private RelativeLayout rlContent;
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private TextView tvBack, tvTitle;
    private ATHouseBean mATHouseBean;
    private List<RankListBean> mRankList;
    private UserRankInfoBean mUserRankInfo;
    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_ranking_list;
    }

    @Override
    protected void findView() {
        recyclerView = findViewById(R.id.recyclerView);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        rlContent = findViewById(R.id.rl_content);
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        findCalorieRankList();
    }

    private void updateAgree(int rankId) {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("rankId", rankId);
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEAGREE, jsonObject);
    }

    private void findCalorieRankList() {
        if(mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDCALORIERANKLIST, jsonObject);
    }

    private void init() {
        ATSystemStatusBarUtils.init(ATRankingListActivity.this, false);

        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);
        tvBack.setOnClickListener(view -> finish());
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            rlContent.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange()));
            if (rlContent.getBackground().getAlpha() == 0) {
                ATSystemStatusBarUtils.init(ATRankingListActivity.this, false);
                tvBack.setSelected(false);
                tvTitle.setTextColor(getResources().getColor(R.color.white));
            } else if (rlContent.getBackground().getAlpha() == 255) {
                ATSystemStatusBarUtils.init(ATRankingListActivity.this, true);
                tvBack.setSelected(true);
                tvTitle.setTextColor(getResources().getColor(R.color._333333));
            }
        });

        mATRankingListRvAdapter = new ATRankingListRvAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mATRankingListRvAdapter);
        mATRankingListRvAdapter.setOnItemClickListener((view, position, id) -> {
            this.position = (Integer)position;
            updateAgree(id);
        });

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findCalorieRankList();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_UPDATEAGREE:
                        mATRankingListRvAdapter.notifyPosition(position);
                        break;
                    case ATConstants.Config.SERVER_URL_FINDCALORIERANKLIST:
                        ATRankingListBean aTRankingListBean = gson.fromJson(jsonResult.getString("data"), ATRankingListBean.class);
                        mRankList = aTRankingListBean.getRankList();
                        mUserRankInfo = aTRankingListBean.getUserRankInfo();
                        mATRankingListRvAdapter.setList(mRankList, mUserRankInfo);
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

    @Override
    protected void onResume() {
        smartRefreshLayout.autoRefresh();
        super.onResume();
    }
}