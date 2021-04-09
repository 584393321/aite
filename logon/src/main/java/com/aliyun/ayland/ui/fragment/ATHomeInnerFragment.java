package com.aliyun.ayland.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.ui.adapter.ATHomeControlRVAdapter;
import com.aliyun.ayland.ui.adapter.ATHomeInnerRightRVAdapter;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ATHomeInnerFragment extends ATBaseFragment {
    private ATHomeControlRVAdapter mATHomeControlRVAdapter;
    private ATHomeInnerRightRVAdapter mHomeDeviceRVAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<List<ATDeviceBean>> mDeviceBeanList = new ArrayList<>();
    private List<ATRoomBean1> mRoomNameList = new ArrayList<>();
    private List<ATRoomBean1> mRoomList = new ArrayList<>();
    private RecyclerView rvHomeDevice;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger eventString) {
        if ("ATHomeInnerFragment".equals(eventString.getClazz())) {
            mHomeDeviceRVAdapter.setLists(mDeviceBeanList.get(eventString.getPosition()));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_home_inner;
    }

    @Override
    protected void findView(View view) {
        rvHomeDevice = view.findViewById(R.id.rv_home_device);
        init();
    }

    private void init() {
        mATHomeControlRVAdapter = new ATHomeControlRVAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mHomeDeviceRVAdapter = new ATHomeInnerRightRVAdapter(getActivity());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        rvHomeDevice.setLayoutManager(layoutManager1);
        rvHomeDevice.setHasFixedSize(true);
        rvHomeDevice.setAdapter(mHomeDeviceRVAdapter);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void setList(List<ATRoomBean1> roomNameList, List<List<ATDeviceBean>> deviceBeanList, String allDevice) {
        mDeviceBeanList.clear();
        mRoomNameList.clear();
        mRoomList.clear();
        mDeviceBeanList.addAll(deviceBeanList);
        mRoomNameList.addAll(roomNameList);
        mRoomList.addAll(roomNameList);
        mRoomList.remove(0);

        ATRoomBean1 roomBean = new ATRoomBean1();
        roomBean.setName("更多");
        roomBean.setType("more");
        mRoomNameList.add(roomBean);
        if (mATHomeControlRVAdapter != null) {
            mATHomeControlRVAdapter.setSelectItem(0);
            mATHomeControlRVAdapter.setLists(mRoomNameList, allDevice);
        }
        if (mHomeDeviceRVAdapter != null)
            mHomeDeviceRVAdapter.setLists(deviceBeanList.get(0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}