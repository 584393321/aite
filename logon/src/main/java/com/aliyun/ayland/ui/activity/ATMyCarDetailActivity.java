package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATCarListBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATVehicleBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

public class ATMyCarDetailActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private Dialog dialog;
    private ATHouseBean mATHouseBean;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private Button button;
    private TextView tvName, tvPhoneNumber, tvIdNumber, tvCarBuilding, tvLicenseNumber, tvCarModel, tvCarBrand, tvCarColor;
    private ATCarListBean mAtCarListBean;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.athome_bg_fkyy_jiashizheng)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_my_car_detail;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        button = findViewById(R.id.button);
        tvName = findViewById(R.id.tv_name);
        tvIdNumber = findViewById(R.id.tv_id_number);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvCarBuilding = findViewById(R.id.tv_car_building);
        tvLicenseNumber = findViewById(R.id.tv_license_number);
        tvCarModel = findViewById(R.id.tv_vehicle_model);
        tvCarBrand = findViewById(R.id.tv_vehicle_brand);
        tvCarColor = findViewById(R.id.tv_vehicle_color);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    public void delMyCar() {
        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", mAtCarListBean.getId());
        jsonObject.put("defaultParkId", mAtCarListBean.getSpaceId());
        jsonObject.put("IfDelete", 1);
        mPresenter.request(ATConstants.Config.SERVER_URL_ADDCAR, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        mAtCarListBean = getIntent().getParcelableExtra("ATCarListBean");
        if (mAtCarListBean != null) {
            tvName.setText(mAtCarListBean.getUserName());
            tvIdNumber.setText(mAtCarListBean.getIdentityId());
            tvPhoneNumber.setText(mAtCarListBean.getMobile());
            tvCarBuilding.setText(mAtCarListBean.getParkName());
            tvLicenseNumber.setText(mAtCarListBean.getPlateNumber());
            tvCarModel.setText(mAtCarListBean.getCarType());
            tvCarBrand.setText(mAtCarListBean.getBrand());
            tvCarColor.setText(mAtCarListBean.getColor());
        }

        titlebar.setSendText(getString(R.string.at_delete));
        titlebar.setPublishClickListener(this::delMyCar);

        initDialog();

        button.setOnClickListener(view -> dialog.show());
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_ADDCAR:
                        showToast(getString(R.string.at_delete_success));
                        finish();
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

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.at_dialog_bj_pic_xsz);
        if (mAtCarListBean != null)
            Glide.with(this).load(mAtCarListBean.getCarImage()).apply(options).into((ImageView) dialog.findViewById(R.id.ima_xsz));
        dialog.setCanceledOnTouchOutside(true);
    }
}