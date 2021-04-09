package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class ATUserCenterActivity extends ATBaseActivity {
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.atpho_s_mine_touxiang)
            .diskCacheStrategy(DiskCacheStrategy.ALL);
    private RelativeLayout rlProfile, rlNick;
    private ImageView imgUserprofile;
    private TextView tvPhone, tvNick;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_user_center;
    }

    @Override
    protected void findView() {
        rlProfile = findViewById(R.id.rl_profile);
        rlNick = findViewById(R.id.rl_nick);
        tvPhone = findViewById(R.id.tv_phone);
        tvNick = findViewById(R.id.tv_nick);
        imgUserprofile = findViewById(R.id.img_userprofile);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        rlProfile.setOnClickListener(view -> startActivity(new Intent(this, ATUserProfileActivity.class)));
        rlNick.setOnClickListener(view -> startActivity(new Intent(this, ATUserNickActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Glide.with(this).load(ATGlobalApplication.getATLoginBean().getAvatarUrl()).apply(options).into(imgUserprofile);
        tvNick.setText(ATGlobalApplication.getATLoginBean().getNickName());
        tvPhone.setText(ATGlobalApplication.getAccount());
    }
}