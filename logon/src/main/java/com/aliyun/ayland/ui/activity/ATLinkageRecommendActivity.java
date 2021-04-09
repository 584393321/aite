package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATBrightnessLightBean;
import com.aliyun.ayland.data.ATDeviceTcaList;
import com.aliyun.ayland.data.ATEventInteger2;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATRecommendTemplateActionBean;
import com.aliyun.ayland.data.ATSceneName;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATLinkageDeviceRvAdapter;
import com.aliyun.ayland.widget.ATRecycleViewItemDecoration;
import com.aliyun.ayland.widget.popup.ATListPopup;
import com.aliyun.ayland.widget.popup.ATMorningTimePopup;
import com.aliyun.ayland.widget.popup.ATMorningTotalTimePopup;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageRecommendActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int REQUEST_CODE_CRON_WEEK = 0x1001;
    private static final int REQUEST_CODE_ADD_CONDITION = 0x1002;
    private ATMainPresenter mPresenter;
    private ATMyTitleBar titlebar;
    private ATLinkageDeviceRvAdapter mATLinkageDeviceRvAdapter;
    private String cron_week, week_text, sceneName, conditionName;
    private int hour, min, total_min, total_second;
    private ATListPopup mATListPopup;
    private ATMorningTimePopup mATMorningTimePopup;
    private ATMorningTotalTimePopup mATMorningTotalTimePopup;
    private ATHouseBean mATHouseBean;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout llSettingCondition, llOutCondition;
    private ImageView imgType;
    private int templateId;
    private RelativeLayout rlHomeCondition, rlRepeat, rlStartTime, rlTotalTime;
    private TextView tvRepeat, tvStartTime, tvTotalTime, tvType, tvCondition, tvHomeCondition;
    private List<Object> list = new ArrayList<>();
    private List<ATDeviceTcaList> mATDeviceTcaList = new ArrayList<>();
    private ATSceneName mATSceneName;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_recommend;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        rlRepeat = findViewById(R.id.rl_repeat);
        rlStartTime = findViewById(R.id.rl_start_time);
        rlTotalTime = findViewById(R.id.rl_total_time);
        tvRepeat = findViewById(R.id.tv_repeat);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvTotalTime = findViewById(R.id.tv_total_time);
        tvHomeCondition = findViewById(R.id.tv_home_condition);
        imgType = findViewById(R.id.img_type);
        tvType = findViewById(R.id.tv_type);
        tvCondition = findViewById(R.id.tv_condition);
        llSettingCondition = findViewById(R.id.ll_setting_condition);
        rlHomeCondition = findViewById(R.id.rl_home_condition);
        llOutCondition = findViewById(R.id.ll_out_condition);
        init();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger2 ATEventInteger2) {
        if ("LinkageRecommendActivity".equals(ATEventInteger2.getClazz())) {
            hour = ATEventInteger2.getPosition();
            min = ATEventInteger2.getSpecs();
            tvStartTime.setText(getHourMinText());
        } else if ("LinkageRecommendActivity1".equals(ATEventInteger2.getClazz())) {
            total_min = ATEventInteger2.getPosition();
            total_second = ATEventInteger2.getSpecs();
            tvTotalTime.setText(getTotalTime());
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if ("回家模式".equals(sceneName))
            findDeviceTca();
        getBrightnessLight();
        findRecommendTemplateActionList();
    }

    public void findDeviceTca() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDDEVICETCA, jsonObject);
    }

    private void addTemplateScene() {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();
        list = mATLinkageDeviceRvAdapter.getData();
        JSONArray actionList = new JSONArray();
        for (Integer integer : mATLinkageDeviceRvAdapter.getSet()) {
            JSONObject object = new JSONObject();
            object.put("actionKey", ((ATBrightnessLightBean) list.get(integer)).getCategoryKey());
            object.put("action", ((ATBrightnessLightBean) list.get(integer)).getAction());
            object.put("iotId", ((ATBrightnessLightBean) list.get(integer)).getIotId());
            actionList.add(object);
        }
        JSONArray triggers = new JSONArray();
        JSONObject object = new JSONObject();
        JSONObject params = new JSONObject();
        switch (sceneName) {
            case "起床模式":
            case "睡眠模式":
                params.put("cron", min + " " + hour + " * * " + cron_week);
                params.put("cronType", "linux");
                object.put("params", params);
                object.put("uri", "trigger/timer");
                break;
            case "回家模式":
                if(mATSceneName == null){
                    showToast(getString(R.string.at_home_condition));
                    closeBaseProgressDlg();
                    return;
                }
                params = JSONObject.parseObject(mATSceneName.getParams());
                if (params.containsKey("compareValue")) {
                    params.put("compareValue", params.getInteger("compareValue"));
                }
                object.put("params", params);
                object.put("uri", mATSceneName.getUri());
                break;
            default:
                break;
        }
        triggers.add(object);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("actionList", actionList);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("conditions", new JSONArray());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("sceneName", sceneName);
        jsonObject.put("totalTime", total_min * 60 + total_second);
        jsonObject.put("triggers", triggers);
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_ADDTEMPLATESCENE, jsonObject);
    }

    private void getBrightnessLight() {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETBRIGHTNESSLIGHT, jsonObject);
    }

    private void findRecommendTemplateActionList() {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("templateId", templateId);
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDRECOMMENDTEMPLATEACTIONLIST, jsonObject);
    }

    private void init() {
        templateId = getIntent().getIntExtra("templateId", 0);
        sceneName = getIntent().getStringExtra("sceneName");
        titlebar.setSendText(getString(R.string.at_add));
        titlebar.setPublishClickListener(() -> {
            if (mATLinkageDeviceRvAdapter.getSet().size() == 0)
                showToast("请选择对应设备");
            else
                addTemplateScene();
        });

        mATListPopup = new ATListPopup(this);
        switch (sceneName) {
            case "起床模式":
                imgType.setImageResource(R.drawable.at_home_ld_cqzm);
                titlebar.setTitle(getString(R.string.at_morning_model));
                tvType.setText(getString(R.string.at_morning_model));
                tvCondition.setText(R.string.at_setting_time);
                llSettingCondition.setVisibility(View.VISIBLE);
                rlHomeCondition.setVisibility(View.GONE);
                llOutCondition.setVisibility(View.GONE);
                break;
            case "回家模式":
                imgType.setImageResource(R.drawable.at_home_ld_hjms);
                titlebar.setTitle(getString(R.string.at_home_model));
                tvType.setText(getString(R.string.at_home_model));
                tvCondition.setText(R.string.at_trigger_condition);
                llSettingCondition.setVisibility(View.GONE);
                rlHomeCondition.setVisibility(View.VISIBLE);
                llOutCondition.setVisibility(View.GONE);
                List<String> list = new ArrayList<>();
                list.add(getString(R.string.at_walk));
                list.add(getString(R.string.at_car));
                list.add(getString(R.string.at_door_lock));
                mATListPopup.setTitle("");
                mATListPopup.setList(list);
                mATListPopup.setOnItemClickListener((view, o, position) -> {
                    conditionName = list.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("flowType", 1);
                    switch (position) {
                        case 0:
                            intent.setClass(this, ATLinkageAccessActivity.class);
                            intent.putExtra("dataType", "108");
                            intent.putExtra("uri", "trigger/biz/pass/event");
                            break;
                        case 1:
                            intent.setClass(this, ATLinkageCarAccessActivity.class);
                            break;
                        case 2:
                            intent.putParcelableArrayListExtra("deviceList", mATDeviceTcaList.get(0).getTrigger());
                            intent.setClass(this, ATLinkageEquipmentActivity.class);
                            break;
                    }
                    startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
                });
                break;
            case "离家模式":
                imgType.setImageResource(R.drawable.at_home_ld_ljms);
                titlebar.setTitle(getString(R.string.at_out_model));
                tvType.setText(getString(R.string.at_out_model));
                tvCondition.setText(R.string.at_trigger_condition);
                llSettingCondition.setVisibility(View.GONE);
                rlHomeCondition.setVisibility(View.GONE);
                llOutCondition.setVisibility(View.VISIBLE);
                break;
            case "睡眠模式":
                imgType.setImageResource(R.drawable.at_home_ld_smms);
                titlebar.setTitle(getString(R.string.at_sleep_model));
                tvType.setText(getString(R.string.at_sleep_model));
                tvCondition.setText(R.string.at_setting_time);
                llSettingCondition.setVisibility(View.VISIBLE);
                rlHomeCondition.setVisibility(View.GONE);
                llOutCondition.setVisibility(View.GONE);
                break;
            case "烹饪模式":
                imgType.setImageResource(R.drawable.at_home_ld_plms);
                titlebar.setTitle(getString(R.string.at_cooking_model));
                tvType.setText(getString(R.string.at_cooking_model));
                tvCondition.setText(R.string.at_trigger_condition);
                llSettingCondition.setVisibility(View.GONE);
                rlHomeCondition.setVisibility(View.GONE);
                llOutCondition.setVisibility(View.GONE);
                break;
            case "娱乐模式":
                imgType.setImageResource(R.drawable.at_home_ld_ylms);
                titlebar.setTitle(getString(R.string.at_recreation_model));
                tvType.setText(getString(R.string.at_recreation_model));
                tvCondition.setText(R.string.at_trigger_condition);
                llSettingCondition.setVisibility(View.GONE);
                rlHomeCondition.setVisibility(View.GONE);
                llOutCondition.setVisibility(View.VISIBLE);
                break;
        }
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        cron_week = "*";
        week_text = "每天";
        hour = 7;
        min = 0;
        total_min = 5;
        total_second = 0;

        tvStartTime.setText(getHourMinText());
        tvTotalTime.setText(getTotalTime());

        mATMorningTimePopup = new ATMorningTimePopup(this);
        mATMorningTotalTimePopup = new ATMorningTotalTimePopup(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        mATLinkageDeviceRvAdapter = new ATLinkageDeviceRvAdapter();
        recyclerView.setAdapter(mATLinkageDeviceRvAdapter);
        recyclerView.addItemDecoration(new ATRecycleViewItemDecoration(ATAutoUtils.getPercentHeightSize(20)));
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            getBrightnessLight();
            findRecommendTemplateActionList();
        });
        rlRepeat.setOnClickListener(view ->
                startActivityForResult(new Intent(this, ATLinkageTimingRepeatActivity.class).putExtra("cron_week", cron_week), REQUEST_CODE_CRON_WEEK));
        tvRepeat.setText(week_text);

        rlStartTime.setOnClickListener(view -> {
            mATMorningTimePopup.setCurrentTime(hour, min);
            mATMorningTimePopup.showPopupWindow();
        });
        rlTotalTime.setOnClickListener(view -> {
            mATMorningTotalTimePopup.setCurrentTime(total_min, total_second);
            mATMorningTotalTimePopup.showPopupWindow();
        });
        rlHomeCondition.setOnClickListener(view -> mATListPopup.showPopupWindow());
    }

    private String getHourMinText() {
        return (hour == 0 ? "00时" : hour + "时") + (min == 0 ? "00分" : min + "分");
    }

    private String getTotalTime() {
        return (total_min == 0 ? "" : total_min + "分") + (total_second == 0 ? "" : total_second + "秒");
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDDEVICETCA:
                        List<ATDeviceTcaList> ATDeviceTcaList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDeviceTcaList>>() {
                        }.getType());
                        mATDeviceTcaList.clear();
                        mATDeviceTcaList.addAll(ATDeviceTcaList);
                        break;
                    case ATConstants.Config.SERVER_URL_FINDRECOMMENDTEMPLATEACTIONLIST:
                        List<ATRecommendTemplateActionBean> aTRecommendTemplateActionBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATRecommendTemplateActionBean>>() {
                        }.getType());
                        mATLinkageDeviceRvAdapter.setList(aTRecommendTemplateActionBeanList);
                        break;
                    case ATConstants.Config.SERVER_URL_ADDTEMPLATESCENE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_morning_model_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_GETBRIGHTNESSLIGHT:
//                        List<ATBrightnessLightBean> brightnessLightList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATBrightnessLightBean>>() {
//                        }.getType());
//                        list.clear();
//                        list.add("light" + "-" + brightnessLightList.size());
//                        list.addAll(brightnessLightList);
//                        mATLinkageDeviceRvAdapter.setList(list);
                        break;
                }
            } else {
                closeBaseProgressDlg();
                if (jsonResult.has("message")) {
                    if (jsonResult.getString("message").length() > 35 && jsonResult.getString("message").substring(0, 35).
                            equals("there are rules with the same content!sceneId:9e53665d7d6942f5ac65a8a766edf5ef".substring(0, 35))
                            ) {
                        showToast("该场景已存在");
                    } else if (jsonResult.getString("message").length() > 10 &&
                            jsonResult.getString("message").substring(0, 10).equals("action is empty".substring(0, 10))) {
                        showToast("请添加对应设备");
                    } else {
                        showToast(jsonResult.getString("message"));
                    }
                } else if (jsonResult.has("localizedMsg")) {
                    showToast(jsonResult.getString("localizedMsg"));
                } else {
                    showToast(getString(R.string.at_morning_model_failed));
                }
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CRON_WEEK:
                    cron_week = data.getStringExtra("cron_week");
                    if (cron_week.length() != 13 && cron_week.length() != 0) {
                        week_text = cron_week.replace("0", "周日").replace("1", "周一").replace("2", "周二")
                                .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                                .replace("6", "周六").replaceAll(",", "、");
                    } else {
                        week_text = getString(R.string.at_every_day);
                        cron_week = "*";
                    }
                    tvRepeat.setText(week_text);
                    break;
                case REQUEST_CODE_ADD_CONDITION:
                    tvHomeCondition.setText(String.format(getString(R.string.at_begin_to_end), conditionName, data.getStringExtra("name")));
                    tvHomeCondition.setTextColor(getResources().getColor(R.color._333333));
                    mATSceneName = new ATSceneName();
                    mATSceneName.setName(data.getStringExtra("name"));
                    mATSceneName.setParams(data.getStringExtra("params"));
                    mATSceneName.setUri(data.getStringExtra("uri"));
                    mATSceneName.setContent(data.getStringExtra("content"));
                    mATSceneName.setDataType(data.getStringExtra("dataType"));
                    break;
            }
        }
    }
}