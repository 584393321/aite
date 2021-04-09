package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.ui.activity.ATDeviceManageActivity;
import com.aliyun.ayland.ui.activity.ATDiscoveryDeviceActivity;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;


public class ATHomePopup extends ATBasePopupWindow {
    public ATHomePopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.tv_add_device).setOnClickListener(view -> {
            context.startActivity(new Intent(context, ATDiscoveryDeviceActivity.class));
            dismiss();
        });
        findViewById(R.id.tv_manage_room).setOnClickListener(view -> {
            EventBus.getDefault().post(new ATEventInteger("EquipmentActivity1", 0));
            dismiss();
        });
        findViewById(R.id.tv_device_manage).setOnClickListener(view -> {
            context.startActivity(new Intent(context, ATDeviceManageActivity.class));
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
        return createPopupById(R.layout.at_popup_home_style);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_container);
    }
}