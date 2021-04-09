package com.aliyun.ayland.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.Category;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATDiscoveryDeviceRvAdapter;
import com.aliyun.ayland.ui.adapter.ATDiscoveryDeviceSearchRVAdapter;
import com.aliyun.ayland.ui.fragment.ATDiscoveryDeviceCloudFragment;
import com.aliyun.ayland.ui.fragment.ATDiscoveryDeviceLocalFragment;
import com.aliyun.ayland.utils.ATAddDeviceScanHelper;
import com.aliyun.ayland.utils.ATLocalDeviceBusiness;
import com.aliyun.ayland.widget.ATCustomViewPager;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.aliyun.ayland.widget.zxing.android.ATCaptureActivity;
import com.aliyun.iot.aep.component.router.Router;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATDiscoveryDeviceActivity extends ATBaseActivity implements ATMainContract.View {
    private String TAG = "ATDiscoveryDeviceActivity";
    private ATLocalDeviceBusiness mATLocalDeviceBusiness;
    private ATMainPresenter mPresenter;
    private ATDiscoveryDeviceRvAdapter mATDiscoveryDeviceRvAdapter;
    private int currentPage = 1;
    private int pageSize = 20;
    private ATDiscoveryDeviceSearchRVAdapter mDiscoveryDeviceRvAdapter1;
    private List<Category> mCategoryList;
    private ATMyTitleBar titlebar;
    private RelativeLayout rlSearch;
    private ATCustomViewPager viewpager;
    private TabLayout tabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_discovery_device;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        viewpager = findViewById(R.id.viewpager);
        rlSearch = findViewById(R.id.rl_search);
        tabLayout = findViewById(R.id.tabLayout);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
//        getProductList("", 1, 20);
    }

    private void getProductList(String productName, int pageNo, int pageSize) {
        this.pageSize = pageSize;
        JSONObject productQuery = new JSONObject();
        productQuery.put("categoryKey", "");
        productQuery.put("productName", productName);
        productQuery.put("pageNo", pageNo);
        productQuery.put("pageSize", pageSize);

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("productQuery", productQuery);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_PRODUCTLIST, jsonObject);
    }

    private void init() {
        String productKey = getIntent().getStringExtra("productKey");
        if (!TextUtils.isEmpty(productKey)) {
            Bundle bundle = new Bundle();
            bundle.putString("productKey", productKey);
            if (!TextUtils.isEmpty(getIntent().getStringExtra("deviceName"))) {
                bundle.putString("deviceName", getIntent().getStringExtra("deviceName"));
            }
            Router.getInstance().toUrlForResult(this, ATConstants.RouterUrl.PLUGIN_ID_DEVICE_CONFIG,
                    ATAddDeviceScanHelper.REQUEST_CODE_CONFIG_WIFI, bundle);
        }
        //
//        rvSearchDevice.setLayoutManager(new LinearLayoutManager(this));
//        mDiscoveryDeviceRvAdapter1 = new ATDiscoveryDeviceSearchRVAdapter(this);
//        rvSearchDevice.setAdapter(mDiscoveryDeviceRvAdapter1);

//        tvSearch.setOnClickListener(view -> {
//            if (getString(R.string.cancel).equals(tvSearch.getText().toString())) {
//                tvSearch.setText(getString(R.string.search));
//                llSearchContent.setVisibility(View.GONE);
//            } else {
//                getProductList(etSearchContent.getText().toString(), 1, 100);
//            }
//        });
//        llSearchContent.setOnClickListener(view -> {
//            llSearchContent.setVisibility(View.GONE);
//        });

//        etSearchContent.setFocusableInTouchMode(false);
//        etSearchContent.setFocusable(false);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive() && getCurrentFocus() != null) {
//            if (getCurrentFocus().getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//
//        etSearchContent.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(etSearchContent.getText().toString())) {
//                    tvSearch.setVisibility(View.GONE);
//                    llSearchContent.setVisibility(View.GONE);
//                } else {
//                    tvSearch.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        rlSearch.setOnClickListener(view -> mHomePopup.showPopupWindow(R.id.img_add));
        rlSearch.setOnClickListener(view ->
                startActivity(new Intent(ATDiscoveryDeviceActivity.this, ATDiscoveryDeviceSearchActivity.class)));

        titlebar.setRightBtTextImage(R.drawable.at_saoyisao);
        titlebar.setRightClickListener(() -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        startActivity(new Intent(this, ATCaptureActivity.class));
                    }
                }
        );

//        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000);
//                getProductList("", currentPage, 20);
//            }
//
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh(2000);
//                currentPage = 1;
//                getProductList("", currentPage, 20);
//            }
//        });

        setUpViewPager();
    }

    private void setUpViewPager() {
        List<Fragment> fragments = initFragments();
        viewpager.setOffscreenPageLimit(fragments.size() - 1);
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        tabLayout.setupWithViewPager(viewpager);
        initTabs();
    }

    private List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ATDiscoveryDeviceCloudFragment());
        fragments.add(new ATDiscoveryDeviceLocalFragment());
        return fragments;
    }

    private void initTabs() {
        final String[] titles = getResources().getStringArray(R.array.tab_discover_device_titles);
        for (int i = 0; i < 2; i++) {
            View view = View.inflate(this, R.layout.at_tab_discover_device, null);
            ((TextView) view.findViewById(R.id.text)).setText(titles[i]);
            tabLayout.getTabAt(i).setCustomView(view);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onSelectedStateChanged(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                onSelectedStateChanged(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void onSelectedStateChanged(TabLayout.Tab tab, boolean selected) {
        if (tab.getCustomView() != null) {
            TextView text = tab.getCustomView().findViewById(R.id.text);
            if (text != null) {
                text.setSelected(selected);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ATAddDeviceScanHelper.REQUEST_CODE_CONFIG_WIFI:
                String productKey = data.getStringExtra("productKey");
                String deviceName = data.getStringExtra("deviceName");
                String token = data.getStringExtra("token");
                String iotId = data.getStringExtra("iotId");
                if (null == productKey) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("productKey", productKey);
                bundle.putString("deviceName", deviceName);
                bundle.putString("token", token);
                bundle.putString("iotId", iotId);
                startActivityForResult(new Intent(ATDiscoveryDeviceActivity.this, ATDeviceBindActivity.class)
                        .putExtras(bundle), ATDeviceBindActivity.REQUEST_CODE);
                break;
            case ATDeviceBindActivity.REQUEST_CODE:
            case ATProductListActivity.REQUEST_CODE:
                String pk = data.getStringExtra("productKey");
                String iotId1 = data.getStringExtra("iotId");
                Intent bundle1 = new Intent();
                bundle1.putExtra("iotId", iotId1);
                bundle1.putExtra("productKey", pk);
                setResult(Activity.RESULT_OK, bundle1);
                finish();
            case ATAddDeviceScanHelper.REQUEST_CODE_SCAN:
                ATAddDeviceScanHelper.wiFiConfig(this, data);
                break;
            default:
                break;

        }
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_PRODUCTLIST:
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}