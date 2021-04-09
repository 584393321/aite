package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATCarBean;
import com.aliyun.ayland.data.ATCarListBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATLinkageCarRVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageCarAccessActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATLinkageCarRVAdapter mParkCarRVAdapter;
    private List<ATCarListBean> mCarList = new ArrayList<>();
    private int car_position = -1, direction = 3;
    private List<String> bizTypeList = new ArrayList<>();
    private boolean replace;
    private JSONObject params = new JSONObject();
    private ATMyTitleBar titlebar;
    private RecyclerView rvVehicleName;
    private RelativeLayout rlIn, rlOut, rlInOut;
    private CheckBox cbIn, cbOut, cbInOut;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_access_car;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        rvVehicleName = findViewById(R.id.rv_vehicle_name);
        rlIn = findViewById(R.id.rl_in);
        rlOut = findViewById(R.id.rl_out);
        rlInOut = findViewById(R.id.rl_in_out);
        cbIn = findViewById(R.id.cb_in);
        cbOut = findViewById(R.id.cb_out);
        cbInOut = findViewById(R.id.cb_in_out);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        list();
    }

    private void list() {
        ATHouseBean mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        if (mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
//        jsonObject.put("status", 1);
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 50);
        mPresenter.request(ATConstants.Config.SERVER_URL_CARPASSLIST, jsonObject);
    }

    private void init() {
        replace = getIntent().getBooleanExtra("replace", false);
        if (replace) {
            params = JSONObject.parseObject(getIntent().getStringExtra("params"));
            direction = params.getInteger("direction");
        }
        rlIn.setOnClickListener(view -> {
            direction = 1;
            cbIn.setChecked(true);
            cbOut.setChecked(false);
            cbInOut.setChecked(false);
        });
        rlOut.setOnClickListener(view -> {
            direction = 2;
            cbIn.setChecked(false);
            cbOut.setChecked(true);
            cbInOut.setChecked(false);
        });
        rlInOut.setOnClickListener(view -> {
            direction = 3;
            cbIn.setChecked(false);
            cbOut.setChecked(false);
            cbInOut.setChecked(true);
        });
        switch (direction){
            case 1:
                cbIn.setChecked(true);
                cbOut.setChecked(false);
                cbInOut.setChecked(false);
                break;
            case 2:
                cbIn.setChecked(false);
                cbOut.setChecked(true);
                cbInOut.setChecked(false);
                break;
            case 3:
                cbIn.setChecked(false);
                cbOut.setChecked(false);
                cbInOut.setChecked(true);
                break;
        }
        rvVehicleName.setLayoutManager(new LinearLayoutManager(this));
        mParkCarRVAdapter = new ATLinkageCarRVAdapter(this);
        rvVehicleName.setAdapter(mParkCarRVAdapter);
        mParkCarRVAdapter.setOnItemClickListener((view, position) -> car_position = position);

        titlebar.setSendText(getString(R.string.at_done));
        titlebar.setPublishClickListener(() -> {
            if (mCarList.size() == 0) {
                showToast(getString(R.string.at_choose_vehicle_park_device));
                return;
            }

            params.put("direction", direction);
            params.put("licence", mCarList.get(car_position).getPlateNumber());
            params.put("carParkName", mCarList.get(car_position).getParkName());
            params.put("carParkCode", mCarList.get(car_position).getSpaceId());

            String content = mCarList.get(car_position).getParkName() + " " + ATResourceUtils.getString(
                    ATResourceUtils.getResIdByName(String.format(getString(R.string.at_car_in_out_), direction), ATResourceUtils.ResourceType.STRING));
            setResult(RESULT_OK, getIntent().putExtra("name", mCarList.get(car_position).getPlateNumber())
                    .putExtra("params", params.toJSONString()).putExtra("uri", "")
                    .putExtra("content", content));
            finish();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_CARPASSLIST:
                        mCarList = gson.fromJson(jsonResult.getJSONObject("data").getString("content"), new TypeToken<List<ATCarListBean>>() {
                        }.getType());
                        int licence_position = 0;
                        if (replace) {
                            String licence = getIntent().getStringExtra("name");
                            for (int i = 0; i < mCarList.size(); i++) {
                                if (licence.equals(mCarList.get(i).getPlateNumber())) {
                                    car_position = i;
                                    break;
                                }
                            }
                        } else {
                            car_position = 0;
                        }
                        mParkCarRVAdapter.setList(mCarList, licence_position);
                        break;
//                    case ATConstants.Config.SERVER_URL_GETUSERLICENCELIST:
//                        mCarList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATCarBean>>() {
//                        }.getType());
//                        int licence_position = 0;
//                        if (replace) {
//                            String licence = getIntent().getStringExtra("name");
//                            for (int i = 0; i < mCarList.size(); i++) {
//                                if (licence.equals(mCarList.get(i).getLicence())) {
//                                    car_position = i;
//                                    break;
//                                }
//                            }
//                        } else {
//                            car_position = 0;
//                        }
//                        mParkCarRVAdapter.setList(mCarList, licence_position);
//                        break;
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