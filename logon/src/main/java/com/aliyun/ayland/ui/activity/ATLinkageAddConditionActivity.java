package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aliyun.ayland.base.ATBaseActivity;
import com.anthouse.xuhui.R;

import static com.aliyun.ayland.ui.activity.ATLinkageAddActivity.REQUEST_CODE_ADD_CONDITION;

public class ATLinkageAddConditionActivity extends ATBaseActivity {
    private ATLinkageAddConditionActivity mContext;
    private RelativeLayout rlTiming, rlDevice, rlSendDeviceMessage, rlSendAppMessage, rlChangeDeviceStatus, rlPerformScene, rlVehicleAccess, rlPersonAccess;
    private LinearLayout llCondition, llAction, llTrigger;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_add_condition;
    }

    @Override
    protected void findView() {
        rlTiming = findViewById(R.id.rl_timing);
        rlDevice = findViewById(R.id.rl_device);
        rlSendDeviceMessage = findViewById(R.id.rl_send_device_message);
        rlSendAppMessage = findViewById(R.id.rl_send_app_message);
        rlChangeDeviceStatus = findViewById(R.id.rl_change_device_status);
        rlPerformScene = findViewById(R.id.rl_perform_scene);
        rlVehicleAccess = findViewById(R.id.rl_vehicle_access);
        rlPersonAccess = findViewById(R.id.rl_person_access);
        llCondition = findViewById(R.id.ll_condition);
        llAction = findViewById(R.id.ll_action);
        llTrigger = findViewById(R.id.ll_trigger);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("timing", false)) {
            rlTiming.setVisibility(View.GONE);
        }
        switch (intent.getIntExtra("flowType", 1)) {
            case 1:
                llCondition.setVisibility(View.VISIBLE);
                llAction.setVisibility(View.GONE);
                llTrigger.setVisibility(View.VISIBLE);
                break;
            case 2:
                llCondition.setVisibility(View.VISIBLE);
                llAction.setVisibility(View.GONE);
                llTrigger.setVisibility(View.GONE);
                break;
            case 3:
                llTrigger.setVisibility(View.GONE);
                llCondition.setVisibility(View.GONE);
                llAction.setVisibility(View.VISIBLE);
                break;

        }
        rlTiming.setOnClickListener(view -> {
            intent.setClass(this, ATLinkageTimingActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        rlDevice.setOnClickListener(view -> {
            intent.setClass(this, ATLinkageDeviceActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        rlSendDeviceMessage.setOnClickListener(view -> {
            intent.setClass(this, ATLinkageDeviceActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        rlSendAppMessage.setOnClickListener(view -> {
            intent.setClass(this, ATLinkageSendAppMessageActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        rlChangeDeviceStatus.setOnClickListener(view -> {
            intent.setClass(this, ATLinkageDeviceActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        rlPerformScene.setOnClickListener(view -> {
            intent.setClass(this, ATLinkagePerformSceneActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        rlVehicleAccess.setOnClickListener(view -> {
            intent.setClass(this, ATLinkageCarAccessActivity.class);
            intent.putExtra("uri", "");
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        rlPersonAccess.setOnClickListener(view -> {
            intent.setClass(this, ATLinkageAccessActivity.class);
            intent.putExtra("dataType", "108");
            intent.putExtra("uri", "trigger/biz/pass/event");
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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