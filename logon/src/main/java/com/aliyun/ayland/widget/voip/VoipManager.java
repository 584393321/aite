package com.aliyun.ayland.widget.voip;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.aliyun.ayland.ui.activity.ATMonitorCallingActivity;
import com.evideo.voip.sdk.EVVoipAccount;
import com.evideo.voip.sdk.EVVoipCall;
import com.evideo.voip.sdk.EVVoipCallParams;
import com.evideo.voip.sdk.EVVoipException;
import com.evideo.voip.sdk.EVVoipManager;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * o
 * Created by lhr on 2019/4/24.
 */
public class VoipManager {
    private String TAG = getClass().getSimpleName();
    private static VoipManager instance;
    private EVVoipCall mMonitorCall;
    private EVVoipCall mComingCall;
    private EVVoipAccount mEVVoipAccount;
    private EVVoipAccount.AccountStateCallback mAccountCallback;
    private EVVoipManager.IncomingCallback mIncomingCallback;
    private Context context;

    public static VoipManager getInstance() {
        if (instance == null)
            instance = new VoipManager();
        return instance;
    }

    /*初始化voip sdk*/
    public void init(Context context) {
        this.context = context;
        EVVoipManager.init(context, new EVVoipManager.OnInitCallback() {
            @Override
            public void complete() {
                Log.i("VoipManager", "voip SDK初始化成功");
//                EVVoipManager.getLc().enableVideo(true, true);
            }

            @Override
            public void error(Throwable throwable) {
                Log.i("VoipManager", "voip SDK初始化失败" + throwable.getMessage());
            }
        });
    }

    /*反初始化voip sdk*/
    private void deinit(Context context) {
        EVVoipManager.deInit(context);
    }

    /*voip登录*/
    public void login(String username, String password, String domain, int port) throws EVVoipException {
        //displayName 字段为空 表情符号等引起问题
        Log.e("VoipManager: ", "username = " + username + ", password = " + password + ", displayName = " + username + ", domain = " + domain + ", port = " + port);
        mEVVoipAccount = EVVoipManager.login(username, password, username, domain, port);//登录完成，获取到当前账号对象
        initListener();
    }

    /*voip退出登录*/
    public void logout() {
        if (mEVVoipAccount == null)
            return;
        mEVVoipAccount.logout();
    }

    private void initListener() {
        if (mAccountCallback == null) {
            mAccountCallback = state -> {
                Log.i("VoipManager", "AccountState :" + state);
                int status = 0;
                if (EVVoipAccount.AccountState.ONLINE.equals(new StateEvent(state).getState())) {
                    status = 2;
                } else if (EVVoipAccount.AccountState.LOGINPROCESS.equals(new StateEvent(state).getState())) {
                    status = 1;
                }
            };
        }
        //来电监听初始化
        if (mIncomingCallback == null) {
            mIncomingCallback = call -> {
                Log.e("VoipManager", "incoming call");
                if (mMonitorCall != null) {
                    try {
                        mMonitorCall.hangup();
                    } catch (EVVoipException e) {
                        Log.e("VoipManager", e.getMessage());
                        e.printStackTrace();
                    }
                }
                //call 表示当前来电的通话
                //如果当前有电话，并且在通话中，则挂断来电
                if (mComingCall != null || isTelephonyCalling()) {
                    Log.e("VoipManager", "当前有电话，并且在通话中，则挂断来电");
                    try {
                        call.hangup();
                    } catch (EVVoipException e) {
                        Log.e("VoipManager", e.getMessage());
                        e.printStackTrace();
                    }
                    return;
                }
                mComingCall = call;
                Log.e("VoipManager", "finishMonitorActivity");
                ActivityStackManager.getInstance().finishMonitorActivity();
                //跳转来电界面
                Log.e("VoipManager", "wakeUnlock");
                wakeUnlock(context);
                Intent intent = new Intent(context, ATMonitorCallingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Activity topActivity = ActivityStackManager.getInstance().getTopActivity();
                if (topActivity != null) {
                    Log.e("VoipManager", "topActivity:" + topActivity.getLocalClassName());
                    ActivityStackManager.getInstance().moveTaskToFront();
                    topActivity.startActivity(intent);
                } else {
                    Log.e("VoipManager", "topActivity == null");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            };
        }
        //添加该账号状态回调接口
        mEVVoipAccount.setAccountStateCallback(mAccountCallback);
        //添加来电提醒回调
        EVVoipManager.setIncomingCallback(mIncomingCallback);
    }

    private boolean isTelephonyCalling() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyManager.CALL_STATE_OFFHOOK == telephonyManager.getCallState() ||
                TelephonyManager.CALL_STATE_RINGING == telephonyManager.getCallState();
    }

    /*获取voip状态*/
    public EVVoipAccount.AccountState getVoipState() {
        if (mEVVoipAccount == null)
            return null;
        return mEVVoipAccount.getState();
    }

    /*主动监视*/
    public EVVoipCall call(String sipNum, EVVoipCallParams params) throws EVVoipException {
        return mMonitorCall = EVVoipManager.call(sipNum, params);
    }

    public EVVoipCall getComingCall() {
        return mComingCall;
    }

    public void setmComingCall(EVVoipCall mComingCall) {
        this.mComingCall = mComingCall;
    }

    private void wakeUnlock(Context context) {
        try {
            // 获取电源管理器对象
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean screenOn = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
                screenOn = pm.isInteractive();
            }
            if (!screenOn) {
                // ScreenOn是LogCat里用的Tag
                PowerManager.WakeLock wl = pm.newWakeLock(
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
                wl.acquire(1000); // 点亮屏幕
                wl.release(); // 释放
            }
            // 屏幕解锁
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(TAG);
            // 屏幕锁定
            keyguardLock.disableKeyguard(); // 解锁
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }
}