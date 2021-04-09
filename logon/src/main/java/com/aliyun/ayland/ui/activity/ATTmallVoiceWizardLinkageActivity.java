package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATSceneBean;
import com.aliyun.ayland.data.ATTmallSceneBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import static com.aliyun.ayland.ui.fragment.ATLinkageFragment.REQUEST_CODE_EDIT_LINKAGE;

public class ATTmallVoiceWizardLinkageActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private TextView tvName, tvLinkage, textView;
    private Button button;
    private ImageView img;
    private RelativeLayout rlContent;
    private ATTmallSceneBean mATTmallSceneBean;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_tmall_voice_wizard_linkage;
    }

    @Override
    protected void findView() {
        tvName = findViewById(R.id.tv_name);
        tvLinkage = findViewById(R.id.tv_linkage);
        textView = findViewById(R.id.textView);
        img = findViewById(R.id.img);
        button = findViewById(R.id.button);
        rlContent = findViewById(R.id.rl_content);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void updateSceneTmallRelation() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        jsonObject.put("sceneName", mATTmallSceneBean.getSceneName());
        jsonObject.put("sceneId", mATTmallSceneBean.getSceneId());
        jsonObject.put("updateType", mATTmallSceneBean.getRelationStatus() == 1 ? 0 : 1);
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATESCENETMALLRELATION, jsonObject);
    }

    private void init() {
        mATTmallSceneBean = getIntent().getParcelableExtra("ATTmallSceneBean");
        tvName.setText(mATTmallSceneBean.getSceneName());
        textView.setText(mATTmallSceneBean.getSceneName());
        switch (mATTmallSceneBean.getSceneName()) {
            case "起床模式":
                img.setImageResource(R.drawable.at_home_ld_cqzm);
                break;
            case "回家模式":
                img.setImageResource(R.drawable.at_home_ld_hjms);
                break;
            case "离家模式":
                img.setImageResource(R.drawable.at_home_ld_ljms);
                break;
            case "睡眠模式":
                img.setImageResource(R.drawable.at_home_ld_smms);
                break;
            case "烹饪模式":
                img.setImageResource(R.drawable.at_home_ld_plms);
                break;
            case "娱乐模式":
                img.setImageResource(R.drawable.at_home_ld_ylms);
                break;
        }
        rlContent.setOnClickListener(v -> startActivityForResult(new Intent(this, ATLinkageAddActivity1.class)
                .putExtra("sceneId", mATTmallSceneBean.getSceneId()), REQUEST_CODE_EDIT_LINKAGE));
        initStatus();
        button.setOnClickListener(v -> {
            updateSceneTmallRelation();
        });
    }

    private void initStatus() {
        if (mATTmallSceneBean.getRelationStatus() == 1) {
            tvLinkage.setText(getString(R.string.at_the_associated_linkage));
            button.setText(getString(R.string.at_cancel_associated));
            button.setBackground(getResources().getDrawable(R.drawable.shape_72px_f57066_to_e9575b));
        } else {
            tvLinkage.setText(getString(R.string.at_the_query_linkage));
            button.setText(getString(R.string.at_associate));
            button.setBackground(getResources().getDrawable(R.drawable.selector_72px_ffb176fda448_efcdaaeeb67d));
        }
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_UPDATESCENETMALLRELATION:
                        showToast(String.format(getString(R.string.at_success_), button.getText().toString()));
                        mATTmallSceneBean.setRelationStatus(mATTmallSceneBean.getRelationStatus() == 1 ? 0 : 1);
                        initStatus();
                        setResult(RESULT_OK);
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