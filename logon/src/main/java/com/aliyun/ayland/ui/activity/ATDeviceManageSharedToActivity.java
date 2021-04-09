package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATDeviceManageBean;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import java.util.ArrayList;

public class ATDeviceManageSharedToActivity extends ATBaseActivity {
    private ATMyTitleBar titlebar;
    private TextView tvDate, tvPhone;
    private ImageView imgUnbind;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_device_manage_share_to;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        tvDate = findViewById(R.id.tv_date);
        tvPhone = findViewById(R.id.tv_phone);
        imgUnbind = findViewById(R.id.img_unbind);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        ArrayList<ATDeviceManageBean.SharedUsersBean> sharedUsersList = getIntent().getParcelableArrayListExtra("sharedUsers");
        ArrayList<String> iotIdList = new ArrayList<>();
        iotIdList.add(getIntent().getStringExtra("iotId"));

        imgUnbind.setOnClickListener(v -> {

        });

        tvDate.setText(String.format(getString(R.string.at_add_shared_), sharedUsersList.get(0).getShareTime()));
        tvPhone.setText(sharedUsersList.get(0).getUsername());

        titlebar.setSendText(getString(R.string.at_add_shared));
        titlebar.setPublishClickListener(() -> startActivity(new Intent(this, ATDeviceManageMineToActivity.class)
                .putExtra("iotIdList", iotIdList)));
    }
}