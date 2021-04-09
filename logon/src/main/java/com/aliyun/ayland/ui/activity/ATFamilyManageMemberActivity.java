package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATFamilyManageRoomBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATFamilyManageMemberRoomRVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyManageMemberActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int REQUEST_CODE_EDIT = 1001;
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private ATFamilyManageMemberRoomRVAdapter mFamilyManageRoomRVAdapter;
    private List<ATFamilyManageRoomBean> mAllRoomList = new ArrayList<>();
    private String memberPersonCode, householdtype;
    private int current_position, status;
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rvRoom;
    private TextView tvName, tvIdentity;
    private Dialog dialog, dialogChange;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_family_manage_member;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        rvRoom = findViewById(R.id.rv_room);
        tvName = findViewById(R.id.tv_name);
        tvIdentity = findViewById(R.id.tv_identity);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        memberRoom();
    }

    private void changeView() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("memberPersonCode", memberPersonCode);
        jsonObject.put("roomCode", mAllRoomList.get(current_position).getRoomCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getATLoginBean().getPersonCode());
        operator.put("hidType", "OPEN");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("status", status);
        mPresenter.request(ATConstants.Config.SERVER_URL_CHANGEVIEW, jsonObject);
    }

    private void updateMemberIdentity() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("memberPersonCode", memberPersonCode);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("householdType", householdtype);
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEMEMBERIDENTITY, jsonObject);
    }

    private void deleteMember() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("memberPersonCode", memberPersonCode);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getATLoginBean().getPersonCode());
        operator.put("hidType", "OPEN");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_DELETEMEMBER, jsonObject);
    }

    private void memberRoom() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("memberPersonCode", memberPersonCode);
        mPresenter.request(ATConstants.Config.SERVER_URL_MEMBERROOM, jsonObject);
    }

    private void init() {
        memberPersonCode = getIntent().getStringExtra("memberPersonCode");
        householdtype = getIntent().getStringExtra("householdtype");
        String nickname = getIntent().getStringExtra("nickname");
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        tvName.setText(nickname);
        tvIdentity.setText(ATResourceUtils.getResIdByName(String.format(getString(R.string.at_householdtype_), householdtype), ATResourceUtils.ResourceType.STRING));
        tvIdentity.setOnClickListener(v -> {
            dialogChange.show();
        });
        titlebar.setSendText(getString(R.string.at_delete));
        titlebar.setPublishClickListener(() -> dialog.show());
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            memberRoom();
        });
        rvRoom.setLayoutManager(new LinearLayoutManager(this));
        mFamilyManageRoomRVAdapter = new ATFamilyManageMemberRoomRVAdapter(this);
        rvRoom.setAdapter(mFamilyManageRoomRVAdapter);
        mFamilyManageRoomRVAdapter.setOnItemClickListener((view, o, position) -> {
            current_position = position;
            status = (int) o;
            changeView();
        });
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_normal, null, false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(getString(R.string.at_sure_delete));
        view.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> dialog.dismiss());
        view.findViewById(R.id.tv_sure).setOnClickListener(view2 -> deleteMember());
        dialog.setContentView(view);

        dialogChange = new Dialog(this, R.style.nameDialog);
        View view1 = LayoutInflater.from(this).inflate(R.layout.at_dialog_change_person_type, null, false);
        view1.findViewById(R.id.tv_member).setOnClickListener(view2 -> {
            householdtype = "102";
            updateMemberIdentity();
        });
        view1.findViewById(R.id.tv_renter).setOnClickListener(view2 -> {
            householdtype = "104";
            updateMemberIdentity();
        });
        dialogChange.setContentView(view1);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_UPDATEMEMBERIDENTITY:
                        dialog.dismiss();
                        showToast("切换成功");
                        tvIdentity.setText(ATResourceUtils.getResIdByName(String.format(getString(R.string.at_householdtype_), householdtype)
                                , ATResourceUtils.ResourceType.STRING));
                        setResult(RESULT_OK);
                        break;
                    case ATConstants.Config.SERVER_URL_DELETEMEMBER:
                        showToast(getString(R.string.at_delete_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_CHANGEVIEW:
                        mAllRoomList.get(current_position).setCanSee(status);
                        mFamilyManageRoomRVAdapter.notifyItem(current_position, status);
                        break;
                    case ATConstants.Config.SERVER_URL_MEMBERROOM:
                        List<ATFamilyManageRoomBean> mNotSeeRoomList = gson.fromJson(jsonResult.getJSONObject("room").getString("notSee"), new TypeToken<List<ATFamilyManageRoomBean>>() {
                        }.getType());
                        List<ATFamilyManageRoomBean> mCanSeeRoomList = gson.fromJson(jsonResult.getJSONObject("room").getString("canSee"), new TypeToken<List<ATFamilyManageRoomBean>>() {
                        }.getType());
                        mAllRoomList.clear();
                        for (ATFamilyManageRoomBean ATFamilyManageRoomBean : mCanSeeRoomList) {
                            ATFamilyManageRoomBean.setCanSee(1);
                            mAllRoomList.add(ATFamilyManageRoomBean);
                        }
                        for (ATFamilyManageRoomBean ATFamilyManageRoomBean : mNotSeeRoomList) {
                            ATFamilyManageRoomBean.setCanSee(0);
                            mAllRoomList.add(ATFamilyManageRoomBean);
                        }
                        mFamilyManageRoomRVAdapter.setLists(mAllRoomList);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_EDIT) {
                finish();
                setResult(RESULT_OK);
            }
        }
    }
}