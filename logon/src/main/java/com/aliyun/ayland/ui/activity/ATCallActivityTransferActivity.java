package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.aliyun.ayland.base.ATBaseActivity;
import com.anthouse.xuhui.R;

public class ATCallActivityTransferActivity extends ATBaseActivity {
    private final int requestIncome = 100;
    private final int time = 500;
    private static Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_main;
    }

    @Override
    protected void findView() {
        init();
        dealIntent(getIntent());
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
    }

    private void dealIntent(Intent intent) {
        handler.removeCallbacksAndMessages(null);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(this, ATVideoATCallActivity.class);
            startActivityForResult(intent, requestIncome);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dealIntent(intent);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(this::finish, time);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /*
     * 通话结束
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestIncome == requestCode && resultCode == RESULT_OK) {
            finish();
        } else if (1002 == requestCode) {
            handler.postDelayed(this::finish, time);
        }
    }
}