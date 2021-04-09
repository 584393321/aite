package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.ui.adapter.ATLinkageEquipmentRVAdapter;
import com.anthouse.xuhui.R;

import java.util.ArrayList;

import static com.aliyun.ayland.ui.activity.ATLinkageAddActivity.REQUEST_CODE_ADD_CONDITION;

public class ATLinkageEquipmentActivity extends ATBaseActivity {
    private RecyclerView rvEquipment;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_equipment;
    }

    @Override
    protected void findView() {
        rvEquipment = findViewById(R.id.rv_equipment);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        Intent intent = getIntent();
        ArrayList<ATDeviceBean> deviceList = intent.getParcelableArrayListExtra("deviceList");
        ATLinkageEquipmentRVAdapter mEquipmentRVAdapter = new ATLinkageEquipmentRVAdapter(this, deviceList);
        rvEquipment.setLayoutManager(new GridLayoutManager(this, 2));
        rvEquipment.setAdapter(mEquipmentRVAdapter);
        mEquipmentRVAdapter.setOnItemClickListener((view, position) -> {
            intent.putExtra("iotId", deviceList.get(position).getIotId());
            intent.putExtra("name", deviceList.get(position).getProductName());
            intent.putExtra("productKey", deviceList.get(position).getProductKey());
            intent.setClass(this, ATLinkageStatusActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_CONDITION:
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        }
    }
}