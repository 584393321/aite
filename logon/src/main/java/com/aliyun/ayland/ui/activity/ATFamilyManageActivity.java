package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.ATAutoAppBarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoCollapsingToolbarLayout;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATFamilyManageRoomBean;
import com.aliyun.ayland.data.ATFamilyMenberBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATFamilyManageRVAdapter;
import com.aliyun.ayland.ui.adapter.ATFamilyManageRoomRVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.ATMyCircleImageView;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyManageActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATFamilyManageRVAdapter mATFamilyManageRVAdapter;
    private ATMyTitleBar titlebar;
    private RelativeLayout rlContent;
    private LinearLayout llAdmin, llOther;
    private TextView tvName, tvAdmin, tvStatus, tvNoOthers;
    private ATMyCircleImageView imgUser;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rvMember, rvRoom;
    private ATFamilyManageRoomRVAdapter mATFamilyManageRoomRVAdapter;
    private ArrayList<ATFamilyMenberBean> mOtherMenberList = new ArrayList<>();
    private List<ATFamilyManageRoomBean> mNotSeeRoomList = new ArrayList<>();
    private ArrayList<ATFamilyManageRoomBean> mCanSeeRoomList = new ArrayList<>();
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_family_manage;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        llAdmin = findViewById(R.id.ll_admin);
        llOther = findViewById(R.id.ll_other);
        rvMember = findViewById(R.id.rv_member);
        rvRoom = findViewById(R.id.rv_room);
        tvName = findViewById(R.id.tv_name);
        tvStatus = findViewById(R.id.tv_status);
        tvAdmin = findViewById(R.id.tv_admin);
        tvNoOthers = findViewById(R.id.tv_no_others);
        imgUser = findViewById(R.id.img_user);
        rlContent = findViewById(R.id.rl_content);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void memberRoom() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("memberPersonCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_MEMBERROOM, jsonObject);
    }

    private void findFamilyMember() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDFAMILYMEMBER, jsonObject);
    }

    private void init() {
        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> titlebar.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange())));

        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        tvName.setText(ATGlobalApplication.getATLoginBean().getNickName());
//        Glide.with(this).load(ATGlobalApplication.getATLoginBean().getAvatarUrl()).apply(options).into(imgUser);
//        rlContent.setOnClickListener(view -> {
//        });
        rvMember.setLayoutManager(new LinearLayoutManager(this));
        mATFamilyManageRVAdapter = new ATFamilyManageRVAdapter(this);
        rvMember.setAdapter(mATFamilyManageRVAdapter);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findFamilyMember();
            memberRoom();
        });
        rvRoom.setLayoutManager(new LinearLayoutManager(this));
        mATFamilyManageRoomRVAdapter = new ATFamilyManageRoomRVAdapter(this);
        rvRoom.setAdapter(mATFamilyManageRoomRVAdapter);
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        dialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_add_member, null, false);
        view.findViewById(R.id.tv_exist).setOnClickListener(view1 -> {
            startActivity(new Intent(this, ATFamilyManageAdviceActivity.class));
            dialog.dismiss();
        });
        view.findViewById(R.id.tv_temporary_regist).setOnClickListener(view2 -> {
            startActivity(new Intent(this, ATFamilyManageEntryActivity.class));
            dialog.dismiss();
        });
        dialog.setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findFamilyMember();
        memberRoom();
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_MEMBERROOM:
                        mNotSeeRoomList = gson.fromJson(jsonResult.getJSONObject("room").getString("notSee"), new TypeToken<List<ATFamilyManageRoomBean>>() {
                        }.getType());
                        mCanSeeRoomList = gson.fromJson(jsonResult.getJSONObject("room").getString("canSee"), new TypeToken<List<ATFamilyManageRoomBean>>() {
                        }.getType());
                        mATFamilyManageRoomRVAdapter.setLists(mCanSeeRoomList, mNotSeeRoomList);
                        closeBaseProgressDlg();
                        smartRefreshLayout.finishRefresh();
                        break;
                    case ATConstants.Config.SERVER_URL_FINDFAMILYMEMBER:
                        List<ATFamilyMenberBean> familyMenberList = gson.fromJson(jsonResult.getString("members"), new TypeToken<List<ATFamilyMenberBean>>() {
                        }.getType());
                        mOtherMenberList.clear();
                        for (ATFamilyMenberBean aTFamilyMenberBean : familyMenberList) {
                            if (!ATGlobalApplication.getATLoginBean().getPersonCode().equals(aTFamilyMenberBean.getPersonCode())) {
                                mOtherMenberList.add(aTFamilyMenberBean);
                            } else {
                                if (ATResourceUtils.getResIdByName(String.format(getString(R.string.at_householdtype_), aTFamilyMenberBean.getHouseholdtype()), ATResourceUtils.ResourceType.STRING) != 0)
                                    tvStatus.setText(ATResourceUtils.getResIdByName(String.format(getString(R.string.at_householdtype_), aTFamilyMenberBean.getHouseholdtype())
                                            , ATResourceUtils.ResourceType.STRING));
                                if (aTFamilyMenberBean.getIfAdmin() == 1) {
                                    //为管理员
                                    tvAdmin.setVisibility(View.VISIBLE);
                                    llAdmin.setVisibility(View.VISIBLE);
                                    llOther.setVisibility(View.GONE);
                                    titlebar.setRightBtTextImage(R.drawable.at_ioc_tianjia);
                                    titlebar.setRightClickListener(() -> dialog.show());
                                    rlContent.setOnClickListener(v -> {
                                        if (mOtherMenberList.size() == 0) {
                                            showToast(getString(R.string.at_transfer_family_manage_to1));
                                        } else {
                                            startActivity(new Intent(this, ATFamilyManageTransferActivity.class)
                                                    .putExtra("mOtherMemberList", mOtherMenberList));
                                        }
                                    });
                                } else {
                                    llAdmin.setVisibility(View.GONE);
                                    llOther.setVisibility(View.VISIBLE);
                                    rlContent.setOnClickListener(v -> {
                                    });
                                    return;
                                }
                            }
                        }
                        if (mOtherMenberList.size() == 0) {
                            tvNoOthers.setVisibility(View.VISIBLE);
                        } else {
                            tvNoOthers.setVisibility(View.GONE);
                        }
                        closeBaseProgressDlg();
                        smartRefreshLayout.finishRefresh();
                        mATFamilyManageRVAdapter.setLists(mOtherMenberList);
                        break;
                }
            } else {
                closeBaseProgressDlg();
                smartRefreshLayout.finishRefresh();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
