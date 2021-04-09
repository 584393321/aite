package com.aliyun.ayland.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.ui.fragment.ATAccessRecordFragment;
import com.aliyun.ayland.ui.fragment.ATCarRecordFragment;
import com.aliyun.ayland.ui.fragment.ATMyCarFragment;
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

import java.util.ArrayList;
import java.util.List;

public class ATVehiclePassageActivity extends ATBaseActivity{
    private ATMyTitleBar titlebar;
    private ATMagicIndicator magicIndicator;
    private ATCustomViewPager viewPager;
    private String[] mTitles;
    private List<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_vehicle_passage;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        viewPager = findViewById(R.id.viewPager);
        magicIndicator = findViewById(R.id.magicIndicator);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        titlebar.setSendText(getString(R.string.at_add_car));
        titlebar.setPublishClickListener(() -> startActivity(new Intent(ATVehiclePassageActivity.this,ATMyCarAddActivity.class)));
        mTitles = getResources().getStringArray(R.array.tab_vehicle_passage);
        mFragments = new ArrayList<>();
        mFragments.add(new ATMyCarFragment());
        mFragments.add(new ATCarRecordFragment());
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
                ATColorTransitionPagerTitleView colorTransitionPagerTitleView = new ATColorTransitionPagerTitleView(ATVehiclePassageActivity.this);
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color._666666));
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color._333333));
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
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ATViewPagerHelper.bind(magicIndicator, viewPager);
    }
}