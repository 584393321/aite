package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import com.aliyun.ayland.data.ATMonitorDeviceBean;
import com.aliyun.ayland.ui.adapter.ATGymSubjectRVAdapter;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;

import java.util.List;

public class ATGymSubjectPopup extends ATBasePopupWindow {
    public ATGymSubjectPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        ATGymSubjectRVAdapter atGymSubjectRVAdapter = new ATGymSubjectRVAdapter(context);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(atGymSubjectRVAdapter);
//        atGymSubjectRVAdapter.setLists(list);
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
        return createPopupById(R.layout.at_popup_gym_subject);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_container);
    }

    public void setLists(List<ATMonitorDeviceBean> list) {
//        this.list.clear();
//        this.list.addAll(list);
//        notifyDataSetChanged();
    }
}