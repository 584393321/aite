package com.aliyun.ayland.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.data.ATShortcutBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.service.ATSocketServer;
import com.aliyun.ayland.ui.activity.ATEquipmentActivity;
import com.aliyun.ayland.ui.activity.ATFamilyManageActivity;
import com.aliyun.ayland.ui.activity.ATIntelligentMonitorActivity;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageError;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;
import com.anthouse.xuhui.R;
import com.baidu.idl.face.platform.utils.BitmapUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;
import static com.aliyun.alink.linksdk.tools.ThreadTools.runOnUiThread;

public class ATSDKUtils implements ATMainContract.View {
    private static ATSDKUtils mATSDKUtils = null;
    private static final int MSG_IOT_CREDENTIAL = 1001;
    private static final int MSG_REFRESH_QRCODE = 1002;
    private ATMainPresenter mPresenter;
    private Context context;
    private Gson gson = new Gson();
    private HashMap<String, ATOnCallBack> resultMap1 = new HashMap<>();
    private HashMap<String, ATShortcutRequestCallBack> resultMap = new HashMap<>();
    private List<ATShortcutBean> mShortcutList;
    private Handler handler;
    private ImageView mImgQR;
    private Dialog mDialogQRCode;
    private ProgressDialog mWaitProgressDlg;
    private ATLoginBean mATLoginBean;
    private String nick, avatarUrl;
    private boolean isNick;
    private ATHouseBean mATHouseBean;
    private Handler mHandler;

    private ATSDKUtils(Context context) {
        this.context = context;
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(context);
    }

    public static ATSDKUtils getInstance(Context context) {
        if (mATSDKUtils == null) {
            mATSDKUtils = new ATSDKUtils(context);
        }
        mATSDKUtils.setContext(context);
        return mATSDKUtils;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void showToast(String msg) {
        ATToastUtils.shortShow(msg);
    }

    public void showBaseProgressDlg() {
        showBaseProgressDlg(context.getString(R.string.at_loading));
    }

    public void showBaseProgressDlg(String msg) {
        if (mWaitProgressDlg != null && mWaitProgressDlg.isShowing()) {
            return;
        }
        mWaitProgressDlg = new ProgressDialog(context);
        mWaitProgressDlg.setCanceledOnTouchOutside(true);
        mWaitProgressDlg.setMessage(msg);
        mWaitProgressDlg.show();
    }

    public void closeBaseProgressDlg() {
        if (mWaitProgressDlg != null) {
            mWaitProgressDlg.dismiss();
        }
    }

    @SuppressLint("HandlerLeak")
    public void startIoTCredential() {
        if (mHandler == null)
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_IOT_CREDENTIAL:
                            IoTCredentialManageImpl.getInstance(ATGlobalApplication.getInstance()).asyncRefreshIoTCredential(new IoTCredentialListener() {
                                @Override
                                public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                                    runOnUiThread(() -> context.startService(new Intent(context, ATSocketServer.class)));
                                }

                                @Override
                                public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                                    mHandler.sendEmptyMessageDelayed(1, 3000);
                                }
                            });
                            break;
                    }
                }
            };
        mHandler.sendEmptyMessageDelayed(MSG_IOT_CREDENTIAL, 1000);
    }

    public void stopIoTCredential() {
        if (isRunService("com.aliyun.ayland.service.ATSocketServer"))
            context.stopService(new Intent(context, ATSocketServer.class));
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    private boolean isRunService(String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*
     * 切换房屋
     */
    public void setPresent(ATHouseBean aTHouseBean, ATOnCallBack mATCallBackUtil) {
        if (TextUtils.isEmpty(ATGlobalApplication.getHid()))
            return;
        mATHouseBean = aTHouseBean;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", ATGlobalApplication.getAccount());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        resultMap1.put(ATConstants.Config.SERVER_URL_SETPRESENT, mATCallBackUtil);
        mPresenter.request(ATConstants.Config.SERVER_URL_SETPRESENT, jsonObject);
    }

    /*
     * 切换房屋
     */
    public void updatePresentPlus(String custId, ATOnCallBack mATOnCallBack) {
        if (TextUtils.isEmpty(ATGlobalApplication.getHid()))
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", ATGlobalApplication.getAccount());
        jsonObject.put("custId", custId);
        resultMap1.put(ATConstants.Config.SERVER_URL_UPDATEPRESENTPLUS, mATOnCallBack);
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEPRESENTPLUS, jsonObject);
    }

    /*
     * 修改头像
     */
    public void updateUserHeadImge(Bitmap bitmap, ATOnCallBack mATOnCallBack) {
        if (TextUtils.isEmpty(ATGlobalApplication.getHid()))
            return;
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imageBase64", BitmapUtils.bitmapToJpegBase64(bitmap, 80));
//        jsonObject.put("openId", mATLoginBean.getOpenid());
        resultMap1.put(ATConstants.Config.SERVER_URL_UPDATEUSERHEADIMGE, mATOnCallBack);
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEUSERHEADIMGE, jsonObject);
    }

    /*
     * 修改昵称
     */
    public void updateUserInfo(String nick) {
        if (TextUtils.isEmpty(ATGlobalApplication.getHid()))
            return;
        isNick = true;
        mATLoginBean = ATGlobalApplication.getATLoginBean();
        this.nick = nick;
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("openId", mATLoginBean.getOpenid());
        jsonObject.put("nickname", nick);
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEUSERINFO, jsonObject);
    }

    /*
     * 住户管理
     */
    public void familyManage() {
        context.startActivity(new Intent(context, ATFamilyManageActivity.class));
    }

    /*
     * 消息中心
     */
    public void messageCenter() {
        Router.getInstance().toUrl(context, "link://router/devicenotices");
    }

    /*
     * 扫码开门
     */
    public void getQrcode() {
        if (TextUtils.isEmpty(ATGlobalApplication.getHid()))
            return;
        showBaseProgressDlg();
        initQRCodeDialog();
        handler = new Handler(Objects.requireNonNull(context).getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case MSG_REFRESH_QRCODE:
                        createQrcode();
                        handler.sendEmptyMessageDelayed(MSG_REFRESH_QRCODE, 180000);
                        break;
                    default:
                        break;
                }
            }
        };
        handler.removeMessages(MSG_REFRESH_QRCODE);
        handler.sendEmptyMessage(MSG_REFRESH_QRCODE);
    }

    /*
     * 首页快捷
     */
    public void shortcutList(ATShortcutRequestCallBack mATShortcutRequestCallBack) {
        if (TextUtils.isEmpty(ATGlobalApplication.getHid())) {
            mATShortcutRequestCallBack.onCallBack(new ArrayList<>(), (view, position) -> {
            });
            return;
        }
        ATHouseBean ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        jsonObject.put("villageId", ATHouseBean.getVillageId());
        jsonObject.put("buildingCode", ATHouseBean.getBuildingCode());
        resultMap.put(ATConstants.Config.SERVER_URL_SHORTCUTLIST, mATShortcutRequestCallBack);
        mPresenter.request(ATConstants.Config.SERVER_URL_SHORTCUTLIST, jsonObject);
    }

    @SuppressLint("InflateParams")
    private void initQRCodeDialog() {
        mDialogQRCode = new Dialog(context, R.style.nameDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.at_dialog_qrcode, null, false);
        mImgQR = view.findViewById(R.id.img);
        view.findViewById(R.id.tv_refresh).setOnClickListener(v -> {
            handler.removeMessages(MSG_REFRESH_QRCODE);
            handler.sendEmptyMessage(MSG_REFRESH_QRCODE);
        });
        view.findViewById(R.id.img_close).setOnClickListener(v -> mDialogQRCode.dismiss());
        mDialogQRCode.setContentView(view);
        mDialogQRCode.setOnDismissListener(dialogInterface -> handler.removeMessages(MSG_REFRESH_QRCODE));
    }

    private void updateUserInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("avatarUrl", avatarUrl);
//        jsonObject.put("openId", mATLoginBean.getOpenid());
        mPresenter.request(ATConstants.Config.SERVER_URL_UPDATEUSERINFO, jsonObject);
    }

    private void createQrcode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_CREATEQRCODE, jsonObject);
    }

    private void control(int position) {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getHid());
        operator.put("hidType", "OPEN");
        JSONArray commands = new JSONArray();
        JSONObject command = new JSONObject();
        JSONObject data = new JSONObject();
        data.put(mShortcutList.get(position).getAttributes().get(0).getAttribute(),
                mShortcutList.get(position).getAttributes().get(0).getValue().equals("1") ? 0 : 1);
        command.put("data", data);
        command.put("type", "SET_PROPERTIES");
        commands.add(command);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetId", mShortcutList.get(position).getItemId());
        jsonObject.put("operator", operator);
        jsonObject.put("commands", commands);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_CONTROL, jsonObject);
    }

    private void sceneInstanceRun(int position) {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getHid());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("sceneId", mShortcutList.get(position).getItemId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEINSTANCERUN, jsonObject);
    }

    private String getPathString() {
        String nowFilePath = "mnt/sdcard/" + getPackageName() + "/" + System.currentTimeMillis() + ".jpg";
        File file = new File("mnt/sdcard/" + getPackageName() + "/");
        if (!file.exists()) {
            file.mkdir();
        }
        return nowFilePath;
    }

    @Override
    public void requestResult(String result, String url) {
        org.json.JSONObject jsonResult = new org.json.JSONObject();
        try {
            if (!StringUtils.isNullOrEmpty(result)) {
                jsonResult = new org.json.JSONObject(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            switch (url) {
                case ATConstants.Config.SERVER_URL_UPDATEPRESENTPLUS:
                    if ("200".equals(jsonResult.getString("code"))) {
                        mATHouseBean = gson.fromJson(jsonResult.getString("data"), ATHouseBean.class);
                        ATGlobalApplication.setHouse(mATHouseBean.toString());
                    }
                    resultMap1.get(url).onCallBack(jsonResult.getInt("code"));
                    closeBaseProgressDlg();
                    break;
                case ATConstants.Config.SERVER_URL_SETPRESENT:
                    if ("200".equals(jsonResult.getString("code"))) {
                        showToast(context.getString(R.string.at_change_house_success));
                        ATGlobalApplication.setHouse(mATHouseBean.toString());
                    }
                    resultMap1.get(url).onCallBack(jsonResult.getInt("code"));
                    closeBaseProgressDlg();
                    break;
                case ATConstants.Config.SERVER_URL_UPDATEUSERHEADIMGE:
                    if ("200".equals(jsonResult.getString("code"))) {
                        avatarUrl = jsonResult.getString("data");
                        updateUserInfo();
                    }
                    break;
                case ATConstants.Config.SERVER_URL_UPDATEUSERINFO:
                    if ("200".equals(jsonResult.getString("code"))) {
                        showToast("修改成功");
                        if (!TextUtils.isEmpty(nick))
                            mATLoginBean.setNickName(nick);
//                        if (!TextUtils.isEmpty(avatarUrl)) {
//                            mATLoginBean.setAvatarUrl(ATConstants.Config.BASE_ALISAAS_URL + avatarUrl);
//                        }
                        ATGlobalApplication.setLoginBeanStr(JSONObject.toJSONString(mATLoginBean));
                        break;
                    }
                    resultMap1.get(url).onCallBack(jsonResult.getInt("code"));
                    closeBaseProgressDlg();
                    break;
                case ATConstants.Config.SERVER_URL_CREATEQRCODE:
                    String qrcode = jsonResult.has("qrcode") ? jsonResult.getString("qrcode") : "";
                    if (!TextUtils.isEmpty(qrcode)) {
                        mImgQR.setImageBitmap(ATQRCodeUtil.createQRImage(qrcode, ATAutoUtils.getPercentWidthSize(701)
                                , ATAutoUtils.getPercentHeightSize(701), getPathString(), false, null));
                        if (!mDialogQRCode.isShowing())
                            mDialogQRCode.show();
                    }
                    break;
                case ATConstants.Config.SERVER_URL_CONTROL:
                    if ("200".equals(jsonResult.getString("code"))) {
                        showToast(context.getString(R.string.at_operate_success));
                    } else {
                        showToast(context.getString(R.string.at_control_failed));
                    }
                    break;
                case ATConstants.Config.SERVER_URL_SCENEINSTANCERUN:
                    if ("200".equals(jsonResult.getString("code"))) {
                        showToast(context.getString(R.string.at_perform_scene_success));
                    } else {
                        showToast(context.getString(R.string.at_perform_failed));
                    }
                    break;
                case ATConstants.Config.SERVER_URL_SHORTCUTLIST:
                    if ("200".equals(jsonResult.getString("code"))) {
                        List<ATShortcutBean> shortcutList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATShortcutBean>>() {
                        }.getType());
                        mShortcutList = new ArrayList<>();
                        if (shortcutList.size() > 4) {
                            for (int i = 0; i < 3; i++) {
                                mShortcutList.add(shortcutList.get(i));
                            }
                        } else {
                            mShortcutList.addAll(shortcutList);
                        }
                        ATShortcutBean atShortcutBean = new ATShortcutBean();
                        atShortcutBean.setItemName("我的设备");
                        atShortcutBean.setItemIcon("https://smarthome.cifi.com.cn/pic/icon/icon_s_yingyong_shebei2x.png");
                        mShortcutList.add(atShortcutBean);
                        resultMap.get(url).onCallBack(mShortcutList, (view, position) -> {
                            if (position == mShortcutList.size() - 1) {
                                context.startActivity(new Intent(context, ATEquipmentActivity.class));
                            } else if (mShortcutList.get(position).getShortcutType() == 2) {
                                sceneInstanceRun(position);
                            } else {
                                if (mShortcutList.get(position).getOperateType() == 1) {
                                    context.startActivity(new Intent(context, ATIntelligentMonitorActivity.class)
                                            .putExtra("productKey", mShortcutList.get(position).getProductKey())
                                            .putExtra("iotId", mShortcutList.get(position).getItemId()));
                                } else {
                                    if (mShortcutList.get(position).getStatus() != 1) {
                                        showToast(context.getString(R.string.at_device_outoff_line));
                                    } else if (mShortcutList.get(position).getAttributes().size() == 0) {
                                        showToast(context.getString(R.string.at_device_control_failed));
                                    } else {
                                        control(position);
                                    }
                                }
                            }
                        });
                    } else {

                    }
                    break;
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface ATShortcutRequestCallBack {
        void onCallBack(List<ATShortcutBean> list, ATOnRVItemClickListener atOnRVItemClickListener);
    }

    public interface ATOnCallBack {
        void onCallBack(int code);
    }
}