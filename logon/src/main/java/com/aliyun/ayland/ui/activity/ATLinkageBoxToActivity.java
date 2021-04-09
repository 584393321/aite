package com.aliyun.ayland.ui.activity;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATAllAppointmentBean;
import com.aliyun.ayland.ui.adapter.ATLinkageBoxRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageBoxToActivity extends ATBaseActivity {
    private ATLinkageBoxRVAdapter mATLinkageBoxRVAdapter;
    private String time;
    private List<ATAllAppointmentBean> mAllAppointmentList = new ArrayList<>();
    private JSONObject params = new JSONObject();
    private ATMyTitleBar titlebar;
    private TextView tvTime1, tvTime2;
    private RelativeLayout rlOne, rlTwo;
    private CheckBox checkBox1, checkBox2;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_colorful_box_to;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        tvTime1 = findViewById(R.id.tv_time1);
        tvTime2 = findViewById(R.id.tv_time2);
        rlOne = findViewById(R.id.rl_one);
        rlTwo = findViewById(R.id.rl_two);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        ATAllAppointmentBean ATAllAppointmentBean = getIntent().getParcelableExtra("appointment");
        titlebar.setRightButtonText(getString(R.string.at_done));
        titlebar.setRightClickListener(() -> {
            if (TextUtils.isEmpty(time))
                return;
            params.put("cron", time.split(":")[1] + " " + time.split(":")[0] + " * * *");
            params.put("cronType", "linux");
            setResult(RESULT_OK, getIntent().putExtra("name", getString(R.string.at_timing)).putExtra("uri", "trigger/timer")
                    .putExtra("content", time + " " + getString(R.string.at_every_day))
                    .putExtra("params", params.toJSONString()));
            finish();
        });
        titlebar.setTitle(String.format(getString(R.string.at_string3_), ATAllAppointmentBean.getDay().substring(5, 10), ATAllAppointmentBean.getTypeName()
                , ATAllAppointmentBean.getProjectName()));
        tvTime1.setText(ATAllAppointmentBean.getAppointmentTime().split("~")[0]);
        tvTime2.setText(ATAllAppointmentBean.getAppointmentTime().split("~")[1]);
        rlOne.setOnClickListener(v -> {
            checkBox1.setChecked(true);
            checkBox2.setChecked(false);
            time = tvTime1.getText().toString();
        });
        rlTwo.setOnClickListener(v -> {
            checkBox1.setChecked(false);
            checkBox2.setChecked(true);
            time = tvTime2.getText().toString();
        });
    }
}