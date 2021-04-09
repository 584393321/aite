package com.aliyun.ayland.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.contract.ATSceneContract;
import com.aliyun.ayland.data.ATApplicationBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.data.ATSceneManualAutoBean;
import com.aliyun.ayland.data.ATShortcutBean;
import com.aliyun.ayland.data.ATSipListBean;
import com.aliyun.ayland.data.ATUserInfo;
import com.aliyun.ayland.data.ATWeatherBean1;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATScenePresenter;
import com.aliyun.ayland.ui.activity.ATChangeHouseActivity;
import com.aliyun.ayland.ui.activity.ATEnvironmentActivity;
import com.aliyun.ayland.ui.activity.ATHomeShortcutActivity;
import com.aliyun.ayland.ui.activity.ATIntelligentMonitorActivity;
import com.aliyun.ayland.ui.activity.ATSettingActivity;
import com.aliyun.ayland.ui.adapter.ATHomeCardRVAdapter;
import com.aliyun.ayland.ui.adapter.ATHomeShortcutRVAdapter;
import com.aliyun.ayland.utils.ATPreferencesUtils;
import com.aliyun.ayland.utils.ATQRCodeUtil;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.ATSmoothnessLayoutManage;
import com.aliyun.ayland.widget.voip.VoipManager;
import com.aliyun.ayland.widget.zxing.android.ATCaptureActivity;
import com.anthouse.xuhui.R;
import com.evideo.voip.sdk.EVVoipException;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.alibaba.sdk.android.ams.common.global.AmsGlobalHolder.getPackageName;

/**
 * Created by fr on 2017/12/19.
 */
public class ATHomeFragment extends ATBaseFragment implements ATSceneContract.View {
    public static final int REQUEST_CODE_HOME_CHANGE = 0x100;
    private static final int MSG_GET_WEATHER = 0x1001;
    private static final int MSG_REFRESH_QRCODE = 1002;
    private ATHomeShortcutRVAdapter mATHomeShortcutRVAdapter;
    private ATHomeCardRVAdapter mHomeAppRVAdapter;
    private ATScenePresenter mPresenter;
    private ArrayList<ATShortcutBean> mShortcutList = new ArrayList<>();
    private ArrayList<ATApplicationBean> mApplicationList = new ArrayList<>();
    private ATWeatherBean1 mWeatherBean;
    private LinearLayout llWeather, llWeather1, llInside;
    private RelativeLayout rlContent;
    private Handler handler;
    private ATHouseBean mATHouseBean;
    private Dialog mDialogQRCode;
    private RecyclerView rvShortcut, rvApplication;
    private SmartRefreshLayout smartRefreshLayout;
    private ImageView imgPmRed, imgPmYello, imgPmInsideRed, imgPmInsideYello, imgTvocRed, imgTvocYello, imgBegin, imgTo, imgEnd, mImgQR;
    private TextView tvOutsideTemp, tvWind, tvWeather, tvPm, tvCity, tvInsideTemp, tvWet, tvPmInside, tvTvoc, tvAddBox;
    private String qrcode, baseString;
    private int shortCutPosition = 0;
    private ImageView imgBack;
    private long clickTime = 0;

    public interface ClickListener {
        void onClick(View view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_home;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATScenePresenter(this);
        mPresenter.install(getActivity());

        findPresent();
    }

    @Override
    protected void findView(View view) {
        rlContent = view.findViewById(R.id.rl_content);
        llInside = view.findViewById(R.id.ll_inside);
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        rvShortcut = view.findViewById(R.id.rv_shortcut);
        rvApplication = view.findViewById(R.id.rv_application);
        imgPmRed = view.findViewById(R.id.img_pm_red);
        imgPmYello = view.findViewById(R.id.img_pm_yello);
        imgPmInsideRed = view.findViewById(R.id.img_pm_inside_red);
        imgPmInsideYello = view.findViewById(R.id.img_pm_inside_yello);
        imgTvocRed = view.findViewById(R.id.img_tvoc_red);
        imgTvocYello = view.findViewById(R.id.img_tvoc_yello);
        imgBegin = view.findViewById(R.id.img_begin);
        imgTo = view.findViewById(R.id.img_to);
        imgEnd = view.findViewById(R.id.img_end);
        llWeather = view.findViewById(R.id.ll_weather);
        llWeather1 = view.findViewById(R.id.ll_weather1);
        tvOutsideTemp = view.findViewById(R.id.tv_outside_temp);
        tvWind = view.findViewById(R.id.tv_wind);
        tvWeather = view.findViewById(R.id.tv_weather);
        tvPm = view.findViewById(R.id.tv_pm);
        tvCity = view.findViewById(R.id.tv_city);
        tvInsideTemp = view.findViewById(R.id.tv_inside_temp);
        tvWet = view.findViewById(R.id.tv_wet);
        tvPmInside = view.findViewById(R.id.tv_pm_inside);
        tvTvoc = view.findViewById(R.id.tv_tvoc);
        tvAddBox = view.findViewById(R.id.tv_add_box);
        imgBack = view.findViewById(R.id.img_back);
        init();
        handler = new Handler(Objects.requireNonNull(getActivity()).getMainLooper()) {
            public void handleMessage(@NonNull android.os.Message msg) {
                switch (msg.what) {
                    case MSG_GET_WEATHER:
                        getWeather();
                        handler.sendEmptyMessageDelayed(MSG_GET_WEATHER, 600000);
                        break;
                    case MSG_REFRESH_QRCODE:
                        createQrcode();
                        handler.sendEmptyMessageDelayed(MSG_REFRESH_QRCODE, 180000);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        baseString = "mnt/sdcard/" + getPackageName() + "/";

        llInside.setVisibility(View.GONE);
        tvAddBox.setVisibility(View.VISIBLE);
        tvAddBox.setText(getString(R.string.at_add_box));

        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(view -> {
            getActivity().finish();
        });
        imgBack.setLongClickable(true);
        imgBack.setOnLongClickListener(v -> {
            startActivity(new Intent(getActivity(), ATSettingActivity.class));
            return true;
        });

        llWeather.setOnClickListener(view -> startActivity(new Intent(getActivity(), ATEnvironmentActivity.class)));
        llWeather.setLongClickable(true);
        llWeather.setOnLongClickListener(v -> {
            startActivity(new Intent(getActivity(), ATChangeHouseActivity.class));
            return true;
        });

        rvShortcut.setLayoutManager(new ATSmoothnessLayoutManage(getActivity(), LinearLayout.HORIZONTAL, false));
        mATHomeShortcutRVAdapter = new ATHomeShortcutRVAdapter(getActivity());
        mATHomeShortcutRVAdapter.setOnItemClickListener((view, position) -> {
            if (position == mShortcutList.size() - 1) {
                //更多
                startActivityForResult(new Intent(getActivity(), ATHomeShortcutActivity.class)
                        .putParcelableArrayListExtra("mShortcutList", mShortcutList), REQUEST_CODE_HOME_CHANGE);
                return;
            }
            shortCutPosition = position;
            if (2 == mShortcutList.get(position).getShortcutType()) {
                sceneInstanceRun();
            } else {
                if (mShortcutList.get(position).getOperateType() == 1) {
                    startActivity(new Intent(getActivity(), ATIntelligentMonitorActivity.class)
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
        rvShortcut.setAdapter(mATHomeShortcutRVAdapter);

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            request();
        });
        rvApplication.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeAppRVAdapter = new ATHomeCardRVAdapter(getActivity());
        rvApplication.setAdapter(mHomeAppRVAdapter);

        mApplicationList = new ArrayList<>();
        ATApplicationBean atApplicationBean = new ATApplicationBean();
        atApplicationBean.setApplicationName("我的设备");
        atApplicationBean.setApplicationIdentification("app_my_equipment");
        mApplicationList.add(atApplicationBean);

        ATApplicationBean atApplicationBean1 = new ATApplicationBean();
        atApplicationBean1.setApplicationName("生活场景");
        atApplicationBean1.setApplicationIdentification("app_scene_linkage");
        mApplicationList.add(atApplicationBean1);

        ATApplicationBean atApplicationBean2 = new ATApplicationBean();
        atApplicationBean2.setApplicationName("家庭安防");
        atApplicationBean2.setApplicationIdentification("app_home_security");
        mApplicationList.add(atApplicationBean2);

        ATApplicationBean atApplicationBean3 = new ATApplicationBean();
        atApplicationBean3.setApplicationName("语音精灵");
        atApplicationBean3.setApplicationIdentification("app_voice_genie");
        mApplicationList.add(atApplicationBean3);

        mHomeAppRVAdapter.setLists(mApplicationList);
        initQRCodeDialog();

        if (!TextUtils.isEmpty(ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "weather", ""))) {
            mWeatherBean = gson.fromJson(ATPreferencesUtils.getString(ATGlobalApplication.getContext(), "weather", ""), ATWeatherBean1.class);
            if (mWeatherBean != null && getActivity() != null) {
                tvOutsideTemp.setText(setSizeSpan(String.format(getString(R.string.at_temp_), mWeatherBean.getOutdoorsWeather().getTem())));
                tvWind.setText(setSizeSpan1(String.format(getString(R.string.at_wind_), mWeatherBean.getOutdoorsWeather().getWindLevel() == null ? "" : mWeatherBean.getOutdoorsWeather().getWindLevel().getCnName())));
                tvPm.setText(setSizeSpan2(String.format(getString(R.string.at_pm_), mWeatherBean.getOutdoorsWeather().get_$PM25153())));
            }
        } else {
            tvOutsideTemp.setText(setSizeSpan(String.format(getString(R.string.at_temp_), "28")));
            tvWind.setText(setSizeSpan1(String.format(getString(R.string.at_wind_), "3级")));
            tvPm.setText(setSizeSpan2(String.format(getString(R.string.at_pm_), "95")));
        }
    }

    private SpannableString setSizeSpan(String s) {
        SpannableString textSpan = new SpannableString(s);
        textSpan.setSpan(new AbsoluteSizeSpan(12, true), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return textSpan;
    }

    private SpannableString setSizeSpan1(String s) {
        SpannableString textSpan = new SpannableString(s);
        textSpan.setSpan(new AbsoluteSizeSpan(12, true), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return textSpan;
    }

    private SpannableString setSizeSpan2(String s) {
        SpannableString textSpan = new SpannableString(s);
        textSpan.setSpan(new AbsoluteSizeSpan(12, true), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        textSpan.setSpan(new AbsoluteSizeSpan(14, true), s.length() - 5, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return textSpan;
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

    private String getPathString() {
        String nowFilePath = baseString + System.currentTimeMillis() + ".jpg";
        File file = new File(baseString);
        if (!file.exists()) {
            file.mkdir();
        }
        return nowFilePath;
    }

    private void list() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 0);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageCode", mATHouseBean.getVillageId());
        jsonObject.put("targetId", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_LIST, jsonObject);
    }

    private void findPresent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", ATPreferencesUtils.getString(getActivity(), "tempAccount", ""));
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDPRESENT, jsonObject);
    }

    private void createQrcode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getATLoginBean().getPersonCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_CREATEQRCODE, jsonObject);
    }

    private void control() {
        showBaseProgressDlg();
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getHid());
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
        operator.put("hid", ATGlobalApplication.getHid());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("sceneId", mShortcutList.get(shortCutPosition).getItemId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_SCENEINSTANCERUN, jsonObject, shortCutPosition);
    }

    private void request() {
        if (TextUtils.isEmpty(ATGlobalApplication.getOpenId()) || mATHouseBean == null)
            return;
        showBaseProgressDlg();
        shortcutList();
//        getWeather();
//        getAppList();
//        getUserInfo();
        getHouseDevice();
    }

    private void getHouseDevice() {
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getATLoginBean().getPersonCode());
        operator.put("hidType", "OPEN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("iotSpaceId", mATHouseBean.getIotSpaceId());
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        jsonObject.put("username", ATGlobalApplication.getAccount());
        mPresenter.request(ATConstants.Config.SERVER_URL_HOUSEDEVICE, jsonObject);
    }

    private void getWeather() {
        if (TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
            return;
        }
        ATHouseBean ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", ATHouseBean.getVillageId());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETWEATHER, jsonObject);
    }

    private void getUserInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETUSERINFO, jsonObject);
    }

    private void getAppList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETAPPLIST, jsonObject);
    }

    private void shortcutList() {
        if (TextUtils.isEmpty(ATGlobalApplication.getHouse())) {
            return;
        }
        ATHouseBean ATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personCode", ATGlobalApplication.getOpenId());
        jsonObject.put("villageId", ATHouseBean.getVillageId());
        jsonObject.put("buildingCode", ATHouseBean.getBuildingCode());
        mPresenter.request(ATConstants.Config.SERVER_URL_SHORTCUTLIST, jsonObject);
    }

    @Override
    public void requestResult(String result, String url, Object o) {
        try {
            if (result == null)
                return;
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if (ATConstants.Config.SERVER_URL_SCENEINSTANCERUN.equals(url)) {
                if ("200".equals(jsonResult.getString("code"))) {
                    mATHomeShortcutRVAdapter.setIsShowing(ATSceneManualAutoBean.SHOWSUCCESS, (int) o);
                    showToast(getString(R.string.at_perform_scene_success));
                } else {
                    mATHomeShortcutRVAdapter.setIsShowing(ATSceneManualAutoBean.NOTSHOW, (int) o);
                }
            }
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDPRESENT:
                        try {
                            if ("200".equals(jsonResult.getString("code"))) {
                                if ("{}".equals(jsonResult.getString("data"))) {
                                    startActivity(new Intent(getActivity(), ATChangeHouseActivity.class));
                                } else {
                                    mATHouseBean = gson.fromJson(jsonResult.getString("data"), ATHouseBean.class);
                                    ATGlobalApplication.setHouse(mATHouseBean.toString());
                                    list();
                                }
                                ATGlobalApplication.setHouseState(jsonResult.getString("houseState"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_LIST:
                        try {
                            List<ATSipListBean> atSipListBean = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATSipListBean>>() {
                            }.getType());
                            if (atSipListBean.size() > 0) {
                                ATSipListBean sipListBean = atSipListBean.get(0);
                                if (sipListBean.getStatus() == 1)
                                    try {
                                        VoipManager.getInstance().login(sipListBean.getSipNumber(), sipListBean.getSipPassword(), sipListBean.getSipHost()
                                                , sipListBean.getSipPort());
                                    } catch (EVVoipException e) {
                                        e.printStackTrace();
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_CREATEQRCODE:
                        qrcode = jsonResult.has("qrcode") ? jsonResult.getString("qrcode") : "";
                        if (!TextUtils.isEmpty(qrcode)) {
                            mImgQR.setImageBitmap(ATQRCodeUtil.createQRImage(qrcode, ATAutoUtils.getPercentWidthSize(701)
                                    , ATAutoUtils.getPercentHeightSize(701), getPathString(), false, null));
                            if (!mDialogQRCode.isShowing())
                                mDialogQRCode.show();
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_GETUSERINFO:
                        ATUserInfo ATUserInfoBean = gson.fromJson(jsonResult.getString("data"), ATUserInfo.class);
                        ATLoginBean mATLoginBean = ATGlobalApplication.getATLoginBean();
//                        mATLoginBean.setAvatarUrl(ATUserInfoBean.getAvatarUrl());
                        mATLoginBean.setNickName(ATUserInfoBean.getNickname());
                        ATGlobalApplication.setLoginBeanStr(JSONObject.toJSONString(mATLoginBean));
                        break;
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:
                        String allDeviceData = jsonResult.getString("data");
                        ATGlobalApplication.setAllDeviceData(allDeviceData);
                        break;
                    case ATConstants.Config.SERVER_URL_GETWEATHER:
                        ATPreferencesUtils.putString(ATGlobalApplication.getContext(), "weather", jsonResult.getString("data"));
                        mWeatherBean = gson.fromJson(jsonResult.getString("data"), ATWeatherBean1.class);
                        if (getActivity() != null)
                            getActivity().runOnUiThread(this::requestComplete);
                        break;
                    case ATConstants.Config.SERVER_URL_SHORTCUTLIST:
                        List<ATShortcutBean> shortcutList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATShortcutBean>>() {
                        }.getType());
                        mShortcutList.clear();
                        mShortcutList.addAll(shortcutList);
                        ATShortcutBean aTShortcutBean = new ATShortcutBean();
                        aTShortcutBean.setItemName("更多");
                        mShortcutList.add(aTShortcutBean);
                        mATHomeShortcutRVAdapter.setLists(mShortcutList);
                        break;
                    case ATConstants.Config.SERVER_URL_GETAPPLIST:
                        List<ATApplicationBean> applicationList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATApplicationBean>>() {
                        }.getType());
                        mApplicationList = new ArrayList<>();
                        for (ATApplicationBean atApplicationBean : applicationList) {
                            if ("app_my_equipment".equals(atApplicationBean.getApplicationIdentification())) {
                                mApplicationList.add(atApplicationBean);
                            }
                        }
                        for (ATApplicationBean atApplicationBean : applicationList) {
                            if ("app_scene_linkage".equals(atApplicationBean.getApplicationIdentification())) {
                                mApplicationList.add(atApplicationBean);
                            }
                        }
                        for (ATApplicationBean atApplicationBean : applicationList) {
                            if ("app_home_security".equals(atApplicationBean.getApplicationIdentification())) {
                                mApplicationList.add(atApplicationBean);
                            }
                        }
                        for (ATApplicationBean atApplicationBean : applicationList) {
                            if ("app_voice_genie".equals(atApplicationBean.getApplicationIdentification())) {
                                mApplicationList.add(atApplicationBean);
                            }
                        }
                        mHomeAppRVAdapter.setLists(mApplicationList);
                        break;
                    case ATConstants.Config.SERVER_URL_CONTROL:
                        if (result.contains("success")) {
                            showToast(getString(R.string.at_operate_success));
                            mShortcutList.get(shortCutPosition).getAttributes().get(0).setValue(mShortcutList.get(shortCutPosition)
                                    .getAttributes().get(0).getValue().equals("1") ? "0" : "1");
                            mATHomeShortcutRVAdapter.setLists(mShortcutList);
                        }
                        break;
                }
            } else {
//                showToast(jsonResult.getString("message").equals("too many requests.") ?
//                        "我需要休息一下" : jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestComplete() {
        if (mWeatherBean != null && isAdded()) {
            tvOutsideTemp.setText(setSizeSpan(String.format(getString(R.string.at_temp_), mWeatherBean.getOutdoorsWeather().getTem()) + "℃"));
            tvWind.setText(setSizeSpan1(String.format(getString(R.string.at_wind_), mWeatherBean.getOutdoorsWeather().getWindLevel().getCnName())));
            tvWeather.setText(mWeatherBean.getOutdoorsWeather().getOutdoorsWeather().getCnName());
            tvPm.setText(setSizeSpan2(String.format(getString(R.string.at_pm_), mWeatherBean.getOutdoorsWeather().get_$PM25153())));
            if (!TextUtils.isEmpty(mWeatherBean.getOutdoorsWeather().get_$PM25153()) &&
                    Integer.parseInt(mWeatherBean.getOutdoorsWeather().get_$PM25153()) > 75 &&
                    Integer.parseInt(mWeatherBean.getOutdoorsWeather().get_$PM25153()) <= 150) {
                imgPmRed.setVisibility(View.GONE);
                imgPmYello.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(mWeatherBean.getOutdoorsWeather().get_$PM25153()) &&
                    Integer.parseInt(mWeatherBean.getOutdoorsWeather().get_$PM25153()) > 150) {
                imgPmYello.setVisibility(View.GONE);
                imgPmRed.setVisibility(View.VISIBLE);
            } else {
                imgPmRed.setVisibility(View.GONE);
                imgPmYello.setVisibility(View.GONE);
            }

            tvCity.setText(TextUtils.isEmpty(mWeatherBean.getOutdoorsWeather().getCity()) ? "上海" : mWeatherBean.getOutdoorsWeather().getCity());
            tvCity.setVisibility(View.VISIBLE);
            switch (mWeatherBean.getRoomWeather().getDeviceStatus()) {
                case 1:
                    llInside.setVisibility(View.VISIBLE);
                    tvInsideTemp.setText(String.format(getString(R.string.at_temp1_), mWeatherBean.getRoomWeather().getTemperature()));
                    tvWet.setText(String.format(getString(R.string.at_wet_), mWeatherBean.getRoomWeather().getHumidity()) + "%");
                    tvPmInside.setText(String.format(getString(R.string.at_pm_), mWeatherBean.getRoomWeather().get_$PM2577()));
                    if (!TextUtils.isEmpty(mWeatherBean.getRoomWeather().get_$PM2577()) &&
                            Integer.parseInt(mWeatherBean.getRoomWeather().get_$PM2577()) > 75 &&
                            Integer.parseInt(mWeatherBean.getRoomWeather().get_$PM2577()) <= 150) {
                        imgPmInsideRed.setVisibility(View.GONE);
                        imgPmInsideYello.setVisibility(View.VISIBLE);
                    } else if (!TextUtils.isEmpty(mWeatherBean.getRoomWeather().get_$PM2577()) &&
                            Integer.parseInt(mWeatherBean.getRoomWeather().get_$PM2577()) > 150) {
                        imgPmInsideYello.setVisibility(View.GONE);
                        imgPmInsideRed.setVisibility(View.VISIBLE);
                    } else {
                        imgPmInsideYello.setVisibility(View.GONE);
                        imgPmInsideRed.setVisibility(View.GONE);
                    }
                    if (String.format(getString(R.string.at_tvoc_), mWeatherBean.getRoomWeather().getTVOC()).equals("TVOC : null")
                            || TextUtils.isEmpty(String.format(getString(R.string.at_tvoc_), mWeatherBean.getRoomWeather().getTVOC()))) {
                        tvTvoc.setVisibility(View.GONE);
                    } else {
                        tvTvoc.setVisibility(View.VISIBLE);
                        tvTvoc.setText(String.format(getString(R.string.at_tvoc_), mWeatherBean.getRoomWeather().getTVOC()));
                    }
                    if (!TextUtils.isEmpty(mWeatherBean.getRoomWeather().getTVOC()) &&
                            Integer.parseInt(mWeatherBean.getRoomWeather().getTVOC()) > 0.3 &&
                            Integer.parseInt(mWeatherBean.getRoomWeather().getTVOC()) <= 0.5) {
                        imgTvocRed.setVisibility(View.GONE);
                        imgTvocYello.setVisibility(View.VISIBLE);
                    } else if (!TextUtils.isEmpty(mWeatherBean.getRoomWeather().getTVOC()) &&
                            Integer.parseInt(mWeatherBean.getRoomWeather().getTVOC()) > 0.5) {
                        imgTvocYello.setVisibility(View.GONE);
                        imgTvocRed.setVisibility(View.VISIBLE);
                    } else {
                        imgTvocRed.setVisibility(View.GONE);
                        imgTvocYello.setVisibility(View.GONE);
                    }
                    tvAddBox.setText("");
                    break;
                case 0:
                    llInside.setVisibility(View.GONE);
                    tvAddBox.setVisibility(View.VISIBLE);
                    tvAddBox.setText(getString(R.string.at_box_off_line));
                    break;
                case -1:
                    llInside.setVisibility(View.GONE);
                    tvAddBox.setVisibility(View.VISIBLE);
                    tvAddBox.setText(getString(R.string.at_add_box));
                    break;
            }
            imgBegin.setVisibility(View.VISIBLE);
            imgTo.setVisibility(View.VISIBLE);
            switch (mWeatherBean.getOutdoorsWeather().getOutdoorsWeather().getCode()) {
                case "21":
                    imgBegin.setImageResource(R.drawable.at_weather_07);
                    imgEnd.setImageResource(R.drawable.at_weather_08);
                    break;
                case "22":
                    imgBegin.setImageResource(R.drawable.at_weather_08);
                    imgEnd.setImageResource(R.drawable.at_weather_09);
                    break;
                case "23":
                    imgBegin.setImageResource(R.drawable.at_weather_09);
                    imgEnd.setImageResource(R.drawable.at_weather_10);
                    break;
                case "24":
                    imgBegin.setImageResource(R.drawable.at_weather_10);
                    imgEnd.setImageResource(R.drawable.at_weather_11);
                    break;
                case "25":
                    imgBegin.setImageResource(R.drawable.at_weather_11);
                    imgEnd.setImageResource(R.drawable.at_weather_12);
                    break;
                case "26":
                    imgBegin.setImageResource(R.drawable.at_weather_14);
                    imgEnd.setImageResource(R.drawable.at_weather_15);
                    break;
                case "27":
                    imgBegin.setImageResource(R.drawable.at_weather_15);
                    imgEnd.setImageResource(R.drawable.at_weather_16);
                    break;
                case "28":
                    imgBegin.setImageResource(R.drawable.at_weather_16);
                    imgEnd.setImageResource(R.drawable.at_weather_17);
                    break;
                default:
                    imgBegin.setVisibility(View.GONE);
                    imgTo.setVisibility(View.GONE);
                    imgEnd.setImageResource(ATResourceUtils.getResIdByName(String.format(getString(R.string.at_weather_), mWeatherBean.getOutdoorsWeather().getOutdoorsWeather().getCode()), ATResourceUtils.ResourceType.DRAWABLE));
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        request();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MSG_GET_WEATHER);
        handler.removeMessages(MSG_REFRESH_QRCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_HOME_CHANGE) {
                smartRefreshLayout.autoRefresh();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (1 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getActivity().startActivity(new Intent(getActivity(), ATCaptureActivity.class));
            } else {
                showToast("拍照权限被拒绝,请允许拍照");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}