package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDiscoveryDeviceSecondLevelBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATDiscoveryDeviceProductRVAdapter;
import com.aliyun.ayland.utils.ATAddDeviceScanHelper;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.aliyun.iot.aep.component.router.Router;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.List;

public class ATDiscoveryDeviceProductActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATDiscoveryDeviceProductRVAdapter mATDiscoveryDeviceProductRVAdapter;
    private List<ATDiscoveryDeviceSecondLevelBean> mATDiscoveryDeviceSecondLevelBeanList, mATDiscoveryDeviceSecondLevelBeanList1;
    private String categoryKey , categoryName ,productName;
    private int superId ,pageNo = 1;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private EditText etSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_discovery_device_product;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.et_search);
        categoryKey = getIntent().getStringExtra("categoryKey");
        categoryName = getIntent().getStringExtra("categoryName");
        superId = getIntent().getIntExtra("superId",0);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        getProductList1();
    }

    private void getProductList1() {
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("hid", ATGlobalApplication.getOpenId());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("categoryName","");
        jsonObject.put("categoryKey",categoryKey);
        jsonObject.put("superId",superId);
        jsonObject.put("pageNo",null);
        jsonObject.put("pageSize",99);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETDEVICETYPELIST, jsonObject);
    }

    private void init() {
        titlebar.setTitle(categoryName);
        mATDiscoveryDeviceProductRVAdapter = new ATDiscoveryDeviceProductRVAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mATDiscoveryDeviceProductRVAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mATDiscoveryDeviceProductRVAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
//                pageNo++;
                getProductList1();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                smartRefreshLayout.setNoMoreData(false);
//                pageNo = 1;
                getProductList1();
            }
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETDEVICETYPELIST:
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonResult.getString("data"));
                            mATDiscoveryDeviceSecondLevelBeanList = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<ATDiscoveryDeviceSecondLevelBean>>() {
                            }.getType());
                        mATDiscoveryDeviceSecondLevelBeanList1 = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<ATDiscoveryDeviceSecondLevelBean>>() {
                        }.getType());
                        mATDiscoveryDeviceProductRVAdapter.setLists(mATDiscoveryDeviceSecondLevelBeanList, mATDiscoveryDeviceSecondLevelBeanList1,pageNo);
//                        Log.e("aaaa",mATDiscoveryDeviceSecondLevelBeanList.size() + "aaa");
                            smartRefreshLayout.setNoMoreData(true);

//                            if (mATDiscoveryDeviceSecondLevelBeanList.size() > 0) {
//                                categoryKey = mATDiscoveryDeviceSecondLevelBeanList.get(0).getCategoryKey();
//                                categoryId = mATDiscoveryDeviceSecondLevelBeanList.get(0).getCategoryId();
//                                productName = "";
//                            }
                        break;
//                    case ATConstants.Config.SERVER_URL_PRODUCTLIST:
//                        org.json.JSONObject jsonObject1 = new org.json.JSONObject(jsonResult.getString("data"));
//                        List<Category> categoryList = gson.fromJson(jsonObject1.getString("data"), new TypeToken<List<Category>>() {
//                        }.getType());
//                        mDiscoveryDeviceRightRVAdapter.setLists(categoryList, pageNo);
//                        if (categoryList.size() == 0) {
//                            smartRefreshLayout.setNoMoreData(true);
//                        }
//                        smartRefreshLayout.finishRefresh();
//                        smartRefreshLayout.finishLoadMore();
//                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadMore();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10001) {
            Bundle bundle = new Bundle();
            bundle.putString("productKey", ATConstants.ProductKey.ESP_TOUCH);
            Router.getInstance().toUrlForResult(this, ATConstants.RouterUrl.PLUGIN_ID_DEVICE_CONFIG,
                    ATAddDeviceScanHelper.REQUEST_CODE_CONFIG_WIFI, bundle);
        }
    }
}
