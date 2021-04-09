package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.service.ATSocketServer;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.anthouse.xuhui.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;

public class ATUserActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_pho_s_mine_touxiang)
            .diskCacheStrategy(DiskCacheStrategy.ALL);
    private ImageView imgBack, imgUser;
    private LinearLayout llUser;
    private RelativeLayout rlSetting, rlAbout, rlFamilyManage, rlSwitchRoom, rlCarManage, rlMessageCenter;
    private TextView tvName;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_user;
    }

    @Override
    protected void findView() {
        llUser = findViewById(R.id.ll_user);
        imgBack = findViewById(R.id.img_back);
        imgUser = findViewById(R.id.img_user);
        rlFamilyManage = findViewById(R.id.rl_family_manage);
        rlSwitchRoom = findViewById(R.id.rl_switch_room);
        rlCarManage = findViewById(R.id.rl_car_manage);
        rlMessageCenter = findViewById(R.id.rl_message_center);
        tvName = findViewById(R.id.tv_name);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

    }

    private void init() {
        imgBack.setOnClickListener(v -> finish());
        llUser.setOnClickListener(v ->
                LoginBusiness.logout(new ILogoutCallback() {
                    @Override
                    public void onLogoutSuccess() {
                        finish();
                        ATGlobalApplication.setAllDeviceData("");
                        ATGlobalApplication.getATLoginBean().setPersonCode("");
                        stopService(new Intent(ATUserActivity.this, ATSocketServer.class));
//                            Intent intent=new Intent(ATSettingActivity.this,LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        LoginBusiness.login(new ILoginCallback() {
                            @Override
                            public void onLoginSuccess() {
                            }

                            @Override
                            public void onLoginFailed(int code, String error) {
                            }
                        });
                    }

                    @Override
                    public void onLogoutFailed(int code, String error) {
                        Log.e("onLogoutFailed: ", code + "----" + error);
                    }
                }));
        rlFamilyManage.setOnClickListener(v -> startActivity(new Intent(this, ATFamilyManageActivity.class)));
        rlSwitchRoom.setOnClickListener(v -> startActivity(new Intent(this, ATChangeHouseActivity.class)));
        rlCarManage.setOnClickListener(v -> startActivity(new Intent(this, ATVehiclePassageActivity.class)));
        rlMessageCenter.setOnClickListener(v -> {
//            startActivity(new Intent(this, ATMessageCenterActivity.class));
            Router.getInstance().toUrl(this, "link://router/devicenotices");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("aaaauser","taskid " + getTaskId() + " hsahcode " + hashCode());

//        Glide.with(this).load(ATGlobalApplication.getATLoginBean().getAvatarUrl()).apply(options).into(imgUser);
//        tvName.setText(ATGlobalApplication.getATLoginBean().getNickName());
        tvName.setText(TextUtils.isEmpty(ATGlobalApplication.getATLoginBean().getNickName())
                ? ATGlobalApplication.getAccount() : ATGlobalApplication.getATLoginBean().getNickName());
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:

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

}