package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATAuthCodeBean;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATWebSockIO;
import com.aliyun.ayland.presenter.ATLoginPresenter;
import com.aliyun.ayland.utils.ATCallbackUtil;
import com.aliyun.ayland.utils.ATNetWorkUtils;
import com.aliyun.ayland.utils.ATPreferencesUtils;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.ayland.widget.ATNoUnderLineSpan;
import com.anthouse.xuhui.R;
import com.espressif.android.v1.EspTouchActivity;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATLoginActivity extends ATBaseActivity implements ATMainContract.View, View.OnClickListener {
    private static final int REQUEST_CODE_SUCCESS = 0x1000;
    private ATLoginPresenter mPresenter;
    private boolean mPasswordVisible = false, accountEmpty = true, passwordEmpty = true;
    private String phone, code, password, phone_regist, password_regist;
    private int time, status = 0;
    private long clickTime = 0;
    private Dialog dialog, mDialogRead;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    private TextView tvForgetPassword, tvAgreement;
    private EditText etPhone, etPassword;
    private Button btnLogin;
    private RelativeLayout rlContent, rlPhone, rlPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_login;
    }

    @Override
    protected void findView() {
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        tvAgreement = findViewById(R.id.tv_agreement);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        rlContent = findViewById(R.id.rl_content);
        rlPhone = findViewById(R.id.rl_phone);
        rlPassword = findViewById(R.id.rl_password);
        findViewById(R.id.tv_regist).setOnClickListener(this);
        findViewById(R.id.tv_forget_password).setOnClickListener(this);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATLoginPresenter(this);
        mPresenter.install(this);
    }

    private void initLoginView() {
        etPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rlPhone.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_ffffff));
            } else {
                rlPhone.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_99ffffff));
            }
        });
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rlPassword.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_ffffff));
            } else {
                rlPassword.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_99ffffff));
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    accountEmpty = true;
                    btnLogin.setBackgroundResource(R.drawable.shape_72px_efcdaa_to_eeb67d);
                    btnLogin.setEnabled(false);
                } else {
                    accountEmpty = false;
                    if (!passwordEmpty) {
                        btnLogin.setBackgroundResource(R.drawable.selector_72px_ffb176fda448_efcdaaeeb67d);
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
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    passwordEmpty = true;
                    btnLogin.setBackgroundResource(R.drawable.shape_72px_efcdaa_to_eeb67d);
                    btnLogin.setEnabled(false);
                } else {
                    passwordEmpty = false;
                    if (!accountEmpty) {
                        btnLogin.setBackgroundResource(R.drawable.selector_72px_ffb176fda448_efcdaaeeb67d);
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

//        imgPasswordVisible.setOnClickListener(v -> {
//            mPasswordVisible = !mPasswordVisible;
//            if (mPasswordVisible) {
//                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                imgPasswordVisible.setImageResource(R.drawable.at_login_hide_p);
//            } else {
//                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                imgPasswordVisible.setImageResource(R.drawable.at_login_hide_n);
//            }
//            etPassword.setSelection(etPassword.getText().toString().length());
//        });

        tvForgetPassword.setOnClickListener(view ->
                startActivityForResult(new Intent(this, ATForgetPasswordActivity.class)
                        .putExtra("regist", false).putExtra("title", getString(R.string.at_text_forget_password1)), REQUEST_CODE_SUCCESS));
    }

    private void init() {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rlContent.getLayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = y;
        rlContent.setLayoutParams(params);

        ATSystemStatusBarUtils.init(this, false);
        phone = ATPreferencesUtils.getString(this, "tempAccount", "");
        password = ATGlobalApplication.getPassword();
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
            accountEmpty = false;
        }
        if (!TextUtils.isEmpty(password)) {
            etPassword.setText(password);
            passwordEmpty = false;
            if (!accountEmpty) {
                btnLogin.setBackgroundResource(R.drawable.selector_72px_ffb176fda448_efcdaaeeb67d);
                btnLogin.setEnabled(true);
            }
        }
        btnLogin.setOnClickListener(v -> {
            if (!ATNetWorkUtils.isNetworkAvailable(ATLoginActivity.this)) {
                showToast(getString(R.string.at_net_work_wrong));
                return;
            }
            phone = etPhone.getText().toString();
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
            if (!ATGlobalApplication.isAgree()) {
                mDialogRead.show();
                return;
            }
            showBaseProgressDlg();
            login();
        });

        tvAgreement.setText(getSpannableString(getString(R.string.at_login_agreement)));
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

        initLoginView();
        if (!TextUtils.isEmpty(phone)) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        pref = getSharedPreferences("data", MODE_PRIVATE);
        initDialog();
    }

    private SpannableString getSpannableString(String agreement) {
        SpannableString s = new SpannableString(agreement);
        s.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                mDialogRead.show();
            }
        }, 13, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                mDialogRead.show();
            }
        }, 22, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        s.setSpan(new ATNoUnderLineSpan(), 13, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ATNoUnderLineSpan(), 22, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBDBDBD")), 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFEBB080")), 13, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBDBDBD")), 21, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFEBB080")), 22, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private SpannableString getSpannableUrlString(String agreement) {
        SpannableString s = new SpannableString(agreement);
        s.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ATLoginActivity.this, ATWebViewActivity.class)
                        .putExtra("title", getString(R.string.at_release_agreement))
                        .putExtra("WebViewUrl", ATConstants.Config.RELEASE_AGREEMENT_URL));
            }
        }, 13, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ATLoginActivity.this, ATWebViewActivity.class)
                        .putExtra("title", getString(R.string.at_privacy_policy))
                        .putExtra("WebViewUrl", ATConstants.Config.PRIVACY_POLICY_URL));
            }
        }, 22, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        s.setSpan(new ATNoUnderLineSpan(), 12, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ATNoUnderLineSpan(), 22, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBDBDBD")), 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFEBB080")), 13, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFBDBDBD")), 21, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFEBB080")), 22, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    /**
     * 验证手机格式
     */
    private boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    private void login() {
        ATPreferencesUtils.putString(this, "tempAccount", phone);
        ATGlobalApplication.setPassword(password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", phone);
        jsonObject.put("password", password);
        mPresenter.request(ATConstants.Config.SERVER_URL_LOGIN, jsonObject);
    }

    private void getAuthCode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", phone);
        jsonObject.put("password", password);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETAUTHCODE, jsonObject);
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
                    case ATConstants.Config.SERVER_URL_GETAUTHCODE:
                        ATAuthCodeBean aTLogin = gson.fromJson(jsonResult.toString(), ATAuthCodeBean.class);
                        ATCallbackUtil.doCallBackMethod(aTLogin.getAuthCode());
                        ATGlobalApplication.setAuthCode(aTLogin.getAuthCode());
                        ATWebSockIO.getInstance().closeSock();
                        ATWebSockIO.getInstance().setUpConnect();

                        if (aTLogin.getHouse() != null)
                            ATGlobalApplication.setHouse(aTLogin.getHouse().toString());

                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_login_success));
                        startActivity(new Intent(ATLoginActivity.this, ATMainActivity.class));
                        mPresenter.request(ATConstants.Config.SERVER_URL_GETACCESSTOKEN, null);
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_LOGIN:
                        ATLoginBean mATLoginBean = gson.fromJson(jsonResult.getString("result"), ATLoginBean.class);
                        ATGlobalApplication.setAccount(etPhone.getText().toString().replaceAll(" ", ""));
                        ATGlobalApplication.setLoginBeanStr(jsonResult.getString("result"));

                        if(mATLoginBean.isHasHouse() && mATLoginBean.getHouse()!=null) {
                            getAuthCode();
                        } else {
                            //游客
                            startActivity(new Intent(ATLoginActivity.this, ATEmptyActivity.class));
                            closeBaseProgressDlg();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_CODETOREGISTER:
                        showToast(getString(R.string.at_get_verification_code_success));
                        closeBaseProgressDlg();
                        break;
                    case ATConstants.Config.SERVER_URL_REGISTERCLIENT:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_regist_success));
                        showBaseProgressDlg(getString(R.string.at_logining));
                        phone = phone_regist;
                        password = password_regist;
                        login();
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
            etPhone.setText(phone);
            etPassword.requestFocus();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - clickTime) > 2000) {
                showToast(getString(R.string.at_click_again_to_exit));
                clickTime = System.currentTimeMillis();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){// 点击了返回按键
//            exitApp(2000);// 退出应用
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    /**
//     * 退出应用
//     * @param timeInterval 设置第二次点击退出的时间间隔
//     */
//    private void exitApp(long timeInterval) {
//        if(System.currentTimeMillis() - clickTime >= timeInterval){
//            showToast(getString(R.string.click_again_to_exit));
//            clickTime = System.currentTimeMillis();
//        }else {
////            ATUserActivity userActivity = new ATUserActivity();
////            userActivity.finish();
////            ATSettingActivity settingActivity = new ATSettingActivity();
////            settingActivity.finish();
////            ATMainActivity mainActivity = new ATMainActivity();
////            mainActivity.finish();
////            List<Activity> topActivity_1 = getAllActivitys();
////            Log.e("aaaatop1", String.valueOf(topActivity_1));
//            finish();// 销毁当前activity
//            System.exit(0);// 完全退出应用
//        }
//    }


//    public List<Activity> getAllActivitys(){
//        List<Activity> list=new ArrayList<>();
//        try {
//            Class<?> activityThread=Class.forName("android.app.ActivityThread");
//            Method currentActivityThread=activityThread.getDeclaredMethod("currentActivityThread");
//            currentActivityThread.setAccessible(true);
//            //获取主线程对象
//            Object activityThreadObject=currentActivityThread.invoke(null);
//            Field mActivitiesField = activityThread.getDeclaredField("mActivities");
//            mActivitiesField.setAccessible(true);
//            Map<Object,Object> mActivities = (Map<Object,Object>) mActivitiesField.get(activityThreadObject);
//            for (Map.Entry<Object,Object> entry:mActivities.entrySet()){
//                Object value = entry.getValue();
//                Class<?> activityClientRecordClass = value.getClass();
//                Field activityField = activityClientRecordClass.getDeclaredField("activity");
//                activityField.setAccessible(true);
//                Object o = activityField.get(value);
//                list.add((Activity) o);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e("aaaa","taskid " + getTaskId() + " hsahcode " + hashCode());
//        List<Activity> topActivity_1 = getAllActivitys();
//        Log.e("aaaatop", String.valueOf(topActivity_1));
//    }
//
//    private void initReadDialog() {
//        dialog = new Dialog(Objects.requireNonNull(this), R.style.nameDialog);
//        @SuppressLint("InflateParams")
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login_read, null, false);
//        cbTip = view.findViewById(R.id.cb_statement);
//        view.findViewById(R.id.tv_sure).setOnClickListener((v) -> {
//            readStatus = (cbTip.isChecked() ? 1 : 0);
//            if (cbTip.isChecked()) {
//                editor.putString("isAgreeToTheTerms", "agreed");
//            } else {
//                editor.putString("isAgreeToTheTerms", "noAgreed");
//            }
//            editor.apply();
//            dialog.dismiss();
//        });
//        view.findViewById(R.id.img_close).setOnClickListener((v) -> {
//            dialog.dismiss();
//        });
//        view.findViewById(R.id.ll_statement).setOnClickListener((v) -> cbTip.setChecked(!cbTip.isChecked()));
//        dialog.setContentView(view);
//    }


    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.wifiDialog);
        View view = this.getLayoutInflater().inflate(R.layout.at_confirm_del_dialog, null, false);
        TextView cancel = view.findViewById(R.id.dialog_cancel);
        cancel.setText(getString(R.string.at_quit_login));
        cancel.setOnClickListener(this);
        cancel.setVisibility(View.GONE);
        TextView tvSure = view.findViewById(R.id.dialog_sure);
        tvSure.setText(getString(R.string.at_login_again));
        tvSure.setOnClickListener(this);

        TextView content = view.findViewById(R.id.dialog_delete_room_content);
        content.setText(getString(R.string.at_some_one_login));
        dialog.setContentView(view);

        mDialogRead = new Dialog(this, R.style.nameDialog);
        View view1 = LayoutInflater.from(this).inflate(R.layout.at_dialog_confirm_read, null, false);
        TextView agreement = view1.findViewById(R.id.tv_service_agreement);
        agreement.setText(getSpannableUrlString(getString(R.string.at_login_agreement)));
        agreement.setMovementMethod(LinkMovementMethod.getInstance());
        view1.findViewById(R.id.tv_agree).setOnClickListener((v) -> {
            ATGlobalApplication.setAgree(true);
            mDialogRead.dismiss();
            showBaseProgressDlg();
            login();
        });
        view1.findViewById(R.id.tv_refuse).setOnClickListener((v) -> mDialogRead.dismiss());
        mDialogRead.setContentView(view1);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_sure) {
            if (dialog != null) {
                dialog.dismiss();
            }
        } else if (i == R.id.dialog_cancel) {
            if (dialog != null) {
                dialog.dismiss();
            }
        } else if (i == R.id.tv_regist) {
            startActivity(new Intent(this, ATRegistActivity.class));
        } else if (i == R.id.tv_forget_password) {
            startActivity(new Intent(this, ATForgetPasswordActivity.class).putExtra("title", getString(R.string.at_text_forget_password1)));
        }
    }
}