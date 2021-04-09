package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATFamilyManageAdviceRoomRVAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyManageAdviceActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATFamilyManageAdviceRoomRVAdapter mATFamilyManageAdviceRoomRVAdapter;
    private List<ATRoomBean1> mRoomList = new ArrayList<>();
    private String account;
    private int householdType = 102;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private Button btnSubscribe;
    private EditText etVisitorPhone;
    private RadioGroup rgIdentity;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_family_manage_advice;
    }

    @Override
    protected void findView() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        btnSubscribe = findViewById(R.id.btn_subscribe);
        etVisitorPhone = findViewById(R.id.et_visitor_phone);
        rgIdentity = findViewById(R.id.rg_identity);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        findOrderRoom();
    }

    private void findOrderRoom() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDORDERROOM, jsonObject);
    }

    private void invitePerson() {
        showBaseProgressDlg();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("account", account);
        jsonObject.put("householdType", householdType);
        JSONArray roomList = new JSONArray();
        for (Integer integer : mATFamilyManageAdviceRoomRVAdapter.getCheckSet()) {
            roomList.add(mRoomList.get(integer).getRoomCode());
        }
        jsonObject.put("roomList", roomList);
        mPresenter.request(ATConstants.Config.SERVER_URL_INVITEPERSON, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATFamilyManageAdviceRoomRVAdapter = new ATFamilyManageAdviceRoomRVAdapter(this);
        recyclerView.setAdapter(mATFamilyManageAdviceRoomRVAdapter);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findOrderRoom();
        });
        btnSubscribe.setOnClickListener(view -> {
            account = etVisitorPhone.getText().toString();
            if (!TextUtils.isEmpty(account) && !isMobileNO(account)) {
                showToast(getString(R.string.at_input_correct_phone));
            }else if(mATFamilyManageAdviceRoomRVAdapter.getCheckSet().size() == 0){
                showToast(getString(R.string.at_please_choose_at_least_one_room));
            }else {
                invitePerson();
            }
        });
        rgIdentity.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_householdtype102) {
                householdType = 102;
            } else {
                householdType = 104;
            }
        });
    }

    /**
     * 验证手机格式
     */
    private boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_INVITEPERSON:
                        showToast(getString(R.string.at_begin_advice_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_FINDORDERROOM:
                        mRoomList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATRoomBean1>>() {
                        }.getType());
                        mATFamilyManageAdviceRoomRVAdapter.setList(mRoomList);
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
