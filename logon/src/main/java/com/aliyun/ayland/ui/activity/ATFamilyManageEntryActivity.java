package com.aliyun.ayland.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.utils.ATIdNumberUtil;
import com.aliyun.ayland.widget.contrarywind.view.WheelView;
import com.aliyun.ayland.widget.pickerview.builder.OptionsPickerBuilder;
import com.aliyun.ayland.widget.pickerview.builder.TimePickerBuilder;
import com.aliyun.ayland.widget.pickerview.view.OptionsPickerView;
import com.aliyun.ayland.widget.pickerview.view.TimePickerView;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ATFamilyManageEntryActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private TimePickerView mPvCustomTime;
    private OptionsPickerView pvOptions;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private int sexPosition = 0;
    private List<String> sexList = new ArrayList<>();
    private String personCode, openId;
    private SpannableString textSpan;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RelativeLayout rlSex, rlBirthDate;
    private Button btnNextFace;
    private View viewManagementFace;
    private RadioButton rbHouseholdtype102, rbHouseholdtype104;
    private TextView tvHint, tvVisitorGender, tvVisitorBirthDate, tvManagementFace;
    private EditText etName;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_family_manage_entry;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        rlSex = findViewById(R.id.rl_sex);
        rlBirthDate = findViewById(R.id.rl_birth_date);
        rbHouseholdtype102 = findViewById(R.id.rb_householdtype102);
        rbHouseholdtype104 = findViewById(R.id.rb_rb_householdtype104);
        etName = findViewById(R.id.et_name);
        tvVisitorGender = findViewById(R.id.tv_visitor_gender);
        tvVisitorBirthDate = findViewById(R.id.tv_visitor_birth_date);
        tvManagementFace = findViewById(R.id.tv_management_face);
        viewManagementFace = findViewById(R.id.view_management_face);
        tvHint = findViewById(R.id.tv_hint);
        btnNextFace = findViewById(R.id.btn_next_face);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void entryFamilyMember() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
//        if (tvVisitorGender.getText().equals("男")) {
//            jsonObject.put("sex", 101);
//        }else {
//            jsonObject.put("sex", 102);
//        }
        jsonObject.put("sex", tvVisitorGender.getText().equals("男") ? 101 : 102);
        jsonObject.put("birthDate", tvVisitorBirthDate.getText());
        jsonObject.put("personName", etName.getText());
        jsonObject.put("householdType", rbHouseholdtype102.isChecked() ? 102 : 104);
        mPresenter.request(ATConstants.Config.SERVER_URL_ENTRYFAMILYMEMBER, jsonObject);
    }

    private void updateFamilyMember() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operatorCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("personCode", getIntent().getStringExtra("memberPersonCode"));
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("sex", tvVisitorGender.getText().equals("男") ? 101 : 102);
        jsonObject.put("birthDate", tvVisitorBirthDate.getText());
        jsonObject.put("personName", etName.getText());
        jsonObject.put("householdType", rbHouseholdtype102.isChecked() ? 102 : 104);
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEFAMILYMEMBER, jsonObject);
    }

    private void init() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("idNumber"))) {
            editorMember();
        }
        String text = "下一步（配置人脸）";
        textSpan = new SpannableString(text);
        textSpan.setSpan(new AbsoluteSizeSpan(14, true), 3, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        btnNextFace.setText(textSpan);
        btnNextFace.setOnClickListener(view -> {
            if (getString(R.string.at_input_name).equals(etName.getText().toString())) {
                showToast(getString(R.string.at_input_name));
            }else if (TextUtils.isEmpty(tvVisitorGender.getText()) || tvVisitorGender.getText().equals("请输入")) {
                showToast(getString(R.string.at_input_sex));
            } else if (TextUtils.isEmpty(tvVisitorBirthDate.getText()) || tvVisitorBirthDate.getText().equals("请选择")) {
                showToast(getString(R.string.at_choose_birth_date));
            }else {
                entryFamilyMember();
            }
        });

        tvVisitorBirthDate.setClickable(true);
        tvVisitorGender.setClickable(true);
        rlSex.setOnClickListener(view -> {
            sexList.clear();
            sexList.add("男");
            sexList.add("女");
            pvOptions.setPicker(sexList);
            pvOptions.setSelectOptions(sexPosition);
            pvOptions.show();
        });

        rlBirthDate.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            Date date;
            try {
//                if (TextUtils.isEmpty(tvVisitorBirthDate.getText()) || tvVisitorBirthDate.getText().equals("请选择")) {
//                    calendar.set(1990,0,1);
//                }else {
//                    date = sdf.parse(tvVisitorBirthDate.getText().toString());
//                    calendar.setTime(date);
//                }
                date = sdf.parse(tvVisitorBirthDate.getText().toString());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mPvCustomTime.setDate(calendar);
            mPvCustomTime.show(tvVisitorBirthDate);
        });

        tvManagementFace.setOnClickListener(view -> {
            startActivity(new Intent(this, ATUserFaceActivity.class)
                    .putExtra("personCode", personCode).putExtra("openId", openId));
            finish();
        });

        tvVisitorGender.setClickable(true);
        tvVisitorGender.setOnClickListener(view -> rlSex.performClick());
        tvVisitorBirthDate.setClickable(true);
        tvVisitorBirthDate.setOnClickListener(view -> rlBirthDate.performClick());

        initDialog();
    }

    private void initDialog() {
        Calendar startDate = Calendar.getInstance();//系统当前时间
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //选中事件回调
        mPvCustomTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
            ((TextView) v).setText(sdf.format(date));
        })
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
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
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }

        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
        }).setLayoutRes(R.layout.at_pickerview_custom_options_yes_no1, v -> {
            v.findViewById(R.id.tv_sure).setOnClickListener(view -> {
                sexPosition = ((WheelView) v.findViewById(R.id.options1)).getCurrentItem();
                tvVisitorGender.setText(sexList.get(sexPosition));
                tvVisitorGender.setTextColor(getResources().getColor(R.color._333333));
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
        Dialog mDialog1 = pvOptions.getDialog();
        if (mDialog1 != null) {
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
                    getScreenWidth(this),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params1.leftMargin = 0;
            params1.rightMargin = 0;
            pvOptions.getDialogContainerLayout().setLayoutParams(params1);

            Window dialogWindow1 = mDialog1.getWindow();
            if (dialogWindow1 != null) {
                dialogWindow1.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow1.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }

    private void editorMember() {
        titlebar.setTitle(getString(R.string.at_edit_family_member));
        titlebar.setSendText(getString(R.string.at_sure1));
        tvHint.setVisibility(View.GONE);
        btnNextFace.setVisibility(View.GONE);
        tvManagementFace.setVisibility(View.VISIBLE);
        etName.setText(getIntent().getStringExtra("lastname"));
        tvVisitorGender.setText(getIntent().getIntExtra("sex", 101) == 101 ? "男" : "女");
        tvVisitorBirthDate.setText(getIntent().getStringExtra("birthDate"));
        rbHouseholdtype104.setChecked(getIntent().getStringExtra("householdtype").equals("104") ? true : false);
        openId = getIntent().getStringExtra("openid");
        titlebar.setPublishClickListener(this::updateFamilyMember);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_ENTRYFAMILYMEMBER:
                        personCode = jsonResult.getJSONObject("data").getString("personCode");
                        openId = jsonResult.getJSONObject("data").getString("openid");
                        tvHint.setVisibility(View.GONE);
                        btnNextFace.setVisibility(View.GONE);
                        tvManagementFace.setVisibility(View.VISIBLE);
                        viewManagementFace.setVisibility(View.VISIBLE);
                        showToast("编辑成功");
                        closeBaseProgressDlg();
                        smartRefreshLayout.finishRefresh();
                        break;
                    case ATConstants.Config.SERVER_URL_UPDATEFAMILYMEMBER:
                        closeBaseProgressDlg();
                        showToast("修改成功");
                        setResult(RESULT_OK);
                        finish();
                }
            } else {
                closeBaseProgressDlg();
                smartRefreshLayout.finishRefresh();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }
}