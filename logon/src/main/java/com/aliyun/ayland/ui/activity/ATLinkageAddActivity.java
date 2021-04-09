package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATSceneContract;
import com.aliyun.ayland.data.ATDeviceAccessBean;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATDeviceTcaList;
import com.aliyun.ayland.data.ATDeviceTslDataType;
import com.aliyun.ayland.data.ATDeviceTslEvents;
import com.aliyun.ayland.data.ATDeviceTslOutputDataType;
import com.aliyun.ayland.data.ATDeviceTslProperties;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATSceneAutoTitle;
import com.aliyun.ayland.data.ATSceneConditionBean;
import com.aliyun.ayland.data.ATSceneDoTitle;
import com.aliyun.ayland.data.ATSceneManualTitle;
import com.aliyun.ayland.data.ATSceneName;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATScenePresenter;
import com.aliyun.ayland.ui.adapter.ATAddLinkageRvAdapter;
import com.aliyun.ayland.ui.adapter.ATLinkageDragGVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.ATDragGridView;
import com.aliyun.ayland.widget.ATRecycleViewItemDecoration;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ATLinkageAddActivity extends ATBaseActivity implements ATSceneContract.View {
    public static final int REQUEST_CODE_ADD_CONDITION = 0x1001;
    public static final int REQUEST_CODE_EDIT_SCENE_NAME = 0x1002;
    public static final int REQUEST_CODE_EDIT_SCENE_ICON = 0x1003;
    private static final int MSG_SCENE_GET_COMPLETE = 0x1000;
    private ATScenePresenter mPresenter;
    private ATAddLinkageRvAdapter mLinkageSceneRvAdapter;
    private ATSceneName mATSceneName;
    private List<ATDeviceBean> mAllDeviceList = new ArrayList<>();
    private List<ATSceneName> triggerScene = new ArrayList<>();
    private List<ATSceneName> conditionScene = new ArrayList<>();
    private List<ATSceneName> actionScene = new ArrayList<>();
    private List<ATSceneConditionBean> mTriggerList = new ArrayList<>();
    private List<ATSceneConditionBean> mConditionList = new ArrayList<>();
    private List<ATSceneConditionBean> mActionList = new ArrayList<>();
    private String sceneId, cron, scene_name, scene_icon, sceneType;
    private String[] cronArr;
    private int currentPositon = -1, stay_position = -1, x_location, y_location;
    private ATHouseBean mATHouseBean;
    private Dialog mDialogName, dialog;
    private List<ATDeviceTcaList> mATDeviceTcaList = new ArrayList<>();
    private ATDeviceTcaList mBoxTca, mTimeTca, mCarTca, mPeopleTca;
    private ATLinkageDragGVAdapter mATLinkageDragGVAdapter;
    private boolean trigger, condition, action, flag = true;
    private Drawable mTriggerDrawable, mConditionDrawable, mActionDrawable;
    private ATSceneManualTitle mATSceneManualTitle = new ATSceneManualTitle();
    private ATMyTitleBar titlebar;
    private ImageView imgIcon, imgIcon2;
    private RelativeLayout rlContent;
    private ATDragGridView gridView;
    private LinearLayout llContent;
    private SwipeMenuRecyclerView smrvAddLinkage;
    private TextView tvLinkageName, tvTrigger, tvAnd, tvAction;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCENE_GET_COMPLETE:
                    if (triggerScene.size() == mTriggerList.size() && conditionScene.size() == mConditionList.size()
                            && actionScene.size() == mActionList.size() && flag) {
                        mHandler.removeMessages(MSG_SCENE_GET_COMPLETE);
                        flag = false;
                        Collections.sort(triggerScene);
                        Collections.sort(conditionScene);
                        Collections.sort(actionScene);
                        mLinkageSceneRvAdapter.addConditions(triggerScene, conditionScene, actionScene);
                        closeBaseProgressDlg();
                        mATSceneManualTitle.setName(scene_name);
                        mATSceneManualTitle.setScene_id(sceneId);
                        mATSceneManualTitle.setAuto(triggerScene.size() > 0);
                        mATSceneManualTitle.setScene_icon(scene_icon);
                        tvLinkageName.setText(mATSceneManualTitle.getName());
                        Glide.with(ATLinkageAddActivity.this).load(mATSceneManualTitle.getScene_icon()).into(imgIcon);
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_add_linkage;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        imgIcon = findViewById(R.id.img_icon);
        imgIcon2 = findViewById(R.id.img_icon2);
        rlContent = findViewById(R.id.rl_content);
        gridView = findViewById(R.id.gridView);
        tvLinkageName = findViewById(R.id.tv_linkage_name);
        tvTrigger = findViewById(R.id.tv_trigger);
        tvAnd = findViewById(R.id.tv_and);
        tvAction = findViewById(R.id.tv_action);
        llContent = findViewById(R.id.ll_content);
        smrvAddLinkage = findViewById(R.id.smrv_add_linkage);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATScenePresenter(this);
        mPresenter.install(this);
        findDeviceTca();
        if (!TextUtils.isEmpty(sceneId)) {
            sceneGet();
        }
        initNameDialog();
        if (TextUtils.isEmpty(ATGlobalApplication.getAllSceneIcon()) || "[]".equals(ATGlobalApplication.getAllSceneIcon())) {
            linkageImageList();
        } else {
            List<String> mSceneIconList = gson.fromJson(ATGlobalApplication.getAllSceneIcon(), new TypeToken<List<String>>() {
            }.getType());
            if (TextUtils.isEmpty(sceneId) && mSceneIconList.size() > 0) {
                scene_icon = mSceneIconList.get(new Random().nextInt(mSceneIconList.size()));
            }
            mATSceneManualTitle.setScene_icon(scene_icon);
            Glide.with(this).load(scene_icon).into(imgIcon);
        }
    }

    public void findDeviceTca() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDDEVICETCA, jsonObject);
    }

    public void linkageImageList() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        mPresenter.request(ATConstants.Config.SERVER_URL_LINKAGEIMAGELIST, jsonObject);
    }

    public void sceneDelete() {
        showBaseProgressDlg();

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("sceneType", TextUtils.isEmpty(sceneType) ? "1" : sceneType);
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());

        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEDELETE, jsonObject);
    }

    private void sceneGet() {
        showBaseProgressDlg();
        flag = true;

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("operator", operator);
        jsonObject.put("sceneType", TextUtils.isEmpty(sceneType) ? "1" : sceneType);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());

        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEGET, jsonObject);
    }

    private void sceneGet(String sceneId, ATSceneName ATSceneName) {
        showBaseProgressDlg();
        flag = true;

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("operator", operator);
        jsonObject.put("sceneType", TextUtils.isEmpty(sceneType) ? "1" : sceneType);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());

        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEGET, jsonObject, ATSceneName);
    }

    private void getDeviceList(String deviceType, ATSceneName ATSceneName) {
        ATHouseBean ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (ATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceType", deviceType);
        jsonObject.put("villageCode", ATHouseBean.getVillageId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETDEVICELIST, jsonObject, ATSceneName);
    }

    private void getTsl(String iotId, ATSceneName ATSceneName) {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("operator", operator);
        jsonObject.put("iotId", iotId);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETTSL, jsonObject, ATSceneName);
    }

    private void sceneBind() {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject target = new JSONObject();
        target.put("hid", ATGlobalApplication.getOpenId());
        target.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("target", target);
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEBIND, jsonObject);
    }

    private void sceneDeployRevoke(String sceneId) {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("sceneType", Integer.parseInt(TextUtils.isEmpty(sceneType) ? "1" : sceneType));
        jsonObject.put("operator", operator);
        jsonObject.put("enable", true);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());

        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEDEPLOYREVOKE, jsonObject);
    }

    private void sceneCreate() {
        showBaseProgressDlg();
        flag = true;
        JSONArray triggers = new JSONArray();
        JSONArray conditions = new JSONArray();
        JSONArray actions = new JSONArray();
        for (int i = 0; i < mLinkageSceneRvAdapter.getData().size(); ) {
            if (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneManualTitle) {
                i++;
                while (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneName) {
                    JSONObject object = new JSONObject();
                    JSONObject params = JSONObject.parseObject(((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getParams());
                    if (params.containsKey("compareValue")) {
                        params.put("compareValue", params.getInteger("compareValue"));
                    }
                    object.put("params", params);
                    object.put("uri", ((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getUri());
                    triggers.add(object);
                    i++;
                }
            } else if (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneAutoTitle) {
                i++;
                while (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneName) {
                    JSONObject object = new JSONObject();
                    JSONObject params = JSONObject.parseObject(((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getParams());
                    if (params.containsKey("compareValue")) {
                        params.put("compareValue", params.getInteger("compareValue"));
                    }
                    object.put("params", params);
                    object.put("uri", ((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getUri());
                    conditions.add(object);
                    i++;
                }
            } else if (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneDoTitle) {
                i++;
                while (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneName) {
                    JSONObject object = new JSONObject();
                    JSONObject propertyItems = new JSONObject();
                    JSONObject params = JSONObject.parseObject(((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getParams());
                    if (params.containsKey("propertyItems")) {
                        params.put("delayedExecutionSeconds", params.getInteger("delayedExecutionSeconds"));
                        for (String key : params.getJSONObject("propertyItems").keySet()) {
                            propertyItems.put(key, params.getJSONObject("propertyItems").getIntValue(key));
                            params.put(key, params.getJSONObject("propertyItems").getIntValue(key));
                        }
                        params.put("propertyItems", propertyItems);
                    }
                    object.put("params", params);
                    object.put("uri", ((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getUri());
                    actions.add(object);
                    i++;
                }
            } else {
                i++;
            }
        }
        if (actions.size() == 0) {
            showToast(getString(R.string.at_add_action));
            closeBaseProgressDlg();
            return;
        }
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        String url;
        JSONObject jsonObject = new JSONObject();
        if (TextUtils.isEmpty(sceneId)) {
            jsonObject.put("icon", scene_icon);
            jsonObject.put("name", scene_name);
            jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
            jsonObject.put("villageId", mATHouseBean.getVillageId());
            url = ATConstants.Config.SERVER_URL_SCENECREATE;
        } else {
            jsonObject.put("sceneId", sceneId);
            url = ATConstants.Config.SERVER_URL_SCENETCAUPDATE;
        }
        jsonObject.put("operator", operator);
        jsonObject.put("triggers", triggers);
        jsonObject.put("sceneType", TextUtils.isEmpty(sceneType) ? "1" : sceneType);
        jsonObject.put("conditions", conditions);
        jsonObject.put("actions", actions);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(url, jsonObject);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        x_location = rlContent.getMeasuredWidth() / 825 * 255;
        y_location = rlContent.getMeasuredHeight();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        sceneId = getIntent().getStringExtra("sceneId");
        sceneType = TextUtils.isEmpty(getIntent().getStringExtra("sceneType")) ? "1" : getIntent().getStringExtra("sceneType");
        if (!TextUtils.isEmpty(sceneId)) {
            titlebar.setTitle(getString(R.string.at_edit_life_linkage));
        } else {
            titlebar.setTitle(getString(R.string.at_add_life_linkage));
        }
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        mBoxTca = new ATDeviceTcaList();
        mBoxTca.setCategoryName(getString(R.string.at_colorful_box));
        mTimeTca = new ATDeviceTcaList();
        mTimeTca.setCategoryName(getString(R.string.at_time));
        mCarTca = new ATDeviceTcaList();
        mCarTca.setCategoryName(getString(R.string.at_vehicle_access));
        mPeopleTca = new ATDeviceTcaList();
        mPeopleTca.setCategoryName(getString(R.string.at_person_access));
        tvLinkageName.setOnClickListener(view -> startActivityForResult(new Intent(this, ATLinkageNameActivity.class)
                .putExtra("ATSceneManualTitle", mATSceneManualTitle), REQUEST_CODE_EDIT_SCENE_NAME));
        imgIcon2.setOnClickListener(view -> startActivityForResult(new Intent(this, ATLinkageIconActivity.class)
                .putExtra("ATSceneManualTitle", mATSceneManualTitle), REQUEST_CODE_EDIT_SCENE_ICON));

        String allDeviceData = ATGlobalApplication.getAllDeviceData();
        JSONObject object = JSON.parseObject(allDeviceData);
        List<ATDeviceBean> deviceList;
        if (object != null) {
            for (Map.Entry<String, Object> entry : object.entrySet()) {
                deviceList = gson.fromJson(entry.getValue().toString(), new TypeToken<List<ATDeviceBean>>() {
                }.getType());
                mAllDeviceList.addAll(deviceList);
            }
        }
        mATLinkageDragGVAdapter = new ATLinkageDragGVAdapter(this);
        gridView.setAdapter(mATLinkageDragGVAdapter);
        gridView.setOnItemMoveListener(new ATDragGridView.OnGridViewItemMoveListener() {
            @Override
            public void onItemMove(int x, int y) {
                if (x > x_location) {
                    if (trigger && y > 0 && y < y_location / 3) {
                        stay_position = 0;
                        tvTrigger.setBackground(getResources().getDrawable(R.drawable.shape_12px_66ffffff_xu));
                        tvAnd.setBackground(mConditionDrawable);
                        tvAction.setBackground(mActionDrawable);
                    } else if (condition && y > y_location / 3 * 2) {
                        stay_position = 1;
                        tvTrigger.setBackground(mTriggerDrawable);
                        tvAnd.setBackground(getResources().getDrawable(R.drawable.shape_12px_66ffffff_xu));
                        tvAction.setBackground(mActionDrawable);
                    } else if (action && y > y_location / 3 && y < y_location / 3 * 2) {
                        stay_position = 2;
                        tvTrigger.setBackground(mTriggerDrawable);
                        tvAnd.setBackground(mConditionDrawable);
                        tvAction.setBackground(getResources().getDrawable(R.drawable.shape_12px_66ffffff_xu));
                    } else {
                        stay_position = -1;
                        tvTrigger.setBackground(mTriggerDrawable);
                        tvAnd.setBackground(mConditionDrawable);
                        tvAction.setBackground(mActionDrawable);
                    }
                } else {
                    stay_position = -1;
                    tvTrigger.setBackground(mTriggerDrawable);
                    tvAnd.setBackground(mConditionDrawable);
                    tvAction.setBackground(mActionDrawable);
                }
            }

            @Override
            public void onItemStart(int mDragPosition, boolean b) {
                if (b) {
                    llContent.setVisibility(View.VISIBLE);
                    switch (mDragPosition) {
                        case 1:
                            trigger = true;
                            condition = true;
                            action = false;
                            break;
                        case 0:
                        case 2:
                        case 3:
                            trigger = true;
                            condition = false;
                            action = false;
                            break;
                        default:
                            trigger = mATDeviceTcaList.get(mDragPosition).getTrigger().size() > 0;
                            condition = mATDeviceTcaList.get(mDragPosition).getCondition() != null && mATDeviceTcaList.get(mDragPosition).getCondition().size() > 0;
                            action = mATDeviceTcaList.get(mDragPosition).getAction().size() > 0;
                            break;
                    }
                    if ("4".equals(sceneType)) {
                        trigger = false;
                        condition = false;
                    }
                    mTriggerDrawable = trigger ? getResources().getDrawable(R.drawable.shape_12px_00ffffff_xu) : getResources().getDrawable(R.drawable.shape_12px_00ffffff_xu_b3);
                    mConditionDrawable = condition ? getResources().getDrawable(R.drawable.shape_12px_00ffffff_xu) : getResources().getDrawable(R.drawable.shape_12px_00ffffff_xu_b3);
                    mActionDrawable = action ? getResources().getDrawable(R.drawable.shape_12px_00ffffff_xu) : getResources().getDrawable(R.drawable.shape_12px_00ffffff_xu_b3);
                    tvTrigger.setBackground(mTriggerDrawable);
                    tvAnd.setBackground(mConditionDrawable);
                    tvAction.setBackground(mActionDrawable);
                } else {
                    llContent.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    switch (stay_position) {
                        case 0:
                            if (mLinkageSceneRvAdapter.getIndexOfTrigger() != 1) {
                                showToast(getString(R.string.at_you_can_only_add_one_trigger));
                                return;
                            } else
                                intent.putExtra("flowType", 1);
                            break;
                        case 1:
                            intent.putExtra("flowType", 2);
                            break;
                        case 2:
                            intent.putExtra("flowType", 3);
                            break;
                        case -1:
                            return;
                    }
                    switch (mDragPosition) {
                        case 0:
                            intent.setClass(ATLinkageAddActivity.this, ATLinkageBoxActivity.class);
                            break;
                        case 1:
                            intent.setClass(ATLinkageAddActivity.this, ATLinkageTimingActivity.class);
                            break;
                        case 2:
                            intent.setClass(ATLinkageAddActivity.this, ATLinkageCarAccessActivity.class);
                            break;
                        case 3:
                            intent.setClass(ATLinkageAddActivity.this, ATLinkageAccessActivity.class);
                            intent.putExtra("dataType", "108");
                            intent.putExtra("uri", "trigger/biz/pass/event");
                            break;
                        default:
                            switch (stay_position) {
                                case 0:
                                    intent.putParcelableArrayListExtra("deviceList", mATDeviceTcaList.get(mDragPosition).getTrigger());
                                    break;
                                case 1:
                                    intent.putParcelableArrayListExtra("deviceList", mATDeviceTcaList.get(mDragPosition).getCondition());
                                    break;
                                case 2:
                                    intent.putParcelableArrayListExtra("deviceList", mATDeviceTcaList.get(mDragPosition).getAction());
                                    break;
                            }
                            intent.setClass(ATLinkageAddActivity.this, ATLinkageEquipmentActivity.class);
                            break;
                    }
                    startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
                    stay_position = -1;
                }
            }
        });
        smrvAddLinkage.setLayoutManager(new LinearLayoutManager(this));
        mLinkageSceneRvAdapter = new ATAddLinkageRvAdapter(sceneId);
        smrvAddLinkage.setNestedScrollingEnabled(false);
        smrvAddLinkage.addItemDecoration(new ATRecycleViewItemDecoration(ATAutoUtils.getPercentHeightSize(24)));
        smrvAddLinkage.setSwipeMenuCreator(mSwipeMenuCreator);
        smrvAddLinkage.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        smrvAddLinkage.setSwipeItemClickListener((view, position) -> {
            if (position == 0 || position == mLinkageSceneRvAdapter.getIndexOfTrigger() || position == mLinkageSceneRvAdapter.getIndexOfCondition()
                    || position == mLinkageSceneRvAdapter.getIndexOfAction())
                return;
            if (position == mLinkageSceneRvAdapter.getIndexOfCondition() + 1) {
                sceneDelete();
                return;
            }
            currentPositon = position;
            Intent intent = new Intent();
            if (position == mLinkageSceneRvAdapter.getIndexOfTrigger()) {
                for (int i = 0; i < mLinkageSceneRvAdapter.getData().size(); i++) {
                    if (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneManualTitle) {
                        i++;
                        while (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneName) {
                            if (getString(R.string.at_timing).equals(((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getName())) {
                                intent.putExtra("timing", true);
                            }
                            i++;
                        }
                    }
                }
                intent.putExtra("flowType", 1);
                intent.setClass(ATLinkageAddActivity.this, ATLinkageAddConditionActivity.class);
            } else if (position == mLinkageSceneRvAdapter.getIndexOfCondition()) {
                intent.putExtra("flowType", 2);
                for (int i = 0; i < mLinkageSceneRvAdapter.getData().size(); i++) {
                    if (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneAutoTitle) {
                        i++;
                        while (mLinkageSceneRvAdapter.getData().get(i) instanceof ATSceneName) {
                            if (getString(R.string.at_time_limit).equals(((ATSceneName) mLinkageSceneRvAdapter.getData().get(i)).getName())) {
                                intent.putExtra("timing", true);
                            }
                            i++;
                        }
                    }
                }
                intent.setClass(ATLinkageAddActivity.this, ATLinkageAddConditionActivity.class);
            } else if (position == mLinkageSceneRvAdapter.getIndexOfAction()) {
                intent.putExtra("flowType", 3);
                intent.setClass(ATLinkageAddActivity.this, ATLinkageAddConditionActivity.class);
            } else {
                mATSceneName = (ATSceneName) mLinkageSceneRvAdapter.getData().get(position);
                intent.putExtra("uri", mATSceneName.getUri());
                intent.putExtra("name", mATSceneName.getName());
                intent.putExtra("content", mATSceneName.getContent());
                intent.putExtra("params", mATSceneName.getParams());
                intent.putExtra("dataType", mATSceneName.getDataType());
                intent.putExtra("replace", true);
                switch (mATSceneName.getUri()) {
                    case "trigger/device/property":
                    case "trigger/device/event":
                        intent.putExtra("flowType", 1);
                        intent.setClass(this, ATLinkageStatusChoiseActivity.class);
                        break;
                    case "trigger/biz/pass/event":
                        intent.putExtra("flowType", 1);
                        if ("106".equals(mATSceneName.getDataType()))
                            intent.setClass(this, ATLinkageCarAccessActivity.class);
                        else
                            intent.setClass(this, ATLinkageAccessActivity.class);
                        break;
                    case "trigger/timer":
                        intent.putExtra("flowType", 1);
                        intent.setClass(this, ATLinkageTimingActivity.class);
                        break;
                    case "condition/device/property":
                    case "condition/device/event":
                        intent.putExtra("flowType", 2);
                        intent.setClass(this, ATLinkageStatusChoiseActivity.class);
                        break;
                    case "condition/timeRange":
                        intent.putExtra("flowType", 2);
                        intent.setClass(this, ATLinkageTimingActivity.class);
                        break;
                    case "action/device/setProperty":
                        intent.putExtra("flowType", 3);
                        intent.setClass(this, ATLinkageStatusChoiseActivity.class);
                        break;
                    case "action/mq/send":
                        intent.putExtra("flowType", 3);
                        intent.setClass(this, ATLinkageSendAppMessageActivity.class);
                        break;
                    case "action/scene/trigger":
                        intent.putExtra("flowType", 3);
                        intent.setClass(this, ATLinkagePerformSceneActivity.class);
                        break;
                    default:
                        intent.putExtra("flowType", 1);
                        intent.setClass(this, ATLinkageCarAccessActivity.class);
                        break;
                }
            }
            startActivityForResult(intent, REQUEST_CODE_ADD_CONDITION);
        });
        smrvAddLinkage.setLongPressDragEnabled(false);
        smrvAddLinkage.setItemViewSwipeEnabled(false);
        smrvAddLinkage.setAdapter(mLinkageSceneRvAdapter);
        titlebar.setSendText(getString(R.string.at_done));
        titlebar.setPublishClickListener(() -> {
            if (TextUtils.isEmpty(scene_name)) {
                mDialogName.show();
            } else {
                sceneCreate();
            }
        });
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_associated_scene, null, false);
        view.findViewById(R.id.tv_sure).setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
    }

    @SuppressLint("InflateParams")
    private void initNameDialog() {
        mDialogName = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_name, null, false);
        EditText edittext = view.findViewById(R.id.edittext);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.at_input_linkage_name));
        view.findViewById(R.id.tv_cancel).setOnClickListener((v) -> mDialogName.dismiss());
        view.findViewById(R.id.tv_sure).setOnClickListener((v) -> {
            scene_name = edittext.getText().toString();
            if (TextUtils.isEmpty(scene_name)) {
                showToast(getString(R.string.at_input_linkage_name));
                return;
            }
            if (TextUtils.isEmpty(scene_icon)) {
                showToast(getString(R.string.at_pick_linkage_icon));
                return;
            }
            mDialogName.dismiss();
            sceneCreate();
        });
        mDialogName.setContentView(view);
    }

    /**
     * 菜单创建器。
     */
    public SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            if (viewType != 3)
                return;
            int width = ATAutoUtils.getPercentWidthSize(180);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem closeItem = new SwipeMenuItem(ATLinkageAddActivity.this)
                    .setBackground(getResources().getDrawable(R.drawable.shape_half_circle_e83434))
                    .setText(getString(R.string.at_delete))
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
//            deleteList.add(mTempRoomList.get(adapterPosition).getIotSpaceId());
//            mTempRoomList.remove(adapterPosition);
            if (adapterPosition == 1)
                sceneType = "1";
            mLinkageSceneRvAdapter.removeCondition(adapterPosition);
        }
    };

    private String getTime(long time) {
        String hour = time / (60 * 60) + "";
        String minute = time % (60 * 60) / 60 + "";
        String seconds = time % (60 * 60) % 60 + "";
        String all = "";
        if (!"0".equals(hour))
            all += hour + "时";
        if (!"0".equals(minute))
            all += minute + "分";
        if (!"0".equals(seconds))
            all += seconds + "秒";
        return all;
    }

    @Override
    public void requestResult(String result, String url, Object o) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDDEVICETCA:
                        List<ATDeviceTcaList> ATDeviceTcaList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDeviceTcaList>>() {
                        }.getType());
                        mATDeviceTcaList.clear();
                        mATDeviceTcaList.add(mBoxTca);
                        mATDeviceTcaList.add(mTimeTca);
                        mATDeviceTcaList.add(mCarTca);
                        mATDeviceTcaList.add(mPeopleTca);
                        mATDeviceTcaList.addAll(ATDeviceTcaList);
                        mATLinkageDragGVAdapter.setList(mATDeviceTcaList);
                        break;
                    case ATConstants.Config.SERVER_URL_LINKAGEIMAGELIST:
                        ATGlobalApplication.setAllSceneIcon(jsonResult.getString("data"));
                        List<String> mSceneIconList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<String>>() {
                        }.getType());
                        closeBaseProgressDlg();
                        if (TextUtils.isEmpty(sceneId)) {
                            scene_icon = mSceneIconList.get(new Random().nextInt(mSceneIconList.size()));
                        }
                        mATSceneManualTitle.setScene_icon(scene_icon);
                        Glide.with(this).load(scene_icon).into(imgIcon);
                        break;
                    case ATConstants.Config.SERVER_URL_SCENEDELETE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_delete_scene_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_SCENEGET:
                        if (o != null) {
                            ((ATSceneName) o).setContent(jsonResult.getJSONObject("data").getString("name"));
                            actionScene.add(((ATSceneName) o));
                            mHandler.sendEmptyMessage(MSG_SCENE_GET_COMPLETE);
                        } else {
                            scene_name = jsonResult.getJSONObject("data").getString("name");
                            scene_icon = jsonResult.getJSONObject("data").getString("icon");
                            if (jsonResult.getJSONObject("data").has("triggers")) {
                                mTriggerList = gson.fromJson(jsonResult.getJSONObject("data").getString("triggers"), new TypeToken<List<ATSceneConditionBean>>() {
                                }.getType());
                                for (ATSceneConditionBean ATSceneConditionBean : mTriggerList) {
                                    mATSceneName = new ATSceneName();
                                    mATSceneName.setParams(ATSceneConditionBean.getParams().toJSONString());
                                    if (ATSceneConditionBean.getUri() == null)
                                        ATSceneConditionBean.setUri("");
                                    mATSceneName.setUri(ATSceneConditionBean.getUri());
                                    switch (ATSceneConditionBean.getUri()) {
                                        case "":
                                            mATSceneName.setName(ATSceneConditionBean.getParams().getString("licence"));
                                            mATSceneName.setContent(ATSceneConditionBean.getParams().getString("carParkName") + " " + ATResourceUtils.getString(ATResourceUtils
                                                    .getResIdByName(String.format(getString(R.string.at_car_in_out_),
                                                            ATSceneConditionBean.getParams().getInteger("direction")), ATResourceUtils.ResourceType.STRING)));
                                            triggerScene.add(mATSceneName);
                                            break;
                                        case "trigger/timer":
                                            cronArr = ATSceneConditionBean.getParams().getString("cron").split(" ");
                                            cron = cronArr[4].replace("0", "周日").replace("1", "周一").replace("2", "周二")
                                                    .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                                                    .replace("6", "周六").replace("7", "周日").replaceAll(",", "、");
                                            if ("*".equals(cron) || cron.length() == 20) {
                                                cron = (cronArr[1].length() < 2 ? "0" + cronArr[1] : cronArr[1]) + ":" + (cronArr[0].length() < 2 ? "0" + cronArr[0] : cronArr[0]) + " 每天";
                                            } else {
                                                cron = (cronArr[1].length() < 2 ? "0" + cronArr[1] : cronArr[1]) + ":" + (cronArr[0].length() < 2 ? "0" + cronArr[0] : cronArr[0]) + cron;
                                            }
                                            mATSceneName.setName(getString(R.string.at_timing));
                                            mATSceneName.setContent(cron);
                                            triggerScene.add(mATSceneName);
                                            break;
                                        case "trigger/device/property":
                                        case "trigger/device/event":
                                            for (ATDeviceBean ATDeviceBean : mAllDeviceList) {
                                                if (ATSceneConditionBean.getParams().getString("iotId").equals(ATDeviceBean.getIotId())) {
                                                    mATSceneName.setName(TextUtils.isEmpty(ATDeviceBean.getNickName()) ? ATDeviceBean.getProductName() : ATDeviceBean.getNickName());
                                                    getTsl(ATSceneConditionBean.getParams().getString("iotId"), mATSceneName);
                                                    break;
                                                }
                                            }
                                            break;
                                        case "trigger/biz/pass/event":
                                            if (ATSceneConditionBean.getParams().getString("bizType").contains("IN")) {
                                                mATSceneName.setContent(ATSceneConditionBean.getParams().getString("bizInfo") + " " + "进");
                                            } else if (ATSceneConditionBean.getParams().getString("bizType").contains("OUT")) {
                                                mATSceneName.setContent(ATSceneConditionBean.getParams().getString("bizInfo") + " " + "出");
                                            } else {
                                                mATSceneName.setContent(ATSceneConditionBean.getParams().getString("bizInfo") + " " + "进和出");
                                            }
                                            if (ATSceneConditionBean.getParams().getString("bizType").contains("CAR")) {
                                                mATSceneName.setDataType("106");
                                                getDeviceList("106", mATSceneName);
                                            } else {
                                                mATSceneName.setDataType("108");
                                                getDeviceList("108", mATSceneName);
                                            }
                                            break;
                                    }
                                }
                            }
                            if (jsonResult.getJSONObject("data").has("conditions")) {
                                mConditionList = gson.fromJson(jsonResult.getJSONObject("data").getString("conditions"), new TypeToken<List<ATSceneConditionBean>>() {
                                }.getType());
                                for (ATSceneConditionBean ATSceneConditionBean : mConditionList) {
                                    mATSceneName = new ATSceneName();
                                    mATSceneName.setParams(ATSceneConditionBean.getParams().toJSONString());
                                    mATSceneName.setUri(ATSceneConditionBean.getUri());
                                    switch (ATSceneConditionBean.getUri()) {
                                        case "condition/timeRange":
                                            cron = ATSceneConditionBean.getParams().getString("repeat").replace("0", "周日").replace("1", "周一").replace("2", "周二")
                                                    .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                                                    .replace("6", "周六").replace("7", "周日").replaceAll(",", "、");
                                            if (cron.length() == 20) {
                                                cron = ATSceneConditionBean.getParams().getString("beginDate") + " " + ATSceneConditionBean.getParams().getString("endDate") + " 每天";
                                            } else {
                                                cron = ATSceneConditionBean.getParams().getString("beginDate") + " " + ATSceneConditionBean.getParams().getString("endDate") + cron;
                                            }
                                            mATSceneName.setName(getString(R.string.at_time_limit));
                                            mATSceneName.setContent(cron);
                                            conditionScene.add(mATSceneName);
                                            break;
                                        case "condition/device/property":
                                        case "condition/device/event":
                                            for (ATDeviceBean ATDeviceBean : mAllDeviceList) {
                                                if (ATSceneConditionBean.getParams().getString("iotId").equals(ATDeviceBean.getIotId())) {
                                                    mATSceneName.setName(TextUtils.isEmpty(ATDeviceBean.getNickName()) ? ATDeviceBean.getProductName() : ATDeviceBean.getNickName());
                                                    getTsl(ATSceneConditionBean.getParams().getString("iotId"), mATSceneName);
                                                    break;
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                            if (jsonResult.getJSONObject("data").has("actions")) {
                                mActionList = gson.fromJson(jsonResult.getJSONObject("data").getString("actions"), new TypeToken<List<ATSceneConditionBean>>() {
                                }.getType());
                                for (ATSceneConditionBean ATSceneConditionBean : mActionList) {
                                    mATSceneName = new ATSceneName();
                                    mATSceneName.setParams(ATSceneConditionBean.getParams().toJSONString());
                                    mATSceneName.setUri(ATSceneConditionBean.getUri());
                                    switch (ATSceneConditionBean.getUri()) {
                                        case "action/device/setProperty":
                                            for (ATDeviceBean ATDeviceBean : mAllDeviceList) {
                                                if (ATSceneConditionBean.getParams().getString("iotId").equals(ATDeviceBean.getIotId())) {
                                                    mATSceneName.setName(TextUtils.isEmpty(ATDeviceBean.getNickName()) ? ATDeviceBean.getProductName() : ATDeviceBean.getNickName());
                                                    for (String s : ATSceneConditionBean.getParams().getJSONObject("propertyItems").keySet()) {
//                                                        mATSceneName.setContent(ATSceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("abilityName") + " "
//                                                                        + (("int".equals(ATSceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueType"))
//                                                                        || "float".equals(ATSceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueType"))
//                                                                        || "float".equals(ATSceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueType"))) ? "等于" : "")
//                                                                        + " " + (TextUtils.isEmpty(ATSceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueName"))
//                                                                        ? ATSceneConditionBean.getParams().getJSONObject("propertyItems").getString(s)
//                                                                        : ATSceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueName")
//                                                                        + ((ATSceneConditionBean.getParams().containsKey("delayedExecutionSeconds") && ATSceneConditionBean.getParams().getIntValue("delayedExecutionSeconds") != 0)
//                                                                            ? "  " + String.format(getString(R.string.delay_after), getTime(ATSceneConditionBean.getParams().getIntValue("delayedExecutionSeconds"))) : "")
//                                                                )
////                                                                + " " + ATSceneConditionBean.getParams().getJSONObject("propertyItems").getString(s)
//                                                        );
                                                        mATSceneName.setContent(((ATSceneConditionBean.getParams().containsKey("delayedExecutionSeconds") && ATSceneConditionBean.getParams().getIntValue("delayedExecutionSeconds") != 0)
                                                                        ? "  " + String.format(getString(R.string.at_delay_after), getTime(ATSceneConditionBean.getParams().getIntValue("delayedExecutionSeconds"))) : "")
//                                                                + " " + ATSceneConditionBean.getParams().getJSONObject("propertyItems").getString(s)
                                                        );
                                                        getTsl(ATSceneConditionBean.getParams().getString("iotId"), mATSceneName);
                                                        break;
                                                    }
                                                    break;
                                                }
                                            }
                                            break;
                                        case "action/device/invokeService":
                                            for (ATDeviceBean ATDeviceBean : mAllDeviceList) {
                                                if (ATSceneConditionBean.getParams().getString("iotId").equals(ATDeviceBean.getIotId())) {
                                                    mATSceneName.setName(TextUtils.isEmpty(ATDeviceBean.getNickName()) ? ATDeviceBean.getProductName() : ATDeviceBean.getNickName());
                                                    getTsl(ATSceneConditionBean.getParams().getString("iotId"), mATSceneName);
                                                    break;
                                                }
                                            }
                                            break;
                                        case "action/scene/trigger":
                                            mATSceneName.setName(getString(R.string.at_perform_scene));
                                            sceneGet(ATSceneConditionBean.getParams().getString("sceneId"), mATSceneName);
                                            break;
                                        case "action/mq/send":
                                            mATSceneName.setName(getString(R.string.at_send_app_message));
                                            mATSceneName.setContent(ATSceneConditionBean.getParams().getJSONObject("customData").getString("message"));
                                            actionScene.add(mATSceneName);
                                            break;
                                    }
                                }
                            }
                            mHandler.sendEmptyMessage(MSG_SCENE_GET_COMPLETE);
//                            ThreadPool.MainThreadHandler.getInstance().post(() -> {
//                                mLinkageSceneRvAdapter.addConditions(triggerScene, conditionScene, actionScene);
//                                closeBaseProgressDlg();
//                                tvName.setText(scene_name);
//                            });
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_GETDEVICELIST:
                        List<ATDeviceAccessBean> ATDeviceAccessBeans = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDeviceAccessBean>>() {
                        }.getType());
                        String deviceId = JSONObject.parseObject(((ATSceneName) o).getParams()).getString("deviceId");
                        for (int i = 0; i < ATDeviceAccessBeans.size(); i++) {
                            if (deviceId.equals(ATDeviceAccessBeans.get(i).getIotId())) {
                                ((ATSceneName) o).setName(ATDeviceAccessBeans.get(i).getName());
                                break;
                            }
                        }
                        triggerScene.add(((ATSceneName) o));
                        break;
                    case ATConstants.Config.SERVER_URL_GETTSL:
                        switch (((ATSceneName) o).getUri()) {
                            case "trigger/device/property":
                                List<ATDeviceTslProperties> ATDeviceTslProperties1 = gson.fromJson(jsonResult.getJSONObject("data").getString("properties"), new TypeToken<List<ATDeviceTslProperties>>() {
                                }.getType());
                                for (ATDeviceTslProperties deviceTslProperty : ATDeviceTslProperties1) {
                                    if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("propertyName").equals(deviceTslProperty.getIdentifier())) {
                                        ATDeviceTslDataType ATDeviceTslDataType = gson.fromJson(deviceTslProperty.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                        ((ATSceneName) o).setDataType(deviceTslProperty.getDataType().toJSONString());
                                        switch (ATDeviceTslDataType.getType()) {
                                            case "bool":
                                            case "enum":
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " " + ATDeviceTslDataType.getSpecs().getString(String.valueOf(JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue"))));
                                                break;
                                            case "int":
                                            case "double":
                                            case "float":
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " "
                                                        + ("==".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ?
                                                        "等于" : ">".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ? "大于" : "小于") + " "
                                                        + JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue") + ATDeviceTslDataType.getSpecs().getString("unit"));
                                                break;
                                            default:
                                                break;
                                        }
                                        triggerScene.add(((ATSceneName) o));
                                        break;
                                    }
                                }
                                break;
                            case "condition/device/property":
                                List<ATDeviceTslProperties> ATDeviceTslProperties2 = gson.fromJson(jsonResult.getJSONObject("data").getString("properties"), new TypeToken<List<ATDeviceTslProperties>>() {
                                }.getType());
                                for (ATDeviceTslProperties deviceTslProperty : ATDeviceTslProperties2) {
                                    if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("propertyName").equals(deviceTslProperty.getIdentifier())) {
                                        ATDeviceTslDataType ATDeviceTslDataType = gson.fromJson(deviceTslProperty.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                        ((ATSceneName) o).setDataType(deviceTslProperty.getDataType().toJSONString());
                                        switch (ATDeviceTslDataType.getType()) {
                                            case "bool":
                                            case "enum":
//                                                Log.e("requestResult: ", deviceTslProperty.getName()+"-"+ATDeviceTslDataType.getSpecs().toString());
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " " + ATDeviceTslDataType.getSpecs().getString(String.valueOf(JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue"))));
                                                break;
                                            case "int":
                                            case "double":
                                            case "float":
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " "
                                                        + ("==".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ?
                                                        "等于" : ">".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ? "大于" : "小于") + " "
                                                        + JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue") + ATDeviceTslDataType.getSpecs().getString("unit"));
                                                break;
                                            default:
                                                break;
                                        }
                                        conditionScene.add(((ATSceneName) o));
                                        break;
                                    }
                                }
                                break;
                            case "trigger/device/event":
                                List<ATDeviceTslEvents> ATDeviceTslEvents = gson.fromJson(jsonResult.getJSONObject("data").getString("events"), new TypeToken<List<ATDeviceTslEvents>>() {
                                }.getType());
                                for (ATDeviceTslEvents deviceTslEvent : ATDeviceTslEvents) {
                                    if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("eventCode").equals(deviceTslEvent.getIdentifier())) {
                                        List<ATDeviceTslOutputDataType> ATDeviceTslOutputDataType = gson.fromJson(deviceTslEvent.getOutputData().toJSONString(), new TypeToken<List<ATDeviceTslOutputDataType>>() {
                                        }.getType());
                                        for (ATDeviceTslOutputDataType tslOutputDataType : ATDeviceTslOutputDataType) {
                                            if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("propertyName").equals(tslOutputDataType.getIdentifier())) {
                                                ATDeviceTslDataType ATDeviceTslDataType = gson.fromJson(tslOutputDataType.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                                ((ATSceneName) o).setDataType(tslOutputDataType.getDataType().toJSONString());
                                                switch (ATDeviceTslDataType.getType()) {
                                                    case "bool":
                                                    case "enum":
                                                        ((ATSceneName) o).setContent(deviceTslEvent.getName() + " " + tslOutputDataType.getName() + " "
                                                                + ATDeviceTslDataType.getSpecs().getString(String.valueOf(JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue"))));
                                                        triggerScene.add(((ATSceneName) o));
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                break;
                            case "action/device/setProperty":
                                List<ATDeviceTslProperties> ATDeviceTslProperties3 = gson.fromJson(jsonResult.getJSONObject("data").getString("properties"), new TypeToken<List<ATDeviceTslProperties>>() {
                                }.getType());
                                String content = ((ATSceneName) o).getContent();
                                for (ATDeviceTslProperties deviceTslProperty : ATDeviceTslProperties3) {
                                    for (String identifier : JSONObject.parseObject(((ATSceneName) o).getParams()).getJSONObject("propertyItems").keySet()) {
                                        if (identifier.equals(deviceTslProperty.getIdentifier())) {
                                            ATDeviceTslDataType ATDeviceTslDataType = gson.fromJson(deviceTslProperty.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                            ((ATSceneName) o).setDataType(deviceTslProperty.getDataType().toJSONString());
                                            switch (ATDeviceTslDataType.getType()) {
                                                case "bool":
                                                case "enum":
                                                    for (String s : ATDeviceTslDataType.getSpecs().keySet()) {
                                                        if (JSONObject.parseObject(((ATSceneName) o).getParams()).getJSONObject("propertyItems").getIntValue(identifier) == Integer.parseInt(s)) {
                                                            ((ATSceneName) o).setContent(deviceTslProperty.getName() + " " + ATDeviceTslDataType.getSpecs().getString(s) + content);
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                case "int":
                                                case "double":
                                                case "float":
                                                    ((ATSceneName) o).setContent(deviceTslProperty.getName() + " 等于 " + JSONObject.parseObject(((ATSceneName) o).getParams()).getJSONObject("propertyItems").getIntValue(identifier)
                                                            + ATDeviceTslDataType.getSpecs().getString("unit") + content);
                                                    break;
                                            }
                                            actionScene.add(((ATSceneName) o));
                                            break;
                                        }
                                    }
                                }
                                break;
                        }
                        mHandler.sendEmptyMessage(MSG_SCENE_GET_COMPLETE);
                        break;
                    case ATConstants.Config.SERVER_URL_SCENECREATE:
                        if ("4".equals(sceneType)) {
                            closeBaseProgressDlg();
                            showToast(getString(R.string.at_add_linkage_success));
                            finish();
                        } else {
                            sceneId = jsonResult.getString("data");
                            sceneBind();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_SCENEBIND:
                        sceneDeployRevoke(sceneId);
                        break;
                    case ATConstants.Config.SERVER_URL_SCENEDEPLOYREVOKE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_add_linkage_success));
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_SCENETCAUPDATE:
                        closeBaseProgressDlg();
                        showToast(getString(R.string.at_edit_linkage_success));
                        finish();
                        break;
                }
            } else {
                closeBaseProgressDlg();
                if (jsonResult.getString("message").length() > 35 && jsonResult.getString("message").substring(0, 35).
                        equals("there are rules with the same content!sceneId:9e53665d7d6942f5ac65a8a766edf5ef".substring(0, 35))
                        ) {
                    showToast("该场景已存在");
                } else if (jsonResult.getString("message").length() > 10 &&
                        jsonResult.getString("message").substring(0, 10).equals("action is empty".substring(0, 10))) {
                    showToast("请添加对应设备");
                } else if ("10188".equals(jsonResult.getString("code"))) {
                    dialog.show();
                } else {
                    showToast(jsonResult.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT_SCENE_ICON:
                    scene_icon = data.getStringExtra("scene_icon");
                    mATSceneManualTitle.setScene_icon(scene_icon);
                    Glide.with(this).load(scene_icon).into(imgIcon);
                    break;
                case REQUEST_CODE_EDIT_SCENE_NAME:
                    scene_name = data.getStringExtra("scene_name");
                    tvLinkageName.setText(scene_name);
                    break;
                case REQUEST_CODE_ADD_CONDITION:
                    mATSceneName = new ATSceneName();
                    mATSceneName.setName(data.getStringExtra("name"));
                    mATSceneName.setParams(data.getStringExtra("params"));
                    mATSceneName.setUri(data.getStringExtra("uri"));
                    mATSceneName.setContent(data.getStringExtra("content"));
                    mATSceneName.setDataType(data.getStringExtra("dataType"));
                    if (data.getBooleanExtra("replace", false)) {
                        mLinkageSceneRvAdapter.replaceCondition(currentPositon, mATSceneName);
                    } else {
                        switch (data.getIntExtra("flowType", 1)) {
                            case 1:
                                mLinkageSceneRvAdapter.addTriggerCondition(mATSceneName);
                                sceneType = TextUtils.isEmpty(data.getStringExtra("uri")) ? "4" : "2";
                                break;
                            case 2:
                                mLinkageSceneRvAdapter.addConditionCondition(mATSceneName);
                                break;
                            case 3:
                                mLinkageSceneRvAdapter.addActionCondition(mATSceneName);
                                break;
                        }
                    }
                    break;
            }
        }
    }
}