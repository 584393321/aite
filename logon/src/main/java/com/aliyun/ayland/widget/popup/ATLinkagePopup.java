package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity1;
import com.aliyun.ayland.ui.activity.ATLinkageLogActivity;
import com.aliyun.ayland.utils.ATToastUtils;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;


public class ATLinkagePopup extends ATBasePopupWindow {
    public ATLinkagePopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.tv_linkage_log).setOnClickListener(view -> {
            //场景日志
            context.startActivity(new Intent(context, ATLinkageLogActivity.class));
            dismiss();
        });
        findViewById(R.id.tv_create_linkage).setOnClickListener(view -> {
            //创建场景
            if (TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
                ATToastUtils.shortShow(context.getString(R.string.at_can_not_create_scene));
                return;
            }
            context.startActivity(new Intent(context, ATLinkageAddActivity1.class));
            dismiss();
        });
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(getScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation());
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.at_popup_linkage);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_container);
    }
}