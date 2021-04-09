package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.service.ATSocketServer;
import com.aliyun.ayland.ui.fragment.ATEmptyFragment1;
import com.aliyun.ayland.ui.fragment.ATEmptyFragment2;
import com.aliyun.ayland.ui.fragment.ATEmptyFragment3;
import com.aliyun.ayland.ui.fragment.ATEmptyFragment4;
import com.aliyun.ayland.ui.fragment.ATHomeFragment;
import com.aliyun.ayland.utils.ATPreferencesUtils;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.ayland.widget.ATKeyBoardUI;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageError;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;
import com.aliyun.iot.push.PushManager;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.aliyun.ayland.global.ATDataDispatcher.ServerMessageType.OUTALONE;
import static com.aliyun.ayland.global.ATDataDispatcher.ServerMessageType.PASSVISITOR;
import static com.aliyun.ayland.global.ATDataDispatcher.ServerMessageType.TALKBACK;
import static com.aliyun.ayland.ui.fragment.ATLinkageFragment.REQUEST_CODE_EDIT_LINKAGE;

public class ATMainActivity extends ATBaseActivity implements View.OnClickListener, ATMainContract.View {
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<>(10);
    public static final int REQUEST_CODE_PUBLISH = 0x1001;
    private ATMainPresenter mPresenter;
    private ATHomeFragment mATHomeFragment;
    private long clickTime = 0;
    private List<Fragment> mFragments;
    private Fragment mCurFragment, toFragment;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    private int position = 0;
    private ATHouseBean mATHouseBean;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 避免在子线程中改变了adapter中的数据
            switch (msg.what) {
                case 1:
                    IoTCredentialManageImpl.getInstance(ATGlobalApplication.getInstance()).asyncRefreshIoTCredential(new IoTCredentialListener() {
                        @Override
                        public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                            runOnUiThread(() -> startService(new Intent(ATMainActivity.this, ATSocketServer.class)));
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
        return R.layout.at_activity_main1;
    }

    @Override
    protected void findView() {
        rb1 = findViewById(R.id.rb_1);
        rb2 = findViewById(R.id.rb_2);
        rb3 = findViewById(R.id.rb_3);
        rb4 = findViewById(R.id.rb_4);
        rb5 = findViewById(R.id.rb_5);

        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);
        rb4.setOnClickListener(this);
        rb5.setOnClickListener(this);

        ATSystemStatusBarUtils.init(this, false);
        ATKeyBoardUI.buildKeyBoardUI(this);
        mHandler.postDelayed(() -> IoTCredentialManageImpl.getInstance(ATGlobalApplication.getInstance())
                .asyncRefreshIoTCredential(new IoTCredentialListener() {
                    @Override
                    public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                        runOnUiThread(() -> startService(new Intent(ATMainActivity.this, ATSocketServer.class)));
                    }

                    @Override
                    public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    }
                }), 1000);

        init();
        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case PASSVISITOR:
                    startActivity(new Intent(this, ATVisitorAppointResultActivity.class)
                            .putExtra("id", getIntent().getStringExtra("id")));
                    break;
            }
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (mATHouseBean != null && !TextUtils.isEmpty(mATHouseBean.getIotSpaceId())) {
            setPresent();
        }
//        startActivity(new Intent(this, ATChangeHouseActivity.class));
    }

    private void setPresent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", ATPreferencesUtils.getString(this, "tempAccount", ""));
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_SETPRESENT, jsonObject);
    }

    private boolean isRunService(String serviceName) {
        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        PushManager.getInstance().bindUser();

        mFragments = new ArrayList<>();
//        mFragments.add(new ATUserFragment());
//        mATConvenientLifeFragment = new ATConvenientLifeFragment();
//        mFragments.add(mATConvenientLifeFragment);
        mFragments.add(new ATEmptyFragment2());
        mFragments.add(new ATEmptyFragment3());
        mFragments.add(new ATEmptyFragment1());
        mFragments.add(new ATEmptyFragment4());
        mATHomeFragment = new ATHomeFragment();
        mFragments.add(mATHomeFragment);
        mCurFragment = mFragments.get(position);
        replaceFragment(mCurFragment);
        switch (position) {
            case 1:
                rb1.setChecked(false);
                rb2.setChecked(true);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                break;
            case 2:
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(true);
                rb4.setChecked(false);
                rb5.setChecked(false);
                break;
            case 3:
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(true);
                rb5.setChecked(false);
                break;
            case 4:
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(true);
                break;
            default:
                rb1.setChecked(true);
                rb2.setChecked(false);
                rb3.setChecked(false);
                rb4.setChecked(false);
                rb5.setChecked(false);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout, fragment).commit();
    }

    private void showFragment(Fragment from, Fragment to) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        if (!to.isAdded()) {
            if (position == 2) {
                transaction.hide(from).add(R.id.main_framelayout, to, "home").commitAllowingStateLoss();
            } else {
                transaction.hide(from).add(R.id.main_framelayout, to).commitAllowingStateLoss();
            }
        } else {
            transaction.hide(from).show(to).commitAllowingStateLoss();
        }
    }

    @Override
    public void requestResult(String result, String url) {
        switch (url) {
            case ATConstants.Config.SERVER_URL_HOUSEDEVICE:
                try {
                    org.json.JSONObject jsonResult = new org.json.JSONObject(result);
                    String allDeviceData = jsonResult.getString("data");
                    ATGlobalApplication.setAllDeviceData(allDeviceData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ATConstants.Config.SERVER_URL_SETPRESENT:
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", ATPreferencesUtils.getString(this, "tempAccount", ""));
                jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
                mPresenter.request(ATConstants.Config.SERVER_URL_FINDPRESENT, jsonObject);
                break;
            case ATConstants.Config.SERVER_URL_FINDPRESENT:
//                mPresenter.request(ATConstants.Config.SERVER_URL_FINDPRESENT, ATPreferencesUtils.getString(this, "tempAccount", ""),
//                        mIoTToken);
                Log.e("ATMainModel", "requestSuccess: ");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT_LINKAGE:
//                    mATConvenientLifeFragment.onActivityResult(requestCode, resultCode, data);
                    break;
                case REQUEST_CODE_PUBLISH:
//                    mATConvenientLifeFragment.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if (listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }


    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }


    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rb_1) {
            rb1.setChecked(true);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            position = 0;
            toFragment = mFragments.get(position);
            showFragment(mCurFragment, toFragment);
            mCurFragment = toFragment;
        } else if (view.getId() == R.id.rb_2) {
            rb1.setChecked(false);
            rb2.setChecked(true);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(false);
            position = 1;
            toFragment = mFragments.get(position);
            showFragment(mCurFragment, toFragment);
            mCurFragment = toFragment;
        } else if (view.getId() == R.id.rb_3) {
            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(true);
            rb4.setChecked(false);
            rb5.setChecked(false);
            position = 2;
            toFragment = mFragments.get(position);
            showFragment(mCurFragment, toFragment);
            mCurFragment = toFragment;
        } else if (view.getId() == R.id.rb_4) {
            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(true);
            rb5.setChecked(false);
            position = 3;
            toFragment = mFragments.get(position);
            showFragment(mCurFragment, toFragment);
            mCurFragment = toFragment;
        } else if (view.getId() == R.id.rb_5) {
            rb1.setChecked(false);
            rb2.setChecked(false);
            rb3.setChecked(false);
            rb4.setChecked(false);
            rb5.setChecked(true);
            position = 4;
            toFragment = mFragments.get(position);
            showFragment(mCurFragment, toFragment);
            mCurFragment = toFragment;
        }
    }
}