package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATEventIntegerSet;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATShareSpaceAppBean;
import com.aliyun.ayland.data.ATShareSpaceProjectBean;
import com.aliyun.ayland.data.ATShareSpaceTimeBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATShareGardenAppRVAdapter;
import com.aliyun.ayland.ui.adapter.ATShareGardenTimeRVAdapter;
import com.aliyun.ayland.ui.adapter.ATShareGardenTimeSelectedRVAdapter;
import com.aliyun.ayland.utils.ATPreferencesUtils;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.ayland.widget.popup.ATBottomCalendarPopup;
import com.aliyun.ayland.widget.popup.ATGymDatePopup;
import com.aliyun.ayland.widget.popup.ATShareGardenSubjectPopup;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ATShareGardenActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String appointmentDay, appointmentHour;
    private List<String> dateList, mTimeSectionList = new ArrayList<>();
    //项目选择
    private ATShareGardenSubjectPopup mATShareGardenSubjectPopup;
    private HashSet<Integer> projectSet = new HashSet<>();
    //时间选择
    private ATShareGardenTimeRVAdapter mATShareGardenTimeRVAdapter;
    private ATShareGardenTimeSelectedRVAdapter mATShareGardenTimeSelectedRVAdapter;
    private ATGymDatePopup mATGymDatePopup;
    private HashMap<String, List<ATShareSpaceTimeBean>> timeMap = new HashMap<>();
    private HashMap<String, HashSet<Integer>> timeSelectedMap = new HashMap<>();
    //日期选择
    private ATBottomCalendarPopup mATBottomCalendarPopup;
    private DecimalFormat df = new DecimalFormat("#####0.00");
    private double total_price = 0, discount = 1.0;
    private int hour_position = 0, refresh_position = 0, app_position = 0, subjectSize = 0;
    private List<ATShareSpaceAppBean> mATShareSpaceAppList = new ArrayList<>();
    private List<ATShareSpaceProjectBean> mATShareSpaceProjectList = new ArrayList<>();
    private Dialog dialog;
    private ATHouseBean mATHouseBean;
    private RecyclerView rvSubject, rvSubjectTime, rvSelecterSubject;
    private ATShareGardenAppRVAdapter mATShareGardenAppRVAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private Button btnPreGym;
    private NestedScrollView scrollView;
    private TextView tvSubjectName, tvCount, tvAppointmentHour, tvDate, tvDiscount;
    private JSONArray projectCodeArr = new JSONArray();

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_share_garden;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        rvSubject = findViewById(R.id.rv_subject);
        rvSubjectTime = findViewById(R.id.rv_subject_time);
        rvSelecterSubject = findViewById(R.id.rv_selecter_subject);
        tvAppointmentHour = findViewById(R.id.tv_time);
        tvSubjectName = findViewById(R.id.tv_subject_name);
        tvCount = findViewById(R.id.tv_count);
        scrollView = findViewById(R.id.scrollView);
        btnPreGym = findViewById(R.id.btn_pre_gym);
        tvDate = findViewById(R.id.tv_date);
        tvDiscount = findViewById(R.id.tv_discount);
        findViewById(R.id.img_back).setOnClickListener(view -> finish());
        findViewById(R.id.ll_count).setOnClickListener(view -> scrollView.fullScroll(View.FOCUS_DOWN));
        init();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventIntegerSet atEventIntegerSet) {
        if ("ATShareGardenActivity".equals(atEventIntegerSet.getClazz())) {
            projectSet.clear();
            projectSet.addAll(atEventIntegerSet.getSet());
            StringBuilder subjectName = new StringBuilder();
            if (projectSet.size() == mATShareSpaceProjectList.size()) {
                //全选
                subjectName = new StringBuilder(getString(R.string.at_choose_all_subject));
            } else {
                for (int i = 1; i < mATShareSpaceProjectList.size() + 1; i++) {
                    if (projectSet.contains(i)) {
                        subjectName.append(mATShareSpaceProjectList.get(i - 1).getProjectName()).append("、");
                    }
                }
                subjectName = new StringBuilder(subjectName.substring(0, subjectName.length() - 1));
            }

            timeMap.clear();
            timeSelectedMap.clear();
            appointmentHour = "";
            tvSubjectName.setText(subjectName.toString());
            tvAppointmentHour.setText(appointmentHour);
            total_price = 0;
            tvCount.setText(String.format(getString(R.string.at_count_2), df.format(total_price)));
            //已选
            mATShareGardenTimeRVAdapter.setLists(new ArrayList<>(), new HashSet<>());
            mATShareGardenTimeSelectedRVAdapter.setMap(timeMap, timeSelectedMap);
            findAppointmentTime();
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        findSharedSpaceApp();
    }

    private void findSharedSpaceApp() {
        refresh_position = 0;
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDSHAREDSPACEAPP, jsonObject);
    }

    private void findProjectRelationApp() {
        refresh_position = 1;
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sharedSpaceCode", mATShareSpaceAppList.get(app_position).getId());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDPROJECTRELATIONAPP, jsonObject);
    }

    private void findAppointmentTime() {
        refresh_position = 2;
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appointmentDay", appointmentDay);
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        projectCodeArr = new JSONArray();
        for (Integer integer : projectSet) {
            projectCodeArr.add(mATShareSpaceProjectList.get(integer - 1).getId());
        }
        jsonObject.put("projectCode", projectCodeArr);
        jsonObject.put("sharedSpaceCode", mATShareSpaceAppList.get(app_position).getId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDAPPOINTMENTTIME, jsonObject);
    }

    private void createOderApp() {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appointmentDay", appointmentDay);
        JSONArray timeslot = new JSONArray();
        for (ATShareSpaceTimeBean atShareSpaceTimeBean : mATShareGardenTimeSelectedRVAdapter.getList()) {
            timeslot.add(atShareSpaceTimeBean.getTimeslot());
        }
//        for (String s : timeSelectedMap.keySet()) {
//            for (Integer integer : timeSelectedMap.get(s)) {
//            }
//        }
        jsonObject.put("timeslot", timeslot);
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        jsonObject.put("projectCode", projectCodeArr);
        jsonObject.put("phone", ATPreferencesUtils.getString(this, "tempAccount", ""));
        jsonObject.put("sharedSpaceCode", mATShareSpaceAppList.get(app_position).getId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("totalPrice", df.format(total_price));
        jsonObject.put("wuyeStatus", 0);
        mPresenter.request(ATConstants.Config.SERVER_URL_CREATEODERAPP, jsonObject);
    }

    private void init() {
        ATSystemStatusBarUtils.init(ATShareGardenActivity.this, false);
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        mATBottomCalendarPopup = new ATBottomCalendarPopup(this, string -> {
            appointmentDay = string;
            hour_position = 0;
            tvDate.setText(String.format(getString(R.string.at_date_), appointmentDay));
            timeMap.clear();
            timeSelectedMap.clear();
            appointmentHour = "";
            tvAppointmentHour.setText(appointmentHour);

            total_price = 0;
            tvCount.setText(String.format(getString(R.string.at_count_2), df.format(total_price)));
            mATShareGardenTimeRVAdapter.setLists(new ArrayList<>(), new HashSet<>());
            mATShareGardenTimeSelectedRVAdapter.setMap(timeMap, timeSelectedMap);
            findAppointmentTime();
        });
        mATShareGardenSubjectPopup = new ATShareGardenSubjectPopup(this);

        initReadDialog();
        initDateList();
        appointmentDay = dateList.get(0);
        btnPreGym.setOnClickListener(view -> {
            if (total_price == 0) {
                subjectSize = 0;
                for (String s : timeSelectedMap.keySet()) {
                    subjectSize += timeSelectedMap.get(s).size();
                }
                if (subjectSize == 0) {
                    showToast(getString(R.string.at_selected_at_less_subject));
                    return;
                }
            }
            dialog.show();
        });
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            if (refresh_position == 0) {
                //刷新空间
                findSharedSpaceApp();
            } else if (refresh_position == 1) {
                //刷新项目
                findProjectRelationApp();
            } else {
                //刷新时间段
                findAppointmentTime();
            }
        });
        tvCount.setText(String.format(getString(R.string.at_count_2), df.format(total_price)));
        mATGymDatePopup = new ATGymDatePopup(this, integer -> {
            if (mTimeSectionList.size() > 0) {
                hour_position = integer;
                appointmentHour = mTimeSectionList.get(hour_position);
                tvAppointmentHour.setText(appointmentHour);
                mATShareGardenTimeRVAdapter.setLists(timeMap.get(appointmentHour), timeSelectedMap.get(appointmentHour));
            }
        });

        tvDate.setText(String.format(getString(R.string.at_date_), appointmentDay));
        tvDate.setOnClickListener(view -> mATBottomCalendarPopup.showPopupWindow());

        tvSubjectName.setOnClickListener(view -> {
            if (mATShareSpaceProjectList.size() == 0)
                showToast(getString(R.string.at_have_no_project1));
            else {
                mATShareGardenSubjectPopup.setLists(mATShareSpaceProjectList, projectSet, discount);
                mATShareGardenSubjectPopup.showPopupWindow();
            }
        });
        tvAppointmentHour.setOnClickListener(view -> {
            if (mTimeSectionList.size() == 0)
                showToast(getString(R.string.at_have_no_time));
            else {
                mATGymDatePopup.setList(mTimeSectionList);
                mATGymDatePopup.showPopupWindow();
            }
        });

        mATShareGardenAppRVAdapter = new ATShareGardenAppRVAdapter(this);
        rvSubject.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rvSubject.setAdapter(mATShareGardenAppRVAdapter);
        rvSubject.getItemAnimator().setChangeDuration(0);
        mATShareGardenAppRVAdapter.setOnItemClickListener((view, position) -> {
            app_position = position;
            mATShareSpaceProjectList.clear();
            projectSet.clear();
            mTimeSectionList.clear();
            tvDiscount.setText("");
            tvSubjectName.setText("");

            timeMap.clear();
            timeSelectedMap.clear();
            appointmentHour = "";
            total_price = 0;

            tvCount.setText(String.format(getString(R.string.at_count_2), df.format(total_price)));
            mATShareGardenSubjectPopup.setLists(mATShareSpaceProjectList, projectSet, discount);
            tvAppointmentHour.setText(appointmentHour);
            mATShareGardenTimeRVAdapter.setLists(new ArrayList<>(), new HashSet<>());
            mATShareGardenTimeSelectedRVAdapter.setMap(timeMap, timeSelectedMap);
            findProjectRelationApp();
        });

        mATShareGardenTimeRVAdapter = new ATShareGardenTimeRVAdapter(this);
        rvSubjectTime.setLayoutManager(new LinearLayoutManager(this));
        rvSubjectTime.setAdapter(mATShareGardenTimeRVAdapter);
        rvSubjectTime.setNestedScrollingEnabled(false);
        mATShareGardenTimeRVAdapter.setOnItemClickListener((view, position) -> {
            if (timeSelectedMap.get(appointmentHour).contains(position))
                timeSelectedMap.get(appointmentHour).remove(position);
            else
                timeSelectedMap.get(appointmentHour).add(position);
            total_price = 0;
            for (String s : timeSelectedMap.keySet()) {
                for (Integer integer : timeSelectedMap.get(s)) {
                    total_price += timeMap.get(s).get(integer).getUnitPrice();
                }
            }
            total_price = total_price * discount / 10;
            tvCount.setText(String.format(getString(R.string.at_count_2), df.format(total_price)));
            mATShareGardenTimeSelectedRVAdapter.setMap(timeMap, timeSelectedMap);
        });

        mATShareGardenTimeSelectedRVAdapter = new ATShareGardenTimeSelectedRVAdapter(this);
        rvSelecterSubject.setLayoutManager(new LinearLayoutManager(this));
        rvSelecterSubject.setAdapter(mATShareGardenTimeSelectedRVAdapter);
        rvSelecterSubject.setNestedScrollingEnabled(false);
        mATShareGardenTimeSelectedRVAdapter.setOnItemClickListener(atShareSpaceTimeBean -> {
            timeSelectedMap.get(atShareSpaceTimeBean.getAppointmentHour()).remove(atShareSpaceTimeBean.getPosition());
            mATShareGardenTimeRVAdapter.setLists(timeMap.get(appointmentHour), timeSelectedMap.get(appointmentHour));
            total_price = 0;
            for (String s : timeSelectedMap.keySet()) {
                for (Integer integer : timeSelectedMap.get(s)) {
                    total_price += timeMap.get(s).get(integer).getUnitPrice();
                }
            }
            total_price = total_price * discount / 10;
            tvCount.setText(String.format(getString(R.string.at_count_2), df.format(total_price)));
        });
    }

    @SuppressLint("InflateParams")
    private void initReadDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_normal, null, false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(getString(R.string.at_do_appointment));
        view.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> dialog.dismiss());
        view.findViewById(R.id.tv_sure).setOnClickListener(view2 -> createOderApp());
        dialog.setContentView(view);
    }

    private void initDateList() {
        dateList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dateList.add(sdf.format(new Date(System.currentTimeMillis() + 86400000 * i)));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDAPPOINTMENTTIME:
                        JSONObject object = JSON.parseObject(jsonResult.getString("result"));

                        mTimeSectionList.clear();
                        timeMap.clear();
                        timeSelectedMap.clear();
                        total_price = 0;
                        tvCount.setText(String.format(getString(R.string.at_count_2), df.format(total_price)));

                        List<ATShareSpaceTimeBean> atShareSpaceTimeBeans;
                        hour_position = 0;
                        for (Map.Entry<String, Object> entry : object.entrySet()) {
                            String valueString = entry.getValue().toString();
                            atShareSpaceTimeBeans = gson.fromJson(valueString, new TypeToken<List<ATShareSpaceTimeBean>>() {
                            }.getType());
                            mTimeSectionList.add(entry.getKey());
                            timeMap.put(entry.getKey(), atShareSpaceTimeBeans);
                            timeSelectedMap.put(entry.getKey(), new HashSet<>());
                        }
                        Collections.sort(mTimeSectionList);
                        appointmentHour = mTimeSectionList.get(hour_position);
                        tvAppointmentHour.setText(appointmentHour);
                        mATShareGardenTimeRVAdapter.setLists(timeMap.get(appointmentHour), timeSelectedMap.get(appointmentHour));
                        mATShareGardenTimeSelectedRVAdapter.setMap(timeMap, timeSelectedMap);
                        break;
                    case ATConstants.Config.SERVER_URL_FINDPROJECTRELATIONAPP:
                        mATShareSpaceProjectList = gson.fromJson(jsonResult.getJSONObject("result").getString("project"), new TypeToken<List<ATShareSpaceProjectBean>>() {
                        }.getType());
                        discount = jsonResult.getJSONObject("result").getDouble("discount");
                        if (discount == 10.0)
                            tvDiscount.setText("");
                        else
                            tvDiscount.setText(String.format(getString(R.string.at_choose_all_subject1), String.valueOf(discount)));//不可去
                        if (mATShareSpaceProjectList.size() > 0) {
                            tvSubjectName.setText(getString(R.string.at_choose_all_subject));
                            for (int i = 0; i < mATShareSpaceProjectList.size(); i++) {
                                projectSet.add(i + 1);
                            }
                            mATShareGardenSubjectPopup.setLists(mATShareSpaceProjectList, projectSet, discount);
                            findAppointmentTime();
                        } else
                            tvSubjectName.setText(getString(R.string.at_have_no_project));
                        break;
                    case ATConstants.Config.SERVER_URL_FINDSHAREDSPACEAPP:
                        mATShareSpaceAppList = gson.fromJson(jsonResult.getString("result"), new TypeToken<List<ATShareSpaceAppBean>>() {
                        }.getType());
                        app_position = 0;
                        if (mATShareSpaceAppList.size() > 0) {
                            mATShareGardenAppRVAdapter.setLists(mATShareSpaceAppList, app_position);
                            findProjectRelationApp();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_CREATEODERAPP:
                        showToast(getString(R.string.at_appoint_success));
                        finish();
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            dialog.dismiss();
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}