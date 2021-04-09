package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.aliyun.ayland.data.ATEventIntegerSet;
import com.aliyun.ayland.data.ATShareSpaceProjectBean;
import com.aliyun.ayland.ui.adapter.ATChooseGardenSubjectRVAdapter;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.List;

public class ATShareGardenSubjectPopup extends ATBasePopupWindow {
    private Activity context;
    private ATChooseGardenSubjectRVAdapter mATChooseGardenSubjectRVAdapter;

    public ATShareGardenSubjectPopup(Activity context) {
        super(context);
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(true);
        this.context = context;
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText(context.getString(R.string.at_choose_subject));
        findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        findViewById(R.id.tv_sure).setOnClickListener(v -> {
            if(mATChooseGardenSubjectRVAdapter.getSet().size() == 0)
                ToastUtils.toast(context, context.getString(R.string.at_choose_at_less_subject));
            else {
                EventBus.getDefault().post(new ATEventIntegerSet("ATShareGardenActivity", mATChooseGardenSubjectRVAdapter.getSet()));
                dismiss();
            }
        });

        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mATChooseGardenSubjectRVAdapter = new ATChooseGardenSubjectRVAdapter(context);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(mATChooseGardenSubjectRVAdapter);
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateAnimation(0, 250 * 2, 300);
    }

    @Override
    public View getClickToDismissView() {
        return findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.at_popup_bottom_recycleview);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_anima);
    }

    public void setLists(List<ATShareSpaceProjectBean> allList, HashSet<Integer> set, double discount) {
        mATChooseGardenSubjectRVAdapter.setLists(allList, set, discount);
    }
}