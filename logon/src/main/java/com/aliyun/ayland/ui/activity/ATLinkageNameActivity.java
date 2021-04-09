package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATSceneManualTitle;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.ATClearEditText;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.json.JSONException;

public class ATLinkageNameActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private String sceneId;
    private ATSceneManualTitle mATSceneManualTitle;
    private ATMyTitleBar titlebar;
    private ATClearEditText clearEditText;
    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_name;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        clearEditText = findViewById(R.id.clearEditText);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    public void baseinfoUpdate() {
        showBaseProgressDlg();

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("name", clearEditText.getText());
        jsonObject.put("icon", TextUtils.isEmpty(mATSceneManualTitle.getScene_icon()) ? "https://g.aliplus.com/scene_icons/default.png" : mATSceneManualTitle.getScene_icon());
        jsonObject.put("description", "");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_BASEINFOUPDATE, jsonObject);
    }

    private void init() {
        titlebar.setRightButtonText(getString(R.string.at_sure1));
        mATSceneManualTitle = getIntent().getParcelableExtra("ATSceneManualTitle");
        sceneId = mATSceneManualTitle.getScene_id();
        if (!TextUtils.isEmpty(mATSceneManualTitle.getName())) {
            clearEditText.setText(mATSceneManualTitle.getName());
            clearEditText.setSelection(mATSceneManualTitle.getName().length());
        }

        titlebar.setRightClickListener(() -> {
            if (TextUtils.isEmpty(clearEditText.getText())) {
                showToast(getString(R.string.at_input_scene_name));
                return;
            }
            if (TextUtils.isEmpty(sceneId)) {
                setResult(RESULT_OK, new Intent().putExtra("scene_name", clearEditText.getText().toString()));
                finish();
            } else {
                baseinfoUpdate();
            }
        });
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_associated_scene, null, false);
        view.findViewById(R.id.tv_sure).setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_BASEINFOUPDATE:
                        setResult(RESULT_OK, new Intent().putExtra("scene_name", clearEditText.getText().toString()));
                        showToast(getString(R.string.at_edit_linkage_name_success));
                        finish();
                        break;
                }
            } else {
                if ("10188".equals(jsonResult.getString("code"))) {
                    dialog.show();
                } else
                    showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
