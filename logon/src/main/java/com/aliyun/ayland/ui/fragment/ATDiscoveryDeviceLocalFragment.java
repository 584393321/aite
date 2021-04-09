package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.IDiscoveryListener;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATLocalDevice;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATDiscoveryDeviceLocalRvAdapter;
import com.aliyun.ayland.utils.ATLocalDeviceBusiness;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATDiscoveryDeviceLocalFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainContract.Presenter mPresenter;
    private ATLocalDeviceBusiness mATLocalDeviceBusiness;
    private ATDiscoveryDeviceLocalRvAdapter mATDiscoveryDeviceLocalRvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_discovery_device_local;
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

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mATDiscoveryDeviceLocalRvAdapter = new ATDiscoveryDeviceLocalRvAdapter();
        recyclerView.setAdapter(mATDiscoveryDeviceLocalRvAdapter);

        mATLocalDeviceBusiness = new ATLocalDeviceBusiness((localDevices) -> {
            for (ATLocalDevice ATLocalDevice : localDevices) {
                mATDiscoveryDeviceLocalRvAdapter.addLocalDevice(ATLocalDevice);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mATLocalDeviceBusiness.reset();
        mATDiscoveryDeviceLocalRvAdapter.clearLocalDevices();
        LocalDeviceMgr.getInstance().startDiscovery(getActivity(), new IDiscoveryListener() {
            @Override
            public void onLocalDeviceFound(DeviceInfo deviceInfo) {
                ATLocalDevice atLocalDevice = new ATLocalDevice();
                atLocalDevice.deviceStatus = atLocalDevice.NEED_BIND;
                atLocalDevice.productKey = deviceInfo.productKey;
                atLocalDevice.deviceName = deviceInfo.deviceName;
                atLocalDevice.token = deviceInfo.token;
                atLocalDevice.addDeviceFrom = deviceInfo.addDeviceFrom;

                List<ATLocalDevice> localDevices = new ArrayList<>();
                localDevices.add(atLocalDevice);
                mATLocalDeviceBusiness.add(localDevices);
            }

            @Override
            public void onEnrolleeDeviceFound(List<DeviceInfo> list) {
                //要配网
                for (DeviceInfo deviceInfo : list) {
                    ATLocalDevice ATLocalDevice = new ATLocalDevice();
                    ATLocalDevice.deviceStatus = ATLocalDevice.NEED_BIND;
                    ATLocalDevice.productKey = deviceInfo.productKey;
                    ATLocalDevice.deviceName = deviceInfo.deviceName;
                    ATLocalDevice.addDeviceFrom = deviceInfo.addDeviceFrom;

                    List<ATLocalDevice> ATLocalDevices = new ArrayList<>();
                    ATLocalDevices.add(ATLocalDevice);
                    mATLocalDeviceBusiness.add(ATLocalDevices);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalDeviceMgr.getInstance().stopDiscovery();
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_CATEGORY:

                        break;
                    case ATConstants.Config.SERVER_URL_PRODUCTLIST:

                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}