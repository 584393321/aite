package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aliyun.ayland.interfaces.OnPopupItemClickListener;
import com.aliyun.ayland.utils.DimenUtils;
import com.aliyun.ayland.widget.pickerview.builder.TimePickerBuilder;
import com.aliyun.ayland.widget.pickerview.view.TimePickerView;
import com.aliyun.ayland.widget.popup.base.BasePopupWindow;
import com.anthouse.lgcs.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AccessRecordPopup extends BasePopupWindow {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private Activity context;
    private TimePickerView mPvCustomTime;
    private Calendar calendar = Calendar.getInstance();
    private OnPopupItemClickListener mOnItemClickListener = null;

    public AccessRecordPopup(Activity context, OnPopupItemClickListener mOnItemClickListener) {
        super(context);
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setDismissWhenTouchOuside(false);
        init();
    }

    private void init() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        TextView tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        TextView tvEndTime = (TextView) findViewById(R.id.tv_end_time);

        tvStartTime.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            if (!context.getString(R.string.at_please_choose).equals(tvStartTime.getText().toString())) {
                Date date;
                try {
                    date = sdf.parse(tvStartTime.getText().toString());
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            mPvCustomTime.setDate(calendar);
            mPvCustomTime.show(tvStartTime);
        });
        tvEndTime.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            if (!context.getString(R.string.at_please_choose).equals(tvEndTime.getText().toString())) {
                Date date;
                try {
                    date = sdf.parse(tvEndTime.getText().toString());
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            mPvCustomTime.setDate(calendar);
            mPvCustomTime.show(tvEndTime);
        });

        findViewById(R.id.button).setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(radioGroup.getCheckedRadioButtonId() == R.id.rb_overtime ? "2" :
                            (radioGroup.getCheckedRadioButtonId() == R.id.rb_no ? "-1" : (radioGroup.getCheckedRadioButtonId() == R.id.rb_has ? "1" : "0"))
                    , context.getString(R.string.at_select_visit_time).equals(tvStartTime.getText().toString()) ? "" : tvStartTime.getText().toString() + ":00"
                    , context.getString(R.string.at_select_leave_time).equals(tvEndTime.getText().toString()) ? "" : tvEndTime.getText().toString() + ":00");
            dismiss();
        });

        findViewById(R.id.tv_reset).setOnClickListener(view -> {
            radioGroup.clearCheck();
            tvStartTime.setTextColor(context.getResources().getColor(R.color._BBBBBB));
            tvStartTime.setText(context.getString(R.string.at_select_visit_time));
            tvEndTime.setTextColor(context.getResources().getColor(R.color._BBBBBB));
            tvEndTime.setText(context.getString(R.string.at_select_leave_time));
        });

        initDialog();
    }

    private void initDialog() {
        Calendar endDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -10);
        //选中事件回调
        mPvCustomTime = new TimePickerBuilder(context, (date, v) -> {
            ((TextView) v).setText(sdf.format(date));
            ((TextView) v).setTextColor(context.getResources().getColor(R.color._333333));
        })
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "")
                .setDividerColor(0xFFEEEEEE)
                .isDialog(true)
                .isCenterLabel(true)
                .build();

        Dialog mDialog = mPvCustomTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            mPvCustomTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);
                dialogWindow.setGravity(Gravity.BOTTOM);
            }
        }
    }

    @Override
    protected Animation initShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -DimenUtils.dipToPx(getContext(), 350f), 0);
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(1));
        return translateAnimation;
    }

    @Override
    protected Animation initExitAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -DimenUtils.dipToPx(getContext(), 350f));
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(-4));
        return translateAnimation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.at_popup_access_record);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }
}
