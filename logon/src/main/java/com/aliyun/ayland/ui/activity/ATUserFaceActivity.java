package com.aliyun.ayland.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.ATAutoAppBarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoCollapsingToolbarLayout;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.fragment.ATUserFaceCheckFragment;
import com.aliyun.ayland.ui.fragment.ATUserFaceRecordFragment;
import com.aliyun.ayland.utils.ATPermissionUtils;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.ayland.widget.ATCustomViewPager;
import com.aliyun.ayland.widget.face.ATFaceLivenessExpActivity;
import com.aliyun.ayland.widget.magicindicator.ATMagicIndicator;
import com.aliyun.ayland.widget.magicindicator.ATViewPagerHelper;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.ATCommonNavigator;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.abs.ATCommonNavigatorAdapter;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.abs.ATIPagerIndicator;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.abs.ATIPagerTitleView;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.indicators.ATLinePagerIndicator;
import com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.titles.ATColorTransitionPagerTitleView;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.aliyun.ayland.utils.ATPermissionUtils.WRITE_PERMISSION_REQ_CODE;

public class ATUserFaceActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private String[] mTitles;
    private List<Fragment> mFragments;
    private String imageUrl = "";
    private ATUserFaceCheckFragment mATUserFaceCheckFragment;
    private ATUserFaceRecordFragment mATUserFaceRecordFragment;
    private String openId = null, personCode = null;
    private boolean idStatus = false;
    private SmartRefreshLayout smartRefreshLayout;
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private ImageView imgFace;
    private ATMagicIndicator magicIndicator;
    private ATCustomViewPager viewpager;
    private RelativeLayout rlContent;
    private TextView tvLogging, tvFaceStatus;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.at_pic_s_rlgl_yiluru)
            .error(R.drawable.at_pic_s_rlgl_yiluru);

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_user_face;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        tvFaceStatus = findViewById(R.id.tv_face_status);
        tvLogging = findViewById(R.id.tv_logging);
        imgFace = findViewById(R.id.img_face);
        viewpager = findViewById(R.id.viewpager);
        magicIndicator = findViewById(R.id.magicIndicator);
        rlContent = findViewById(R.id.rl_content);

        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void faceVillageList() {
        mATUserFaceCheckFragment.setList(new ArrayList<>());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", idStatus ? openId : ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FACEVILLAGELIST, jsonObject);
    }

    private void getFace() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userType", "OPEN");
        jsonObject.put("userId", idStatus ? openId : ATGlobalApplication.getOpenId());
        jsonObject.put("imageFormat", "URL");
        mPresenter.request(ATConstants.Config.SERVER_URL_GETFACE, jsonObject);
    }

    private void init() {
        openId = getIntent().getStringExtra("openId");
        personCode = getIntent().getStringExtra("personCode");
        idStatus = !TextUtils.isEmpty(openId);
        ATSystemStatusBarUtils.init(ATUserFaceActivity.this, false);
        mTitles = getResources().getStringArray(R.array.tab_face_manage);
        mFragments = new ArrayList<>();
        mATUserFaceCheckFragment = new ATUserFaceCheckFragment();
        mATUserFaceRecordFragment = new ATUserFaceRecordFragment();
        mFragments.add(mATUserFaceCheckFragment);
        mFragments.add(mATUserFaceRecordFragment);

        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> rlContent.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange())));

        setUpViewPager();
        imgFace.setOnClickListener(view -> startActivity(new Intent(this, ATFaceLivenessExpActivity.class)
                .putExtra("imageUrl", imageUrl).putExtra("openId", openId).putExtra("personCode", personCode)));
        tvLogging.setOnClickListener(view -> {
            if (ATPermissionUtils.justcheckCameraPermission(this))
                startActivity(new Intent(this, ATFaceLivenessExpActivity.class)
                        .putExtra("imageUrl", imageUrl).putExtra("openId", openId).putExtra("personCode", personCode));
        });
    }

    private void setUpViewPager() {
        viewpager.setOffscreenPageLimit(mFragments.size() - 1);
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                ATColorTransitionPagerTitleView colorTransitionPagerTitleView = new ATColorTransitionPagerTitleView(ATUserFaceActivity.this);
                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color._666666));
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color._333333));
                colorTransitionPagerTitleView.setText(mTitles[index]);
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setOnClickListener(view -> viewpager.setCurrentItem(index));
                ATAutoUtils.autoSize(colorTransitionPagerTitleView);
                return colorTransitionPagerTitleView;
            }

            @Override
            public ATIPagerIndicator getIndicator(Context context) {
                ATLinePagerIndicator indicator = new ATLinePagerIndicator(context);
                indicator.setMode(ATLinePagerIndicator.MODE_EXACTLY);
                //设置indicator的宽度
                indicator.setLineWidth(60);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ATViewPagerHelper.bind(magicIndicator, viewpager);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            imageUrl = "";
            getFace();
            faceVillageList();
            mATUserFaceRecordFragment.queryVisitorRecord(0);
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FACEVILLAGELIST:
                        mATUserFaceCheckFragment.setJsonResult(jsonResult);
                        break;
                    case ATConstants.Config.SERVER_URL_GETFACE:
                        imageUrl = jsonResult.getJSONObject("data").getString("imageUrl");
                        if (!this.isDestroyed())
                            Glide.with(this).load(imageUrl).apply(options).into(imgFace);
                        tvFaceStatus.setText(R.string.at_login_yet);
                        tvLogging.setText(R.string.at_logging_again);
                        break;
                }
            } else if ("2008".equals(jsonResult.getString("code"))) {
                imgFace.setImageResource(R.drawable.at_pic_s_rlgl_weiluru);
                tvFaceStatus.setText(R.string.at_no_login_yet);
                tvLogging.setText(R.string.at_login_now);
            } else {
                showToast(jsonResult.getString("message"));
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION_REQ_CODE) {
            //请求权限
            for (int i = 0; i < permissions.length; ++i) {
                if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        startActivity(new Intent(this, ATFaceLivenessExpActivity.class)
                                .putExtra("imageUrl", imageUrl).putExtra("openId", openId).putExtra("personCode", personCode));
                    else
                        showToast(getString(R.string.at_camera_permission));
                    return;
                }
            }
        }
    }
}