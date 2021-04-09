package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSFDeviceListBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATSFDeviceRVAdapter;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.List;

public class ATSFDeviceFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATSFDeviceRVAdapter mATSFDeviceRVAdapter;
    private RecyclerView recyclerView;
    private ATHouseBean mATHouseBean;
    private List<ATSFDeviceListBean> mDeviceList;
    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_recycleview;
    }

    @Override
    protected void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
    }

    public void setLists(List<ATSFDeviceListBean> deviceList) {
        mDeviceList = deviceList;
        if(deviceList != null && deviceList.size() > 0){
            mATSFDeviceRVAdapter.setLists(deviceList);
        }
    }

    private void deliveryDevice(int position) {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iotId", mDeviceList.get(position).getIotId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_DELIVERYDEVICE, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mATSFDeviceRVAdapter = new ATSFDeviceRVAdapter(getActivity());
        recyclerView.setAdapter(mATSFDeviceRVAdapter);
        mATSFDeviceRVAdapter.setOnItemClickListener((view, position) -> {
            this.position = position;
            deliveryDevice(position);
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_DELIVERYDEVICE:
                        if(jsonResult.has("houseState") && "105".equals(jsonResult.getString("houseState"))){
                            ATGlobalApplication.setHouseState(jsonResult.getString("houseState"));
                            getActivity().finish();
                            showToast(getString(R.string.at_sf_sf_success));
                        }else {
                            mDeviceList.remove(position);
                            mATSFDeviceRVAdapter.setLists(mDeviceList);
                            closeBaseProgressDlg();
                            showToast(getString(R.string.at_sf_hand_success));
                        }
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