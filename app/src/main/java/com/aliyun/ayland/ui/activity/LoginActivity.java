package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.BaseActivity;
import com.aliyun.ayland.contract.MainContract;
import com.aliyun.ayland.data.LoginBean;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.global.WebSockIO;
import com.aliyun.ayland.presenter.LoginPresenter;
import com.aliyun.ayland.utils.CallbackUtil;
import com.aliyun.ayland.utils.NetWorkUtils;
import com.aliyun.ayland.utils.PreferencesUtils;
import com.aliyun.ayland.utils.SystemStatusBarUtils;
import com.anthouse.lgcs.R;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements MainContract.View {
    private static final int REQUEST_CODE_SUCCESS = 0x1000;
    private static final int MSG_TIME_CLICK = 0x1001;
    private LoginPresenter mPresenter;
    private boolean mPasswordVisible = false, accountEmpty = true, passwordEmpty = true, accountEmpty1 = true, passwordEmpty1 = true;
    private String phone, code, password, phone_regist, password_regist;
    private int time;
    private Button btnLogin;
    private RadioGroup radioGroup;
    private RadioButton rbLogin, rbRegist;
    private LinearLayout llLogin, llRegist;
    private ImageView imgLinePhone, imgLinePhone1, imgLineCode, imgLinePassword, imgLinePassword1, imgPasswordVisible;
    private TextView tvAgreement, tvGetCode, tvForgetPassword;
    private EditText etNewPassword, etCode, etNumberPhone1, etPassword, etNumberPhone, etPassword1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_CLICK:
                    --time;
                    if (time == 0) {
                        tvGetCode.setClickable(true);
                        tvGetCode.setText(getResources().getString(R.string.at_get_code_again));
                        tvGetCode.setTextColor(getResources().getColor(R.color._B98C67));
                    } else {
                        tvGetCode.setText(String.format(getString(R.string.at_get_code_after), time));
                        tvGetCode.setTextColor(getResources().getColor(R.color._999999));
                        mHandler.sendEmptyMessageDelayed(MSG_TIME_CLICK, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void findView() {
        llLogin = findViewById(R.id.ll_login);
        radioGroup = findViewById(R.id.radioGroup);
        llRegist = findViewById(R.id.ll_regist);
        rbRegist = findViewById(R.id.rb_regist);
        rbLogin = findViewById(R.id.rb_login);
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        btnLogin = findViewById(R.id.btn_login);
        tvGetCode = findViewById(R.id.tv_get_code);
        tvAgreement = findViewById(R.id.tv_agreement);
        etCode = findViewById(R.id.et_code);
        etNumberPhone = findViewById(R.id.et_number_phone);
        etNumberPhone1 = findViewById(R.id.et_number_phone1);
        etPassword = findViewById(R.id.et_password);
        etPassword1 = findViewById(R.id.et_password1);
        imgLinePhone = findViewById(R.id.img_line_phone);
        imgLinePhone1 = findViewById(R.id.img_line_phone1);
        imgLineCode = findViewById(R.id.img_line_code);
        imgLinePassword = findViewById(R.id.img_line_password);
        imgLinePassword1 = findViewById(R.id.img_line_password1);
        imgPasswordVisible = findViewById(R.id.img_password_visible);
        SystemStatusBarUtils.init(this, true);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
        mPresenter.install(this);
    }

    private void initRegistView() {
        tvGetCode.setOnClickListener(view -> {
                    phone_regist = etNumberPhone1.getText().toString();
                    if (!isMobileNO(phone_regist)) {
                        showToast(getString(R.string.at_input_correct_phone));
                        return;
                    }
                    showBaseProgressDlg();
                    codeToRegister();
                }
        );

        etNumberPhone1.setOnFocusChangeListener((view, b) -> imgLinePhone1.setBackgroundColor(b ? getResources().getColor(R.color._333333) : getResources().getColor(R.color._CCCCCC)));
        etNumberPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    accountEmpty1 = true;
                    btnLogin.setEnabled(false);
                } else {
                    accountEmpty1 = false;
                    if (!passwordEmpty1) {
                        btnLogin.setEnabled(true);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        etCode.setOnFocusChangeListener((view, b) -> imgLineCode.setBackgroundColor(b ? getResources().getColor(R.color._333333) : getResources().getColor(R.color._CCCCCC)));
        etPassword1.setOnFocusChangeListener((view, b) -> imgLinePassword1.setBackgroundColor(b ? getResources().getColor(R.color._333333) : getResources().getColor(R.color._CCCCCC)));
        etPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    passwordEmpty1 = true;
                    btnLogin.setEnabled(false);
                } else {
                    passwordEmpty1 = false;
                    if (!accountEmpty1) {
                        btnLogin.setEnabled(true);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        etPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void initLoginView() {
        etNumberPhone.setOnFocusChangeListener((view, b) -> imgLinePhone.setBackgroundColor(b ? getResources().getColor(R.color._333333) : getResources().getColor(R.color._CCCCCC)));
        etNumberPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    accountEmpty = true;
                    btnLogin.setEnabled(false);
                } else {
                    accountEmpty = false;
                    if (!passwordEmpty) {
                        btnLogin.setEnabled(true);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        etPassword.setOnFocusChangeListener((view, b) -> imgLinePassword.setBackgroundColor(b ? getResources().getColor(R.color._333333) : getResources().getColor(R.color._CCCCCC)));
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    passwordEmpty = true;
                    btnLogin.setEnabled(false);
                } else {
                    passwordEmpty = false;
                    if (!accountEmpty) {
                        btnLogin.setEnabled(true);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        imgPasswordVisible.setOnClickListener(v -> {
            mPasswordVisible = !mPasswordVisible;
            if (mPasswordVisible) {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            etPassword.setSelection(etPassword.getText().toString().length());
        });
    }

    private void init() {
        phone = PreferencesUtils.getString(this, "tempAccount", "");
        password = ATApplication.getPassword();
        rbLogin.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                btnLogin.setText(getString(R.string.at_login));
                llLogin.setVisibility(View.VISIBLE);
                llRegist.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(phone)) {
                    etNumberPhone.setText(phone);
                    etNumberPhone.setSelection(phone.length());
                    accountEmpty = false;
                }
                if (!TextUtils.isEmpty(password)) {
                    etPassword.setText(password);
                    passwordEmpty = false;
                    btnLogin.setEnabled(true);
                }
            }
        });
        rbRegist.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                llLogin.setVisibility(View.GONE);
                llRegist.setVisibility(View.VISIBLE);
                btnLogin.setText(getString(R.string.at_regist));
                if (!TextUtils.isEmpty(phone_regist)) {
                    etNumberPhone1.setText(phone_regist);
                    etNumberPhone1.setSelection(phone_regist.length());
                    accountEmpty1 = false;
                }
                if (!TextUtils.isEmpty(password_regist)) {
                    etPassword1.setText(password_regist);
                    passwordEmpty1 = false;
                    btnLogin.setEnabled(true);
                }
            }
        });
        radioGroup.check(R.id.rb_login);
        btnLogin.setOnClickListener(v -> {
            if (getString(R.string.at_login).equals(btnLogin.getText())) {
                if (!NetWorkUtils.isNetworkAvailable(LoginActivity.this)) {
                    showToast(getString(R.string.at_net_work_wrong));
                    return;
                }
                phone = etNumberPhone.getText().toString();
                if (!isMobileNO(phone)) {
                    showToast(getString(R.string.at_input_correct_phone));
                    return;
                }
                password = etPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showToast(getResources().getString(R.string.at_empty_password));
                    return;
                } else {
                    if (password.length() > 16 || password.length() < 6) {
                        showToast(getResources().getString(R.string.at_text_hint_import_password));
                        return;
                    } else {
                        if (!isPassword(password)) {
                            showToast(getResources().getString(R.string.at_text_hint_password_not_china));
                            return;
                        }
                    }
                }
                showBaseProgressDlg();
                appLogin();
            } else {
                phone_regist = etNumberPhone1.getText().toString();
                if (!isMobileNO(phone_regist)) {
                    showToast(getString(R.string.at_input_correct_phone));
                    return;
                }
                code = etCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    showToast(getString(R.string.at_empty_code));
                    return;
                }
                password_regist = etPassword1.getText().toString();
                if (TextUtils.isEmpty(password_regist)) {
                    showToast(getResources().getString(R.string.at_empty_password));
                    return;
                } else {
                    if (password_regist.length() > 16 || password_regist.length() < 6) {
                        showToast(getResources().getString(R.string.at_text_hint_import_password));
                        return;
                    } else {
                        if (!isPassword(password_regist)) {
                            showToast(getResources().getString(R.string.at_text_hint_password_not_china));
                            return;
                        }
                    }
                }
                showBaseProgressDlg();
                registerPhone();
            }
        });
        SpannableString s = new SpannableString(getString(R.string.at_login_agreement));
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF999999")), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFCCA56A")), 10, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(s);
        initLoginView();
        initRegistView();
        if (!TextUtils.isEmpty(phone)) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    /**
     * 验证手机格式
     */
    private boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3456789]\\d{9}";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    private void setCodeText() {
        time = 60;
        tvGetCode.setClickable(false);
        tvGetCode.setText(String.format(getString(R.string.at_get_code_after), time));
        tvGetCode.setTextColor(getResources().getColor(R.color._999999));
        mHandler.sendEmptyMessageDelayed(MSG_TIME_CLICK, 1000);
    }

    private void codeToRegister() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone_regist);
        jsonObject.put("areacode", "86");
        mPresenter.request(Constants.Config.SERVER_URL_CODETOREGISTER, jsonObject);
    }

    private void registerPhone() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", phone_regist);
        jsonObject.put("password", password_regist);
        jsonObject.put("type", "default");
        jsonObject.put("nickName", "");
        jsonObject.put("phoneCode", code);
        jsonObject.put("phone", phone_regist);
        mPresenter.request(Constants.Config.SERVER_URL_REGISTERPHONE, jsonObject);
    }

    private void appLogin() {
        PreferencesUtils.putString(this, "tempAccount", phone);
        ATApplication.setPassword(password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", phone);
        jsonObject.put("password", password);
        mPresenter.request(Constants.Config.SERVER_URL_APPLOGIN, jsonObject);
    }

    private boolean isPassword(String mpassword) {
        Pattern p = Pattern.compile("[^\u4e00-\u9fa5]+");
        Matcher m = p.matcher(mpassword);
        return m.matches();
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            if (TextUtils.isEmpty(result))
                return;
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case Constants.Config.SERVER_URL_APPLOGIN:
                        LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                        ATApplication.setAccount(etNumberPhone.getText().toString().replaceAll(" ", ""));
                        ATApplication.setHouse(loginBean.getHouse().toString());
                        ATApplication.setLoginBeanStr(result);

                        CallbackUtil.doCallBackMethod(loginBean.getAuthCode());
//                        ((MyOALoginAdapter)LoginBusiness.getLoginAdapter()).authLogin(loginBean.getAuthCode());
                        WebSockIO.getInstance().closeSock();
                        WebSockIO.getInstance().setUpConnect();
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_login_success));
//                        try {
//                            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//                            PreferencesUtils.putString(this, "version_name", packInfo.versionName);
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        startActivity(new Intent(this, EquipmentActivity.class));
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                        break;
                    case Constants.Config.SERVER_URL_CODETOREGISTER:
                        showToast(getString(R.string.at_get_verification_code_success));
                        closeBaseProgressDlg();
                        setCodeText();
                        break;
                    case Constants.Config.SERVER_URL_REGISTERPHONE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_regist_success));
                        showBaseProgressDlg(getString(R.string.at_logining));
                        phone = phone_regist;
                        password = password_regist;
                        appLogin();
                        break;
                }
            } else {
                closeBaseProgressDlg();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String phone = data.getStringExtra("phone");
            etNumberPhone.setText(phone);
            etPassword.requestFocus();
        }
    }
}