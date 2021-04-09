package com.aliyun.ayland.ui.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.json.JSONException;

public class ATUserNickActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATLoginBean mLoginBean;
    private String nick;
    private ATMyTitleBar titleBar;
    private EditText etNick;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_user_nick;
    }

    @Override
    protected void findView() {
        titleBar = findViewById(R.id.titlebar);
        etNick = findViewById(R.id.et_nick);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void updateUserInfo() {
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("openId", mLoginBean.getOpenid());
        jsonObject.put("nickname", nick);
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEUSERINFO, jsonObject);
    }

    private void init() {
        mLoginBean = ATGlobalApplication.getATLoginBean();
        nick = mLoginBean.getNickName();
        etNick.setText(nick);
        etNick.setSelection(nick.length());
        titleBar.setSendText(getString(R.string.at_sure1));
        titleBar.setPublishClickListener(() -> {
            nick = etNick.getText().toString();
            if (TextUtils.isEmpty(nick)) {
                showToast(getString(R.string.at_input_nick));
                return;
            }
            updateUserInfo();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_UPDATEUSERINFO:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_change_nick_success));
                        mLoginBean.setNickName(nick);
                        ATGlobalApplication.setLoginBeanStr(JSONObject.toJSONString(mLoginBean));
                        finish();
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