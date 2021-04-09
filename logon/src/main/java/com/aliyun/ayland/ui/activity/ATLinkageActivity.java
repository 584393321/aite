package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSceneManualAutoBean;
import com.aliyun.ayland.data.ATSceneTemplateBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATLinkageRecommendRVAdapter;
import com.aliyun.ayland.ui.adapter.ATLinkageSceneAutoRVAdapter;
import com.aliyun.ayland.widget.ATRecycleViewItemDecoration;
import com.aliyun.ayland.widget.popup.ATHomePopup;
import com.aliyun.ayland.widget.popup.ATLinkagePopup;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fr on 2017/12/19.
 */

public class ATLinkageActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATLinkageSceneAutoRVAdapter mLinkageSceneRVAdapter;
    private ATLinkageRecommendRVAdapter mATLinkageRecommendRVAdapter;
    private int sceneType = 1, current_position = 0;
    private List<ATSceneManualAutoBean> mSceneAutoList;
    private List<ATSceneManualAutoBean> mSceneManualList;
    private ATHouseBean mATHouseBean;
    private boolean enableStatus;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rvRecommend;
    private TextView tvAddLinkage, tvManual, tvLinkage;
    private SwipeMenuRecyclerView swipeMenuRecyclerView;
    private LinearLayout llRecommend, llMine, llEmpty;
    private Dialog dialog;
    private ATLinkagePopup mATLinkagePopup;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage1;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        llRecommend = findViewById(R.id.ll_recommend);
        llMine = findViewById(R.id.ll_mine);
        llEmpty = findViewById(R.id.ll_empty);
        tvManual = findViewById(R.id.tv_manual);
        tvLinkage = findViewById(R.id.tv_linkage);
        rvRecommend = findViewById(R.id.rv_recommend);
        swipeMenuRecyclerView = findViewById(R.id.swipeMenuRecyclerView);
        tvAddLinkage = findViewById(R.id.tv_add_linkage);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getHouseDevice();
    }

    private void getHouseDevice() {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getHid());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("iotSpaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("username", ATGlobalApplication.getAccount());
        mPresenter.request(ATConstants.Config.SERVER_URL_HOUSEDEVICE, jsonObject);
    }

    public void sceneDelete() {
        showBaseProgressDlg();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneType == 1 ? mSceneManualList.get(current_position).getSceneId() : mSceneAutoList.get(current_position).getSceneId());
        jsonObject.put("operator", operator);

        if (sceneType == 1) {
            jsonObject.put("sceneType", mSceneManualList.get(current_position).getSceneType());
        } else {
            jsonObject.put("sceneType", mSceneAutoList.get(current_position).getSceneType());
        }

        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEDELETE, jsonObject);
    }

    private void sceneDeployRevoke(String sceneId, boolean enable, String sceneType) {
        showBaseProgressDlg();
        enableStatus = enable;
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("sceneType", Integer.parseInt(TextUtils.isEmpty(sceneType) ? "1" : sceneType));
        jsonObject.put("operator", operator);
        jsonObject.put("enable", enable);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEDEPLOYREVOKE, jsonObject);
    }

    private void sceneInstanceRun(String sceneId) {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEINSTANCERUN, jsonObject);
    }

    private void findRecommendTemplateList() {
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDRECOMMENDTEMPLATELIST, new JSONObject());
    }

    private void getLocalUserSceneList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("sceneType", sceneType);
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETLOCALUSERSCENELIST, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        titlebar.setTitleStrings(getString(R.string.at_mine), getString(R.string.at_recommend));
        titlebar.setRightBtTextImage(R.drawable.at_ioc_tianjia);
        titlebar.setRightClickListener(() -> mATLinkagePopup.showPopupWindow(titlebar.getRightButton()));
        titlebar.setCeneterClick(who -> {
            if (who == 0) {
                llRecommend.setVisibility(View.GONE);
                llMine.setVisibility(View.VISIBLE);
            } else {
                llRecommend.setVisibility(View.VISIBLE);
                llMine.setVisibility(View.GONE);
            }
        });

        mATLinkagePopup = new ATLinkagePopup(this);

        Drawable mDrawableSelete = getResources().getDrawable(R.drawable.shape_54px9px_fda448);
        mDrawableSelete.setBounds(0, 0, mDrawableSelete.getMinimumWidth(), mDrawableSelete.getMinimumHeight());
        tvManual.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);
        tvManual.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvManual.setTextColor(getResources().getColor(R.color._333333));
        tvManual.setCompoundDrawables(null, null, null, mDrawableSelete);

        tvManual.setOnClickListener(v -> {
            swipeMenuRecyclerView.smoothCloseMenu();
            tvManual.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);
            tvManual.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvManual.setCompoundDrawables(null, null, null, mDrawableSelete);
            tvManual.setTextColor(getResources().getColor(R.color._333333));
            tvLinkage.setTextColor(getResources().getColor(R.color._666666));
            tvLinkage.setTextSize(TypedValue.COMPLEX_UNIT_PX, 42);
            tvLinkage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvLinkage.setCompoundDrawables(null, null, null, null);
            sceneType = 1;
            mLinkageSceneRVAdapter.setLists(new ArrayList<>(), sceneType);
            if (mSceneManualList == null) {
                getLocalUserSceneList();
            } else {
                mLinkageSceneRVAdapter.setLists(mSceneManualList, sceneType);
                llEmpty.setVisibility(mSceneManualList.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        tvLinkage.setOnClickListener(v -> {
            swipeMenuRecyclerView.smoothCloseMenu();
            tvLinkage.setTextSize(TypedValue.COMPLEX_UNIT_PX, 48);
            tvLinkage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvLinkage.setCompoundDrawables(null, null, null, mDrawableSelete);
            tvLinkage.setTextColor(getResources().getColor(R.color._333333));
            tvManual.setTextColor(getResources().getColor(R.color._666666));
            tvManual.setTextSize(TypedValue.COMPLEX_UNIT_PX, 42);
            tvManual.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvManual.setCompoundDrawables(null, null, null, null);
            sceneType = 2;
            mLinkageSceneRVAdapter.setLists(new ArrayList<>(), sceneType);
            if (mSceneAutoList == null) {
                getLocalUserSceneList();
            } else {
                mLinkageSceneRVAdapter.setLists(mSceneAutoList, sceneType);
                llEmpty.setVisibility(mSceneAutoList.size() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        rvRecommend.setLayoutManager(new LinearLayoutManager(this));
        mATLinkageRecommendRVAdapter = new ATLinkageRecommendRVAdapter(this);
        rvRecommend.setAdapter(mATLinkageRecommendRVAdapter);

        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLinkageSceneRVAdapter = new ATLinkageSceneAutoRVAdapter(this);
        swipeMenuRecyclerView.setNestedScrollingEnabled(false);
        swipeMenuRecyclerView.addItemDecoration(new ATRecycleViewItemDecoration(ATAutoUtils.getPercentHeightSize(54)));
        swipeMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        swipeMenuRecyclerView.setLongPressDragEnabled(false);
        swipeMenuRecyclerView.setItemViewSwipeEnabled(false);
        swipeMenuRecyclerView.setAdapter(mLinkageSceneRVAdapter);
        mLinkageSceneRVAdapter.setOnItemClickListener(new ATLinkageSceneAutoRVAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                current_position = position;
                sceneInstanceRun(mSceneManualList.get(position).getSceneId());
            }

            @Override
            public void onItemClick(int position, boolean check) {
                sceneDeployRevoke(mSceneAutoList.get(position).getSceneId(), check, mSceneAutoList.get(position).getSceneType());
            }
        });
        tvAddLinkage.setOnClickListener(view -> {
            if (TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
                showToast(getString(R.string.at_can_not_create_scene));
                return;
            }
            startActivity(new Intent(this, ATLinkageAddActivity1.class));
        });
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findRecommendTemplateList();
            getLocalUserSceneList();
        });
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_associated_scene, null, false);
        view.findViewById(R.id.tv_sure).setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
    }

    /**
     * 菜单创建器。
     */
    public SwipeMenuCreator mSwipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {
        int width = ATAutoUtils.getPercentWidthSize(171);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        SwipeMenuItem closeItem = new SwipeMenuItem(this)
                .setImage(R.drawable.at_ioc_hdshanchu)
                .setWidth(width)
                .setHeight(height);
        swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = menuBridge -> {
        menuBridge.closeMenu();
        current_position = menuBridge.getAdapterPosition();
        sceneDelete();
    };

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if (ATConstants.Config.SERVER_URL_SCENEINSTANCERUN.equals(url)) {
                if ("200".equals(jsonResult.getString("code"))) {
                    mLinkageSceneRVAdapter.setIsShowing(ATSceneManualAutoBean.SHOWSUCCESS, current_position);
                    showToast(getString(R.string.at_perform_scene_success));
                } else {
                    mLinkageSceneRVAdapter.setIsShowing(ATSceneManualAutoBean.NOTSHOW, current_position);
                    showToast(jsonResult.getString("localizedMsg"));
                }
            }
            switch (url) {
                case ATConstants.Config.SERVER_URL_HOUSEDEVICE:
                    String allDeviceData = jsonResult.getString("data");
                    ATGlobalApplication.setAllDeviceData(allDeviceData);
                    break;
                case ATConstants.Config.SERVER_URL_SCENEDELETE:
                    if ("200".equals(jsonResult.getString("code"))) {
                        closeBaseProgressDlg();
                        if (sceneType == 1) {
                            mSceneManualList.remove(current_position);
                        } else {
                            mSceneAutoList.remove(current_position);
                        }
                        mLinkageSceneRVAdapter.remove(current_position);
                        showToast(getString(R.string.at_delete_scene_success));
                    } else if ("10188".equals(jsonResult.getString("code"))) {
                        dialog.show();
                    } else {
                        showToast(getString(R.string.at_delete_scene_failed));
                    }
                    break;
                case ATConstants.Config.SERVER_URL_SCENEDEPLOYREVOKE:
                    if ("200".equals(jsonResult.getString("code"))) {
                        if (enableStatus) {
                            showToast("打开场景成功");
                        } else {
                            showToast("关闭场景成功");
                        }
                    } else {
                        showToast(getString(R.string.at_control_failed));
                    }
                    break;
                case ATConstants.Config.SERVER_URL_FINDRECOMMENDTEMPLATELIST:
                    if ("200".equals(jsonResult.getString("code"))) {
                        List<ATSceneTemplateBean> aTSceneTemplateList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATSceneTemplateBean>>() {
                        }.getType());
                        mATLinkageRecommendRVAdapter.setLists(aTSceneTemplateList);
                    } else {
                        showToast(getString(R.string.at_get_failed_please_check));
                    }
                    break;
                case ATConstants.Config.SERVER_URL_GETLOCALUSERSCENELIST:
                    if ("200".equals(jsonResult.getString("code"))) {
                        List<ATSceneManualAutoBean> aTSceneManualAutoBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATSceneManualAutoBean>>() {
                        }.getType());
                        if (sceneType == 1) {
                            mSceneManualList = new ArrayList<>();
                            mSceneManualList.addAll(aTSceneManualAutoBeanList);
                            mLinkageSceneRVAdapter.setLists(mSceneManualList, sceneType);
                            llEmpty.setVisibility(mSceneManualList.size() == 0 ? View.VISIBLE : View.GONE);
                        } else {
                            mSceneAutoList = new ArrayList<>();
                            mSceneAutoList.addAll(aTSceneManualAutoBeanList);
                            mLinkageSceneRVAdapter.setLists(mSceneAutoList, sceneType);
                            llEmpty.setVisibility(mSceneAutoList.size() == 0 ? View.VISIBLE : View.GONE);
                        }
                    } else {
                        showToast(getString(R.string.at_get_failed_please_check));
                    }
                    break;
            }
            smartRefreshLayout.finishRefresh();
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        findRecommendTemplateList();
        getLocalUserSceneList();
    }
}