package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;

public class ATProductSelectPopup extends ATBasePopupWindow {
    public ATProductSelectPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.tv_synthesize).setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(view, null, -1);
            dismiss();
        });
        findViewById(R.id.tv_descending).setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(view, null, 0);
            dismiss();
        });
        findViewById(R.id.tv_ascending).setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(view, null, 1);
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
        return createPopupById(R.layout.at_popup_product_select);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_container);
    }



    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}