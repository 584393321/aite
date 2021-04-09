package com.aliyun.ayland.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.ui.activity.ATAboutActivity;
import com.aliyun.ayland.ui.activity.ATChangeHouseActivity;
import com.aliyun.ayland.ui.activity.ATFamilyManageActivity;
import com.aliyun.ayland.ui.activity.ATMessageCenterActivity;
import com.aliyun.ayland.ui.activity.ATSettingActivity;
import com.aliyun.ayland.ui.activity.ATUserCenterActivity;
import com.aliyun.ayland.widget.ATMyCircleImageView;
import com.anthouse.xuhui.R;

public class ATUserFragment extends ATBaseFragment {
    private ATMyCircleImageView imgUser;
    private TextView tvNickname, tvMessageCenter, tvSwitchRoom, tvFamilyManage, tvSetting, tvFeedBack, tvAbout;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_user;
    }

    @Override
    protected void findView(View view) {
        imgUser = view.findViewById(R.id.img_user);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvMessageCenter = view.findViewById(R.id.tv_message_center);
        tvSwitchRoom = view.findViewById(R.id.tv_switch_room);
        tvFamilyManage = view.findViewById(R.id.tv_family_manage);
        tvSetting = view.findViewById(R.id.tv_setting);
        tvFeedBack = view.findViewById(R.id.tv_feed_back);
        tvAbout = view.findViewById(R.id.tv_about);
        init();
    }

    private void init() {
        tvMessageCenter.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ATMessageCenterActivity.class));
//            Router.getInstance().toUrl(getActivity(), "link://router/devicenotices");
        });
        tvSwitchRoom.setOnClickListener(v -> startActivity(new Intent(getActivity(), ATChangeHouseActivity.class)));
        tvFamilyManage.setOnClickListener(v -> startActivity(new Intent(getActivity(), ATFamilyManageActivity.class)));
        tvSetting.setOnClickListener(v -> startActivity(new Intent(getActivity(), ATSettingActivity.class)));
        imgUser.setOnClickListener(v -> startActivity(new Intent(getActivity(), ATUserCenterActivity.class)));
        tvFeedBack.setOnClickListener(v -> {
        });

        tvAbout.setOnClickListener(v -> startActivity(new Intent(getActivity(), ATAboutActivity.class)));
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