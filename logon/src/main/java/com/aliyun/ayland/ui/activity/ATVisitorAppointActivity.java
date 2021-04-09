package com.aliyun.ayland.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATParkBean;
import com.aliyun.ayland.data.ATParkNumberBean;
import com.aliyun.ayland.data.db.ATParkNumberDao;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATVisitorHistoryRVAdapter;
import com.aliyun.ayland.utils.EmojiExcludeFilter;
import com.aliyun.ayland.widget.ATObservableScrollView;
import com.aliyun.ayland.widget.contrarywind.view.WheelView;
import com.aliyun.ayland.widget.pickerview.builder.OptionsPickerBuilder;
import com.aliyun.ayland.widget.pickerview.builder.TimePickerBuilder;
import com.aliyun.ayland.widget.pickerview.view.OptionsPickerView;
import com.aliyun.ayland.widget.pickerview.view.TimePickerView;
import com.aliyun.ayland.widget.popup.ATBottomPlatePopup;
import com.aliyun.ayland.widget.popup.ATSlideFromBottomInputPopup;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATVisitorAppointActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private TimePickerView mPvCustomTime;
    private OptionsPickerView pvOptions;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private ATParkNumberDao mATParkNumberDao;
    private List<ATParkNumberBean> mParkNumberList;
    private ATBottomPlatePopup mATBottomPlatePopup;
    private ATSlideFromBottomInputPopup mATSlideFromBottomInputPopup;
    private int park_name_position = 0;
    private String visitorName, visitorTel, visite_time, leave_time, carNumber, visite_park;
    private List<String> mParkNameList = new ArrayList<>();
    private List<ATParkBean> mATParkNameBeanList = new ArrayList<>();
    private ATHouseBean mATHouseBean;
    private PopupWindow popupWindow;
    private ATVisitorHistoryRVAdapter mATVisitorHistoryRVAdapter;
    private List<String> mList = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    private RadioButton rbMotorsYes, rbAgentYes, rbParkYes;
    private EditText etVisitorName, etVisitorPhone;
    private RelativeLayout rlPlate, rlFee, rlPlateNumbers;
    private Button btnSubscribe;
    private ATObservableScrollView observableScrollView;
    private LinearLayout llDismiss, llVisitePark, llPark, llVisiteTime, llLeaveTime;
    private RadioGroup rgMotors, rgAgent, rgParkFee;
    private TextView tvVisitorRoom, tvVisiteTime, tvLeaveTime, tvVisitePark, tvPlateNumbers, tvStatement, tvPlateNumber;


    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_visitor_appoint;
    }

    @Override
    protected void findView() {
        observableScrollView = findViewById(R.id.observableScrollView);
        llDismiss = findViewById(R.id.ll_dismiss);
        llVisitePark = findViewById(R.id.ll_visite_park);
        llPark = findViewById(R.id.ll_park);
        llVisiteTime = findViewById(R.id.ll_visite_time);
        llLeaveTime = findViewById(R.id.ll_leave_time);
        rlPlate = findViewById(R.id.rl_plate);
        rlFee = findViewById(R.id.rl_fee);
        rlPlateNumbers = findViewById(R.id.rl_plate_numbers);
        rgMotors = findViewById(R.id.rg_motors);
        rgAgent = findViewById(R.id.rg_agent);
        rgParkFee = findViewById(R.id.rg_park_fee);
        rbMotorsYes = findViewById(R.id.rb_motors_yes);
        rbAgentYes = findViewById(R.id.rb_agent_yes);
        rbParkYes = findViewById(R.id.rb_park_yes);
        etVisitorName = findViewById(R.id.et_visitor_name);
        etVisitorPhone = findViewById(R.id.et_visitor_phone);
        tvVisitorRoom = findViewById(R.id.tv_visitor_room);
        tvVisiteTime = findViewById(R.id.tv_visite_time);
        tvLeaveTime = findViewById(R.id.tv_leave_time);
        tvVisitePark = findViewById(R.id.tv_visite_park);
        tvPlateNumber = findViewById(R.id.tv_plate_number);
        tvPlateNumbers = findViewById(R.id.tv_plate_numbers);
        tvStatement = findViewById(R.id.tv_statement);
        btnSubscribe = findViewById(R.id.btn_subscribe);
        initPopup();
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        parkingList();
    }

    private void parkingList() {
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_PARKINGLIST, jsonObject);
    }

    private void addVisitorReservation() {
        visitorName = etVisitorName.getText().toString();
        if (TextUtils.isEmpty(visitorName)) {
            showToast(getString(R.string.at_input_visitors_name));
            return;
        }
        visitorTel = etVisitorPhone.getText().toString();
        if (TextUtils.isEmpty(visitorTel)) {
            showToast(getString(R.string.at_text_phone_null));
            return;
        }
        if (!TextUtils.isEmpty(visitorTel) && !isMobileNO(visitorTel)) {
            showToast(getString(R.string.at_text_phone_error));
            return;
        }
        String visitorRoom = tvVisitorRoom.getText().toString();
        if (getString(R.string.at_select_visit_room).equals(visitorRoom)) {
            showToast(getString(R.string.at_select_visit_room));
            return;
        }
        visite_time = tvVisiteTime.getText().toString();
        if (getString(R.string.at_select_visit_time).equals(visite_time)) {
            visite_time = "";
            showToast(getString(R.string.at_select_visit_time));
            return;
        }
        leave_time = tvLeaveTime.getText().toString();
        if (getString(R.string.at_select_leave_time).equals(leave_time)) {
            leave_time = "";
            showToast(getString(R.string.at_select_leave_time));
            return;
        }
        if (!compareDate()) {
            showToast(getString(R.string.at_error_leave_time));
            return;
        }
        visite_park = tvVisitePark.getText().toString();
        if (rbMotorsYes.isChecked()) {
            carNumber = tvPlateNumbers.getText().toString();
            if (TextUtils.isEmpty(carNumber) || carNumber.equals("请输入访客车牌号")) {
                showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                return;
            }
            if (getString(R.string.at_input_plate_number).equals(carNumber)) {
                showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                carNumber = "";
            }
            if (!TextUtils.isEmpty(carNumber) && carNumber.contains("-")) {
                String firstString = carNumber.substring(0, 1);
                if ("港".equals(firstString)) {
                    showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                    return;
                }
                carNumber = carNumber.split("-")[0].substring(0, 2) + "-" + carNumber.split("-")[1];
                //字母开头
                Pattern patternA = Pattern.compile("^[A-Z]{1}");
                //文字开头
                Pattern pattern1 = Pattern.compile("^[\u4e00-\u9fff]{1}[A-Z][-]{1}[A-Z0-9]{4}[A-Z0-9\u4e00-\u9fff]{1}$");
                //七位车牌号码，新能源车
                Pattern pattern2 = Pattern.compile("^[\u4e00-\u9fff]{1}[A-Z][-]{1}[A-Z0-9]{5}[A-Z0-9\u4e00-\u9fff]{1}$");
                Matcher mA = patternA.matcher(carNumber);
                Matcher m1 = pattern1.matcher(carNumber);
                Matcher m2 = pattern2.matcher(carNumber);
                if (m1.matches() || m2.matches()) {
                    ATParkNumberBean ATParkNumberBean = mATParkNumberDao.getByParkNumber(firstString);
                    ATParkNumberBean.setCreate_time(new Date().getTime());
                    mATParkNumberDao.update(ATParkNumberBean);
                } else if (mA.matches()) {

                } else {
                    //车牌错误
                    showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                    return;
                }
            } else {
                showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                return;
            }
            if (getString(R.string.at_select_park).equals(visite_park))
                visite_park = "";
            if (TextUtils.isEmpty(visite_park)) {
                showToast(getString(R.string.at_select_park));
                return;
            }
        }
        setSharedPrefer();

        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        if (rbMotorsYes.isChecked()) {
            jsonObject.put("carNumber", carNumber);
            jsonObject.put("carPark", mATParkNameBeanList.get(park_name_position).getId());
        }
        jsonObject.put("createPerson", ATGlobalApplication.getOpenId());
        jsonObject.put("hasCar", rbMotorsYes.isChecked() ? 1 : -1);
        jsonObject.put("intermediary", rbAgentYes.isChecked() ? 1 : -1);
        jsonObject.put("payPerson", rbParkYes.isChecked() ? 1 : 2);
        jsonObject.put("reservationEndTime", tvLeaveTime.getText());
        jsonObject.put("reservationStartTime", tvVisiteTime.getText());
        jsonObject.put("visitorName", visitorName);
        jsonObject.put("visitorHouse", visitorRoom);
        jsonObject.put("visitorTel", visitorTel);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_ADDVISITORRESERVATION, jsonObject);
    }

    public boolean compareDate() {
        try {
            Date now = sdf.parse(visite_time);
            Date compare = sdf.parse(leave_time);
            if (now != null)
                return now.before(compare);
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void init() {
        btnSubscribe.setOnClickListener(view -> addVisitorReservation());
        llDismiss.setOnClickListener(view -> popupWindow.dismiss());
        SpannableString s = new SpannableString(getString(R.string.at_statement));
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF666666")), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFCD3A3A")), 7, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvStatement.setText(s);

        llVisitePark.setOnClickListener(view -> {
            if (mParkNameList.size() > 0) {
                pvOptions.setSelectOptions(park_name_position);
                pvOptions.show();
            } else
                parkingList();
        });
        btnSubscribe.setEnabled(false);
        etVisitorName.setOnClickListener(view -> popupWindow.showAsDropDown(view));
        etVisitorName.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        etVisitorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mATVisitorHistoryRVAdapter.getFilter().filter(charSequence.toString());
                if (TextUtils.isEmpty(charSequence)) {
                    btnSubscribe.setBackgroundResource(R.drawable.shape_72px_b5a198);
                    btnSubscribe.setEnabled(false);
                } else {
                    btnSubscribe.setBackgroundResource(R.drawable.selector_72px_86523c_b5a198);
                    btnSubscribe.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        if (!TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
            mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
            tvVisitorRoom.setText(mATHouseBean.getName());
            tvVisitorRoom.setTextColor(getResources().getColor(R.color._333333));
        }
        rgMotors.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_motors_yes) {
                rlPlate.setVisibility(View.VISIBLE);
                llPark.setVisibility(View.VISIBLE);
                rlFee.setVisibility(View.GONE);
            } else {
                rlPlate.setVisibility(View.GONE);
                llPark.setVisibility(View.GONE);
                rlFee.setVisibility(View.GONE);
            }
        });
        mATParkNumberDao = new ATParkNumberDao(this);
        mParkNumberList = mATParkNumberDao.getAll();
        if (mParkNumberList.size() == 0)
            initLitepal();
        mATBottomPlatePopup = new ATBottomPlatePopup(this);
        mATBottomPlatePopup.setOnItemClickListener((view, position) -> {
            String park_number = mParkNumberList.get(position).getPark_number();
            updateLitepal(park_number);
            tvPlateNumber.setText(park_number);
            tvPlateNumbers.setText(park_number);
            tvPlateNumbers.setTextColor(getResources().getColor(R.color._333333));
//                observableScrollView.smoothScrollTo(0, 10000);
            observableScrollView.fullScroll(View.FOCUS_DOWN);
            mATSlideFromBottomInputPopup.showPopupWindow();
        });
        mATSlideFromBottomInputPopup = new ATSlideFromBottomInputPopup(this, "ATVisitorAppointActivity");
        mATSlideFromBottomInputPopup.setOnItemClickListener((view, text) -> {
            String parkNumber = tvPlateNumbers.getText().toString();
            if (TextUtils.isEmpty(text)) {
                if (parkNumber.length() > 1) {
                    tvPlateNumbers.setText(parkNumber.substring(0, parkNumber.length() - 1));
                } else {
                    tvPlateNumbers.setText(getString(R.string.at_input_plate_number));
                    tvPlateNumbers.setTextColor(getResources().getColor(R.color._CCCCCC));
                    mATSlideFromBottomInputPopup.dismiss();
                    observableScrollView.fullScroll(View.FOCUS_DOWN);
//                        observableScrollView.smoothScrollTo(0, 10000);
                    mATBottomPlatePopup.showPopupWindow();
                }
            } else {
                if (parkNumber.length() == 2)
                    parkNumber += "-";
                tvPlateNumbers.setText(parkNumber + text);
            }
        });

        tvPlateNumber.setText(mParkNumberList.get(0).getPark_number());
        tvPlateNumbers.setText(mParkNumberList.get(0).getPark_number());
//        tvPlateNumber.setText(carNumber);
        tvPlateNumbers.setTextColor(getResources().getColor(R.color._333333));

        tvPlateNumbers.setOnClickListener(view -> {
//            observableScrollView.smoothScrollTo(0, 10000);
            observableScrollView.fullScroll(View.FOCUS_DOWN);
            if (getString(R.string.at_input_plate_number).equals(tvPlateNumbers.getText().toString()))
                mATBottomPlatePopup.showPopupWindow();
            else
                mATSlideFromBottomInputPopup.showPopupWindow();
        });

        rlPlateNumbers.setOnClickListener(view -> {
            observableScrollView.fullScroll(View.FOCUS_DOWN);
            mATBottomPlatePopup.showPopupWindow();
        });

        tvVisiteTime.setText(sdf.format(System.currentTimeMillis()));
        tvLeaveTime.setText(sdf.format(System.currentTimeMillis() + 86400000));
        rgAgent.check(R.id.rb_agent_no);
        rgMotors.check(R.id.rb_motors_no);
        rgParkFee.check(R.id.rb_park_no);

        llVisiteTime.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            Date date;
            try {
                date = sdf.parse(tvVisiteTime.getText().toString());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mPvCustomTime.setDate(calendar);
            mPvCustomTime.show(tvVisiteTime);
        });
        llLeaveTime.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            Date date;
            try {
                date = sdf.parse(tvLeaveTime.getText().toString());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mPvCustomTime.setDate(calendar);
            mPvCustomTime.show(tvLeaveTime);
        });

        initDialog();
    }

    private boolean isMobileNO(String mobiles) {
        return !TextUtils.isEmpty(mobiles) && mobiles.matches("[1][34578]\\d{9}");
    }

    private void updateLitepal(String park_number) {
        ATParkNumberBean ATParkNumberBean = mATParkNumberDao.getByParkNumber(park_number);
        ATParkNumberBean.setCreate_time(new Date().getTime());
        mATParkNumberDao.update(ATParkNumberBean);
    }

    private void initLitepal() {
        List<String> s = new ArrayList<>();
        s.add("新");
        s.add("宁");
        s.add("青");
        s.add("甘");
        s.add("陕");
        s.add("藏");
        s.add("云");
        s.add("贵");
        s.add("川");
        s.add("渝");
        s.add("琼");
        s.add("桂");
        s.add("晋");
        s.add("蒙");
        s.add("鄂");
        s.add("豫");
        s.add("鲁");
        s.add("冀");
        s.add("闽");
        s.add("皖");
        s.add("浙");
        s.add("苏");
        s.add("沪");
        s.add("黑");
        s.add("吉");
        s.add("辽");
        s.add("津");
        s.add("京");
        s.add("湘");
        s.add("粤");
        s.add("赣");
        // 添加用户数据
        for (int i = 0; i < s.size(); i++) {
            ATParkNumberBean ATParkNumberBean = new ATParkNumberBean();
            ATParkNumberBean.setCreate_time(new Date().getTime() + i * 10);
            ATParkNumberBean.setPark_number(s.get(i));
            mATParkNumberDao.add(ATParkNumberBean);
        }
        mParkNumberList = mATParkNumberDao.getAll();
    }


    private void initDialog() {
        Calendar startDate = Calendar.getInstance();//系统当前时间
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 10);
        //选中事件回调
        mPvCustomTime = new TimePickerBuilder(this, (date, v) -> ((TextView) v).setText(sdf.format(date)))
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "")
                .setDividerColor(0xFFEEEEEE)
                .setCancelColor(0xFF666666)
                .setSubmitColor(0xFFEBB080)
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
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }

        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
        }).setLayoutRes(R.layout.at_pickerview_custom_options_yes_no, v -> {
            v.findViewById(R.id.tv_sure).setOnClickListener(view -> {
                park_name_position = ((WheelView) v.findViewById(R.id.options1)).getCurrentItem();
                tvVisitePark.setText(mParkNameList.get(park_name_position));
                tvVisitePark.setTextColor(getResources().getColor(R.color._333333));
                pvOptions.dismiss();
            });
            v.findViewById(R.id.tv_cancel).setOnClickListener(view -> pvOptions.dismiss());
        })
                .isDialog(true)
                .setDividerColor(0xFFEEEEEE)
                .setContentTextSize(20)
                .setOutSideCancelable(true)
                .setOptionsSelectChangeListener((options1, options2, options3) -> {
                })
                .build();
    }

    private void initPopup() {
        editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        pref = getSharedPreferences("data", MODE_PRIVATE);

        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_visitor_history, null, false);
        popupWindow = new PopupWindow(this);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mATVisitorHistoryRVAdapter = new ATVisitorHistoryRVAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mATVisitorHistoryRVAdapter);
        if (!TextUtils.isEmpty(pref.getString("visitorname0", null)))
            mList.add(pref.getString("visitorname0", null));
        if (!TextUtils.isEmpty(pref.getString("visitorname1", null)))
            mList.add(pref.getString("visitorname1", null));
        if (!TextUtils.isEmpty(pref.getString("visitorname2", null)))
            mList.add(pref.getString("visitorname2", null));
        if (!TextUtils.isEmpty(pref.getString("visitorname3", null)))
            mList.add(pref.getString("visitorname3", null));
        if (!TextUtils.isEmpty(pref.getString("visitorname4", null)))
            mList.add(pref.getString("visitorname4", null));
        mATVisitorHistoryRVAdapter.setLists(mList);
        mATVisitorHistoryRVAdapter.setOnItemClickListener((list, position) -> {
            etVisitorName.setText(list.get(position));
            popupWindow.dismiss();
        });
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setContentView(view);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_ADDVISITORRESERVATION:
                        String id = jsonResult.getString("id");
                        startActivity(new Intent(this, ATVisitorAppointResultActivity.class)
                                .putExtra("id", id));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_PARKINGLIST:
                        mATParkNameBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATParkBean>>() {
                        }.getType());
                        mParkNameList.clear();
                        if (mATParkNameBeanList.size() > 0) {
                            for (ATParkBean atParkBean : mATParkNameBeanList) {
                                mParkNameList.add(atParkBean.getName());
                            }
                            pvOptions.setPicker(mParkNameList);
                            tvVisitePark.setText(mParkNameList.get(0));
                            tvVisitePark.setTextColor(getResources().getColor(R.color._333333));
                        }
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void setSharedPrefer() {
        if (!TextUtils.isEmpty(pref.getString("visitorname0", null)) &&
                pref.getString("visitorname0", null).equals(etVisitorName.getText().toString()))
            return;
        if (!TextUtils.isEmpty(pref.getString("visitorname1", null)) &&
                pref.getString("visitorname1", null).equals(etVisitorName.getText().toString()))
            return;
        if (!TextUtils.isEmpty(pref.getString("visitorname2", null)) &&
                pref.getString("visitorname2", null).equals(etVisitorName.getText().toString()))
            return;
        if (!TextUtils.isEmpty(pref.getString("visitorname3", null)) &&
                pref.getString("visitorname3", null).equals(etVisitorName.getText().toString()))
            return;
        if (!TextUtils.isEmpty(pref.getString("visitorname4", null)) &&
                pref.getString("visitorname4", null).equals(etVisitorName.getText().toString()))
            return;
        if (TextUtils.isEmpty(pref.getString("visitorname0", null))) {
            editor.putString("visitorname0", etVisitorName.getText().toString());
            editor.apply();
        } else if (TextUtils.isEmpty(pref.getString("visitorname1", null))) {
            editor.putString("visitorname1", etVisitorName.getText().toString());
            editor.apply();
        } else if (TextUtils.isEmpty(pref.getString("visitorname2", null))) {
            editor.putString("visitorname2", etVisitorName.getText().toString());
            editor.apply();
        } else if (TextUtils.isEmpty(pref.getString("visitorname3", null))) {
            editor.putString("visitorname3", etVisitorName.getText().toString());
            editor.apply();
        } else if (TextUtils.isEmpty(pref.getString("visitorname4", null))) {
            editor.putString("visitorname4", etVisitorName.getText().toString());
            editor.apply();
        } else {
            editor.putString("visitorname0", pref.getString("visitorname1", null));
            editor.putString("visitorname1", pref.getString("visitorname2", null));
            editor.putString("visitorname2", pref.getString("visitorname3", null));
            editor.putString("visitorname3", pref.getString("visitorname4", null));
            editor.putString("visitorname4", etVisitorName.getText().toString());
            editor.apply();
        }
    }
}