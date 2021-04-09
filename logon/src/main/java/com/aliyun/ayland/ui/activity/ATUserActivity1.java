package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.ATMyCircleImageView;
import com.anthouse.xuhui.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ATUserActivity1 extends ATBaseActivity {
    private ATMyCircleImageView imgUser;
    private TextView tvNickname, tvMessageCenter, tvSwitchRoom, tvFamilyManage, tvSetting, tvFeedBack, tvAbout;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.atpho_s_mine_touxiang)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_user;
    }

    @Override
    protected void findView() {
        imgUser = findViewById(R.id.img_user);
        tvNickname = findViewById(R.id.tv_nickname);
        tvMessageCenter = findViewById(R.id.tv_message_center);
        tvSwitchRoom = findViewById(R.id.tv_switch_room);
        tvFamilyManage = findViewById(R.id.tv_family_manage);
        tvSetting = findViewById(R.id.tv_setting);
        tvFeedBack = findViewById(R.id.tv_feed_back);
        tvAbout = findViewById(R.id.tv_about);
        init();
    }

    private void init() {
        tvMessageCenter.setOnClickListener(v -> {
            startActivity(new Intent(this, ATMessageCenterActivity.class));
//            Router.getInstance().toUrl(getActivity(), "link://router/devicenotices");
        });
        tvSwitchRoom.setOnClickListener(v -> startActivity(new Intent(this, ATChangeHouseActivity.class)));
        tvFamilyManage.setOnClickListener(v -> startActivity(new Intent(this, ATFamilyManageActivity.class)));
        tvSetting.setOnClickListener(v -> startActivity(new Intent(this, ATSettingActivity.class)));
        imgUser.setOnClickListener(v -> startActivity(new Intent(this, ATUserCenterActivity.class)));
        tvFeedBack.setOnClickListener(v -> {
        });

        tvAbout.setOnClickListener(v -> startActivity(new Intent(this, ATAboutActivity.class)));
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void onResume() {
        super.onResume();
//        Glide.with(this).load(ATGlobalApplication.getATLoginBean().getAvatarUrl()).apply(options).into(imgUser);
        tvNickname.setText(TextUtils.isEmpty(ATGlobalApplication.getATLoginBean().getNickName())
                ? ATGlobalApplication.getAccount() : ATGlobalApplication.getATLoginBean().getNickName());
    }
}
