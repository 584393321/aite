package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.data.ATEventInteger2;
import com.aliyun.ayland.utils.ATToastUtils;
import com.aliyun.ayland.widget.contrarywind.view.WheelView;
import com.aliyun.ayland.widget.pickerview.builder.OptionsPickerBuilder;
import com.aliyun.ayland.widget.pickerview.view.OptionsPickerView;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ATMorningTotalTimePopup extends ATBasePopupWindow {
    private Activity context;
    private OptionsPickerView pvOptions;
    private WheelView mWheelMinute, mWheelSecond;
    private List<Integer> mSecondList;

    public ATMorningTotalTimePopup(Activity context) {
        super(context);
        this.context = context;
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(true);
        init();
    }

    private void init() {
        findViewById(R.id.tv_cancel).setOnClickListener(view -> dismiss());
        ((TextView) findViewById(R.id.tv_title)).setText(context.getString(R.string.at_slow_open));
        findViewById(R.id.tv_sure).setOnClickListener(view -> {
            if (mWheelMinute.getCurrentItem() == 0 && mWheelSecond.getCurrentItem() == 0) {
                ATToastUtils.shortShow(context.getString(R.string.at_total_time_limit));
                return;
            }
            EventBus.getDefault().post(new ATEventInteger2("LinkageRecommendActivity1", mWheelMinute.getCurrentItem()
                    , mSecondList.get(mWheelSecond.getCurrentItem())));
            dismiss();
        });
        pvOptions = new OptionsPickerBuilder(context, (options1, options2, options3, v) -> {
        }).setLayoutRes(R.layout.at_pickerview_morning_time, v -> {
            mWheelMinute = v.findViewById(R.id.options1);
            mWheelSecond = v.findViewById(R.id.options2);
        })
                .isDialog(false)
                .setContentTextSize(20)
//                .setSelectOptions(compareTypeENList.indexOf(compareType))//默认选中项compareValue,compareType
                .setLineSpacingMultiplier(0f)
                .setDividerColor(0xFFEEEEEE)
                .setOutSideCancelable(false)
                .setTitleBgColor(Color.WHITE)
                .setTextColorCenter(0xFF333333)
                .setTextColorOut(0xFF999999)
                .setLabels("分", "秒", "")
                .setCyclic(true, true, true)
                .isCenterLabel(true)
                .setDecorView((LinearLayout) findViewById(R.id.popup_container))
                .setOptionsSelectChangeListener((options1, options2, options3) -> {

                })
                .setBgColor(Color.WHITE)
                .build();
        List<Integer> minList1 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minList1.add(i);
        }
        List<List<Integer>> secondList = new ArrayList<>();
        mSecondList = new ArrayList<>();
        for (int j = 0; j < 60; j += 10) {
            mSecondList.add(j);
        }
        for (int i = 0; i < 60; i++) {
            secondList.add(mSecondList);
        }
        pvOptions.setPicker(minList1, secondList);
        pvOptions.show(false);
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
        return createPopupById(R.layout.at_bottom_time_popup);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    public void setCurrentTime(int min, int second) {
        mWheelMinute.setCurrentItem(min);
        mWheelSecond.setCurrentItem(mSecondList.indexOf(second));
    }
}