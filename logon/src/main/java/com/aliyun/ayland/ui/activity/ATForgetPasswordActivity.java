package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.utils.ATNetWorkUtils;
import com.aliyun.ayland.widget.ATChoiseContryDialog;
import com.aliyun.ayland.widget.ATClearEditText;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATForgetPasswordActivity extends ATBaseActivity implements ATMainContract.View, OnClickListener {
    private static final int MSG_TIME_CLICK = 0x1001;
    private ATMainPresenter mPresenter;
    private String[] areaCodes;
    private String areaCode = "86";
    private ATChoiseContryDialog areaDialog;
    private String phone = "", code = "", password;
    private int time;
    private ATClearEditText cetPhone, cetCode, cetPassword, cetComfirmPassword;
    private ATMyTitleBar titlebar;
    private TextView tvAtLeastSix, tvDifferPassword, tvGetCode1, tvGetCode, tvCanNotReceptCode;
    private CheckBox cbPassword, cbComfirmPassword;
    private boolean clickable = true;

    private Handler handlertime = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    --time;
                    if (time == 0) {
                        tvGetCode1.setTextColor(getResources().getColor(R.color._86523C));
                        tvGetCode1.setText(getResources().getString(R.string.at_get_code_again));
                        clickable = true;
                    } else {
                        tvGetCode1.setText(String.valueOf(time));// 显示计时数字
                        handlertime.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_forget_password;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        cetPhone = findViewById(R.id.cet_phone);
        cetCode = findViewById(R.id.cet_code);
        cetPassword = findViewById(R.id.cet_password);
        cetComfirmPassword = findViewById(R.id.cet_comfirm_password);
        tvGetCode1 = findViewById(R.id.tv_getcode);
        tvAtLeastSix = findViewById(R.id.tv_at_least_six);
        tvDifferPassword = findViewById(R.id.tv_differ_password);
        tvGetCode = findViewById(R.id.tv_get_code);
        cbPassword = findViewById(R.id.cb_password);
        cbComfirmPassword = findViewById(R.id.cb_comfirm_password);
        tvCanNotReceptCode = findViewById(R.id.tv_can_not_recept_code);

        tvCanNotReceptCode.setOnClickListener(this);
        tvGetCode1.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        findViewById(R.id.btn_sure).setOnClickListener(this);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void init() {
        titlebar.setTitle(getIntent().getStringExtra("title"));

        cetPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (cetPassword.getText().length() < 6)
                    tvAtLeastSix.setVisibility(View.VISIBLE);
                else
                    tvAtLeastSix.setVisibility(View.INVISIBLE);
            }
        });
        cetComfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!isSamePassword())
                    tvDifferPassword.setVisibility(View.VISIBLE);
                else
                    tvDifferPassword.setVisibility(View.INVISIBLE);
            }
        });
        cbPassword.setOnCheckedChangeListener((arg0, isCheck) -> {
            if (isCheck)
                cetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            else
                cetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            cetPassword.setSelection(cetPassword.length());
        });
        cbComfirmPassword.setOnCheckedChangeListener((arg0, isCheck) -> {
            if (isCheck)
                cetComfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            else
                cetComfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            cetComfirmPassword.setSelection(cetComfirmPassword.length());
        });
    }

    private boolean isSamePassword() {
        return cetPassword.getText().toString().equals(cetComfirmPassword.getText().toString());
    }

    private String getPhone() {
        return cetPhone.getText().toString().trim().replace(" ", "");
    }

    private String getCode() {
        return cetCode.getText().toString().trim();
    }

    private String getPassword() {
        return cetPassword.getText().toString().trim();
    }

    private boolean isPassword() {
        if (TextUtils.isEmpty(getPassword()) || getPassword().length() > 16 || getPassword().length() < 6) {
            showToast(getResources().getString(R.string.at_text_hint_import_password));
            tvAtLeastSix.setVisibility(View.VISIBLE);
            return false;
        }
        tvAtLeastSix.setVisibility(View.INVISIBLE);
        if (!isPassword(getPassword())) {
            showToast(getResources().getString(R.string.at_text_hint_password_not_china));
            return false;
        }
        return true;
    }

    protected boolean isPassword(String passWord) {
        boolean flag;
        try {
            Pattern p = Pattern.compile("[^\u4e00-\u9fa5]+");
            Matcher m = p.matcher(passWord);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    private void resetPasswd() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("passwd", password);
        jsonObject.put("phoneCode", code);
        jsonObject.put("phone", phone);
        mPresenter.request(ATConstants.Config.SERVER_URL_RESETPASSWD, jsonObject);
    }

    private void getMpasswdcode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("areacode", areaCode);
        jsonObject.put("codetype", "cifi");
        mPresenter.request(ATConstants.Config.SERVER_URL_GETMPASSWDCODE, jsonObject);
    }

    /**
     * 验证手机格式
     */
    private boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    private void setCodeText() {
        time = 60;
        tvGetCode1.setText(String.valueOf(time));
        tvGetCode1.setTextColor(getResources().getColor(R.color._A5ACB8));
        handlertime.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlertime.removeCallbacksAndMessages(null);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_RESETPASSWD:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_change_password_success));
                        setResult(RESULT_OK, new Intent().putExtra("phone", phone));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_GETMPASSWDCODE:
                        showToast(getString(R.string.at_get_verification_code_success));
                        closeBaseProgressDlg();
                        clickable = false;
                        cetCode.setVisibility(View.VISIBLE);
                        tvGetCode1.setVisibility(View.VISIBLE);
                        tvGetCode.setVisibility(View.GONE);
                        tvCanNotReceptCode.setVisibility(View.VISIBLE);
                        cetCode.requestFocus();
                        setCodeText();
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sure) {
            if (isMobileNO(getPhone()) && isPassword()) {
                if (!isSamePassword()) {
                    tvDifferPassword.setVisibility(View.VISIBLE);
                    return;
                } else {
                    tvDifferPassword.setVisibility(View.INVISIBLE);
                }
                if (!TextUtils.isEmpty(cetCode.getText().toString())) {
                    resetPasswd();
                } else {
                    showToast(getString(R.string.at_the_code_cannot_be_empty));
                }
            }
        } else if (i == R.id.tv_get_code || i == R.id.tv_getcode) {
            if (!ATNetWorkUtils.isNetworkAvailable(ATForgetPasswordActivity.this)) {
                showToast(getString(R.string.at_net_work_wrong));
                return;
            }
            if (isMobileNO(getPhone())) {
                clickable = false;
                getMpasswdcode();
            } else {
                showToast(getString(R.string.at_text_phone_error1));
            }
        }
    }
}