package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aliyun.ayland.base.BaseActivity;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.utils.ScreenUtils;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.anthouse.xuhui.R;


public class WelcomeActivity extends BaseActivity {
    private String TAG = "WelcomeActivity";
    private WelcomeActivity mContext;

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
        ScreenUtils.getScreenData(this);

//            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
//
        if (!LoginBusiness.isLogin() || TextUtils.isEmpty(ATApplication.getLoginBeanStr())) {
            LoginBusiness.login(new ILoginCallback() {
                @Override
                public void onLoginSuccess() {
                    Log.e(TAG, "登录成功");
                }

                @Override
                public void onLoginFailed(int code, String error) {
                    Log.e(TAG, "登录失败");
                }
            });
        } else {
            if(getIntent().hasExtra("message")){
                startActivity(getIntent().setClass(WelcomeActivity.this, MainActivity.class));
            }else {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        }
        finish();
    }
}