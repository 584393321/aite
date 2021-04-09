package com.aliyun.ayland.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATEnvironmentBean;
import com.aliyun.ayland.data.ATEnvironmentOutsideBottom;
import com.aliyun.ayland.data.ATEnvironmentTopBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.activity.ATDiscoveryDeviceActivity;
import com.aliyun.ayland.ui.adapter.ATEnvironmentRVAdapter;
import com.aliyun.ayland.widget.ATIndexHorizontalScrollView;
import com.aliyun.ayland.widget.ATSmoothnessLayoutManage;
import com.aliyun.ayland.widget.ATToday24HourView;
import com.aliyun.ayland.widget.popup.ATGymDatePopup;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATEnvironmentInsideFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATHouseBean mATHouseBean;
    private String category = "1";
    private double max = 0, min = 0;
    private int timeInterval = 1;
    private ATGymDatePopup mATGymDatePopup;
    private RecyclerView recyclerView;
    private ATToday24HourView today24HourView;
    private ATIndexHorizontalScrollView indexHorizontalScrollView;
    private RelativeLayout rlNoData;
    private LinearLayout llContent;
    private Button button;
    private TextView tvTime, tvNoData, tvTemp, tvWet, tvPm, tvHcho, tvCo2, tvTvoc;
    private View vHcho, vCo2, vTvoc, vWaterPurifier;
    private List<String> mTime = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_environment_inside;
    }

    @Override
    protected void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        today24HourView = view.findViewById(R.id.today24HourView);
        indexHorizontalScrollView = view.findViewById(R.id.indexHorizontalScrollView);
        tvTime = view.findViewById(R.id.tv_time);
        tvNoData = view.findViewById(R.id.tv_no_data);
        tvTemp = view.findViewById(R.id.tv_temp);
        tvWet = view.findViewById(R.id.tv_wet);
        tvPm = view.findViewById(R.id.tv_pm);
        tvHcho = view.findViewById(R.id.tv_hcho);
        tvCo2 = view.findViewById(R.id.tv_co2);
        tvTvoc = view.findViewById(R.id.tv_tvoc);
        vHcho = view.findViewById(R.id.v_hcho);
        vCo2 = view.findViewById(R.id.v_co2);
        vTvoc = view.findViewById(R.id.v_tvoc);
        vWaterPurifier = view.findViewById(R.id.v_water_purifier);
        rlNoData = view.findViewById(R.id.rl_no_data);
        llContent = view.findViewById(R.id.ll_content);
        button = view.findViewById(R.id.button);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        request();
    }

    public void request() {
        if (mATHouseBean == null)
            return;
        getIndoorEnvironmentData();
        getIndoorEnvironmentHistoryData();
    }

    private void getIndoorEnvironmentHistoryData() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("category", category);
        jsonObject.put("timeInterval", timeInterval);
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETINDOORENVIRONMENTHISTORYDATA, jsonObject);
    }

    private void getIndoorEnvironmentData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETINDOORENVIRONMENTDATA, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        recyclerView.setLayoutManager(new ATSmoothnessLayoutManage(getActivity(), LinearLayout.HORIZONTAL, false));
        ATEnvironmentRVAdapter ATEnvironmentRVAdapter = new ATEnvironmentRVAdapter(getActivity());
        recyclerView.setAdapter(ATEnvironmentRVAdapter);
        List<ATEnvironmentBean> list = new ArrayList<>();
        ATEnvironmentBean ATEnvironmentBean = new ATEnvironmentBean();
        ATEnvironmentBean.setTemp("温度");
        ATEnvironmentBean.setUnit("单位：℃");
        ATEnvironmentBean.setCategory("1");
        ATEnvironmentBean ATEnvironmentBean1 = new ATEnvironmentBean();
        ATEnvironmentBean1.setTemp("湿度");
        ATEnvironmentBean1.setUnit("单位：%");
        ATEnvironmentBean1.setCategory("2");
        ATEnvironmentBean ATEnvironmentBean3 = new ATEnvironmentBean();
        ATEnvironmentBean3.setTemp("PM2.5");
        ATEnvironmentBean3.setUnit("单位：ug/m3");
        ATEnvironmentBean3.setCategory("4");
//        ATEnvironmentBean ATEnvironmentBean4 = new ATEnvironmentBean();
//        ATEnvironmentBean4.setTemp("HCHO");
//        ATEnvironmentBean4.setUnit("单位：mg/m3");
//        ATEnvironmentBean4.setCategory("6");
//        ATEnvironmentBean ATEnvironmentBean5 = new ATEnvironmentBean();
//        ATEnvironmentBean5.setTemp("CO2");
//        ATEnvironmentBean5.setUnit("单位：mg/m3");
//        ATEnvironmentBean5.setCategory("7");
//        ATEnvironmentBean ATEnvironmentBean6 = new ATEnvironmentBean();
//        ATEnvironmentBean6.setTemp("TVOC");
//        ATEnvironmentBean6.setUnit("单位：mg/m3");
//        ATEnvironmentBean6.setCategory("8");
        list.add(ATEnvironmentBean);
        list.add(ATEnvironmentBean1);
        list.add(ATEnvironmentBean3);
//        list.add(ATEnvironmentBean4);
//        list.add(ATEnvironmentBean5);
//        list.add(ATEnvironmentBean6);
        ATEnvironmentRVAdapter.setLists(list);
        ATEnvironmentRVAdapter.setOnItemClickListener((view, o, position) -> {
            category = list.get(position).getCategory();
            showBaseProgressDlg();
            getIndoorEnvironmentHistoryData();
        });
        mATGymDatePopup = new ATGymDatePopup(getActivity(), integer -> {
            tvTime.setText(mTime.get(integer));
            timeInterval = integer == 0 ? 1 : (integer == 1 ? 3 : 4);
            getIndoorEnvironmentHistoryData();
        });
        mTime.clear();
        mTime.add("24小时");
        mTime.add("30天");
        mTime.add("一年");
        mATGymDatePopup.setList(mTime);
        tvTime.setOnClickListener(view -> mATGymDatePopup.showPopupWindow());

        button.setOnClickListener(view -> startActivity(new Intent(getActivity(), ATDiscoveryDeviceActivity.class)));
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETINDOORENVIRONMENTDATA:
                        ATEnvironmentTopBean aTEnvironmentTopBean = gson.fromJson(jsonResult.getJSONObject("data").getString("environmentData"), ATEnvironmentTopBean.class);
                        if(aTEnvironmentTopBean.getDeviceStatus() == -1){
                            llContent.setVisibility(View.GONE);
                            rlNoData.setVisibility(View.VISIBLE);
                        }else {
                            llContent.setVisibility(View.VISIBLE);
                            rlNoData.setVisibility(View.GONE);
                        }
                        tvTemp.setText(aTEnvironmentTopBean.getTem());
                        tvWet.setText(aTEnvironmentTopBean.getHumidity());
                        tvPm.setText(aTEnvironmentTopBean.getAir_pm25());
                        tvHcho.setText(String.valueOf(aTEnvironmentTopBean.getHCHO()));
                        tvCo2.setText(String.valueOf(aTEnvironmentTopBean.getCO2()));
                        tvTvoc.setText(String.valueOf(aTEnvironmentTopBean.getTVOC()));
                        int maxValue;
                        if (aTEnvironmentTopBean.getCO2() > aTEnvironmentTopBean.getHCHO()) {
                            maxValue = Math.max(aTEnvironmentTopBean.getTVOC(), aTEnvironmentTopBean.getCO2());
                        } else {
                            if (aTEnvironmentTopBean.getTVOC() < aTEnvironmentTopBean.getCO2()) {
                                maxValue = aTEnvironmentTopBean.getHCHO();
                            } else {
                                maxValue = Math.max(aTEnvironmentTopBean.getTVOC(), aTEnvironmentTopBean.getHCHO());
                            }
                        }
                        if(maxValue == 0)
                            return;
                        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) vHcho.getLayoutParams();
                        linearParams.width = 600 * aTEnvironmentTopBean.getHCHO() / maxValue;
                        vHcho.setLayoutParams(linearParams);

                        LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) vCo2.getLayoutParams();
                        linearParams1.width = 600 * aTEnvironmentTopBean.getCO2() / maxValue;
                        vCo2.setLayoutParams(linearParams1);

                        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) vTvoc.getLayoutParams();
                        linearParams2.width = 600 * aTEnvironmentTopBean.getTVOC() / maxValue;
                        vTvoc.setLayoutParams(linearParams2);

                        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) vTvoc.getLayoutParams();
                        linearParams3.width = 600 * aTEnvironmentTopBean.getTVOC() / maxValue;
                        vWaterPurifier.setLayoutParams(linearParams3);
                        break;
                    case ATConstants.Config.SERVER_URL_GETINDOORENVIRONMENTHISTORYDATA:
                        List<ATEnvironmentOutsideBottom> ATEnvironmentOutsideBottomList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATEnvironmentOutsideBottom>>() {
                        }.getType());
                        for (ATEnvironmentOutsideBottom ATEnvironmentOutsideBottom : ATEnvironmentOutsideBottomList) {
                            max = Math.max(max, ATEnvironmentOutsideBottom.getNum());
                            min = Math.min(min, ATEnvironmentOutsideBottom.getNum());
                        }
                        today24HourView.initEnvironmentOutsideBottoms(ATEnvironmentOutsideBottomList, max, min);
                        indexHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                        if (ATEnvironmentOutsideBottomList.size() == 0) {
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            tvNoData.setVisibility(View.GONE);
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