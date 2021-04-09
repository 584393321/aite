package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSIPEnterBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.utils.NumberUtil;
import com.aliyun.ayland.widget.voip.ActivityStackManager;
import com.aliyun.ayland.widget.voip.VoipManager;
import com.anthouse.xuhui.R;
import com.evideo.voip.sdk.EVVideoView;
import com.evideo.voip.sdk.EVVoipCall;
import com.evideo.voip.sdk.EVVoipException;

import org.json.JSONException;

public class ATMonitorCallingActivity extends ATBaseActivity implements ATMainContract.View, EVVoipCall.CallStateCallback, View.OnClickListener {
    private ATMainPresenter mPresenter;
    private EVVideoView eVideoView;
    private EVVoipCall evVoipCall;
    private CountDownTimer countDownTimer;
    private TextView tvName, tvName1, tvTime, tvAnswer;
    private Dialog dialog;
    private Runnable hangUpRunnable = new Runnable() {
        @Override
        public void run() {
            if (evVoipCall != null) {
                evVoipCall.setCallStateCallback(null);
                try {
                    evVoipCall.hangup();
                } catch (EVVoipException e) {
                    e.printStackTrace();
                }
                evVoipCall = null;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_monitor_calling;
    }

    @Override
    protected void findView() {
        eVideoView = (EVVideoView) getFragmentManager().findFragmentById(R.id.eVideoView);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvName1 = (TextView) findViewById(R.id.tv_name1);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAnswer = (TextView) findViewById(R.id.tv_answer);
        tvAnswer.setOnClickListener(this);
        findViewById(R.id.tv_hang_up).setOnClickListener(this);
        findViewById(R.id.tv_open).setOnClickListener(this);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        if (evVoipCall == null) {
            return;
        }
        showName(evVoipCall.getRemoteAccount().getUsername());
    }

    private void showName(String sipNumber) {
        ATHouseBean houseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if(houseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sipNumber", sipNumber);
        jsonObject.put("villageCode", houseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_SHOWNAME, jsonObject);
    }

    private void init() {
        if (getIntent().hasExtra("message")) {
            long currentTimeMillis = System.currentTimeMillis();
            long receiveTimeMillis = getIntent().getLongExtra("content", currentTimeMillis);
            if(currentTimeMillis - receiveTimeMillis > 180000 || VoipManager.getInstance().getComingCall() == null){
                showToast("通话已失效");
                finish();
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        evVoipCall = VoipManager.getInstance().getComingCall();
        if (evVoipCall == null) {
            return;
        }
        evVoipCall.setCallStateCallback(this);
        ActivityStackManager.getInstance().addActivity(this);

//        if (evVoipCall.getRemoteAccount() != null) {
//            String displayName = evVoipCall.getRemoteAccount().getDisplayName();
//            if (TextUtils.isEmpty(displayName)) {
//                tvName.setText(evVoipCall.getRemoteAccount().getUsername());
//            } else {
//                tvName.setText(displayName);
//            }
//        }
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_clean, null, false);
        TextView tvNumber = view.findViewById(R.id.tv_title);
        TextView tvLeft = view.findViewById(R.id.tv_cancel);
        TextView tvRight = view.findViewById(R.id.tv_sure);
        tvNumber.setText(getString(R.string.at_sure_to_open_door));
        tvLeft.setText(getString(R.string.at_back));
        tvRight.setText(getString(R.string.at_sure));
        tvLeft.setOnClickListener(v -> dialog.dismiss());
        tvRight.setOnClickListener(v -> {
            showBaseProgressDlg();
            evVoipCall.unlock(new EVVoipCall.UnlockCallback() {
                @Override
                public void onSuccess() {
                    closeBaseProgressDlg();
                    showToast(getString(R.string.at_open_success));
                    dialog.dismiss();
                }

                @Override
                public void onFailure() {
                    showToast(getString(R.string.at_open_failed));
                    closeBaseProgressDlg();
                }
            });
        });
        dialog.setContentView(view);
    }

    @Override
    public void onBackPressed() {
        new Thread(hangUpRunnable).start();
        super.onBackPressed();
    }

    @Override
    public void onState(EVVoipCall.CallState callState, EVVoipCall.EndReason endReason) {
        if (callState.equals(EVVoipCall.CallState.END)) {
            evVoipCall.setCallStateCallback(null);
            onError(endReason);
        } else if (callState.equals(EVVoipCall.CallState.CONNECTED)) {
            runOnUiThread(this::switchLayout);
            //true-关闭麦克风，false-开启麦克风
            evVoipCall.enableMicrophone(false);
            //true-开启，false-关闭
            evVoipCall.enableSpeaker(true);
        }
    }

    private void switchLayout() {
        countDownTimer = new CountDownTimer(3 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                tvTime.setText(String.format("%s:%s", NumberUtil.formatLong(time / 60, "00"),
                        NumberUtil.formatLong(time % 60, "00")));
                if (time <= 0) {
                    new Thread(hangUpRunnable).start();
                    finish();
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void onError(EVVoipCall.EndReason reason) {
        if (reason == EVVoipCall.EndReason.BUSY) {
        } else if (reason == EVVoipCall.EndReason.TIMEOUT) {
        } else if (reason == EVVoipCall.EndReason.NOTFOUND) {
        } else if (reason == EVVoipCall.EndReason.REJECTED) {
        } else if (reason == EVVoipCall.EndReason.NONE) {//代表被挂断超时被挂断
        } else {
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
        VoipManager.getInstance().setmComingCall(null);
        ActivityStackManager.getInstance().removeActivity(this);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_SHOWNAME:
                        ATSIPEnterBean atsipEnterBean = gson.fromJson(jsonResult.getString("data"), ATSIPEnterBean.class);
                        tvName.setText(atsipEnterBean.getBuildingName());
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_open) {
            dialog.show();
        } else if (id == R.id.tv_hang_up) {
            new Thread(hangUpRunnable).start();
            finish();
        } else if (id == R.id.tv_answer) {
            eVideoView.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);
            tvName1.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.GONE);
            try {
                evVoipCall.accept(eVideoView);
            } catch (EVVoipException e) {
                e.printStackTrace();
            }
        }
    }
}