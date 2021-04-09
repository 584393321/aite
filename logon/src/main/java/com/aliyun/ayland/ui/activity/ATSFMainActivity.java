package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSFDeviceListBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.fragment.ATSFDeviceFragment;
import com.aliyun.ayland.ui.fragment.ATSFLinkageFragment;
import com.aliyun.ayland.widget.ATCustomViewPager;
import com.aliyun.ayland.widget.magicindicator.ATMagicIndicator;
import com.aliyun.ayland.widget.magicindicator.ATViewPagerHelper;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.ATCommonNavigator;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.abs.ATCommonNavigatorAdapter;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.abs.ATIPagerIndicator;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.abs.ATIPagerTitleView;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.indicators.ATLinePagerIndicator;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.titles.ATColorTransitionPagerTitleView;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATSFMainActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATMyTitleBar titlebar;
    private ATMagicIndicator magicIndicator;
    private TextView tvHanding;
    private ATCustomViewPager viewPager;
    private String[] mTitles;
    private List<Fragment> mFragments;
    private ATHouseBean mATHouseBean;
    private ATSFDeviceFragment mDeviceFragment;
    private ATSFLinkageFragment mLinkageFragment;
    private SmartRefreshLayout smartRefreshLayout;
    private String code = "";
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 避免在子线程中改变了adapter中的数据
            if (msg.what == 1) {
                findDeliveryStatus();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.at_sf_activity_main;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        viewPager = findViewById(R.id.viewPager);
        magicIndicator = findViewById(R.id.magicIndicator);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        tvHanding = findViewById(R.id.tv_handing);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        mHandler.sendEmptyMessage(1);
    }

    private void deliveryHouse() {
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_DELIVERYHOUSE, jsonObject);
    }

    private void findDeliveryStatus() {
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDDELIVERYSTATUS, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (mATHouseBean != null){
            titlebar.setTitle1(mATHouseBean.getHouseAddress());
        }
        titlebar.setClickTurnListener(() -> {
            if("203".equals(code))
                showBaseProgressDlg(getString(R.string.at_sf_is_handing_tip));
            else
                deliveryHouse();
        });

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            mHandler.sendEmptyMessage(1);
        });

        String text = tvHanding.getText().toString();
        tvHanding.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            int index = 0;
            boolean visible = false;
            Runnable animationRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!visible)
                        return;
                    index++;
                    if (index > 3) {
                        index = 0;
                    }
                    String currentText = text;
                    for (int i = 0; i < index; i++) {
                        currentText += ".";
                    }
                    tvHanding.setText(currentText);
                    tvHanding.postDelayed(animationRunnable, index == 3 ? 1000 : 500);
                }
            };

            @Override
            public void onViewAttachedToWindow(View v) {
                visible = true;
                index = 0;
                tvHanding.postDelayed(animationRunnable, 500);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                visible = false;
            }

        });

        mTitles = getResources().getStringArray(R.array.sf_tab_main);
        mDeviceFragment = new ATSFDeviceFragment();
        mLinkageFragment = new ATSFLinkageFragment();
        mFragments = new ArrayList<>();
        mFragments.add(mDeviceFragment);
        mFragments.add(mLinkageFragment);
        setUpViewPager();
    }

    private void setUpViewPager() {
        viewPager.setOffscreenPageLimit(mFragments.size() - 1);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        ATCommonNavigator commonNavigator = new ATCommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new ATCommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitles == null ? 0 : mTitles.length;
            }

            @Override
            public ATIPagerTitleView getTitleView(Context context, final int index) {
                ATColorTransitionPagerTitleView colorTransitionPagerTitleView = new ATColorTransitionPagerTitleView(ATSFMainActivity.this);
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color._999999));
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color._5F7EE1));
                colorTransitionPagerTitleView.setText(mTitles[index]);
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                ATAutoUtils.autoSize(colorTransitionPagerTitleView);
                return colorTransitionPagerTitleView;
            }

            @Override
            public ATIPagerIndicator getIndicator(Context context) {
                ATLinePagerIndicator indicator = new ATLinePagerIndicator(context);
                indicator.setMode(ATLinePagerIndicator.MODE_EXACTLY);
                //设置indicator的宽度
                indicator.setLineWidth(54);
                indicator.setColors(R.color._9AAFF2);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ATViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            switch (url) {
                case ATConstants.Config.SERVER_URL_FINDDELIVERYSTATUS:
                    code = jsonResult.getString("code");
                    if ("200".equals(code)) {
                        if("105".equals(jsonResult.getString("houseState"))){
                            //移交成功
                            ATGlobalApplication.setHouseState(jsonResult.getString("houseState"));
                            mDeviceFragment.setLists(new ArrayList<>());
                            tvHanding.setVisibility(View.GONE);
                            showToast(getString(R.string.at_sf_sf_success));
                            finish();
                        }
                    } else if ("203".equals(code)) {
                        //正在移交
                        List<ATSFDeviceListBean> carList = gson.fromJson(jsonResult.getString("deviceList"), new TypeToken<List<ATSFDeviceListBean>>() {
                        }.getType());
                        mDeviceFragment.setLists(carList);
                        tvHanding.setVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessageDelayed(1, 5000);
                    } else if("205".equals(code)){
                        //暂未移交
                        List<ATSFDeviceListBean> carList = gson.fromJson(jsonResult.getString("deviceList"), new TypeToken<List<ATSFDeviceListBean>>() {
                        }.getType());
                        tvHanding.setVisibility(View.GONE);
                        mDeviceFragment.setLists(carList);
                    } else {
                        tvHanding.setVisibility(View.GONE);
                        showToast(jsonResult.getString("message"));
                    }
                    break;
                case ATConstants.Config.SERVER_URL_DELIVERYHOUSE:
                    if ("200".equals(jsonResult.getString("code"))) {
                        mHandler.sendEmptyMessage(1);
                    } else {
                        showToast(jsonResult.getString("message"));
                    }
                    break;
            }
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }
}