package com.aliyun.ayland.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.BaseActivity;
import com.aliyun.ayland.contract.MainContract;
import com.aliyun.ayland.data.AllVillageDetailBean1;
import com.aliyun.ayland.data.FamilyMemberBean;
import com.aliyun.ayland.data.HouseBean;
import com.aliyun.ayland.data.ParkNumberBean;
import com.aliyun.ayland.data.ParkingLotBean;
import com.aliyun.ayland.data.db.ParkNumberDao;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.interfaces.OnPopupItemClickListener;
import com.aliyun.ayland.presenter.MainPresenter;
import com.aliyun.ayland.widget.ObservableScrollView;
import com.aliyun.ayland.widget.contrarywind.view.WheelView;
import com.aliyun.ayland.widget.pickerview.builder.OptionsPickerBuilder;
import com.aliyun.ayland.widget.pickerview.listener.OnOptionsSelectChangeListener;
import com.aliyun.ayland.widget.pickerview.view.OptionsPickerView;
import com.aliyun.ayland.widget.popup.BottomPlatePopup;
import com.aliyun.ayland.widget.popup.Options1Popup;
import com.aliyun.ayland.widget.popup.SelectVillagePopup;
import com.aliyun.ayland.widget.popup.SlideFromBottomInputPopup;
import com.aliyun.ayland.widget.titlebar.MyTitleBar;
import com.anthouse.lgcs.R;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisitorRegist2Activity extends BaseActivity implements MainContract.View, OnDateSetListener {
    private MainPresenter mPresenter;
    private MyTitleBar titleBar;
    private Button button;
    private RadioGroup rgMotors;
    private ObservableScrollView observableScrollView;
    private RelativeLayout rlPark, rlPlate;
    private RadioButton rbMotorsYes, rbMotorsNo, rbParkYes, rbParkNo;
    private TextView tvName, tvOwner, tvVisitAddress, tvVisitTime, tvLeaveTime, tvVisitPark, tvPlateNumbers;
    private EditText etVisitorPhone;
    private List<String> mParkNameList = new ArrayList<>();
    private List<ParkingLotBean> mParkingLotBeanList = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private int park_name_position = 0;
    private ParkNumberDao mParkNumberDao;
    private List<ParkNumberBean> mParkNumberList;
    private BottomPlatePopup mBottomPlatePopup;
    private SlideFromBottomInputPopup mSlideFromBottomInputPopup;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private TimePickerDialog mDialogAll;
    private boolean mTextFlag;
    private HouseBean mHouseBean;
    private String id_type, id_number, id_name, visitorTel, visit_address, ownerName, visit_time, leave_time, carNumber, visit_park, buildingCode, ownerPerson;
    private SelectVillagePopup mSelectVillagePopup;
    private List<AllVillageDetailBean1> mAllVillageBeanList;
    private Options1Popup options1Popup;
    private List<FamilyMemberBean> mFamilyMemberList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visitor_regist2;
    }

    @Override
    protected void findView() {
        observableScrollView = findViewById(R.id.observableScrollView);
        etVisitorPhone = findViewById(R.id.et_visitor_phone);
        tvName = findViewById(R.id.tv_name);
        tvOwner = findViewById(R.id.tv_owner);
        tvVisitAddress = findViewById(R.id.tv_address);
        tvVisitTime = findViewById(R.id.tv_visit_time);
        tvLeaveTime = findViewById(R.id.tv_leave_time);
        tvPlateNumbers = findViewById(R.id.tv_plate_numbers);
        tvVisitPark = findViewById(R.id.tv_visite_park);
        rbMotorsYes = findViewById(R.id.rb_motors_yes);
        rbMotorsNo = findViewById(R.id.rb_motors_no);
        rgMotors = findViewById(R.id.rg_motors);
        button = findViewById(R.id.button);
        rlPark = findViewById(R.id.rl_park);
        rlPlate = findViewById(R.id.rl_plate);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(this);
        mPresenter.install(this);
        if (!TextUtils.isEmpty(ATApplication.getHouse())) {
            mHouseBean = gson.fromJson(ATApplication.getHouse(), HouseBean.class);
            findTreeBuilding();
        }
    }

    public void visitorReservation() {
        visitorTel = etVisitorPhone.getText().toString();
        if (!TextUtils.isEmpty(visitorTel) && !isMobileNO(visitorTel)) {
            showToast(getString(R.string.at_text_phone_error));
            return;
        }
        visit_address = tvVisitAddress.getText().toString();
        if (getString(R.string.select_visit_room).equals(visit_address)) {
            showToast(getString(R.string.select_visit_room));
            return;
        }
        ownerName = tvOwner.getText().toString();
        if (getString(R.string.select_visit_the_owner).equals(ownerName)) {
            showToast(getString(R.string.select_visit_the_owner));
            return;
        }
        visit_time = tvVisitTime.getText().toString();
        if (getString(R.string.at_select_visit_time).equals(visit_time)) {
            visit_time = "";
            showToast(getString(R.string.at_select_visit_time));
            return;
        }
        leave_time = tvLeaveTime.getText().toString();
        if (getString(R.string.at_select_leave_time).equals(leave_time)) {
            leave_time = "";
            showToast(getString(R.string.at_select_leave_time));
            return;
        }
        visit_park = tvVisitPark.getText().toString();
        if (rbMotorsYes.isChecked()) {
            carNumber = tvPlateNumbers.getText().toString();
            if (getString(R.string.at_input_plate_number).equals(carNumber)) {
                showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                carNumber = "";
                return;
            }
            if (!TextUtils.isEmpty(carNumber) && carNumber.contains("-")) {
                String firstString = carNumber.substring(0, 1);
                if ("港".equals(firstString)) {
                    showToast(getString(R.string.at_input_correct_license_plate_number) + "\r\n（例：粤B-88888）");
                    return;
                }
                carNumber = carNumber.split("-")[0].substring(0, 1) + "-" + carNumber.split("-")[0].substring(1, 2) + carNumber.split("-")[1];
                //字母开头
                Pattern patternA = Pattern.compile("^[A-Z]{1}");
                //文字开头
                Pattern pattern1 = Pattern.compile("^[\u4e00-\u9fff]{1}[-][A-Z]{1}[A-Z0-9]{4}[A-Z0-9\u4e00-\u9fff]{1}$");
                Matcher mA = patternA.matcher(carNumber);
                Matcher m1 = pattern1.matcher(carNumber);
                if (m1.matches()) {
                    ParkNumberBean parkNumberBean = mParkNumberDao.getByParkNumber(firstString);
                    parkNumberBean.setCreate_time(new Date().getTime());
                    mParkNumberDao.update(parkNumberBean);
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
            if (getString(R.string.at_select_park).equals(visit_park))
                visit_park = "";
            if (TextUtils.isEmpty(visit_park)) {
                showToast(getString(R.string.at_select_park));
                return;
            }
        }
        showBaseProgressDlg();
        HouseBean houseBean;
        JSONObject jsonObject = new JSONObject();
        if (rbMotorsYes.isChecked()) {
            jsonObject.put("carNumber", carNumber.replace("-",""));
            if (mParkingLotBeanList.size() != 0)
                jsonObject.put("carPark", mParkingLotBeanList.get(park_name_position).getParkcode());
        }
        jsonObject.put("idType", id_type);
        jsonObject.put("idNumber", id_number);
        jsonObject.put("openId", ATApplication.getOpenId());
        jsonObject.put("buildingCode", buildingCode);
        jsonObject.put("visitorTel", visitorTel);
        jsonObject.put("reservationStartTime", tvVisitTime.getText() + ":00");
        jsonObject.put("visitorName", id_name);
        jsonObject.put("ownerPerson", ownerPerson);
        jsonObject.put("ownerName", ownerName);
        jsonObject.put("reservationEndTime", tvLeaveTime.getText() + ":00");
        jsonObject.put("villageId", mHouseBean.getVillageId());
        jsonObject.put("hasCar", rbMotorsYes.isChecked() ? 1 : -1);
        mPresenter.request(Constants.Config.SERVER_URL_VISITORRESERVATION, jsonObject);
    }

    public void findFamilyMember() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mHouseBean.getVillageId());
        jsonObject.put("buildingCode", buildingCode);
        mPresenter.request(Constants.Config.SERVER_URL_FINDFAMILYMEMBER, jsonObject);
    }

    public void findTreeBuilding() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mHouseBean.getVillageId());
        mPresenter.request(Constants.Config.SERVER_URL_FINDTREEBUILDING, jsonObject);
    }

    private void init() {
        id_type = getIntent().getStringExtra("id_type");
        id_number = getIntent().getStringExtra("id_number");
        id_name = getIntent().getStringExtra("id_name");

        tvName.setText(id_name);

        button.setOnClickListener(view -> {
            visitorReservation();
        });
        tvVisitAddress.setOnClickListener(view -> {
            if (mAllVillageBeanList == null) {
                findTreeBuilding();
            } else {
                mSelectVillagePopup = new SelectVillagePopup(this, mAllVillageBeanList);
                mSelectVillagePopup.setOnItemClickListener((String name, String code) -> {
                    tvVisitAddress.setText(name);
                    tvVisitAddress.setTextColor(getResources().getColor(R.color._333333));
                    buildingCode = code;
                    findFamilyMember();
                });
                mSelectVillagePopup.showPopupWindow();
            }
        });

        tvOwner.setOnClickListener(view -> {
            if (TextUtils.isEmpty(buildingCode)) {
                showToast(getString(R.string.select_visit_room_first));
                if (mAllVillageBeanList == null) {
                    findTreeBuilding();
                } else {
                    mSelectVillagePopup = new SelectVillagePopup(this, mAllVillageBeanList);
                    mSelectVillagePopup.setOnItemClickListener((String name, String code) -> {
                        tvVisitAddress.setText(name);
                        tvVisitAddress.setTextColor(getResources().getColor(R.color._333333));
                        buildingCode = code;
                        mFamilyMemberList = null;
                        tvOwner.setText(R.string.select_visit_the_owner);
                        tvOwner.setTextColor(getResources().getColor(R.color._DDDDDD));
                        findFamilyMember();
                    });
                    mSelectVillagePopup.showPopupWindow();
                }
            } else {
                if (mFamilyMemberList == null)
                    findFamilyMember();
                else {
                    options1Popup.showPopupWindow();
                }
            }
        });
        rlPark.setOnClickListener(view -> {
            if (mParkNameList.size() > 0) {
                pvOptions.setSelectOptions(park_name_position);
                pvOptions.show();
            } else {
//                findParkingLotList();
            }
        });

        button.setEnabled(true);
        rgMotors.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_motors_yes) {
                rlPlate.setVisibility(View.VISIBLE);
                rlPark.setVisibility(View.VISIBLE);
            } else {
                rlPlate.setVisibility(View.GONE);
                rlPark.setVisibility(View.GONE);
            }
        });

        mParkNumberDao = new ParkNumberDao(this);
        mParkNumberList = mParkNumberDao.getAll();
        if (mParkNumberList.size() == 0) {
            initLitepal();
        }
        mBottomPlatePopup = new BottomPlatePopup(this);
        mBottomPlatePopup.setOnItemClickListener((view, position) -> {
            String park_number = mParkNumberList.get(position).getPark_number();
            updateLitepal(park_number);
            tvPlateNumbers.setText(park_number);
            observableScrollView.smoothScrollTo(0, 1500);
            mSlideFromBottomInputPopup.showPopupWindow();
        });
        mSlideFromBottomInputPopup = new SlideFromBottomInputPopup(this, "VisitorAppointActivity");
        mSlideFromBottomInputPopup.setOnItemClickListener((view, text) -> {
            String parkNumber = tvPlateNumbers.getText().toString();
            if (TextUtils.isEmpty(text)) {
                if (parkNumber.length() > 1) {
                    tvPlateNumbers.setText(parkNumber.substring(0, parkNumber.length() - 1));
                } else {
                    tvPlateNumbers.setText(getString(R.string.at_input_plate_number));
                    mSlideFromBottomInputPopup.dismiss();
                    observableScrollView.smoothScrollTo(0, 1000);
                    mBottomPlatePopup.showPopupWindow();
                }
            } else {
                if (parkNumber.length() == 2)
                    parkNumber += "-";
                tvPlateNumbers.setText(parkNumber + text);
            }
        });
        tvPlateNumbers.setText(mParkNumberList.get(0).getPark_number());

        rlPlate.setOnClickListener(view -> {
            observableScrollView.smoothScrollTo(0, 1000);
            if (getString(R.string.at_input_plate_number).equals(tvPlateNumbers.getText().toString())) {
                mBottomPlatePopup.showPopupWindow();
            } else {
                mSlideFromBottomInputPopup.showPopupWindow();
            }
        });

        tvVisitTime.setText(sdf.format(System.currentTimeMillis()));
        tvVisitTime.setTextColor(getResources().getColor(R.color._333333));
        tvLeaveTime.setText(sdf.format(System.currentTimeMillis() + 86400000));
        tvLeaveTime.setTextColor(getResources().getColor(R.color._333333));
        rgMotors.check(R.id.rb_motors_no);

        tvVisitTime.setOnClickListener(view -> {
            if (mDialogAll.isAdded())
                return;
            mTextFlag = false;
            mDialogAll.show(getSupportFragmentManager(), "visit_time");
        });
        tvLeaveTime.setOnClickListener(view -> {
            createLeaveDialog(tvVisitTime.getText().toString());
        });
        initDialog();
    }

    private boolean isMobileNO(String mobiles) {
        return !TextUtils.isEmpty(mobiles) && mobiles.matches("[1][3456789]\\d{9}");
    }

    private void updateLitepal(String park_number) {
        ParkNumberBean parkNumberBean = mParkNumberDao.getByParkNumber(park_number);
        parkNumberBean.setCreate_time(new Date().getTime());
        mParkNumberDao.update(parkNumberBean);
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
            ParkNumberBean parkNumberBean = new ParkNumberBean();
            parkNumberBean.setCreate_time(new Date().getTime() + i * 10);
            parkNumberBean.setPark_number(s.get(i));
            mParkNumberDao.add(parkNumberBean);
        }
        mParkNumberList = mParkNumberDao.getAll();
    }

    private void initDialog() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(R.string.at_cancel))
                .setSureStringId(getString(R.string.at_sure))
                .setTitleStringId("")
                .setYearText(getString(R.string.at_year))
                .setMonthText(getString(R.string.at_month))
                .setDayText(getString(R.string.at_day))
                .setHourText(getString(R.string.at_hour1))
                .setMinuteText(getString(R.string.at_minute))
                .setCyclic(true)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color._86523C))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();

        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
        }).setLayoutRes(R.layout.at_pickerview_custom_options_yes_no, v -> {
            v.findViewById(R.id.tv_sure).setOnClickListener(view -> {
                park_name_position = ((WheelView) v.findViewById(R.id.options1)).getCurrentItem();
                tvVisitPark.setText(mParkNameList.get(park_name_position));
                pvOptions.dismiss();
            });
            v.findViewById(R.id.tv_cancel).setOnClickListener(view -> pvOptions.dismiss());
        })
                .isDialog(true)
                .setDividerColor(0xFFEEEEEE)
                .setContentTextSize(20)
                .setOutSideCancelable(true)
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                    }
                })
                .build();
    }

    private void createLeaveDialog(String time) {
        mTextFlag = true;
        TimePickerDialog mDialogLeave = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(R.string.at_cancel))
                .setSureStringId(getString(R.string.at_sure))
                .setTitleStringId("")
                .setYearText(getString(R.string.at_year))
                .setMonthText(getString(R.string.at_month))
                .setDayText(getString(R.string.at_day))
                .setHourText(getString(R.string.at_hour1))
                .setMinuteText(getString(R.string.at_minute))
                .setCyclic(false)
                .setMinMillseconds(getLongTime(time))
                .setMaxMillseconds(getLongTime(time) + 1000 * 60 * 60 * 24L)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color._86523C))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
        mDialogLeave.show(getSupportFragmentManager(), "leave_time");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String time = getDateToString(millseconds);
        if (mTextFlag) {
            tvLeaveTime.setText(time);
            tvLeaveTime.setTextColor(getResources().getColor(R.color._333333));
        } else {
            tvVisitTime.setText(time);
            tvVisitTime.setTextColor(getResources().getColor(R.color._333333));
            tvLeaveTime.setText(getString(R.string.at_select_leave_time));
            tvLeaveTime.setTextColor(getResources().getColor(R.color._DDDDDD));
            createLeaveDialog(time);
        }
    }

    private Long getLongTime(String timeString) {
        long l = 0;
        Date d;
        try {
            d = sdf.parse(timeString);
            l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sdf.format(d);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case Constants.Config.SERVER_URL_VISITORRESERVATION:
                        showToast(getString(R.string.at_appoint_success));
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case Constants.Config.SERVER_URL_FINDTREEBUILDING:
                        mAllVillageBeanList = gson.fromJson(jsonResult.getString("list").replace("\"area\":{}", "\"area\":[]")
                                .replace("\"immeuble\":{}", "\"immeuble\":[]").replace("\"unit\":{}", "\"unit\":[]")
                                .replace("\"floor\":{}", "\"floor\":[]").replace("\"room\":{}", "\"room\":[]"), new TypeToken<List<AllVillageDetailBean1>>() {
                        }.getType());
                        runOnUiThread(() -> {
                            if (mAllVillageBeanList.size() == 0) {
                                showToast("无法获取小区信息");
                            }
                        });
                        break;
                    case Constants.Config.SERVER_URL_FINDFAMILYMEMBER:
                        mFamilyMemberList = gson.fromJson(jsonResult.getString("members"), new TypeToken<List<FamilyMemberBean>>() {
                        }.getType());
                        List<String> nameList = new ArrayList<>();
                        for (FamilyMemberBean familyMemberBean : mFamilyMemberList) {
                            nameList.add(familyMemberBean.getLastname() + familyMemberBean.getFirstname());
                        }
                        runOnUiThread(() -> {
                            options1Popup = new Options1Popup(this, "选择拜访业主", nameList);
                            options1Popup.setOnItemClickListener(new OnPopupItemClickListener() {
                                @Override
                                public void onItemClick(int i1, int i2) {
                                    tvOwner.setText(nameList.get(i1));
                                    tvOwner.setTextColor(getResources().getColor(R.color._333333));
                                    ownerPerson = mFamilyMemberList.get(i1).getPersonCode();
                                }
                                @Override
                                public void onItemClick(String s1, String s2, String s3) {
                                }
                            });
                        });
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
}