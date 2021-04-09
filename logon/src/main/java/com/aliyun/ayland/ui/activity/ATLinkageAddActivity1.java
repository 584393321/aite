package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATSceneContract;
import com.aliyun.ayland.data.ATDeviceAccessBean;
import com.aliyun.ayland.data.ATDeviceBean;
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
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATScenePresenter;
import com.aliyun.ayland.ui.adapter.ATAddLinkageRvAdapter1;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.utils.EmojiExcludeFilter;
import com.aliyun.ayland.widget.ATRecycleViewItemDecoration;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
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

public class ATLinkageAddActivity1 extends ATBaseActivity implements ATSceneContract.View {
    public static final int REQUEST_CODE_ADD_CONDITION = 0x1001;
    public static final int REQUEST_CODE_EDIT_SCENE_NAME = 0x1002;
    public static final int REQUEST_CODE_EDIT_SCENE_ICON = 0x1003;
    private static final int MSG_SCENE_GET_COMPLETE = 0x1000;
    private ATScenePresenter mPresenter;
    private ATAddLinkageRvAdapter1 mLinkageSceneRvAdapter;
    private ATSceneName mSceneName;
    private List<ATDeviceBean> mAllDeviceList = new ArrayList<>();
    private List<ATSceneName> triggerScene = new ArrayList<>();
    private List<ATSceneName> conditionScene = new ArrayList<>();
    private List<ATSceneName> actionScene = new ArrayList<>();
    private List<ATSceneConditionBean> mTriggerList = new ArrayList<>();
    private List<ATSceneConditionBean> mConditionList = new ArrayList<>();
    private List<ATSceneConditionBean> mActionList = new ArrayList<>();
    private String sceneId, cron, scene_name, scene_icon, sceneType;
    private String[] cronArr;
    private int currentPositon = -1;
    private ATHouseBean houseBean;
    private Dialog mDialogName;
    private ATSceneManualTitle sceneManualTitle = new ATSceneManualTitle();
    private ATMyTitleBar titleBar;
    private SwipeMenuRecyclerView smrvAddLinkage;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCENE_GET_COMPLETE:
                    if (triggerScene.size() == mTriggerList.size() && conditionScene.size() == mConditionList.size()
                            && actionScene.size() == mActionList.size()) {
                        mHandler.removeMessages(MSG_SCENE_GET_COMPLETE);
                        Collections.sort(triggerScene);
                        Collections.sort(conditionScene);
                        Collections.sort(actionScene);
                        mLinkageSceneRvAdapter.addConditions(triggerScene, conditionScene, actionScene);
                        closeBaseProgressDlg();
                        sceneManualTitle.setName(scene_name);
                        sceneManualTitle.setScene_id(sceneId);
                        sceneManualTitle.setAuto(triggerScene.size() > 0);
                        sceneManualTitle.setScene_icon(scene_icon);
                        mLinkageSceneRvAdapter.setName(sceneManualTitle.getName());
                        mLinkageSceneRvAdapter.setSceneIcon(sceneManualTitle.getScene_icon());
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_add_linkage1;
    }

    @Override
    protected void findView() {
        titleBar = findViewById(R.id.titlebar);
        smrvAddLinkage = findViewById(R.id.smrv_add_linkage);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATScenePresenter(this);
        mPresenter.install(this);
        if (!TextUtils.isEmpty(sceneId)) {
            sceneGet();
        }
        initNameDialog();
        linkageImageList();
        if (TextUtils.isEmpty(ATGlobalApplication.getAllSceneIcon()) || "[]".equals(ATGlobalApplication.getAllSceneIcon())) {
            linkageImageList();
        } else {
            List<String> mSceneIconList = gson.fromJson(ATGlobalApplication.getAllSceneIcon(), new TypeToken<List<String>>() {
            }.getType());
            if (TextUtils.isEmpty(sceneId) && mSceneIconList.size() > 0) {
                scene_icon = mSceneIconList.get(new Random().nextInt(mSceneIconList.size()));
            }
            sceneManualTitle.setScene_icon(scene_icon);
            mLinkageSceneRvAdapter.setSceneIcon(sceneManualTitle.getScene_icon());
        }
    }

    public void baseinfoUpdate() {
        showBaseProgressDlg();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("name", scene_name);
        jsonObject.put("icon", TextUtils.isEmpty(sceneManualTitle.getScene_icon()) ? "https://g.aliplus.com/scene_icons/default.png" : sceneManualTitle.getScene_icon());
        jsonObject.put("description", "");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_BASEINFOUPDATE, jsonObject);
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
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEDELETE, jsonObject);
    }

    private void sceneGet() {
        showBaseProgressDlg();

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("operator", operator);
        jsonObject.put("sceneType", sceneType);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());

        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEGET, jsonObject);
    }

    private void sceneGet(String sceneId, ATSceneName sceneName) {
        showBaseProgressDlg();

        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", sceneId);
        jsonObject.put("operator", operator);
        jsonObject.put("sceneType", sceneType);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEGET, jsonObject, sceneName);
    }

    private void getDeviceList(String deviceType, ATSceneName sceneName) {
        ATHouseBean houseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        if (houseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceType", deviceType);
        jsonObject.put("villageCode", houseBean.getVillageId());
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETDEVICELIST, jsonObject, sceneName);
    }

    private void getTsl(String iotId, ATSceneName sceneName) {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("operator", operator);
        jsonObject.put("iotId", iotId);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETTSL, jsonObject, sceneName);
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
        jsonObject.put("buildingCode", houseBean.getBuildingCode());
        jsonObject.put("villageId", houseBean.getVillageId());
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
        jsonObject.put("sceneType", sceneType);
        jsonObject.put("operator", operator);
        jsonObject.put("enable", true);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());

        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEDEPLOYREVOKE, jsonObject);
    }

    private void sceneCreate() {
        showBaseProgressDlg();
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
            jsonObject.put("buildingCode", houseBean.getBuildingCode());
            jsonObject.put("villageId", houseBean.getVillageId());
            url = ATConstants.Config.SERVER_URL_SCENECREATE;
        } else {
            jsonObject.put("sceneId", sceneId);
            url = ATConstants.Config.SERVER_URL_SCENETCAUPDATE;
        }
        jsonObject.put("operator", operator);
        jsonObject.put("triggers", triggers);
        jsonObject.put("sceneType", sceneType);
        jsonObject.put("conditions", conditions);
        jsonObject.put("actions", actions);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(url, jsonObject);
    }

    private void init() {
        sceneId = getIntent().getStringExtra("sceneId");
        sceneType = TextUtils.isEmpty(getIntent().getStringExtra("sceneType")) ? "1" : getIntent().getStringExtra("sceneType");
        if (!TextUtils.isEmpty(sceneId)) {
            titleBar.setTitle(getString(R.string.at_edit_life_linkage));
        } else {
            titleBar.setTitle(getString(R.string.at_add_scene));
        }
        houseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

//        tvLinkageName.setOnClickListener(view -> {
//            startActivityForResult(new Intent(this, LinkageNameActivity.class)
//                    .putExtra("SceneManualTitle", sceneManualTitle), REQUEST_CODE_EDIT_SCENE_NAME);
//        });
//        rlIcon.setOnClickListener(view -> startActivityForResult(new Intent(this, LinkageIconActivity.class)
//                .putExtra("SceneManualTitle", sceneManualTitle), REQUEST_CODE_EDIT_SCENE_ICON));

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

        smrvAddLinkage.setLayoutManager(new LinearLayoutManager(this));
        mLinkageSceneRvAdapter = new ATAddLinkageRvAdapter1(this, sceneId);
        smrvAddLinkage.setNestedScrollingEnabled(false);
        smrvAddLinkage.setSwipeMenuCreator(mSwipeMenuCreator);
        smrvAddLinkage.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mLinkageSceneRvAdapter.setOnItemClickListener((view, o, position) -> {
            if (position == 1)
                startActivityForResult(new Intent(this, ATLinkageIconActivity.class)
                        .putExtra("ATSceneManualTitle", sceneManualTitle), REQUEST_CODE_EDIT_SCENE_ICON);
            else if (position == 2)
                mDialogName.show();
        });
        smrvAddLinkage.setSwipeItemClickListener((view, position) -> {
            if (position == 0 || position == mLinkageSceneRvAdapter.getIndexOfTrigger() + 1 || position == mLinkageSceneRvAdapter.getIndexOfCondition() + 1)
                return;
            if (position == mLinkageSceneRvAdapter.getIndexOfAction() + 1) {
                sceneDelete();
                return;
            }
            currentPositon = position;
            Intent intent = new Intent();
            if (position == mLinkageSceneRvAdapter.getIndexOfTrigger()) {
                if ("4".equals(sceneType)) {
                    showToast(getString(R.string.at_only_can_choose_one_vehicle_access_condition));
                    return;
                }
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
                Log.e("init: ", mLinkageSceneRvAdapter.getIndexOfCondition() + "--");
                intent.putExtra("car_access", mLinkageSceneRvAdapter.getIndexOfCondition());
                intent.setClass(ATLinkageAddActivity1.this, ATLinkageAddConditionActivity.class);
            } else if (position == mLinkageSceneRvAdapter.getIndexOfCondition()) {
                if ("4".equals(sceneType)) {
                    showToast(getString(R.string.at_only_can_choose_one_vehicle_access_condition));
                    return;
                }
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
                intent.setClass(ATLinkageAddActivity1.this, ATLinkageAddConditionActivity.class);
            } else if (position == mLinkageSceneRvAdapter.getIndexOfAction()) {
                intent.putExtra("flowType", 3);
                intent.setClass(ATLinkageAddActivity1.this, ATLinkageAddConditionActivity.class);
            } else {
                mSceneName = (ATSceneName) mLinkageSceneRvAdapter.getData().get(position);
                intent.putExtra("uri", mSceneName.getUri());
                intent.putExtra("name", mSceneName.getName());
                intent.putExtra("content", mSceneName.getContent());
                intent.putExtra("params", mSceneName.getParams());
                intent.putExtra("dataType", mSceneName.getDataType());
                intent.putExtra("replace", true);
                switch (mSceneName.getUri()) {
                    case "trigger/device/property":
                    case "trigger/device/event":
                        intent.putExtra("flowType", 1);
                        intent.setClass(this, ATLinkageStatusChoiseActivity.class);
                        break;
                    case "trigger/biz/pass/event":
                        intent.putExtra("flowType", 1);
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
        smrvAddLinkage.addItemDecoration(new ATRecycleViewItemDecoration(ATAutoUtils.getPercentHeightSize(24)));
        smrvAddLinkage.setAdapter(mLinkageSceneRvAdapter);
        titleBar.setSendText(getString(R.string.at_done));
        titleBar.setPublishClickListener(() -> {
            scene_name = mLinkageSceneRvAdapter.getName();
            if (TextUtils.isEmpty(scene_name)) {
                mDialogName.show();
            } else {
                sceneCreate();
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initNameDialog() {
        mDialogName = new Dialog(this, R.style.nameDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.at_dialog_name, null, false);
        EditText edittext = view.findViewById(R.id.edittext);
        edittext.setText(scene_name);
        edittext.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.at_input_linkage_name));
        view.findViewById(R.id.tv_cancel).setOnClickListener((v) -> mDialogName.dismiss());
        view.findViewById(R.id.tv_sure).setOnClickListener((v) -> {
            if (TextUtils.isEmpty(edittext.getText().toString())) {
                showToast(getString(R.string.at_input_scene_name));
                return;
            }
            scene_name = edittext.getText().toString();
            if (!TextUtils.isEmpty(sceneId)) {
                baseinfoUpdate();
            } else {
                mLinkageSceneRvAdapter.setName(scene_name);
                if (TextUtils.isEmpty(scene_icon)) {
                    showToast(getString(R.string.at_pick_linkage_icon));
                    return;
                }
                mDialogName.dismiss();
                sceneCreate();
            }
        });
        mDialogName.setContentView(view);
    }

    /**
     * 菜单创建器。
     */
    public SwipeMenuCreator mSwipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {
        if (viewType != 3)
            return;
        int width = ATAutoUtils.getPercentWidthSize(171);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        SwipeMenuItem closeItem = new SwipeMenuItem(ATLinkageAddActivity1.this)
                .setImage(R.drawable.at_ioc_hdshanchu)
                .setWidth(width)
                .setHeight(height);
        swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。
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
                    case ATConstants.Config.SERVER_URL_BASEINFOUPDATE:
                        showToast(getString(R.string.at_edit_linkage_name_success));
                        mLinkageSceneRvAdapter.setName(scene_name);
                        mDialogName.dismiss();
                        closeBaseProgressDlg();
                        break;
                    case ATConstants.Config.SERVER_URL_LINKAGEIMAGELIST:
                        List<String> mSceneIconList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<String>>() {
                        }.getType());
                        closeBaseProgressDlg();
                        if (TextUtils.isEmpty(sceneId) && mSceneIconList.size() > 0) {
                            scene_icon = mSceneIconList.get(new Random().nextInt(mSceneIconList.size()));
                        } else
                            return;
                        sceneManualTitle.setScene_icon(scene_icon);
                        mLinkageSceneRvAdapter.setSceneIcon(sceneManualTitle.getScene_icon());
                        ATGlobalApplication.setAllSceneIcon(jsonResult.getString("data"));
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
                                for (ATSceneConditionBean sceneConditionBean : mTriggerList) {
                                    mSceneName = new ATSceneName();
                                    mSceneName.setParams(sceneConditionBean.getParams().toJSONString());
                                    if (sceneConditionBean.getUri() == null)
                                        sceneConditionBean.setUri("");
                                    mSceneName.setUri(sceneConditionBean.getUri());
                                    switch (sceneConditionBean.getUri()) {
                                        case "":
                                            mSceneName.setName(sceneConditionBean.getParams().getString("licence"));
                                            mSceneName.setContent(sceneConditionBean.getParams().getString("carParkName") + " " + ATResourceUtils.getString(ATResourceUtils
                                                    .getResIdByName(String.format(getString(R.string.at_car_in_out_),
                                                            sceneConditionBean.getParams().getInteger("direction")), ATResourceUtils.ResourceType.STRING)));
                                            triggerScene.add(mSceneName);
                                            break;
                                        case "trigger/timer":
                                            cronArr = sceneConditionBean.getParams().getString("cron").split(" ");
                                            cron = cronArr[4].replace("0", "周日").replace("1", "周一").replace("2", "周二")
                                                    .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                                                    .replace("6", "周六").replace("7", "周日").replaceAll(",", "、");
                                            if ("*".equals(cron) || cron.length() == 20) {
                                                cron = (cronArr[1].length() < 2 ? "0" + cronArr[1] : cronArr[1]) + ":" + (cronArr[0].length() < 2 ? "0" + cronArr[0] : cronArr[0]) + " 每天";
                                            } else {
                                                cron = (cronArr[1].length() < 2 ? "0" + cronArr[1] : cronArr[1]) + ":" + (cronArr[0].length() < 2 ? "0" + cronArr[0] : cronArr[0]) + cron;
                                            }
                                            mSceneName.setName(getString(R.string.at_timing));
                                            mSceneName.setContent(cron);
                                            triggerScene.add(mSceneName);
                                            break;
                                        case "trigger/device/property":
                                        case "trigger/device/event":
                                            for (ATDeviceBean deviceBean : mAllDeviceList) {
                                                if (sceneConditionBean.getParams().getString("iotId").equals(deviceBean.getIotId())) {
                                                    mSceneName.setName(TextUtils.isEmpty(deviceBean.getNickName()) ? deviceBean.getProductName() : deviceBean.getNickName());
                                                    getTsl(sceneConditionBean.getParams().getString("iotId"), mSceneName);
                                                    break;
                                                }
                                            }
                                            break;
                                        case "trigger/biz/pass/event":
                                            if (sceneConditionBean.getParams().getString("bizType").contains("IN")) {
                                                mSceneName.setContent(sceneConditionBean.getParams().getString("bizInfo") + " " + "进");
                                            } else if (sceneConditionBean.getParams().getString("bizType").contains("OUT")) {
                                                mSceneName.setContent(sceneConditionBean.getParams().getString("bizInfo") + " " + "出");
                                            } else {
                                                mSceneName.setContent(sceneConditionBean.getParams().getString("bizInfo") + " " + "进和出");
                                            }
                                            if (sceneConditionBean.getParams().getString("bizType").contains("CAR")) {
                                                mSceneName.setDataType("106");
                                                getDeviceList("106", mSceneName);
                                            } else {
                                                mSceneName.setDataType("108");
                                                getDeviceList("108", mSceneName);
                                            }
                                            break;
                                    }
                                }
                            }
                            if (jsonResult.getJSONObject("data").has("conditions")) {
                                mConditionList = gson.fromJson(jsonResult.getJSONObject("data").getString("conditions"), new TypeToken<List<ATSceneConditionBean>>() {
                                }.getType());
                                for (ATSceneConditionBean sceneConditionBean : mConditionList) {
                                    mSceneName = new ATSceneName();
                                    mSceneName.setParams(sceneConditionBean.getParams().toJSONString());
                                    mSceneName.setUri(sceneConditionBean.getUri());
                                    switch (sceneConditionBean.getUri()) {
                                        case "condition/timeRange":
                                            cron = sceneConditionBean.getParams().getString("repeat").replace("0", "周日").replace("1", "周一").replace("2", "周二")
                                                    .replace("3", "周三").replace("4", "周四").replace("5", "周五")
                                                    .replace("6", "周六").replace("7", "周日").replaceAll(",", "、");
                                            if (cron.length() == 20) {
                                                cron = sceneConditionBean.getParams().getString("beginDate") + " " + sceneConditionBean.getParams().getString("endDate") + " 每天";
                                            } else {
                                                cron = sceneConditionBean.getParams().getString("beginDate") + " " + sceneConditionBean.getParams().getString("endDate") + cron;
                                            }
                                            mSceneName.setName(getString(R.string.at_time_limit));
                                            mSceneName.setContent(cron);
                                            conditionScene.add(mSceneName);
                                            break;
                                        case "condition/device/property":
                                        case "condition/device/event":
                                            for (ATDeviceBean deviceBean : mAllDeviceList) {
                                                if (sceneConditionBean.getParams().getString("iotId").equals(deviceBean.getIotId())) {
                                                    mSceneName.setName(TextUtils.isEmpty(deviceBean.getNickName()) ? deviceBean.getProductName() : deviceBean.getNickName());
                                                    getTsl(sceneConditionBean.getParams().getString("iotId"), mSceneName);
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
                                for (ATSceneConditionBean sceneConditionBean : mActionList) {
                                    mSceneName = new ATSceneName();
                                    mSceneName.setParams(sceneConditionBean.getParams().toJSONString());
                                    mSceneName.setUri(sceneConditionBean.getUri());
                                    switch (sceneConditionBean.getUri()) {
                                        case "action/device/setProperty":
                                            for (ATDeviceBean deviceBean : mAllDeviceList) {
                                                if (sceneConditionBean.getParams().getString("iotId").equals(deviceBean.getIotId())) {
                                                    mSceneName.setName(TextUtils.isEmpty(deviceBean.getNickName()) ? deviceBean.getProductName() : deviceBean.getNickName());
                                                    for (String s : sceneConditionBean.getParams().getJSONObject("propertyItems").keySet()) {
//                                                        mSceneName.setContent(sceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("abilityName") + " "
//                                                                        + (("int".equals(sceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueType"))
//                                                                        || "float".equals(sceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueType"))
//                                                                        || "float".equals(sceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueType"))) ? "等于" : "")
//                                                                        + " " + (TextUtils.isEmpty(sceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueName"))
//                                                                        ? sceneConditionBean.getParams().getJSONObject("propertyItems").getString(s)
//                                                                        : sceneConditionBean.getParams().getJSONObject("propertyNamesItems").getJSONObject(s).getString("valueName")
//                                                                        + ((sceneConditionBean.getParams().containsKey("delayedExecutionSeconds") && sceneConditionBean.getParams().getIntValue("delayedExecutionSeconds") != 0)
//                                                                            ? "  " + String.format(getString(R.string.delay_after), getTime(sceneConditionBean.getParams().getIntValue("delayedExecutionSeconds"))) : "")
//                                                                )
////                                                                + " " + sceneConditionBean.getParams().getJSONObject("propertyItems").getString(s)
//                                                        );
                                                        mSceneName.setContent(((sceneConditionBean.getParams().containsKey("delayedExecutionSeconds") && sceneConditionBean.getParams().getIntValue("delayedExecutionSeconds") != 0)
                                                                        ? "  " + String.format(getString(R.string.at_delay_after), getTime(sceneConditionBean.getParams().getIntValue("delayedExecutionSeconds"))) : "")
//                                                                + " " + sceneConditionBean.getParams().getJSONObject("propertyItems").getString(s)
                                                        );
                                                        getTsl(sceneConditionBean.getParams().getString("iotId"), mSceneName);
                                                        break;
                                                    }
                                                    break;
                                                }
                                            }
                                            break;
                                        case "action/device/invokeService":
                                            for (ATDeviceBean deviceBean : mAllDeviceList) {
                                                if (sceneConditionBean.getParams().getString("iotId").equals(deviceBean.getIotId())) {
                                                    mSceneName.setName(TextUtils.isEmpty(deviceBean.getNickName()) ? deviceBean.getProductName() : deviceBean.getNickName());
                                                    getTsl(sceneConditionBean.getParams().getString("iotId"), mSceneName);
                                                    break;
                                                }
                                            }
                                            break;
                                        case "action/scene/trigger":
                                            mSceneName.setName(getString(R.string.at_perform_scene));
                                            sceneGet(sceneConditionBean.getParams().getString("sceneId"), mSceneName);
                                            break;
                                        case "action/mq/send":
                                            mSceneName.setName(getString(R.string.at_send_app_message));
                                            mSceneName.setContent(sceneConditionBean.getParams().getJSONObject("customData").getString("message"));
                                            actionScene.add(mSceneName);
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
                        List<ATDeviceAccessBean> deviceAccessBeans = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATDeviceAccessBean>>() {
                        }.getType());
                        String deviceId = JSONObject.parseObject(((ATSceneName) o).getParams()).getString("deviceId");
                        for (int i = 0; i < deviceAccessBeans.size(); i++) {
                            if (deviceId.equals(deviceAccessBeans.get(i).getIotId())) {
                                ((ATSceneName) o).setName(deviceAccessBeans.get(i).getName());
                                break;
                            }
                        }
                        triggerScene.add(((ATSceneName) o));
                        break;
                    case ATConstants.Config.SERVER_URL_GETTSL:
                        switch (((ATSceneName) o).getUri()) {
                            case "trigger/device/property":
                                List<ATDeviceTslProperties> deviceTslProperties1 = gson.fromJson(jsonResult.getJSONObject("data").getString("properties"), new TypeToken<List<ATDeviceTslProperties>>() {
                                }.getType());
                                for (ATDeviceTslProperties deviceTslProperty : deviceTslProperties1) {
                                    if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("propertyName").equals(deviceTslProperty.getIdentifier())) {
                                        ATDeviceTslDataType deviceTslDataType = gson.fromJson(deviceTslProperty.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                        ((ATSceneName) o).setDataType(deviceTslProperty.getDataType().toJSONString());
                                        switch (deviceTslDataType.getType()) {
                                            case "bool":
                                            case "enum":
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " " + deviceTslDataType.getSpecs().getString(String.valueOf(JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue"))));
                                                break;
                                            case "int":
                                            case "double":
                                            case "float":
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " "
                                                        + ("==".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ?
                                                        "等于" : ">".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ? "大于" : "小于") + " "
                                                        + JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue") + deviceTslDataType.getSpecs().getString("unit"));
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
                                List<ATDeviceTslProperties> deviceTslProperties2 = gson.fromJson(jsonResult.getJSONObject("data").getString("properties"), new TypeToken<List<ATDeviceTslProperties>>() {
                                }.getType());
                                for (ATDeviceTslProperties deviceTslProperty : deviceTslProperties2) {
                                    if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("propertyName").equals(deviceTslProperty.getIdentifier())) {
                                        ATDeviceTslDataType deviceTslDataType = gson.fromJson(deviceTslProperty.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                        ((ATSceneName) o).setDataType(deviceTslProperty.getDataType().toJSONString());
                                        switch (deviceTslDataType.getType()) {
                                            case "bool":
                                            case "enum":
//                                                Log.e("requestResult: ", deviceTslProperty.getName()+"-"+deviceTslDataType.getSpecs().toString());
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " " + deviceTslDataType.getSpecs().getString(String.valueOf(JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue"))));
                                                break;
                                            case "int":
                                            case "double":
                                            case "float":
                                                ((ATSceneName) o).setContent(deviceTslProperty.getName() + " "
                                                        + ("==".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ?
                                                        "等于" : ">".equals(JSONObject.parseObject(((ATSceneName) o).getParams()).getString("compareType")) ? "大于" : "小于") + " "
                                                        + JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue") + deviceTslDataType.getSpecs().getString("unit"));
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
                                List<ATDeviceTslEvents> deviceTslEvents = gson.fromJson(jsonResult.getJSONObject("data").getString("events"), new TypeToken<List<ATDeviceTslEvents>>() {
                                }.getType());
                                for (ATDeviceTslEvents deviceTslEvent : deviceTslEvents) {
                                    if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("eventCode").equals(deviceTslEvent.getIdentifier())) {
                                        List<ATDeviceTslOutputDataType> deviceTslOutputDataType = gson.fromJson(deviceTslEvent.getOutputData().toJSONString(), new TypeToken<List<ATDeviceTslOutputDataType>>() {
                                        }.getType());
                                        for (ATDeviceTslOutputDataType tslOutputDataType : deviceTslOutputDataType) {
                                            if (JSONObject.parseObject(((ATSceneName) o).getParams()).getString("propertyName").equals(tslOutputDataType.getIdentifier())) {
                                                ATDeviceTslDataType deviceTslDataType = gson.fromJson(tslOutputDataType.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                                ((ATSceneName) o).setDataType(tslOutputDataType.getDataType().toJSONString());
                                                switch (deviceTslDataType.getType()) {
                                                    case "bool":
                                                    case "enum":
                                                        ((ATSceneName) o).setContent(deviceTslEvent.getName() + " " + tslOutputDataType.getName() + " "
                                                                + deviceTslDataType.getSpecs().getString(String.valueOf(JSONObject.parseObject(((ATSceneName) o).getParams()).getInteger("compareValue"))));
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
                                List<ATDeviceTslProperties> deviceTslProperties3 = gson.fromJson(jsonResult.getJSONObject("data").getString("properties"), new TypeToken<List<ATDeviceTslProperties>>() {
                                }.getType());
                                String content = ((ATSceneName) o).getContent();
                                for (ATDeviceTslProperties deviceTslProperty : deviceTslProperties3) {
                                    for (String identifier : JSONObject.parseObject(((ATSceneName) o).getParams()).getJSONObject("propertyItems").keySet()) {
                                        if (identifier.equals(deviceTslProperty.getIdentifier())) {
                                            ATDeviceTslDataType deviceTslDataType = gson.fromJson(deviceTslProperty.getDataType().toJSONString(), ATDeviceTslDataType.class);
                                            ((ATSceneName) o).setDataType(deviceTslProperty.getDataType().toJSONString());
                                            switch (deviceTslDataType.getType()) {
                                                case "bool":
                                                case "enum":
                                                    for (String s : deviceTslDataType.getSpecs().keySet()) {
                                                        if (JSONObject.parseObject(((ATSceneName) o).getParams()).getJSONObject("propertyItems").getIntValue(identifier) == Integer.parseInt(s)) {
                                                            ((ATSceneName) o).setContent(deviceTslProperty.getName() + " " + deviceTslDataType.getSpecs().getString(s) + content);
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                case "int":
                                                case "double":
                                                case "float":
                                                    ((ATSceneName) o).setContent(deviceTslProperty.getName() + " 等于 " + JSONObject.parseObject(((ATSceneName) o).getParams()).getJSONObject("propertyItems").getIntValue(identifier)
                                                            + deviceTslDataType.getSpecs().getString("unit") + content);
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
                showToast(jsonResult.getString("message"));
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
                    sceneManualTitle.setScene_icon(scene_icon);
                    mLinkageSceneRvAdapter.setSceneIcon(scene_icon);
                    break;
                case REQUEST_CODE_EDIT_SCENE_NAME:
                    scene_name = data.getStringExtra("scene_name");
//                    tvLinkageName.setText(scene_name);
                    break;
                case REQUEST_CODE_ADD_CONDITION:
                    mSceneName = new ATSceneName();
                    mSceneName.setName(data.getStringExtra("name"));
                    mSceneName.setParams(data.getStringExtra("params"));
                    mSceneName.setUri(data.getStringExtra("uri"));
                    mSceneName.setContent(data.getStringExtra("content"));
                    mSceneName.setDataType(data.getStringExtra("dataType"));
                    if (data.getBooleanExtra("replace", false)) {
                        mLinkageSceneRvAdapter.replaceCondition(currentPositon, mSceneName);
                    } else {
                        switch (data.getIntExtra("flowType", 1)) {
                            case 1:
                                mLinkageSceneRvAdapter.addTriggerCondition(mSceneName);
                                sceneType = TextUtils.isEmpty(data.getStringExtra("uri")) ? "4" : "2";
                                break;
                            case 2:
                                mLinkageSceneRvAdapter.addConditionCondition(mSceneName);
                                break;
                            case 3:
                                mLinkageSceneRvAdapter.addActionCondition(mSceneName);
                                break;
                        }
                    }
                    break;
            }
        }
    }
}