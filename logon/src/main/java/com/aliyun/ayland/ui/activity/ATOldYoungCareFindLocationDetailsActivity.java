package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATCaringRecordBean;
import com.aliyun.ayland.data.ATLastCaringRecordBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

public class ATOldYoungCareFindLocationDetailsActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATLastCaringRecordBean mATLastCaringRecordBean;
    private Button btnCheckMonitoring;
    private SmartRefreshLayout smartRefreshLayout;
    private TextView tvLocation, tvDate, tvContent;
    private ImageView imgCamera;
    private int type;
    private String personCode, iotId;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_pic_kongkongruye)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_old_young_care_find_location_details;
    }

    @Override
    protected void findView() {
        btnCheckMonitoring = findViewById(R.id.btn_check_monitoring);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        imgCamera = findViewById(R.id.img_camera);
        tvLocation = findViewById(R.id.tv_location);
        tvContent = findViewById(R.id.tv_content);
        tvDate = findViewById(R.id.tv_date);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        if (type == 0)
            findLastCaringRecordByPersonCode();
    }

    private void findLastCaringRecordByPersonCode() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", personCode);
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDLASTCARINGRECORDBYPERSONCODE, jsonObject);
    }

    private void queryLiveStreaming() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iotId", iotId);
        mPresenter.request(ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING, jsonObject);
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            personCode = getIntent().getStringExtra("personCode");
            smartRefreshLayout.setEnableRefresh(true);
        } else {
            ATCaringRecordBean aTCaringRecordBean = getIntent().getParcelableExtra("ATCaringRecordBean");
            iotId = aTCaringRecordBean.getIotId();
            tvLocation.setText(aTCaringRecordBean.getAddress());
            tvDate.setText(aTCaringRecordBean.getEventTime());
            tvContent.setText(String.format(getString(R.string.at_last_location_), aTCaringRecordBean.getPersonName()));
            Glide.with(this).load(aTCaringRecordBean.getPicUrl()).apply(options).into(imgCamera);
            smartRefreshLayout.setEnableRefresh(false);
        }
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findLastCaringRecordByPersonCode();
        });
        btnCheckMonitoring.setOnClickListener(view -> {
            if (type == 0 && mATLastCaringRecordBean == null) {
                findLastCaringRecordByPersonCode();
            }else
                queryLiveStreaming();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDLASTCARINGRECORDBYPERSONCODE:
                        if (jsonResult.has("data")) {
                            mATLastCaringRecordBean = gson.fromJson(jsonResult.getString("data"), ATLastCaringRecordBean.class);
                            tvLocation.setText(mATLastCaringRecordBean.getAddress());
                            tvDate.setText(mATLastCaringRecordBean.getEventTime());
                            tvContent.setText(String.format(getString(R.string.at_last_location_), mATLastCaringRecordBean.getPersonName()));
                            iotId = mATLastCaringRecordBean.getIotId();
                            Glide.with(this).load(mATLastCaringRecordBean.getPicUrl()).apply(options).into(imgCamera);
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_QUERYLIVESTREAMING:
                        String path = jsonResult.getJSONObject("data").getString("path");
                        startActivity(new Intent(this, ATIntelligentMonitorActivity.class)
                                .putExtra("path", path)
                                .putExtra("iotId", iotId));
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