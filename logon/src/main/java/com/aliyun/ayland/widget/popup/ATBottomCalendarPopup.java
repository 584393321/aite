package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.aliyun.ayland.data.ATEventString;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.aliyun.ayland.interfaces.ATOnStringCallBack;
import com.aliyun.ayland.utils.ATToastUtils;
import com.aliyun.ayland.widget.ATCalendarDecorator;
import com.aliyun.ayland.widget.popup.base.ATBasePopupWindow;
import com.anthouse.xuhui.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.TimeZone;

public class ATBottomCalendarPopup extends ATBasePopupWindow {
    private View popupView;
    private int currentPosition;
    private ATOnStringCallBack atOnStringCallBack;

    public ATBottomCalendarPopup(Activity context, ATOnStringCallBack atOnStringCallBack) {
        super(context);
        this.atOnStringCallBack = atOnStringCallBack;
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(true);
        init();
    }

    private void init() {
        MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        //设置周的文本
        calendarView.setWeekDayLabels(new String[]{"一", "二", "三", "四", "五", "六", "日"});
        //设置年月的title
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(day.getYear()).append("年").append(day.getMonth()).append("月");
                return buffer;
            }
        });
        Calendar calendars = Calendar.getInstance();
        calendars.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendarView.setSelectedDate(CalendarDay.today());
        calendarView.setCurrentDate(CalendarDay.today());
        calendarView.addDecorators(
                new ATCalendarDecorator()
        );
        findViewById(R.id.ll_close).setOnClickListener(view -> dismiss());
        findViewById(R.id.ll_right).setOnClickListener(view -> {
            CalendarDay selectedDate = calendarView.getSelectedDate();
            if(selectedDate != null && selectedDate.isBefore(CalendarDay.today())){
                ATToastUtils.shortShow("不能预约今天之前的日期");
                return;
            }
            if (selectedDate != null) {
                atOnStringCallBack.callBack(selectedDate.getYear() +
                        (selectedDate.getMonth() < 9 ? "-0" + selectedDate.getMonth() : "-" + selectedDate.getMonth()) +
                        (selectedDate.getDay() < 10 ? "-0" + selectedDate.getDay() : "-" + selectedDate.getDay()));
                dismiss();
            }
        });
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
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.at_bottom_calendar_popup, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }
}
