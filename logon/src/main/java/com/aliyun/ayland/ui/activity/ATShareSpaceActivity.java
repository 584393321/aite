package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.ATAutoAppBarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoCollapsingToolbarLayout;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATShareAppointRecordBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATShareAppointRecordRVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATShareSpaceActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATShareAppointRecordRVAdapter mATShareAppointRecordRVAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rvVisitorRecord;
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private TextView tvTitle, tvAppoint, tvDialogTitle;
    private ImageView imgBack;
    private RelativeLayout rlContent;
    private ATHouseBean mATHouseBean;
    private int pageNo = 1, mPosition, appointmentStatus;
    private List<ATShareAppointRecordBean> mAtShareAppointBeanList = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_share_space;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        rvVisitorRecord = findViewById(R.id.recyclerView);
        tvTitle = findViewById(R.id.tv_title);
        imgBack = findViewById(R.id.img_back);
        rlContent = findViewById(R.id.rl_content);
        tvAppoint = findViewById(R.id.tv_appoint);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void findAppointmentStatus() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        jsonObject.put("pageNo", pageNo);
        jsonObject.put("pageSize", 10);
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDAPPOINTMENTSTATUS, jsonObject);
    }

    private void setAppointmentStatusApp() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appointmentOrderCode", mAtShareAppointBeanList.get(mPosition).getAppointmentOrderCode());
        jsonObject.put("appointmentStatus", appointmentStatus);
        jsonObject.put("confirmPerson", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_SETAPPOINTMENTSTATUSAPP, jsonObject);
    }

    private void init() {
        ATSystemStatusBarUtils.init(ATShareSpaceActivity.this, false);
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);

        tvAppoint.setOnClickListener(view -> startActivity(new Intent(this, ATShareGardenActivity.class)));

        imgBack.setOnClickListener(view -> finish());
        imgBack.setSelected(true);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            rlContent.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange()));
            if (rlContent.getBackground().getAlpha() == 0) {
                ATSystemStatusBarUtils.init(ATShareSpaceActivity.this, false);
                imgBack.setSelected(false);
                tvTitle.setTextColor(getResources().getColor(R.color.white));
            } else if (rlContent.getBackground().getAlpha() == 255) {
                ATSystemStatusBarUtils.init(ATShareSpaceActivity.this, true);
                imgBack.setSelected(true);
                tvTitle.setTextColor(getResources().getColor(R.color._333333));
            }
        });

        rvVisitorRecord.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvVisitorRecord.setNestedScrollingEnabled(false);
        mATShareAppointRecordRVAdapter = new ATShareAppointRecordRVAdapter(this);
        rvVisitorRecord.setAdapter(mATShareAppointRecordRVAdapter);
        mATShareAppointRecordRVAdapter.setATOnRVItemIntegerClickListener((view, position, type) -> {
            mPosition = position;
            appointmentStatus = type;
            tvDialogTitle.setText(ATResourceUtils.getString(ATResourceUtils.getResIdByName(String.format(
                    getString(R.string.at_sure_appoint_operate), appointmentStatus), ATResourceUtils.ResourceType.STRING)));
            dialog.show();
        });

        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                pageNo++;
                findAppointmentStatus();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                pageNo = 1;
                findAppointmentStatus();
                smartRefreshLayout.setNoMoreData(false);
            }
        });
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_normal, null, false);
        tvDialogTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.tv_sure).setOnClickListener(v -> setAppointmentStatusApp());
        dialog.setContentView(view);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadMore();
            closeBaseProgressDlg();
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_FINDAPPOINTMENTSTATUS.equals(url)) {
                    List<ATShareAppointRecordBean> atShareAppointBeanList = gson.fromJson(jsonResult.getString("result"), new TypeToken<List<ATShareAppointRecordBean>>() {
                    }.getType());
                    if (pageNo == 1)
                        mAtShareAppointBeanList.clear();
                    if (atShareAppointBeanList.size() == 0) {
                        if (pageNo != 1)
                            pageNo--;
                        smartRefreshLayout.setNoMoreData(true);
                        return;
                    }
                    mAtShareAppointBeanList.addAll(atShareAppointBeanList);
                    mATShareAppointRecordRVAdapter.setLists(mAtShareAppointBeanList);
                } else if (ATConstants.Config.SERVER_URL_SETAPPOINTMENTSTATUSAPP.equals(url)) {
                    showToast(ATResourceUtils.getString(ATResourceUtils.getResIdByName(String.format(
                            getString(R.string.at_sure_appoint_operate_success), appointmentStatus), ATResourceUtils.ResourceType.STRING)));
                    mAtShareAppointBeanList.get(mPosition).setAppointmentStatus(appointmentStatus);
                    mATShareAppointRecordRVAdapter.setLists(mAtShareAppointBeanList, mPosition);
                    dialog.dismiss();
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        smartRefreshLayout.autoRefresh();
    }
}