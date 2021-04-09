package com.aliyun.ayland.ui.activity;

import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATLoginPresenter;
import com.aliyun.ayland.utils.ATNetWorkUtils;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.ayland.widget.ATClearEditText;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATRegistActivity extends ATBaseActivity implements ATMainContract.View, OnClickListener {
    private ATLoginPresenter mPresenter;
    private ATClearEditText cetPhone, cetPassword, cetComfirmPassword;
    private Button btnGetCode;
    private EditText cetCode;
    private ATMyTitleBar titlebar;
    private TextView tvAtLeastSix, tvDifferPassword, tvGetCode, tvCanNotReceptCode;
    private CheckBox cbPassword, cbComfirmPassword;
    private RelativeLayout rlContent, rlPhone, rlCode, rlPassword, rlComfirmPassword;
    private int time = 60;
    private Handler handlertime = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    --time;
                    if (time == 0) {
//                        btnGetCode.setTextColor(getResources().getColor(R.color._86523C));
                        btnGetCode.setText(getResources().getString(R.string.at_get_code_again));
                        rlCode.setClickable(true);
                    } else {
                        btnGetCode.setText(String.valueOf(time));// 显示计时数字
                        handlertime.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void initPresenter() {
        mPresenter = new ATLoginPresenter(this);
        mPresenter.install(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_regist;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        cetPhone = findViewById(R.id.cet_phone);
        cetCode = findViewById(R.id.cet_code);
        cetPassword = findViewById(R.id.cet_password);
        cetComfirmPassword = findViewById(R.id.cet_comfirm_password);
        btnGetCode = findViewById(R.id.btn_getcode);
        rlContent = findViewById(R.id.rl_content);
        rlPhone = findViewById(R.id.rl_phone);
        rlCode = findViewById(R.id.rl_code);
        rlPassword = findViewById(R.id.rl_password);
        rlComfirmPassword = findViewById(R.id.rl_comfirm_password);
        tvAtLeastSix = findViewById(R.id.tv_at_least_six);
        tvDifferPassword = findViewById(R.id.tv_differ_password);
        tvGetCode = findViewById(R.id.tv_get_code);
        cbPassword = findViewById(R.id.cb_password);
        cbComfirmPassword = findViewById(R.id.cb_comfirm_password);
        tvCanNotReceptCode = findViewById(R.id.tv_can_not_recept_code);

        tvCanNotReceptCode.setOnClickListener(this);
        rlCode.setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        init();
    }

    private void init() {
        ATSystemStatusBarUtils.init(this, false);
        titlebar.setBackImage(R.drawable.ioc_back_w);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;
        FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) rlContent.getLayoutParams();
        params.width=FrameLayout.LayoutParams.MATCH_PARENT;
        params.height=y;
        rlContent.setLayoutParams(params);

        cetPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rlPassword.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_ffffff));
            } else {
                if (cetPassword.getText().length() < 6)
                    tvAtLeastSix.setVisibility(View.VISIBLE);
                else
                    tvAtLeastSix.setVisibility(View.INVISIBLE);
                rlPassword.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_99ffffff));
            }
        });
        cetComfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rlComfirmPassword.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_ffffff));
            } else {
                if (!isSamePassword())
                    tvDifferPassword.setVisibility(View.VISIBLE);
                else
                    tvDifferPassword.setVisibility(View.INVISIBLE);
                rlComfirmPassword.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_99ffffff));
            }
        });
        cetCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rlCode.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_ffffff));
            } else {
                rlCode.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_99ffffff));
            }
        });
        cetPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rlPhone.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_ffffff));
            } else {
                rlPhone.setBackground(getResources().getDrawable(R.drawable.shape_81px3px_99ffffff));
            }
        });
        cbPassword.setOnCheckedChangeListener((arg0, isCheck) -> {
            if (isCheck)
                cetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            else
                cetPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        });
        cbComfirmPassword.setOnCheckedChangeListener((arg0, isCheck) -> {
            if (isCheck)
                cetComfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            else
                cetComfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        });
    }

    private void codeButtonRefresh() {
        time = 60;
        btnGetCode.setText(String.valueOf(time));
//        btnGetCode.setTextColor(getResources().getColor(R.color._A5ACB8));
        handlertime.sendEmptyMessageDelayed(1, 1000);
    }

    private void codeToRegister() {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("phone", getPhone());
//        jsonObject.put("areacode", "86");
//        jsonObject.put("type", "cifi");
        mPresenter.request(ATConstants.Config.SERVER_URL_CODETOREGISTER, jsonObject);
    }

    private void registerClient() {
        showBaseProgressDlg();
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("phone", getPhone());
        jsonObject.put("username", getPhone());
        jsonObject.put("password", getPassword());
//        jsonObject.put("type", "default");
        jsonObject.put("userType", "1");
        jsonObject.put("nickName", "");
        jsonObject.put("phoneCode", "123");
//        jsonObject.put("phoneCode", getCode());
//        jsonObject.put("phone", getPhone());
        mPresenter.request(ATConstants.Config.SERVER_URL_REGISTERCLIENT, jsonObject);
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

    protected boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
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
                    case ATConstants.Config.SERVER_URL_CODETOREGISTER:
                        tvGetCode.setVisibility(View.GONE);
                        rlCode.setClickable(false);
                        cetCode.setVisibility(View.VISIBLE);
                        btnGetCode.setVisibility(View.VISIBLE);
                        showToast(getString(R.string.at_get_verification_code_success));
                        closeBaseProgressDlg();
                        tvCanNotReceptCode.setVisibility(View.VISIBLE);
                        cetCode.requestFocus();
                        codeButtonRefresh();
                        break;
                    case ATConstants.Config.SERVER_URL_REGISTERCLIENT:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_regist_success));
                        finish();
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
        if (i == R.id.rl_code) {
            if (!ATNetWorkUtils.isNetworkAvailable(ATRegistActivity.this)) {
                showToast(getString(R.string.at_net_work_wrong));
                return;
            }
            if (isMobileNO(getPhone())) {
                codeToRegister();
                rlCode.setClickable(false);
            } else {
                showToast(getString(R.string.at_text_phone_error));
            }
        } else if (i == R.id.btn_next) {
            if (isMobileNO(getPhone()) && isPassword()) {
                if (!isSamePassword()) {
                    tvDifferPassword.setVisibility(View.VISIBLE);
                    return;
                } else {
                    tvDifferPassword.setVisibility(View.INVISIBLE);
                }
                if (!TextUtils.isEmpty(cetCode.getText().toString())) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInput(cetComfirmPassword, InputMethodManager.SHOW_FORCED);
                    registerClient();
                } else {
                    showToast(R.string.at_the_code_cannot_be_empty);
                }
            }
        }
    }
}