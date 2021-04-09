package com.aliyun.ayland.ui.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.ATAutoAppBarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoCollapsingToolbarLayout;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATRecommendSceneBean;
import com.aliyun.ayland.data.ATSceneManualAutoBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATLinkageSceneAutoRVAdapter;
import com.aliyun.ayland.ui.fragment.ATEmptyFragment;
import com.aliyun.ayland.ui.fragment.ATEnvironmentInsideFragment;
import com.aliyun.ayland.ui.fragment.ATEnvironmentOutsideFragment;
import com.aliyun.ayland.ui.fragment.ATEnvironmentWaterFragment;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fr on 2017/12/19.
 */

public class ATEnvironmentActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATLinkageSceneAutoRVAdapter mLinkageSceneRVAdapter;
    private int sceneType = 1, current_position = 0;
    private List<ATSceneManualAutoBean> mSceneAutoList;
    private List<ATSceneManualAutoBean> mSceneManualList;
    private List<Fragment> mFragments;
    private Fragment mCurFragment, toFragment;
    private int position = 0;
    private List<ATRecommendSceneBean> mSceneManualAutoBeanList;
    private ATEnvironmentOutsideFragment mATEnvironmentOutsideFragment;
    private ATEnvironmentInsideFragment mATEnvironmentInsideFragment;
    private ATEnvironmentWaterFragment mATEnvironmentWaterFragment;
    private ATHouseBean mATHouseBean;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RadioButton rbOutside, rbInside, rbCommunity;
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private RelativeLayout rlContent;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_environment;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        rbInside = findViewById(R.id.rb_inside);
        rbOutside = findViewById(R.id.rb_outside);
        rbCommunity = findViewById(R.id.rb_community);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        rlContent = findViewById(R.id.rl_content);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void init() {
        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            rlContent.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange()));
        });

        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        titlebar.setTitle(getString(R.string.at_wisdom_environment));
//        titlebar.setTvBack("");
//        titlebar.setTitleStrings(getString(R.string.environmental_data), getString(R.string.recommended_scene));
//        titlebar.setCeneterClick(who -> {
//            if (who == 0) {
//                llRecommend.setVisibility(View.GONE);
//                llEnvironment.setVisibility(View.VISIBLE);
//                framelayout.setVisibility(View.VISIBLE);
//                llContent.setBackground(getResources().getDrawable(R.drawable.home_hjsj_bg_a));
//            } else {
//                llRecommend.setVisibility(View.VISIBLE);
//                llEnvironment.setVisibility(View.GONE);
//                framelayout.setVisibility(View.GONE);
//                llContent.setBackgroundColor(getResources().getColor(R.color._EEEEEE));
//            }
//        });

        mFragments = new ArrayList<>();
        if (mFragments.size() == 0) {
            mATEnvironmentOutsideFragment = new ATEnvironmentOutsideFragment();
            mATEnvironmentInsideFragment = new ATEnvironmentInsideFragment();
            mATEnvironmentWaterFragment = new ATEnvironmentWaterFragment();
            mFragments.add(mATEnvironmentOutsideFragment);
            mFragments.add(mATEnvironmentInsideFragment);
            mFragments.add(mATEnvironmentWaterFragment);
            mFragments.add(new ATEmptyFragment());
        }

        mCurFragment = mFragments.get(position);
        replaceFragment(mCurFragment);

        rbOutside.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                position = 0;
                toFragment = mFragments.get(position);
                showFragment(mCurFragment, toFragment);
                mCurFragment = toFragment;
                rbInside.setChecked(false);
                rbCommunity.setChecked(false);
            }
        });
        rbCommunity.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                position = 2;
                toFragment = mFragments.get(position);
                showFragment(mCurFragment, toFragment);
                mCurFragment = toFragment;
                rbInside.setChecked(false);
                rbOutside.setChecked(false);
            }
        });
        rbInside.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                position = 1;
                toFragment = mFragments.get(position);
                showFragment(mCurFragment, toFragment);
                mCurFragment = toFragment;
                rbOutside.setChecked(false);
                rbCommunity.setChecked(false);
            }
        });

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            if (position == 0) {
                mATEnvironmentOutsideFragment.request();
            }
            if (position == 1) {
                mATEnvironmentInsideFragment.request();
            }
            if (position == 2) {
//                mATEnvironmentWaterFragment.request();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment).commit();
    }

    private void showFragment(Fragment from, Fragment to) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        if (!to.isAdded()) {
            transaction.hide(from).add(R.id.framelayout, to).commitAllowingStateLoss();
        } else {
            transaction.hide(from).show(to).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETENVIRONMENTRECOMMENDSCENELIST:
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            smartRefreshLayout.finishRefresh();
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}