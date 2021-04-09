package com.aliyun.ayland.ui.activity;


import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;

import org.json.JSONException;

public class ATTmallWizardBindingDescriptionActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private TextView tvTip1, tvTip2, tvTip3, tvTip4;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_tmall_wizard_binding_description;
    }

    @Override
    protected void findView() {
        tvTip1 = findViewById(R.id.tv_tip1);
        tvTip2 = findViewById(R.id.tv_tip2);
        tvTip3 = findViewById(R.id.tv_tip3);
        tvTip4 = findViewById(R.id.tv_tip4);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void init() {
        SpannableString s1 = new SpannableString(getString(R.string.at_tmall_wizard_binding_account_description_tip1));
        s1.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s1.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style2), 17, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s1.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 21, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTip1.setText(s1);
        SpannableString s2 = new SpannableString(getString(R.string.at_tmall_wizard_binding_account_description_tip2));
        s2.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s2.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style2), 16, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s2.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 18, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTip2.setText(s2);
        SpannableString s3 = new SpannableString(getString(R.string.at_tmall_wizard_binding_account_description_tip3));
        s3.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s3.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style2), 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s3.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 9, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTip3.setText(s3);
        SpannableString s4 = new SpannableString(getString(R.string.at_tmall_wizard_binding_account_description_tip4));
        s4.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s4.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style2), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s4.setSpan(new TextAppearanceSpan(this, R.style.tmall_tip_text_style1), 7, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTip4.setText(s4);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:

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
