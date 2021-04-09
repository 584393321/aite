package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.utils.ATScreenUtils;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.anthouse.xuhui.R;

public class ATWelcomeActivity extends ATBaseActivity {
    private String TAG = "ATWelcomeActivity";
    private ATWelcomeActivity mContext;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_welcome;
    }

    @Override
    protected void findView() {
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().getDecorView().setFitsSystemWindows(true);

        ATScreenUtils.getScreenData(this);

        if (!LoginBusiness.isLogin() || TextUtils.isEmpty(ATGlobalApplication.getLoginBeanStr())) {
            LoginBusiness.login(new ILoginCallback() {
                @Override
                public void onLoginSuccess() {
                    Log.e(TAG, "登录成功");
                }

                @Override
                public void onLoginFailed(int code, String error) {
                    Log.e(TAG, "登录失败" + code + error);
                }
            });
        } else {
            if (getIntent().hasExtra("type")) {
//                startActivity(getIntent().setClass(ATWelcomeActivity.this, ATEquipmentActivity.class));
                startActivity(getIntent().setClass(ATWelcomeActivity.this, ATMainActivity.class));
            } else {
//                startActivity(new Intent(ATWelcomeActivity.this, ATEquipmentActivity.class));
                startActivity(new Intent(ATWelcomeActivity.this, ATMainActivity.class));
            }
        }
        finish();
    }
}