package com.aliyun.ayland.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATSceneContract;
import com.aliyun.ayland.data.ATEventBoolean;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATEventInteger2;
import com.aliyun.ayland.data.ATFamilyDeviceBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATPublicDeviceBean;
import com.aliyun.ayland.data.ATSceneBean1;
import com.aliyun.ayland.data.ATShortcutBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATScenePresenter;
import com.aliyun.ayland.ui.adapter.ATShortcutSMRVAdapter;
import com.aliyun.ayland.widget.ATRecycleViewItemDecoration;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ATHomeShortcutActivity extends ATBaseActivity implements ATSceneContract.View {
    private ATScenePresenter mPresenter;
    private ATShortcutSMRVAdapter mATShortcutSMRVAdapter;
    private ArrayList<ATShortcutBean> mShortcutList = new ArrayList<>();
    private ATHouseBean mATHouseBean;
    private int current_type = 0, editorStatus = 0;
    private Activity mContent;
    private int shortCutPosition = 0;
    private ATMyTitleBar titlebar;
    private SwipeMenuRecyclerView smrvMyShortcut;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_home_shortcut;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smrvMyShortcut = findViewById(R.id.smrv_my_shortcut);
        mContent = this;
        init();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger2 eventInteger) {
        if ("ATHomeShortcutActivity".equals(eventInteger.getClazz())) {
            mATShortcutSMRVAdapter.removePosition(eventInteger.getPosition());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger ATEventInteger) {
        if ("ATHomeShortcutActivity".equals(ATEventInteger.getClazz())) {
            mATShortcutSMRVAdapter.checkType(ATEventInteger.getPosition());
            current_type = ATEventInteger.getPosition();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventBoolean ATEventBoolean) {
        if ("ATHomeShortcutActivity".equals(ATEventBoolean.getClazz())) {
            mATShortcutSMRVAdapter.addDelete(ATEventBoolean.getPosition(), ATEventBoolean.isFlag());
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATScenePresenter(this);
        mPresenter.install(this);
//        showBaseProgressDlg();
//        sceneList();
//        findPublicDevice();
//        findShortcutDevice();
    }

    private void shortcutUpdate() {
        if(mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONArray shortcutList = new JSONArray();
        for (int i = 0; i < mATShortcutSMRVAdapter.getShortcutList().size(); i++) {
            JSONObject shortcut = new JSONObject();
            shortcut.put("itemId", mATShortcutSMRVAdapter.getShortcutList().get(i).getItemId());
            shortcut.put("operateType", mATShortcutSMRVAdapter.getShortcutList().get(i).getOperateType());
            shortcut.put("shortcutType", mATShortcutSMRVAdapter.getShortcutList().get(i).getShortcutType() == 0
                    ? 1 : mATShortcutSMRVAdapter.getShortcutList().get(i).getShortcutType());
            shortcutList.add(shortcut);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        jsonObject.put("shortcutList", shortcutList);
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_SHORTCUTUPDATE, jsonObject);
    }

    private void findPublicDevice() {
        if(mATHouseBean == null)
            return;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDPUBLICDEVICE, jsonObject);
    }

//    private void findDevices() {
//        ATHouseBean houseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
//        showBaseProgressDlg();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type", "4");
//        JSONObject operator = new JSONObject();
//        operator.put("hid", ATGlobalApplication.getOpenId());
//        operator.put("hidType", "OPEN");
//        jsonObject.put("operator", operator);
//        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
//        jsonObject.put("iotSpaceId", houseBean.getIotSpaceId());
//        mPresenter.request(ATConstants.Config.SERVER_URL_FINDDEVICES, jsonObject);
//    }

    private void findShortcutDevice() {
        if(mATHouseBean == null)
            return;
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("iotSpaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDSHORTCUTDEVICE, jsonObject);
    }

    private void sceneList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENELIST, jsonObject);
    }

    private void shortcutList() {
        if(mATHouseBean == null)
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_SHORTCUTLIST, jsonObject);
    }

    private void control() {
        showBaseProgressDlg();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONArray commands = new JSONArray();
        JSONObject command = new JSONObject();
        JSONObject data = new JSONObject();
        data.put(mShortcutList.get(shortCutPosition).getAttributes().get(0).getAttribute(),
                mShortcutList.get(shortCutPosition).getAttributes().get(0).getValue().equals("1") ? 0 : 1);
        command.put("data", data);
        command.put("type", "SET_PROPERTIES");
        commands.add(command);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetId", mShortcutList.get(shortCutPosition).getItemId());
        jsonObject.put("operator", operator);
        jsonObject.put("commands", commands);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_CONTROL, jsonObject);
    }

    private void sceneInstanceRun() {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("sceneId", mShortcutList.get(shortCutPosition).getItemId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEINSTANCERUN, jsonObject, shortCutPosition);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        mShortcutList = getIntent().getParcelableArrayListExtra("mShortcutList");
        if (mShortcutList.size() > 0)
            mShortcutList.remove(0);
        mATShortcutSMRVAdapter = new ATShortcutSMRVAdapter(this, getIntent().getParcelableArrayListExtra("mShortcutList"), editorStatus);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0 || position == mATShortcutSMRVAdapter.getShortcutList().size() + 1) ? 3 : 1;
            }
        });
        smrvMyShortcut.setLayoutManager(gridLayoutManager);
        smrvMyShortcut.setAdapter(mATShortcutSMRVAdapter);
        smrvMyShortcut.addItemDecoration(new ATRecycleViewItemDecoration(ATAutoUtils.getPercentHeightSize(48)));
        mATShortcutSMRVAdapter.setOnItemClickListener((view, position) -> {
            shortCutPosition = position;
            if (2 == mShortcutList.get(position).getShortcutType()) {
                sceneInstanceRun();
            } else {
                if (mShortcutList.get(position).getOperateType() == 1) {
                    startActivity(new Intent(mContent, ATIntelligentMonitorActivity.class)
                            .putExtra("productKey", mShortcutList.get(position).getProductKey())
                            .putExtra("iotId", mShortcutList.get(position).getItemId()));
                } else {
                    if (mShortcutList.get(position).getStatus() != 1) {
                        showToast(getString(R.string.at_device_outoff_line));
                    } else if (mShortcutList.get(position).getAttributes().size() == 0) {
                        showToast(getString(R.string.at_device_control_failed));
                    } else {
                        control();
                    }
                }
            }
        });
        smrvMyShortcut.setAdapter(mATShortcutSMRVAdapter);

        titlebar.setSendText(getString(R.string.at_edit));
        titlebar.setPublishClickListener(() -> {
            if (editorStatus == 0) {
                editorStatus = 1;
                titlebar.setSendText(getString(R.string.at_done));
                current_type = 0;
                showBaseProgressDlg();
                sceneList();
                findPublicDevice();
                findShortcutDevice();
                mATShortcutSMRVAdapter = new ATShortcutSMRVAdapter(mContent, mShortcutList, editorStatus);
                smrvMyShortcut.setOnItemMoveListener(onItemMoveListener);// Item的拖拽/侧滑删除时，手指状态发生变化监听。
                smrvMyShortcut.setOnItemStateChangedListener(mOnItemStateChangedListener); // 监听Item上的手指状态。
                smrvMyShortcut.setLongPressDragEnabled(true); // 开启拖拽。
                smrvMyShortcut.setItemViewSwipeEnabled(false); // 开启滑动删除。
                smrvMyShortcut.setAdapter(mATShortcutSMRVAdapter);
                smrvMyShortcut.setAdapter(mATShortcutSMRVAdapter);
            } else {
                shortcutUpdate();
                titlebar.setSendText(getString(R.string.at_edit));
                editorStatus = 0;
            }
        });
    }

    /**
     * 监听拖拽和侧滑删除，更新UI和数据源。
     */
    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
            // 不同的ViewType不能拖拽换位置。
            if (srcHolder.getItemViewType() != 1 || srcHolder.getItemViewType() != targetHolder.getItemViewType())
                return false;
            int fromPosition = srcHolder.getAdapterPosition();
            int toPosition = targetHolder.getAdapterPosition();
            mATShortcutSMRVAdapter.notifyItemMoveded(fromPosition, toPosition);
            return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
        }

        @Override
        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
            mATShortcutSMRVAdapter.notifyItemRemoved(srcHolder.getAdapterPosition());
        }
    };

    /**
     * Item的拖拽/侧滑删除时，手指状态发生变化监听。
     */
    private OnItemStateChangedListener mOnItemStateChangedListener = (viewHolder, actionState) -> {
        if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
//                当前状态（"状态：拖拽");
            // 拖拽的时候背景就透明了，这里我们可以添加一个特殊背景。
            if (viewHolder.getItemViewType() != 1)
                return;
            viewHolder.itemView.setBackground(ContextCompat.getDrawable(ATHomeShortcutActivity.this, R.drawable.shape_18px_f2f0ef));
        } else if (actionState == OnItemStateChangedListener.ACTION_STATE_IDLE) {
//                当前状态（"状态：手指松开");
            // 在手松开的时候还原背景。
            if (viewHolder.getItemViewType() != 1)
                return;
            ViewCompat.setBackground(viewHolder.itemView, ContextCompat.getDrawable(ATHomeShortcutActivity.this, R.drawable.selector_18px_ffffff_eeeeee));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    @Override
//    public void requestResult(String result, String url) {
//        try {
//            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
//            if ("200".equals(jsonResult.getString("code"))) {
//                switch (url) {
////                    case ATConstants.Config.SERVER_URL_FINDDEVICES:
////                        List<ATFamilyDeviceBean> familyDeviceList1 = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATFamilyDeviceBean>>() {
////                        }.getType());
////                        for (ATShortcutBean shortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
////                            for (ATFamilyDeviceBean familyDeviceBean : familyDeviceList1) {
////                                if (shortcutBean.getItemId().equals(familyDeviceBean.getIotId())) {
////                                    familyDeviceBean.setAdd(true);
////                                }
////                            }
////                        }
////                        mATShortcutSMRVAdapter.setFamilyDeviceLists(familyDeviceList1,1);
////                        if (current_type == 0)
////                            mATShortcutSMRVAdapter.checkType(0);
////                        break;
//                    case ATConstants.Config.SERVER_URL_FINDSHORTCUTDEVICE:
//                        List<ATFamilyDeviceBean> familyDeviceList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATFamilyDeviceBean>>() {
//                        }.getType());
//                        for (ATShortcutBean shortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
//                            for (ATFamilyDeviceBean familyDeviceBean : familyDeviceList) {
//                                if (shortcutBean.getItemId().equals(familyDeviceBean.getIotId())) {
//                                    familyDeviceBean.setAdd(true);
//                                }
//                            }
//                        }
//                        mATShortcutSMRVAdapter.setFamilyDeviceLists(familyDeviceList);
//                        if (current_type == 0) {
//                            mATShortcutSMRVAdapter.checkType(0);
//                        }else if (current_type == 1) {
//                            mATShortcutSMRVAdapter.checkType(1);
//                        }else {
//                            mATShortcutSMRVAdapter.checkType(2);
//                        }
////                        findDevices();
//                        break;
//                    case ATConstants.Config.SERVER_URL_FINDPUBLICDEVICE:
//                        List<ATPublicDeviceBean> publicDeviceList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATPublicDeviceBean>>() {
//                        }.getType());
//                        for (ATShortcutBean shortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
//                            for (ATPublicDeviceBean publicDeviceBean : publicDeviceList) {
//                                if (shortcutBean.getItemId().equals(publicDeviceBean.getDeviceId())) {
//                                    publicDeviceBean.setAdd(true);
//                                }
//                            }
//                        }
//                        mATShortcutSMRVAdapter.setPublicDeviceLists(publicDeviceList);
////                        if (current_type == 1)
////                            mATShortcutSMRVAdapter.checkType(1);
//                        if (current_type == 0) {
//                            mATShortcutSMRVAdapter.checkType(0);
//                        }else if (current_type == 1) {
//                            mATShortcutSMRVAdapter.checkType(1);
//                        }else {
//                            mATShortcutSMRVAdapter.checkType(2);
//                        }
//                        break;
//                    case ATConstants.Config.SERVER_URL_SCENELIST:
//                        List<ATSceneBean1> sceneBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATSceneBean1>>() {
//                        }.getType());
//                        for (ATShortcutBean shortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
//                            for (ATSceneBean1 sceneBean1 : sceneBeanList) {
//                                if (shortcutBean.getItemId().equals(sceneBean1.getSceneId())) {
//                                    sceneBean1.setAdd(true);
//                                }
//                            }
//                        }
//                        mATShortcutSMRVAdapter.setSceneLists(sceneBeanList);
////                        if (current_type == 2)
////                            mATShortcutSMRVAdapter.checkType(2);
//                        if (current_type == 0) {
//                            mATShortcutSMRVAdapter.checkType(0);
//                        }else if (current_type == 1) {
//                            mATShortcutSMRVAdapter.checkType(1);
//                        }else {
//                            mATShortcutSMRVAdapter.checkType(2);
//                        }
//                        break;
//                    case ATConstants.Config.SERVER_URL_SHORTCUTUPDATE:
//                        showToast(getString(R.string.shortcut_edit_success));
//                        setResult(RESULT_OK);
//                        finish();
//                        break;
//                    case ATConstants.Config.SERVER_URL_SHORTCUTLIST:
//
//                }
//            } else {
//                showToast(jsonResult.getString("message"));
//            }
//            closeBaseProgressDlg();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void requestResult(String result, String url, Object o) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if (ATConstants.Config.SERVER_URL_SCENEINSTANCERUN.equals(url)) {
                if ("200".equals(jsonResult.getString("code"))) {
//                    mATShortcutSMRVAdapter.setIsShowing(ATSceneManualAutoBean.SHOWSUCCESS, (int) o);
                    showToast(getString(R.string.at_perform_scene_success));
                } else {
                    Toast.makeText(this, jsonResult.getString("message"), Toast.LENGTH_SHORT).show();
//                    showToast(jsonResult.getString("message"));
//                    mATShortcutSMRVAdapter.setIsShowing(ATSceneManualAutoBean.NOTSHOW, (int) o);
                }
            }
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
//                    case ATConstants.Config.SERVER_URL_FINDDEVICES:
//                        List<ATFamilyDeviceBean> familyDeviceList1 = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATFamilyDeviceBean>>() {
//                        }.getType());
//                        for (ATShortcutBean shortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
//                            for (ATFamilyDeviceBean familyDeviceBean : familyDeviceList1) {
//                                if (shortcutBean.getItemId().equals(familyDeviceBean.getIotId())) {
//                                    familyDeviceBean.setAdd(true);
//                                }
//                            }
//                        }
//                        mATShortcutSMRVAdapter.setFamilyDeviceLists(familyDeviceList1,1);
//                        if (current_type == 0)
//                            mATShortcutSMRVAdapter.checkType(0);
//                        break;
                    case ATConstants.Config.SERVER_URL_FINDSHORTCUTDEVICE:
                        List<ATFamilyDeviceBean> familyDeviceList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATFamilyDeviceBean>>() {
                        }.getType());
                        for (ATShortcutBean ATShortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
                            for (ATFamilyDeviceBean ATFamilyDeviceBean : familyDeviceList) {
                                if (ATShortcutBean.getItemId().equals(ATFamilyDeviceBean.getIotId())) {
                                    ATFamilyDeviceBean.setAdd(true);
                                }
                            }
                        }
                        mATShortcutSMRVAdapter.setFamilyDeviceLists(familyDeviceList);
                        if (current_type == 0) {
                            mATShortcutSMRVAdapter.checkType(0);
                        } else if (current_type == 1) {
                            mATShortcutSMRVAdapter.checkType(1);
                        } else {
                            mATShortcutSMRVAdapter.checkType(2);
                        }
//                        findDevices();
                        break;
                    case ATConstants.Config.SERVER_URL_FINDPUBLICDEVICE:
                        List<ATPublicDeviceBean> publicDeviceList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATPublicDeviceBean>>() {
                        }.getType());
                        for (ATShortcutBean ATShortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
                            for (ATPublicDeviceBean ATPublicDeviceBean : publicDeviceList) {
                                if (ATShortcutBean.getItemId().equals(ATPublicDeviceBean.getDeviceId())) {
                                    ATPublicDeviceBean.setAdd(true);
                                }
                            }
                        }
                        mATShortcutSMRVAdapter.setPublicDeviceLists(publicDeviceList);
//                        if (current_type == 1)
//                            mATShortcutSMRVAdapter.checkType(1);
                        if (current_type == 0) {
                            mATShortcutSMRVAdapter.checkType(0);
                        } else if (current_type == 1) {
                            mATShortcutSMRVAdapter.checkType(1);
                        } else {
                            mATShortcutSMRVAdapter.checkType(2);
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_SCENELIST:
                        List<ATSceneBean1> sceneBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATSceneBean1>>() {
                        }.getType());
                        for (ATShortcutBean ATShortcutBean : mATShortcutSMRVAdapter.getShortcutList()) {
                            for (ATSceneBean1 ATSceneBean1 : sceneBeanList) {
                                if (ATShortcutBean.getItemId().equals(ATSceneBean1.getSceneId())) {
                                    ATSceneBean1.setAdd(true);
                                }
                            }
                        }
                        mATShortcutSMRVAdapter.setSceneLists(sceneBeanList);
//                        if (current_type == 2)
//                            mATShortcutSMRVAdapter.checkType(2);
                        if (current_type == 0) {
                            mATShortcutSMRVAdapter.checkType(0);
                        } else if (current_type == 1) {
                            mATShortcutSMRVAdapter.checkType(1);
                        } else {
                            mATShortcutSMRVAdapter.checkType(2);
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_SHORTCUTUPDATE:
                        showToast(getString(R.string.at_shortcut_edit_success));
                        shortcutList();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case ATConstants.Config.SERVER_URL_SHORTCUTLIST:
                        mShortcutList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATShortcutBean>>() {
                        }.getType());
                        mATShortcutSMRVAdapter = new ATShortcutSMRVAdapter(this, mShortcutList, editorStatus);
                        smrvMyShortcut.setLongPressDragEnabled(false);
                        smrvMyShortcut.setAdapter(mATShortcutSMRVAdapter);
                        mATShortcutSMRVAdapter.setOnItemClickListener((view, position) -> {
                            shortCutPosition = position;
                            if (2 == mShortcutList.get(position).getShortcutType()) {
                                sceneInstanceRun();
                            } else {
                                if (mShortcutList.get(position).getOperateType() == 1) {
                                    startActivity(new Intent(mContent, ATIntelligentMonitorActivity.class)
                                            .putExtra("productKey", mShortcutList.get(position).getProductKey())
                                            .putExtra("iotId", mShortcutList.get(position).getItemId()));
                                } else {
                                    if (mShortcutList.get(position).getStatus() != 1) {
                                        showToast(getString(R.string.at_device_outoff_line));
                                    } else if (mShortcutList.get(position).getAttributes().size() == 0) {
                                        showToast(getString(R.string.at_device_control_failed));
                                    } else {
                                        control();
                                    }
                                }
                            }
                        });
                        break;
                    case ATConstants.Config.SERVER_URL_CONTROL:
                        if (result.contains("success")) {
                            showToast(getString(R.string.at_operate_success));
                            mShortcutList.get(shortCutPosition).getAttributes().get(0).setValue(mShortcutList.get(shortCutPosition)
                                    .getAttributes().get(0).getValue().equals("1") ? "0" : "1");
                            mATShortcutSMRVAdapter.setList(mShortcutList);
                        }
                        break;
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