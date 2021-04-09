package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.widget.contrarywind.view.WheelView;
import com.aliyun.ayland.widget.pickerview.builder.TimePickerBuilder;
import com.aliyun.ayland.widget.pickerview.view.TimePickerView;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

public class ATLinkageTimingActivity extends ATBaseActivity {
    private static final int REQUEST_CODE_CRON_WEEK = 0x1001;
    private WheelView wheelMin, wheelHour;
    private String cron_week, week_text, uri;
    private WheelView wheelEndHour;
    private WheelView wheelEndMin;
    private WheelView wheelBeginMin;
    private WheelView wheelBeginHour;
    private ATMyTitleBar titlebar;
    private RelativeLayout rlRepeat, rlTriggerTiming, rlBegin, rlBeginTiming, rlEnd, rlEndTiming;
    private LinearLayout llCondition, llTiming, llBeginTiming1, llEndTiming1;
    private TextView tvBeginTime, tvEndTime, tvWeek;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_timing;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        rlRepeat = findViewById(R.id.rl_repeat);
        rlTriggerTiming = findViewById(R.id.rl_trigger_timing);
        rlBegin = findViewById(R.id.rl_begin);
        rlBeginTiming = findViewById(R.id.rl_begin_timing);
        rlEnd = findViewById(R.id.rl_end);
        rlEndTiming = findViewById(R.id.rl_end_timing);
        llCondition = findViewById(R.id.ll_condition);
        llTiming = findViewById(R.id.ll_timing);
        llBeginTiming1 = findViewById(R.id.ll_begin_timing1);
        llEndTiming1 = findViewById(R.id.ll_end_timing1);
        tvBeginTime = findViewById(R.id.tv_begin_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        tvWeek = findViewById(R.id.tv_week);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        titlebar.setSendText(getString(R.string.at_done));
        TimePickerView pvCustomTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
//                btn_CustomTime.setText(getTime(date));
        }).setLayoutRes(R.layout.at_pickerview_custom_time, v -> {
            wheelMin = v.findViewById(R.id.min);
            wheelHour = v.findViewById(R.id.hour);
        })
                .isDialog(false)
                .isCyclic(true)
                .setContentTextSize(24)
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("", "", "", "", "", "")
                .setLineSpacingMultiplier(2.5f)
                .isCenterLabel(true)
                .setDividerColor(0xFFFDA448)
                .setDecorView(llTiming)
                .setOutSideCancelable(false)
                .build();
        pvCustomTime.setKeyBackCancelable(false);
        pvCustomTime.show(false);

        TimePickerView pvBiginTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
//                btn_CustomTime.setText(getTime(date));
            Log.e("onTimeSelect: ", date.toString());
//                tvEndTime.setText(getTime(date));
        }).setLayoutRes(R.layout.at_pickerview_custom_time, v -> {
            wheelBeginMin = v.findViewById(R.id.min);
            wheelBeginHour = v.findViewById(R.id.hour);
            wheelBeginHour.setATOnItemSelectedListener(index -> tvBeginTime.setText((index < 10 ? "0" + index : index) + ":" +
                    (wheelBeginMin.getCurrentItem() < 10 ? "0" + wheelBeginMin.getCurrentItem() : wheelBeginMin.getCurrentItem())));
            wheelBeginMin.setATOnItemSelectedListener(index -> tvBeginTime.setText((wheelBeginHour.getCurrentItem() < 10 ? "0" + wheelBeginHour.getCurrentItem() : wheelBeginHour.getCurrentItem()) + ":" +
                    (index < 10 ? "0" + index : index)));
        })
                .isDialog(false)
                .isCyclic(true)
                .setContentTextSize(24)
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("", "", "", "", "", "")
                .setLineSpacingMultiplier(2.5f)
                .isCenterLabel(true)
                .setDividerColor(0xFFFDA448)
                .setDecorView(llBeginTiming1)
                .setOutSideCancelable(false)
                .build();
        pvBiginTime.setKeyBackCancelable(false);
        pvBiginTime.show(false);
        TimePickerView pvEndTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
    //                btn_CustomTime.setText(getTime(date));
                }).setLayoutRes(R.layout.at_pickerview_custom_time, v -> {
                    wheelEndHour = v.findViewById(R.id.hour);
                    wheelEndMin = v.findViewById(R.id.min);
                    wheelEndHour.setATOnItemSelectedListener(index -> tvEndTime.setText((index < 10 ? "0" + index : index) + ":" +
                            (wheelEndMin.getCurrentItem() < 10 ? "0" + wheelEndMin.getCurrentItem() : wheelEndMin.getCurrentItem())));
                    wheelEndMin.setATOnItemSelectedListener(index -> tvEndTime.setText((wheelEndHour.getCurrentItem() < 10 ? "0" + wheelEndHour.getCurrentItem() : wheelEndHour.getCurrentItem()) + ":" +
                            (index < 10 ? "0" + index : index)));
                })
                .isDialog(false)
                .isCyclic(true)
                .setContentTextSize(24)
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("", "", "", "", "", "")
                .setLineSpacingMultiplier(2.5f)
                .isCenterLabel(true)
                .setDividerColor(0xFFFDA448)
                .setDecorView(llEndTiming1)
                .setOutSideCancelable(false)
                .build();
        pvEndTime.setKeyBackCancelable(false);
        pvEndTime.show(false);

        int flowType = getIntent().getIntExtra("flowType", 1);
        switch (flowType) {
            case 1:
                uri = "trigger/timer";
                rlTriggerTiming.setVisibility(View.VISIBLE);
                llCondition.setVisibility(View.GONE);
                if (getIntent().getStringExtra("params") != null) {
                    String[] cronArr = JSONObject.parseObject(getIntent().getStringExtra("params")).getString("cron").split(" ");
                    wheelHour.setCurrentItem(Integer.parseInt(cronArr[1]));
                    wheelMin.setCurrentItem(Integer.parseInt(cronArr[0]));
                    cron_week = cronArr[4];
                    week_text = cronArr[4].replace("0", "周日").replace("1", "周一").replace("2", "周二")
                            .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                            .replace("6", "周六").replace("7", "周日").replaceAll(",", "、");
                    if ("*".equals(week_text) || week_text.length() == 20) {
                        week_text = "每天";
                        cron_week = "*";
                    }
                } else {
                    week_text = getString(R.string.at_every_day);
                    cron_week = "*";
                }
                break;
            case 2:
                uri = "condition/timeRange";
                if (getIntent().getStringExtra("params") != null) {
                    tvBeginTime.setText(JSONObject.parseObject(getIntent().getStringExtra("params")).getString("beginDate"));
                    tvEndTime.setText(JSONObject.parseObject(getIntent().getStringExtra("params")).getString("endDate"));
                    cron_week = JSONObject.parseObject(getIntent().getStringExtra("params")).getString("repeat");
                    week_text = cron_week.replace("0", "周日").replace("1", "周一").replace("2", "周二")
                            .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                            .replace("6", "周六").replace("7", "周日").replaceAll(",", "、");
                    if ("*".equals(week_text) || week_text.length() == 20) {
                        week_text = "每天";
                        cron_week = "*";
                    }
                } else {
                    week_text = getString(R.string.at_every_day);
                    cron_week = "*";
                }
                rlTriggerTiming.setVisibility(View.GONE);
                llCondition.setVisibility(View.VISIBLE);
                break;
        }

        tvWeek.setText(week_text);

        rlBegin.setOnClickListener(view -> {
            rlBeginTiming.setVisibility(View.VISIBLE);
            rlEndTiming.setVisibility(View.GONE);

            wheelBeginHour.setCurrentItem(Integer.parseInt(tvBeginTime.getText().toString().split(":")[0]));
            wheelBeginMin.setCurrentItem(Integer.parseInt(tvBeginTime.getText().toString().split(":")[1]));

        });
        rlEnd.setOnClickListener(view -> {
            rlEndTiming.setVisibility(View.VISIBLE);
            rlBeginTiming.setVisibility(View.GONE);
            wheelEndHour.setCurrentItem(Integer.parseInt(tvEndTime.getText().toString().split(":")[0]));
            wheelEndMin.setCurrentItem(Integer.parseInt(tvEndTime.getText().toString().split(":")[1]));
        });
        rlRepeat.setOnClickListener(view -> startActivityForResult(new Intent(this, ATLinkageTimingRepeatActivity.class)
                .putExtra("cron_week", cron_week), REQUEST_CODE_CRON_WEEK));
        titlebar.setPublishClickListener(() -> {
            JSONObject params = new JSONObject();
            String name, content;
            switch (flowType) {
                case 2:
                    uri = "condition/timeRange";
                    name = getString(R.string.at_time_limit);
                    content = tvBeginTime.getText().toString() + " " + tvEndTime.getText().toString() + " " + week_text;
                    params.put("format", "HH:mm");
                    params.put("beginDate", tvBeginTime.getText().toString());
                    params.put("endDate", tvEndTime.getText().toString());
                    params.put("timezoneID", "Asia/Shanghai");
                    params.put("repeat", "*".equals(cron_week) ? "1,2,3,4,5,6,7" : cron_week);
                    break;
                default:
                    uri = "trigger/timer";
                    name = getString(R.string.at_timing);
                    content = (wheelHour.getCurrentItem() < 10 ? "0" + wheelHour.getCurrentItem() : wheelHour.getCurrentItem()) + ":" + (wheelMin.getCurrentItem() < 10 ? "0" + wheelMin.getCurrentItem() : wheelMin.getCurrentItem()) + " " + week_text;
                    params.put("cron", wheelMin.getCurrentItem() + " " + wheelHour.getCurrentItem() + " * * " + cron_week);
                    params.put("cronType", "linux");
                    break;
            }
            setResult(RESULT_OK, getIntent().putExtra("name", name).putExtra("uri", uri)
                    .putExtra("content", content)
                    .putExtra("params", params.toJSONString()));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CRON_WEEK) {
            cron_week = data.getStringExtra("cron_week");
            if (cron_week.length() != 13 && cron_week.length() != 0) {
                week_text = cron_week.replace("0", "周日").replace("1", "周一").replace("2", "周二")
                        .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                        .replace("6", "周六").replaceAll(",", "、");
            } else {
                week_text = getString(R.string.at_every_day);
                cron_week = "*";
            }
            tvWeek.setText(week_text);
        }
    }
}