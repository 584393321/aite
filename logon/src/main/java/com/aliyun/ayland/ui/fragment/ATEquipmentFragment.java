package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATEventClazz;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATEquipmentControlRVAdapter;
import com.aliyun.ayland.ui.adapter.ATEquipmentRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fr on 2017/12/19.
 */
public class ATEquipmentFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATEquipmentControlRVAdapter mATEquipmentControlRVAdapter;
    private ATEquipmentRVAdapter mATEquipmentRVAdapter;
    private List<List<ATDeviceBean>> mDeviceBeanList = new ArrayList<>();
    private List<ATRoomBean1> mRoomNameList = new ArrayList<>();
    private List<ATRoomBean1> mRoomList = new ArrayList<>();
    private boolean room, device;
    private ATHouseBean mATHouseBean;
    private String mAllDeviceData;
    private ATMyTitleBar titlebar;
    private RecyclerView rvControl, rvEquipment;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_equipment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger eventString) {
        if ("ATEquipmentActivity".equals(eventString.getClazz())) {
            mATEquipmentRVAdapter.setLists(mDeviceBeanList.get(eventString.getPosition()));
        }
    }

    @Override
    protected void findView(View view) {
        titlebar = view.findViewById(R.id.titlebar);
        rvControl = view.findViewById(R.id.rv_control);
        rvEquipment = view.findViewById(R.id.rv_equipment);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());

        if (TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
            return;
        }
        showBaseProgressDlg();
        request();
    }

    private void request() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        findOrderRoom();
        getHouseDevice();
    }

    private void findOrderRoom() {
        room = false;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        ATLoginBean ATLoginBean = ATGlobalApplication.getATLoginBean();
        jsonObject.put("personCode", ATLoginBean.getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDORDERROOM, jsonObject);
    }

    private void getHouseDevice() {
        device = false;
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("iotSpaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("username", ATGlobalApplication.getAccount());
        mPresenter.request(ATConstants.Config.SERVER_URL_HOUSEDEVICE, jsonObject);
    }


    private void init() {
        titlebar.setTitleBarClickBackListener(() -> EventBus.getDefault().post(new ATEventClazz("ATHomeFragment")));
        mATEquipmentControlRVAdapter = new ATEquipmentControlRVAdapter(getActivity());
        rvControl.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        rvControl.setAdapter(mATEquipmentControlRVAdapter);

        mATEquipmentRVAdapter = new ATEquipmentRVAdapter(getActivity());
        rvEquipment.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvEquipment.setAdapter(mATEquipmentRVAdapter);
    }

    private void requestComplete() {
        if (room && device) {
            closeBaseProgressDlg();
            mDeviceBeanList.clear();
            List<ATDeviceBean> ATDeviceBeanList;
            ATGlobalApplication.setAllDeviceData(mAllDeviceData);
            JSONObject mAllDevice = JSON.parseObject(mAllDeviceData);
            for (int i = 0; i < mRoomNameList.size(); i++) {
                if (mAllDevice.containsKey(mRoomNameList.get(i).getIotSpaceId())) {
                    ATDeviceBeanList = gson.fromJson(mAllDevice.getString(mRoomNameList.get(i).getIotSpaceId()), new TypeToken<List<ATDeviceBean>>() {
                    }.getType());
                    mDeviceBeanList.add(ATDeviceBeanList);
                } else {
                    ATDeviceBeanList = new ArrayList<>();
                    mDeviceBeanList.add(ATDeviceBeanList);
                }
            }
            mATEquipmentRVAdapter.setLists(mDeviceBeanList.get(0));
            mATEquipmentControlRVAdapter.setSelectItem(0);
            mATEquipmentControlRVAdapter.setLists(mRoomNameList);
            room = false;
            device = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void setList(List<ATRoomBean1> roomNameList, List<List<ATDeviceBean>> deviceBeanList) {
        mDeviceBeanList.clear();
        mRoomNameList.clear();
        mRoomList.clear();
        mDeviceBeanList.addAll(deviceBeanList);
        mRoomNameList.addAll(roomNameList);
        mRoomList.addAll(roomNameList);
        mRoomList.remove(0);
        if (mATEquipmentControlRVAdapter != null) {
            mATEquipmentControlRVAdapter.setSelectItem(0);
            mATEquipmentControlRVAdapter.setLists(mRoomNameList);
        }
        if (mATEquipmentRVAdapter != null)
            mATEquipmentRVAdapter.setLists(deviceBeanList.get(0));
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:
                        mAllDeviceData = jsonResult.getString("data");
                        device = true;
                        requestComplete();
                        break;
                    case ATConstants.Config.SERVER_URL_FINDORDERROOM:
                        mRoomNameList.clear();
                        ATRoomBean1 roomBean = new ATRoomBean1();
                        roomBean.setType("all");
                        roomBean.setIotSpaceId("all");
                        roomBean.setName("全部设备");
                        mRoomNameList.add(roomBean);
                        List<ATRoomBean1> roomNameList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATRoomBean1>>() {
                        }.getType());
                        if (roomNameList.size() > 0)
                            mRoomNameList.addAll(roomNameList);
                        room = true;
                        requestComplete();
                        break;
                }
            } else {
                closeBaseProgressDlg();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}