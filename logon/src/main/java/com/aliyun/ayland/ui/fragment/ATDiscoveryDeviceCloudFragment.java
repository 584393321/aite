package com.aliyun.ayland.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDiscoveryDeviceFirstLevelBean;
import com.aliyun.ayland.data.ATDiscoveryDeviceSecondLevelBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.activity.ATDiscoveryDeviceProductActivity;
import com.aliyun.ayland.ui.adapter.ATDiscoveryDeviceControlRVAdapter;
import com.aliyun.ayland.ui.adapter.ATDiscoveryDeviceRightRVAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATDiscoveryDeviceCloudFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainContract.Presenter mPresenter;
    private ATDiscoveryDeviceControlRVAdapter mATDiscoveryDeviceControlRVAdapter;
    private ATDiscoveryDeviceRightRVAdapter mATDiscoveryDeviceRightRVAdapter;
    private int categoryId, pageNo = 1;
    private String categoryKey;
    private List<ATDiscoveryDeviceFirstLevelBean> mATDiscoveryDeviceFirstLevelBeanList;
    private List<ATDiscoveryDeviceSecondLevelBean> mATDiscoveryDeviceSecondLevelBeanList;
    private int statuURL = 0;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rvControl, rvDevice;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_discovery_device_cloud;
    }

    @Override
    protected void findView(View view) {
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        rvControl = view.findViewById(R.id.rv_control);
        rvDevice = view.findViewById(R.id.rv_device);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        category1();
//        getProductList("", 1, 20);
    }

    private void category() {
        JSONObject jsonObject = new JSONObject();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_CATEGORY, jsonObject);
    }

    private void category1() {
//        "categoryName": "电",                    //模糊搜索，非必填
//                "openId": "1191564967282798592",
//                "superId": 71,                    //上级菜单id，非必填，将默认查第一级
//                "pageNo": 1,                        //非必填
//                "pageSize": 5                        //非必填
        statuURL = 1;
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("hid", ATGlobalApplication.getOpenId());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("categoryName", "");
        jsonObject.put("superId", null);
        jsonObject.put("pageNo", null);
        jsonObject.put("pageSize", null);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETDEVICETYPELIST, jsonObject);
    }

    private void getProductList1() {
        statuURL = 2;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("categoryName", "");
        jsonObject.put("categoryKey", categoryKey);
        jsonObject.put("superId", categoryId);
        jsonObject.put("pageNo", null);
        jsonObject.put("pageSize", null);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETDEVICETYPELIST, jsonObject);
    }

    private void init() {
        mATDiscoveryDeviceControlRVAdapter = new ATDiscoveryDeviceControlRVAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvControl.setLayoutManager(layoutManager);
        rvControl.setHasFixedSize(true);
        rvControl.setAdapter(mATDiscoveryDeviceControlRVAdapter);
        mATDiscoveryDeviceControlRVAdapter.setOnItemClickListener(position -> {
            categoryKey = mATDiscoveryDeviceFirstLevelBeanList.get(position).getCategoryKey();
            categoryId = mATDiscoveryDeviceFirstLevelBeanList.get(position).getCategoryId();
            pageNo = 1;
            mATDiscoveryDeviceRightRVAdapter.setLists(new ArrayList<>(), 1);
            getProductList1();
        });

        mATDiscoveryDeviceRightRVAdapter = new ATDiscoveryDeviceRightRVAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvDevice.setLayoutManager(gridLayoutManager);
        rvDevice.setHasFixedSize(true);
        rvDevice.setAdapter(mATDiscoveryDeviceRightRVAdapter);
        mATDiscoveryDeviceRightRVAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getActivity(), ATDiscoveryDeviceProductActivity.class);
            intent.putExtra("categoryKey", mATDiscoveryDeviceSecondLevelBeanList.get(position).getCategoryKey())
                    .putExtra("categoryName", mATDiscoveryDeviceSecondLevelBeanList.get(position).getCategoryName())
                    .putExtra("superId", mATDiscoveryDeviceSecondLevelBeanList.get(position).getCategoryId());
            startActivity(intent);
        });

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                getProductList1();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                smartRefreshLayout.setNoMoreData(false);
                pageNo = 1;
                getProductList1();
            }
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_GETDEVICETYPELIST.equals(url)) {
                    if (statuURL == 1) {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonResult.getString("data"));
                        mATDiscoveryDeviceFirstLevelBeanList = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<ATDiscoveryDeviceFirstLevelBean>>() {
                        }.getType());
                        if (mATDiscoveryDeviceFirstLevelBeanList.size() > 0) {
                            categoryKey = mATDiscoveryDeviceFirstLevelBeanList.get(0).getCategoryKey();
                            categoryId = mATDiscoveryDeviceFirstLevelBeanList.get(0).getCategoryId();
                            pageNo = 1;
                            getProductList1();
                        }
                        mATDiscoveryDeviceControlRVAdapter.setLists(mATDiscoveryDeviceFirstLevelBeanList);
                    } else if (statuURL == 2) {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(jsonResult.getString("data"));
                        mATDiscoveryDeviceSecondLevelBeanList = gson.fromJson(jsonObject.getString("data"), new TypeToken<List<ATDiscoveryDeviceSecondLevelBean>>() {
                        }.getType());
                        mATDiscoveryDeviceRightRVAdapter.setLists(mATDiscoveryDeviceSecondLevelBeanList, pageNo);
                        if (mATDiscoveryDeviceSecondLevelBeanList.size() == 0) {
                            smartRefreshLayout.setNoMoreData(true);
                        }
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore();
                    }
                    //                    case ATConstants.Config.SERVER_URL_PRODUCTLIST:
//                        org.json.JSONObject jsonObject1 = new org.json.JSONObject(jsonResult.getString("data"));
//                        List<Category> categoryList = gson.fromJson(jsonObject1.getString("data"), new TypeToken<List<Category>>() {
//                        }.getType());
//                        mATDiscoveryDeviceRightRVAdapter.setLists(categoryList, pageNo);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}