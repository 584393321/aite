package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATEventIntent;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.interfaces.ATOnBindRVItemClickListener;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATRoomDeviceRVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATEditRoomActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int REQUEST_CODE_CHANGE_ROOM_PIC = 0x1003;
    private ATEditRoomActivity mContext;
    private ATMainPresenter mPresenter;
    private ATRoomBean1 mRoomBean;
    private String room_type, room_name;
    private List<ATDeviceBean> roomDeviceList = new ArrayList<>();
    private ATHouseBean mATHouseBean;
    private boolean addRoom = true;
    private List<ATDeviceBean> houseDeviceList = new ArrayList<>();
//    private List<ATDeviceBean> houseDeviceList1 = new ArrayList<>();
    private JSONObject mAllDevice;
    private JSONArray roomDevice = new JSONArray();
    private ATRoomDeviceRVAdapter mATRoomDeviceRVAdapter;
    private ATRoomDeviceRVAdapter mATRoomDeviceRVAdapter1;
    public int mCurrentPosition = 0;
    private ATMyTitleBar titlebar;
    private RelativeLayout rlRoomType;
    private RecyclerView rvRoomDevice, rvHouseDevice;
    private EditText etRoomName;
    private TextView tvRoomType;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_edit_room;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        rlRoomType = findViewById(R.id.rl_room_type);
        tvRoomType = findViewById(R.id.tv_room_type);
        rvRoomDevice = findViewById(R.id.rv_room_device);
        rvHouseDevice = findViewById(R.id.rv_house_device);
        etRoomName = findViewById(R.id.et_room_name);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        findHouseDev();
    }

    private void findHouseDev() {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("iotSpaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());

        mPresenter.request(ATConstants.Config.SERVER_URL_FINDHOUSEDEV, jsonObject);
    }

    private void unbindDevRoom(String iotId) {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONArray iotIdArr = new JSONArray();
        iotIdArr.add(iotId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceId", mRoomBean.getIotSpaceId());
        jsonObject.put("rootSpaceId", mATHouseBean.getRootSpaceId());
        jsonObject.put("iotIdList", iotIdArr);
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_UNBINDDEVROOM, jsonObject);
    }

    private void bindDevRoom(String iotId) {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONArray iotIdArr = new JSONArray();
        iotIdArr.add(iotId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceId", mRoomBean.getIotSpaceId());
        jsonObject.put("rootSpaceId", mATHouseBean.getRootSpaceId());
        jsonObject.put("iotIdList", iotIdArr);
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_BINDDEVROOM, jsonObject);
    }

    private void createRoom() {
        if (mATHouseBean == null)
            return;
        showBaseProgressDlg();

        JSONObject spaceInfo = new JSONObject();
        spaceInfo.put("rootSpaceId", mATHouseBean.getRootSpaceId());
        spaceInfo.put("name", etRoomName.getText().toString());
        spaceInfo.put("description", "");
        spaceInfo.put("parentId", mATHouseBean.getIotSpaceId());
        spaceInfo.put("roomType", room_type);
        spaceInfo.put("typeCode", "room");
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONArray iotIdArr = new JSONArray();
        for (int i = 0; i < roomDeviceList.size(); i++) {
            iotIdArr.add(roomDeviceList.get(i).getIotId());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("spaceInfo", spaceInfo);
        jsonObject.put("iotIdList", iotIdArr);
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_CREATEROOM, jsonObject);
    }

    private void updateRoom() {
        showBaseProgressDlg();

        JSONObject spaceInfo = new JSONObject();
        spaceInfo.put("spaceId", mRoomBean.getIotSpaceId());
        spaceInfo.put("name", etRoomName.getText().toString());
        spaceInfo.put("roomType", room_type);

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("spaceInfo", spaceInfo);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEROOM, jsonObject);
    }

    private void init() {
        mContext = this;
        if (!TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
            mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        }
        mRoomBean = getIntent().getParcelableExtra("roombean");
        if ((List<ATDeviceBean>) getIntent().getSerializableExtra("deviceBeanList") != null) {
            roomDeviceList.addAll((List<ATDeviceBean>) getIntent().getSerializableExtra("deviceBeanList"));
        }
        String allDeviceData = getIntent().getStringExtra("allDeviceData");
        mAllDevice = JSON.parseObject(allDeviceData);

        if (mRoomBean != null) {
            addRoom = false;
            room_name = mRoomBean.getName();
            room_type = mRoomBean.getType();
        } else {
            room_name = getIntent().getStringExtra("room_name");
            room_type = getIntent().getStringExtra("room_type");
        }

        etRoomName.setText(room_name);
        etRoomName.setSelection(room_name.length());

        rlRoomType.setOnClickListener(view -> startActivityForResult(new Intent(mContext, ATRoomPicActivity.class), REQUEST_CODE_CHANGE_ROOM_PIC));
        tvRoomType.setText(ATResourceUtils.getResIdByName("at_" + room_type, ATResourceUtils.ResourceType.STRING));
//        imgRoomPic.setImageResource(ATResourceUtils.getResIdByName("at_room_at_" + room_type + "_a", ATResourceUtils.ResourceType.DRAWABLE));

        rvRoomDevice.setLayoutManager(new LinearLayoutManager(this));
        mATRoomDeviceRVAdapter = new ATRoomDeviceRVAdapter(this, false);
        mATRoomDeviceRVAdapter.setLists(roomDeviceList);
        rvRoomDevice.setAdapter(mATRoomDeviceRVAdapter);
        mATRoomDeviceRVAdapter.setOnRVClickListener((view, iotId, position) -> {
            mCurrentPosition = position;
            if (addRoom) {
                houseDeviceList.add(roomDeviceList.get(position));
                roomDeviceList.remove(position);
                mATRoomDeviceRVAdapter.setLists(roomDeviceList);
                mATRoomDeviceRVAdapter1.setLists(houseDeviceList);
            } else {
                unbindDevRoom(iotId);
            }
        });

        rvHouseDevice.setLayoutManager(new LinearLayoutManager(this));
        mATRoomDeviceRVAdapter1 = new ATRoomDeviceRVAdapter(this, true);
        rvHouseDevice.setAdapter(mATRoomDeviceRVAdapter1);
        mATRoomDeviceRVAdapter1.setOnRVClickListener(new ATOnBindRVItemClickListener() {
            @Override
            public void onItemClick(View view, String iotId, int position) {
                mCurrentPosition = position;
                if (addRoom) {
                    roomDeviceList.add(houseDeviceList.get(position));
                    houseDeviceList.remove(position);
                    mATRoomDeviceRVAdapter.setLists(roomDeviceList);
                    mATRoomDeviceRVAdapter1.setLists(houseDeviceList);
                } else {
                    bindDevRoom(iotId);
                }
            }
        });
        titlebar.setSendText(getString(R.string.at_sure1));
        titlebar.setPublishClickListener(() -> {
            if (TextUtils.isEmpty(etRoomName.getText().toString())) {
                showToast(getString(R.string.at_input_room_name));
                return;
            }
            if (addRoom) {
                createRoom();
            } else {
                updateRoom();
            }
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_CREATEROOM:
                        String iotSpaceId = jsonResult.getString("iotSpaceId");
                        roomDevice = new JSONArray();
                        roomDevice.addAll(roomDeviceList);
                        mAllDevice.put(iotSpaceId, roomDevice);
                        showToast(getString(R.string.at_add_room_success));
                        EventBus.getDefault().post(new ATEventIntent("ATManageRoomActivity", new Intent().putExtra("room_name", etRoomName.getText().toString())
                                .putExtra("room_type", room_type).putExtra("iotSpaceId", iotSpaceId)
                                .putExtra("allDeviceData", mAllDevice.toJSONString())));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_FINDHOUSEDEV:
                        houseDeviceList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDeviceBean>>() {
                        }.getType());
//                        houseDeviceList1 = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDeviceBean>>() {
//                        }.getType());
                        mATRoomDeviceRVAdapter1.setLists(houseDeviceList);
                        break;
                    case ATConstants.Config.SERVER_URL_UPDATEROOM:
                        showToast(getString(R.string.at_save_success));
                        EventBus.getDefault().post(new ATEventIntent("ATManageRoomActivity", new Intent().putExtra("room_name", etRoomName.getText().toString())
                                .putExtra("room_type", room_type)
                                .putExtra("allDeviceData", mAllDevice.toJSONString())));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_BINDDEVROOM:
                        roomDeviceList.add(houseDeviceList.get(mCurrentPosition));
                        houseDeviceList.remove(mCurrentPosition);
                        mATRoomDeviceRVAdapter.setLists(roomDeviceList);
                        mATRoomDeviceRVAdapter1.setLists(houseDeviceList);
                        showToast(getString(R.string.at_save_success));
                        roomDevice = new JSONArray();
                        roomDevice.addAll(roomDeviceList);
                        mAllDevice.put(mRoomBean.getIotSpaceId(), roomDevice);
                        EventBus.getDefault().post(new ATEventIntent("ATManageRoomActivity", new Intent().putExtra("room_name", etRoomName.getText().toString())
                                .putExtra("room_type", room_type)
                                .putExtra("allDeviceData", mAllDevice.toJSONString())));
                        break;
                    case ATConstants.Config.SERVER_URL_UNBINDDEVROOM:
                        houseDeviceList.add(roomDeviceList.get(mCurrentPosition));
                        roomDeviceList.remove(mCurrentPosition);
                        mATRoomDeviceRVAdapter.setLists(roomDeviceList);
                        mATRoomDeviceRVAdapter1.setLists(houseDeviceList);
                        showToast(getString(R.string.at_unbind_success));
                        roomDevice = new JSONArray();
                        roomDevice.addAll(roomDeviceList);
                        mAllDevice.put(mRoomBean.getIotSpaceId(), roomDevice);
                        EventBus.getDefault().post(new ATEventIntent("ATManageRoomActivity", new Intent().putExtra("room_name", etRoomName.getText().toString())
                                .putExtra("room_type", room_type)
                                .putExtra("allDeviceData", mAllDevice.toJSONString())));
                        break;
                }
            } else {
                if(jsonResult.getString("message").contains("当前层级下有相同名称空间，请重命名当前空间名称")) {
                    showToast("房屋名称已存在");
                }else {
                    showToast(jsonResult.getString("message"));
                }
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHANGE_ROOM_PIC) {
            room_type = data.getStringExtra("room_type");
            tvRoomType.setText(ATResourceUtils.getResIdByName("at_" + room_type, ATResourceUtils.ResourceType.STRING));
        }
    }
}
