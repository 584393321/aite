package com.aliyun.ayland.ui.activity;

import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATParkSpaceBean;
import com.aliyun.ayland.data.ATParkingBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.ATSemicircleProgressView;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

public class ATVehicleCheckDetailActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATParkingBean mATParkingBean;
    private ATSemicircleProgressView semicircleProgressView;
    private TextView tvAll, tvParkName, tvParkCode, tvBuyOutSpace, tvPublicSpace, tvTitleSpace, tvCurrent, tvEmptyVehicle;
    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_vehicle_check_detail;
    }

    @Override
    protected void findView() {
        tvAll = findViewById(R.id.tv_all);
        tvParkName = findViewById(R.id.tv_park_name);
        tvParkCode = findViewById(R.id.tv_park_code);
        tvBuyOutSpace = findViewById(R.id.tv_buy_out_space);
        tvPublicSpace = findViewById(R.id.tv_public_space);
        tvTitleSpace = findViewById(R.id.tv_title_space);
        tvCurrent = findViewById(R.id.tv_current);
        tvEmptyVehicle = findViewById(R.id.tv_empty_vehicle);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        semicircleProgressView = findViewById(R.id.semicircleProgressView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        mATParkingBean = getIntent().getParcelableExtra("parkingBean");
        if (!TextUtils.isEmpty(mATParkingBean.getParkcode())) {
            tvAll.setText("/" + mATParkingBean.getTotalspace());
            tvParkName.setText(mATParkingBean.getParkname());
            tvParkCode.setText(String.format(getString(R.string.at_number_), mATParkingBean.getParkcode()));
            tvBuyOutSpace.setText(mATParkingBean.getBuyoutspace());
            tvPublicSpace.setText(mATParkingBean.getPublicspace());
            tvTitleSpace.setText(mATParkingBean.getTotalspace());
            findSpace();
        }
    }

    private void findSpace() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parkCode", mATParkingBean.getParkcode());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDSPACE, jsonObject);
    }

    private void init() {
        ATAutoUtils.autoSize(semicircleProgressView);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findSpace();
        });
//        arcSeekBar.setOnProgressChangeListener(new ArcSeekBar.OnProgressChangeListener() {
//            @Override
//            public void onProgressChanged(ArcSeekBar seekBar, int progress, boolean isUser) {}
//
//            @Override
//            public void onStartTrackingTouch(ArcSeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(ArcSeekBar seekBar) {}
//        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDSPACE:
                        ATParkSpaceBean ATParkSpaceBean = gson.fromJson(jsonResult.getString("data"), new TypeToken<ATParkSpaceBean>() {
                        }.getType());
                        semicircleProgressView.setSesameValues(Integer.parseInt(ATParkSpaceBean.getCurrent_space()), Integer.parseInt(mATParkingBean.getTotalspace()));
                        tvCurrent.setText(ATParkSpaceBean.getCurrent_space());
                        tvEmptyVehicle.setText(ATParkSpaceBean.getCurrent_space());
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
