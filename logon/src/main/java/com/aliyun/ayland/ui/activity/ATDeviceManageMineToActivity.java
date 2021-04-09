package com.aliyun.ayland.ui.activity;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.ArrayList;

public class ATDeviceManageMineToActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private String phone;
    private ArrayList<String> mIotIdList;
    private EditText editText;
    private Button button;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_device_manage_mine_to;
    }

    @Override
    protected void findView() {
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void shareDevice() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        JSONArray iotIdList = new JSONArray();
        iotIdList.addAll(mIotIdList);
        jsonObject.put("iotIdList", iotIdList);
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SHAREDEVICE, jsonObject);
    }

    private void init() {
        mIotIdList = getIntent().getStringArrayListExtra("iotIdList");
        button.setOnClickListener(v -> {
            phone = editText.getText().toString();
            if (!TextUtils.isEmpty(phone) && !isMobileNO(phone)) {
                showToast(getString(R.string.at_input_correct_phone));
            } else {
                shareDevice();
            }
        });
    }

    private boolean isMobileNO(String mobiles) {
        return !TextUtils.isEmpty(mobiles) && mobiles.matches("[1][34578]\\d{9}");
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_SHAREDEVICE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_share_success));
                        finish();
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