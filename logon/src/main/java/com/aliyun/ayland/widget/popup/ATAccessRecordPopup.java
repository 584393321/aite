package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aliyun.ayland.data.ATEventAccessRecord;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.widget.pickerview.builder.TimePickerBuilder;
import com.aliyun.ayland.widget.pickerview.view.TimePickerView;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ATAccessRecordPopup extends ATBasePopupWindow {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private Activity context;
    private TimePickerView mPvCustomTime;
    private Calendar calendar = Calendar.getInstance();
    private RadioGroup mRgCarWalk;

    public ATAccessRecordPopup(Activity context) {
        super(context);
        this.context = context;
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setDismissWhenTouchOuside(false);
        setPopupWindowFullScreen(true);
        init();
    }

    public void setCheck(int check) {
        if(check != mRgCarWalk.getCheckedRadioButtonId())
            mRgCarWalk.check(check);
    }

    private void init() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRgCarWalk = (RadioGroup) findViewById(R.id.rg_car_walk);
        mRgCarWalk.setOnCheckedChangeListener((radioGroup1, i) -> {
            EventBus.getDefault().post(new ATEventInteger("ATAccessRecordActivity", i));
        });
        TextView tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        TextView tvEndTime = (TextView) findViewById(R.id.tv_end_time);

//        tvStartTime.setText(sdf.format(System.currentTimeMillis() - 86400000));
//        tvEndTime.setText(sdf.format(System.currentTimeMillis()));

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
        findViewById(R.id.rl_select).setOnClickListener(view -> {
            dismiss();
        });

        findViewById(R.id.button).setOnClickListener(view -> {
            EventBus.getDefault().post(new ATEventAccessRecord("ATAccessRecordActivity",
                    radioGroup.getCheckedRadioButtonId() == R.id.rb_in_out ? "" : radioGroup.getCheckedRadioButtonId() == R.id.rb_in ? "in" : "out"
                    , tvStartTime.getText().toString(), tvEndTime.getText().toString()));
            dismiss();
        });
        radioGroup.check(R.id.rb_in_out);

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
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return findViewById(R.id.click_to_dismiss);
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