package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.ayland.base.BaseActivity;
import com.aliyun.ayland.contract.MainContract;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.presenter.MainPresenter;
import com.aliyun.ayland.service.SocketServer;
import com.aliyun.ayland.utils.PreferencesUtils;
import com.aliyun.ayland.utils.SystemStatusBarUtils;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageError;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;
import com.aliyun.iot.push.PushManager;
import com.anthouse.lgcs.R;

import static com.aliyun.ayland.global.DataDispatcher.ServerMessageType.TALKBACK;

public class MainActivity extends BaseActivity implements MainContract.View {
    public static final int REQUEST_CODE_PUBLISH = 0x1001;
    private MainPresenter mPresenter;
    private String iotSpaceId;
    private long clickTime = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 避免在子线程中改变了adapter中的数据
            switch (msg.what) {
                case 1:
                    IoTCredentialManageImpl.getInstance(ATApplication.getInstance()).asyncRefreshIoTCredential(new IoTCredentialListener() {
                        @Override
                        public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                            runOnUiThread(() -> startService(new Intent(MainActivity.this, SocketServer.class)));
                        }

                        @Override
                        public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                            mHandler.sendEmptyMessageDelayed(1, 3000);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findView() {
        findViewById(R.id.rl_visitor_appoint).setOnClickListener(v -> startActivity(new Intent(this,ATWelcomeActivity.class)));
        findViewById(R.id.rl_visitor_record).setOnClickListener(v -> startActivity(new Intent(this,AccessRecordActivity.class)));

        SystemStatusBarUtils.init(this, false);
        mHandler.postDelayed(() -> IoTCredentialManageImpl.getInstance(ATApplication.getInstance())
                .asyncRefreshIoTCredential(new IoTCredentialListener() {
                    @Override
                    public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                        runOnUiThread(() -> startService(new Intent(MainActivity.this, SocketServer.class)));
                    }

                    @Override
                    public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    }
                }), 1000);

        init();
        String message = getIntent().getStringExtra("message");
        if (!TextUtils.isEmpty(message)) {
            switch (message) {
                case TALKBACK:
                    String iotid = getIntent().getStringExtra("iotId");
                    PanelDevice panelDevice = new PanelDevice(iotid);
                    panelDevice.getStatus(new IPanelCallback() {
                        @Override
                        public void onComplete(boolean bSuc, Object o) {
                            Log.e("panelDeviceonComplete: ", o.toString());
                        }
                    });
                    panelDevice.getProperties(new IPanelCallback() {
                        @Override
                        public void onComplete(boolean bSuc, Object o) {
                            Log.e("panelDevicegetPropert: ", o.toString());
                        }
                    });
                    break;
            }
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(this);
        mPresenter.install(this);
    }

    private void setPresent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", PreferencesUtils.getString(this, "tempAccount", ""));
        jsonObject.put("iotToken", ATApplication.getIoTToken());
        jsonObject.put("iotSpaceId", iotSpaceId);
        mPresenter.request(Constants.Config.SERVER_URL_SETPRESENT, jsonObject);
    }

    private void init() {
        PushManager.getInstance().bindUser();
    }

    @Override
    public void requestResult(String result, String url) {
        switch (url) {
            case Constants.Config.SERVER_URL_SETPRESENT:
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", PreferencesUtils.getString(this, "tempAccount", ""));
                jsonObject.put("iotToken", ATApplication.getIoTToken());
                mPresenter.request(Constants.Config.SERVER_URL_FINDPRESENT, jsonObject);
                break;
            case Constants.Config.SERVER_URL_FINDPRESENT:
//                mPresenter.request(Constants.Config.SERVER_URL_FINDPRESENT, PreferencesUtils.getString(this, "tempAccount", ""),
//                        mIoTToken);
                Log.e("MainModel", "requestSuccess: ");
                break;
            case Constants.Config.SERVER_URL_HOUSEDEVICE:
//                mPresenter.request(Constants.Config.SERVER_URL_FINDPRESENT, PreferencesUtils.getString(this, "tempAccount", ""),
//                        mIoTToken);
                Log.e("MainModel", "requestSuccess: ");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - clickTime) > 2000) {
                showToast(getString(R.string.at_click_again_to_exit));
                clickTime = System.currentTimeMillis();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}