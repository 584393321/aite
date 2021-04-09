package com.aliyun.ayland.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATSceneContract;
import com.aliyun.ayland.data.ATApplicationBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATScenePresenter;
import com.aliyun.ayland.ui.activity.ATEnvironmentActivity;
import com.aliyun.ayland.ui.activity.ATEquipmentActivity;
import com.aliyun.ayland.ui.activity.ATFamilyMonitorActivity;
import com.aliyun.ayland.ui.activity.ATFamilySecurityActivity;
import com.aliyun.ayland.ui.activity.ATLinkageActivity;
import com.aliyun.ayland.ui.activity.ATOldYoungCareActivity;
import com.aliyun.ayland.ui.activity.ATPublicSecurityMainActivity;
import com.aliyun.ayland.ui.activity.ATShareSpaceActivity;
import com.aliyun.ayland.ui.activity.ATTmallVoiceWizardActivity;
import com.aliyun.ayland.ui.activity.ATUserFaceActivity;
import com.aliyun.ayland.ui.activity.ATVehicleCheckActivity;
import com.aliyun.ayland.ui.activity.ATVehiclePassageActivity;
import com.aliyun.ayland.ui.activity.ATVisitorRecordActivity;
import com.aliyun.ayland.ui.activity.ATVisualIntercomActivity;
import com.aliyun.ayland.ui.adapter.ATHomeAppRVAdapter;
import com.aliyun.ayland.utils.ATQRCodeUtil;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;

public class ATEmptyFragment1 extends ATBaseFragment implements ATSceneContract.View {
    private static final int MSG_REFRESH_QRCODE = 0x1001;
    private ATScenePresenter mPresenter;
    private ATMyTitleBar titlebar;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private Handler handler;
    private Dialog mDialogQRCode;
    private ImageView mImgQR;
    private String baseString;
    private ATHomeAppRVAdapter mHomeAppRVAdapter;
    private ArrayList<ATApplicationBean> mApplicationList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_empty1;
    }

    @Override
    protected void findView(View view) {
        titlebar = view.findViewById(R.id.titlebar);
        recyclerView = view.findViewById(R.id.recyclerView);
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        init();
        initQRCodeDialog();
        handler = new Handler(Objects.requireNonNull(getActivity()).getMainLooper()) {
            public void handleMessage(@NonNull android.os.Message msg) {
                if (msg.what == MSG_REFRESH_QRCODE) {
                    createQrcode();
                    handler.sendEmptyMessageDelayed(MSG_REFRESH_QRCODE, 180000);
                }
            }
        };
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATScenePresenter(this);
        mPresenter.install(getActivity());
        getAppList();
    }

    private void init() {
        titlebar.setTitle("汇生活");
        baseString = "mnt/sdcard/" + getPackageName() + "/";
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            getAppList();
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mHomeAppRVAdapter = new ATHomeAppRVAdapter(getActivity());
        recyclerView.setAdapter(mHomeAppRVAdapter);
        mHomeAppRVAdapter.setOnItemClickListener((view, position) -> {
            switch (mApplicationList.get(position).getApplicationIdentification()) {
                case "app_park_select":
                    //车位查看
                    startActivity(new Intent(getActivity(), ATVehicleCheckActivity.class));
                    break;
                case "app_my_car":
                    //我家的车
//                        mContext.startActivity(new Intent(mContext, ATVehiclePassageActivity.class));
                    break;
                case "app_visitor_appointment":
                    //访客预约
                    startActivity(new Intent(getActivity(), ATVisitorRecordActivity.class));
                    break;
                case "app_face_access":
                    //人脸通行
                    startActivity(new Intent(getActivity(), ATUserFaceActivity.class));
                    break;
                case "app_access_record":
                    //通行记录
                    startActivity(new Intent(getActivity(), ATVehiclePassageActivity.class));
                    break;
                case "app_video_intercom":
                    //云对讲
                    startActivity(new Intent(getActivity(), ATVisualIntercomActivity.class));
                    break;
                case "app_home_security":
                    //家庭安防
                    startActivity(new Intent(getActivity(), ATFamilySecurityActivity.class));
                    break;
                case "app_public_security":
                    //公区安防
                    startActivity(new Intent(getActivity(), ATPublicSecurityMainActivity.class));
                    break;
                case "app_scene_linkage":
                    //生活场景
                    startActivity(new Intent(getActivity(), ATLinkageActivity.class));
                    break;
                case "app_wisdom_health":
                    //智慧健康
                    break;
                case "app_care":
                    //老幼关怀
                    startActivity(new Intent(getActivity(), ATOldYoungCareActivity.class));
                    break;
                case "app_home_monitor":
                    //家庭监控
                    startActivity(new Intent(getActivity(), ATFamilyMonitorActivity.class));
                    break;
                case "app_my_equipment":
                    //我的设备
                    startActivity(new Intent(getActivity(), ATEquipmentActivity.class));
                    break;
                case "app_intelligent_environment":
                    //智慧环境
                    startActivity(new Intent(getActivity(), ATEnvironmentActivity.class));
                    break;
                case "app_voice_genie":
                    //语音精灵
//                        startActivity(new Intent(getActivity(), ATTmallWizardActivity.class));
                    startActivity(new Intent(getActivity(), ATTmallVoiceWizardActivity.class));
                    break;
                case "app_qrcode_access":
                    //二维码通行
                    showBaseProgressDlg();
                    handler.removeMessages(MSG_REFRESH_QRCODE);
                    handler.sendEmptyMessage(MSG_REFRESH_QRCODE);
                    break;
                case "app_space_appointment":
                    //空间预约
                    startActivity(new Intent(getActivity(), ATShareSpaceActivity.class));
                    break;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initQRCodeDialog() {
        mDialogQRCode = new Dialog(Objects.requireNonNull(getActivity()), R.style.nameDialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.at_dialog_qrcode, null, false);
        mImgQR = view.findViewById(R.id.img);
        view.findViewById(R.id.tv_refresh).setOnClickListener(v -> {
            handler.removeMessages(MSG_REFRESH_QRCODE);
            handler.sendEmptyMessage(MSG_REFRESH_QRCODE);
        });
        view.findViewById(R.id.img_close).setOnClickListener(v -> mDialogQRCode.dismiss());
        mDialogQRCode.setContentView(view);
        mDialogQRCode.setOnDismissListener(dialogInterface -> handler.removeMessages(MSG_REFRESH_QRCODE));
    }

    private void getAppList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETAPPLIST, jsonObject);
    }

    private void createQrcode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_CREATEQRCODE, jsonObject);
    }

    private String getPathString() {
        String nowFilePath = baseString + System.currentTimeMillis() + ".jpg";
        File file = new File(baseString);
        if (!file.exists()) {
            file.mkdir();
        }
        return nowFilePath;
    }

    @Override
    public void requestResult(String result, String url, Object o) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_CREATEQRCODE:
                        String qrcode = jsonResult.has("qrcode") ? jsonResult.getString("qrcode") : "";
                        if (!TextUtils.isEmpty(qrcode)) {
                            mImgQR.setImageBitmap(ATQRCodeUtil.createQRImage(qrcode, ATAutoUtils.getPercentWidthSize(701)
                                    , ATAutoUtils.getPercentHeightSize(701), getPathString(), false, null));
                            if (!mDialogQRCode.isShowing())
                                mDialogQRCode.show();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_GETAPPLIST:
                        mApplicationList = gson.fromJson(jsonResult.getString("result"), new TypeToken<List<ATApplicationBean>>() {
                        }.getType());
                        mHomeAppRVAdapter.setLists(mApplicationList);
                        break;
                }
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}