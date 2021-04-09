package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.ATAutoAppBarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoCollapsingToolbarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoToolbar;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATTmallSceneBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATTmallVoiceWizardRVAdapter;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.List;

public class ATTmallVoiceWizardActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private static final int REQUEST_CODE_ASSOCIATED = 1001;
    public static final int REQUEST_CODE_AUTHORIZE = 1002;
    private RecyclerView recyclerView;
    private TextView tvAuthorization, tvBack, tvBind, tvTitle, tvAssociateLinkage, tvAssociateDevice;
    private ATTmallVoiceWizardRVAdapter mATTmallVoiceWizardRVAdapter;
    private Drawable drawable1, drawable2;
    private SmartRefreshLayout smartRefreshLayout;
    private ATAutoToolbar toolbar;
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private RelativeLayout rlContent;
    private ATHouseBean mATHouseBean;
    private String sceneName;
    private List<ATTmallSceneBean> mAtTmallSceneList;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_tmall_voice_wizard;
    }

    @Override
    protected void findView() {
        tvBack = findViewById(R.id.tv_back);
        tvBind = findViewById(R.id.tv_bind);
        tvTitle = findViewById(R.id.tv_title);
        rlContent = findViewById(R.id.rl_content);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        tvAuthorization = findViewById(R.id.tv_authorization);
        tvAssociateLinkage = findViewById(R.id.tv_associate_linkage);
        tvAssociateDevice = findViewById(R.id.tv_associate_device);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void syncInfo(int syncType) {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("syncType", syncType);
        mPresenter.request(ATConstants.Config.SERVER_URL_SYNCINFO, jsonObject);
    }

    private void findSceneTmallList() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDSCENETMALLLIST, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        ATSystemStatusBarUtils.init(this, false);
        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);
        ATAutoUtils.auto(toolbar);
        tvBack.setOnClickListener(view -> finish());
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            rlContent.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange()));
            if (rlContent.getBackground().getAlpha() == 0) {
                ATSystemStatusBarUtils.init(ATTmallVoiceWizardActivity.this, false);
                tvBack.setSelected(false);
                tvBind.setSelected(false);
                tvTitle.setTextColor(getResources().getColor(R.color.white));
            } else if (rlContent.getBackground().getAlpha() == 255) {
                ATSystemStatusBarUtils.init(ATTmallVoiceWizardActivity.this, true);
                tvBack.setSelected(true);
                tvBind.setSelected(true);
                tvTitle.setTextColor(getResources().getColor(R.color._333333));
            }
        });

        drawable1 = getResources().getDrawable(R.drawable.at_ico_tmjl_yisq);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());

        drawable2 = getResources().getDrawable(R.drawable.atico_junp_w);
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());

        tvAuthorization.setOnClickListener(v ->
                startActivity(new Intent(this, ATTmallWizardActivity.class)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setNestedScrollingEnabled(false);
        mATTmallVoiceWizardRVAdapter = new ATTmallVoiceWizardRVAdapter(this);
        recyclerView.setAdapter(mATTmallVoiceWizardRVAdapter);
        mATTmallVoiceWizardRVAdapter.setOnItemClickListener((view, o, position) -> {
            if (!TextUtils.isEmpty(((ATTmallSceneBean) o).getSceneId()))
                startActivityForResult(new Intent(ATTmallVoiceWizardActivity.this, ATTmallVoiceWizardLinkageActivity.class)
                        .putExtra("ATTmallSceneBean", (ATTmallSceneBean) o), REQUEST_CODE_ASSOCIATED);
//            else
//                startActivity(new Intent(ATTmallVoiceWizardActivity.this, ATLinkageRecommendActivity.class)
//                        .putExtra("sceneName", ((ATTmallSceneBean) o).getSceneName()));
        });
        getBind();

        tvBind.setOnClickListener(v -> startActivity(new Intent(this, ATTmallWizardBindingDescriptionActivity.class)));
        tvAssociateDevice.setOnClickListener(v -> syncInfo(1));
        tvAssociateLinkage.setOnClickListener(v -> syncInfo(2));

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findSceneTmallList();
        });
    }

    public void getBind() {
        IoTRequest ioTRequest = new IoTRequestBuilder()
                .setAuthType("iotAuth")
                .setApiVersion("1.0.5")
                .setPath("/account/thirdparty/get")
                .addParam("accountType", "TAOBAO")
                .setScheme(Scheme.HTTPS)
                .build();
        new IoTAPIClientFactory().getClient().send(ioTRequest, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                Log.e("onResponse: ", e.getMessage());
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ThreadPool.MainThreadHandler.getInstance().post(() -> {
                    if (ioTResponse.getData() != null && TextUtils.isEmpty(ioTResponse.getData().toString())) {
                        tvAuthorization.setText(getString(R.string.at_unauthorized));
                        tvAuthorization.setCompoundDrawables(null, null, drawable2, null);
                    } else {
                        tvAuthorization.setText(getString(R.string.at_authorized));
                        tvAuthorization.setCompoundDrawables(drawable1, null, drawable2, null);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ASSOCIATED) {
//            mATVoiceWizardBean.setAssociated(!mATVoiceWizardBean.isAssociated());
//            if (mATVoiceWizardBean.isAssociated()) {
//                for (int i = 0; i < notAssociatedList.size(); i++) {
//                    if (notAssociatedList.get(i).getName().equals(mATVoiceWizardBean.getName())) {
//                        notAssociatedList.remove(i);
//                    }
//                }
//                associatedList.add(0, mATVoiceWizardBean);
//            } else {
//                for (int i = 0; i < associatedList.size(); i++) {
//                    if (associatedList.get(i).getName().equals(mATVoiceWizardBean.getName())) {
//                        associatedList.remove(i);
//                    }
//                }
//                notAssociatedList.add(0, mATVoiceWizardBean);
//            }
//            allList.clear();
//            allList.addAll(associatedList);
//            allList.addAll(notAssociatedList);
//            mATTmallVoiceWizardRVAdapter.setLists(allList);
//            recyclerView.scrollToPosition(0);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AUTHORIZE) {
            getBind();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        findSceneTmallList();
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_SYNCINFO:
                        showToast(getString(R.string.at_associate_success));
                        break;
                    case ATConstants.Config.SERVER_URL_FINDSCENETMALLLIST:
                        mAtTmallSceneList = gson.fromJson(jsonResult.getString("result"), new TypeToken<List<ATTmallSceneBean>>() {
                        }.getType());
                        mATTmallVoiceWizardRVAdapter.setLists(mAtTmallSceneList);
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}