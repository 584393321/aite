package com.aliyun.ayland.base;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aliyun.ayland.base.autolayout.BBActivity;
import com.aliyun.ayland.utils.BackHandlerHelper;
import com.aliyun.ayland.utils.ToastUtils;
import com.anthouse.xuhui.R;
import com.google.gson.Gson;

public abstract class BaseActivity1 extends BBActivity {
    private ProgressDialog mWaitProgressDlg;
    protected abstract int getLayoutId();
    protected abstract void initPresenter();
    protected abstract void findView();
    protected Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStatusBarColor("#66000000");
        setContentView(getLayoutId());
        findView();
        initPresenter();
    }

    protected void setStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            // 取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 设置状态栏颜色
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        closeBaseProgressDlg();
    }

    public void closeBaseProgressDlg() {
        if (mWaitProgressDlg != null) {
            mWaitProgressDlg.dismiss();
        }
    }

    public void showBaseProgressDlg() {
        showBaseProgressDlg(getString(R.string.at_loading));
    }

    public void showBaseProgressDlg(String msg) {
        if (mWaitProgressDlg == null) {
            mWaitProgressDlg = new ProgressDialog(this);
            mWaitProgressDlg.setCanceledOnTouchOutside(true);
//            mWaitProgressDlg.setCanceledOnTouchOutside(false);
        } else if (mWaitProgressDlg.isShowing()) {
            return;
        }
        mWaitProgressDlg.setMessage(msg);
        mWaitProgressDlg.show();
    }

    protected void showToast(String msg) {
        ToastUtils.shortShow(msg);
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}