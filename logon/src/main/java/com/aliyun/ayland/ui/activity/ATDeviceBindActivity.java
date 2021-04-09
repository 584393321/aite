package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATDevice;
import com.aliyun.ayland.presenter.ATDeviceBindPresenter;
import com.aliyun.ayland.ui.fragment.ATDeviceBindFragment;
import com.aliyun.ayland.utils.ATDeviceBindBusiness;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

public class ATDeviceBindActivity extends ATBaseActivity {
    public static final int REQUEST_CODE = 0x898;
    private String TAG = "ATDeviceBindActivity";
    private ATDeviceBindBusiness mATDeviceBindBusiness;
    private ATDeviceBindPresenter mPresenter = null;
    private ATDeviceBindFragment mATDeviceBindFragment = null;
    private ATMyTitleBar titlebar;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_device_bind;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String productKey = intent.getStringExtra("productKey");
        String deviceName = intent.getStringExtra("deviceName");
        String netType = intent.getStringExtra("netType");

        ATDevice ATDevice = new ATDevice();
        ATDevice.dn = deviceName;
        ATDevice.pk = productKey;
        ATDevice.token = intent.getStringExtra("token");
        ATDevice.iotId = intent.getStringExtra("iotId");

        mATDeviceBindBusiness = new ATDeviceBindBusiness();

        if (null == savedInstanceState) {
            mATDeviceBindFragment = new ATDeviceBindFragment();
            mPresenter = new ATDeviceBindPresenter(mATDeviceBindBusiness, ATDevice, mATDeviceBindFragment);
            mATDeviceBindFragment.setPresenter(mPresenter);
        }
        titlebar.showRightButton(true);
        titlebar.setRightButtonText(getString(R.string.at_done));
        titlebar.setTitle(getResources().getString(R.string.at_label_bind_device));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mATDeviceBindFragment, mATDeviceBindFragment.getClass().getSimpleName())
                .disallowAddToBackStack()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
//        if (null != ATDevice.roomId) {
//            titleBar.showRightButton(false);
//            titleBar.setTitle(getResources().getString(R.string.label_bind_device));
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, mATDeviceBindFragment, mATDeviceBindFragment.getClass().getSimpleName())
//                    .disallowAddToBackStack()
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .commit();
//        } else {
//            titleBar.showRightButton(true);
//            titleBar.setRightClickListener(new ATOnTitleRightClickInter() {
//                @Override
//                public void rightClick() {
//                    onRoomSelected();
//                }
//            });
//
//            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//                @Override
//                public void onBackStackChanged() {
//                    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//                        titleBar.showRightButton(true);
//                        ((TextView) findViewById(R.id.title)).setText(R.string.label_room_list);
//                    } else {
//                        titleBar.showRightButton(false);
//                        ((TextView) findViewById(R.id.title)).setText(R.string.label_bind_device);
//                    }
//                }
//            });
//            setupRecyclerView();
//            initRooms();
//        }
    }

    private void onRoomSelected() {
//        if (adapter.checkedPosition != RecyclerView.NO_POSITION &&
//                adapter.getItem(adapter.checkedPosition) != null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, mATDeviceBindFragment, mATDeviceBindFragment.getClass().getSimpleName())
//                    .addToBackStack(mATDeviceBindFragment.getClass().getSimpleName())
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .commit();
//            mPresenter.setRoomId(adapter.getItem(adapter.checkedPosition).roomId);
//        } else {
//            LinkToast.makeText(this, R.string.hint_no_room_selected, Toast.LENGTH_SHORT).show();
//        }
    }
}
