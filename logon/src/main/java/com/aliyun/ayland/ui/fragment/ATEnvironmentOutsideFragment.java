package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.aliyun.ayland.ui.adapter.ATEnvironmentRVAdapter;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.ATIndexHorizontalScrollView;
import com.aliyun.ayland.widget.ATSmoothnessLayoutManage;
import com.aliyun.ayland.widget.ATToday24HourView;
import com.aliyun.ayland.widget.popup.ATEnvironmentTimePopup;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATEnvironmentOutsideFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATEnvironmentRVAdapter mATEnvironmentRVAdapter;
    private ATHouseBean mATHouseBean;
    private int category = 1, timeInterval = 1;
    private double max = 0, min = 0;
    private ATEnvironmentTimePopup mATEnvironmentTimePopup;
    private List<String> mTime = new ArrayList<>();
    private RecyclerView recyclerView;
    private ATToday24HourView today24HourView;
    private ATIndexHorizontalScrollView indexHorizontalScrollView;
    private TextView tvTime, tvNoData, tvTemp, tvWet, tvPm, tvWeather, tvCity, tvWind, tvAirQuality, tvNoise, tvTips;
    private ImageView imgBegin, imgTo, imgEnd;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_environment_outside;
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
        tvWeather = view.findViewById(R.id.tv_weather);
        tvCity = view.findViewById(R.id.tv_city);
        tvWind = view.findViewById(R.id.tv_wind);
        tvAirQuality = view.findViewById(R.id.tv_air_quality);
        tvNoise = view.findViewById(R.id.tv_noise);
        tvTips = view.findViewById(R.id.tv_tips);
        imgBegin = view.findViewById(R.id.img_begin);
        imgTo = view.findViewById(R.id.img_to);
        imgEnd = view.findViewById(R.id.img_end);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        request();
    }

    public void request() {
        if(mATHouseBean == null)
            return;
        getOutdoorEnvironmentData();
        getOutdoorEnvironmentHistoryData();
    }

    private void getOutdoorEnvironmentHistoryData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("category", category);
        jsonObject.put("timeInterval", timeInterval);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTHISTORYDATA, jsonObject);
    }

    private void getOutdoorEnvironmentData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTDATA, jsonObject);
    }

    private void init() {
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);

        recyclerView.setLayoutManager(new ATSmoothnessLayoutManage(getActivity(), LinearLayout.HORIZONTAL, false));
        mATEnvironmentRVAdapter = new ATEnvironmentRVAdapter(getActivity());
        recyclerView.setAdapter(mATEnvironmentRVAdapter);
        List<ATEnvironmentBean> list = new ArrayList<>();
        ATEnvironmentBean ATEnvironmentBean = new ATEnvironmentBean();
        ATEnvironmentBean.setTemp("温度");
        ATEnvironmentBean.setUnit("单位：℃");
        ATEnvironmentBean ATEnvironmentBean1 = new ATEnvironmentBean();
        ATEnvironmentBean1.setTemp("湿度");
        ATEnvironmentBean1.setUnit("单位：%");
        ATEnvironmentBean ATEnvironmentBean3 = new ATEnvironmentBean();
        ATEnvironmentBean3.setTemp("PM2.5");
        ATEnvironmentBean3.setUnit("单位：ug/m3");
//        ATEnvironmentBean ATEnvironmentBean4 = new ATEnvironmentBean();
//        ATEnvironmentBean4.setTemp("风力");
//        ATEnvironmentBean4.setUnit("单位：m/s");
//        ATEnvironmentBean ATEnvironmentBean5 = new ATEnvironmentBean();
//        ATEnvironmentBean5.setTemp("噪声");
//        ATEnvironmentBean5.setUnit("单位：dB");
        list.add(ATEnvironmentBean);
        list.add(ATEnvironmentBean1);
        list.add(ATEnvironmentBean3);
//        list.add(ATEnvironmentBean4);
//        list.add(ATEnvironmentBean5);
        mATEnvironmentRVAdapter.setLists(list);
        mATEnvironmentRVAdapter.setOnItemClickListener((view, o, position) -> {
            category = ++position;
            showBaseProgressDlg();
            getOutdoorEnvironmentHistoryData();
        });
        mATEnvironmentTimePopup = new ATEnvironmentTimePopup(getActivity());
        mTime.clear();
        mTime.add("24小时");
        mTime.add("30天");
        mTime.add("一年");
        mATEnvironmentTimePopup.setList(mTime);
        mATEnvironmentTimePopup.setOnItemClickListener((view, o, position) -> {
            tvTime.setText(mTime.get(position));
            timeInterval = position == 0 ? 1 : (position == 1 ? 3 : 4);
            getOutdoorEnvironmentHistoryData();

        });
        tvTime.setOnClickListener(view -> mATEnvironmentTimePopup.showPopupWindow());
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if(getActivity() == null)
                return;
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTHISTORYDATA:
                        List<ATEnvironmentOutsideBottom> aTEnvironmentOutsideBottomList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATEnvironmentOutsideBottom>>() {
                        }.getType());
                        for (ATEnvironmentOutsideBottom atEnvironmentOutsideBottom : aTEnvironmentOutsideBottomList) {
                            max = Math.max(max, atEnvironmentOutsideBottom.getNum());
                            min = Math.min(min, atEnvironmentOutsideBottom.getNum());
                        }
                        today24HourView.initEnvironmentOutsideBottoms(aTEnvironmentOutsideBottomList, max, min);
                        indexHorizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                        if(aTEnvironmentOutsideBottomList.size() == 0){
                            tvNoData.setVisibility(View.VISIBLE);
                        }else {
                            tvNoData.setVisibility(View.GONE);
                        }
                        break;
                    case ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTDATA:
                        ATEnvironmentTopBean ATEnvironmentTopBean = gson.fromJson(jsonResult.getJSONObject("data").getString("environmentData"), ATEnvironmentTopBean.class);
                        tvTemp.setText(ATEnvironmentTopBean.getTem());
                        tvWeather.setText(ATEnvironmentTopBean.getWea());
                        tvCity.setText(ATEnvironmentTopBean.getCity());
                        tvWet.setText(ATEnvironmentTopBean.getHumidity() + "  ");
                        tvPm.setText(ATEnvironmentTopBean.getAir_pm25() + "ug/m3");
                        tvWind.setText(ATEnvironmentTopBean.getWin_speed());
                        tvAirQuality.setText(ATEnvironmentTopBean.getAir_level());
                        tvNoise.setText(ATEnvironmentTopBean.getSoundDecibelValue() + "dB");
                        tvTips.setText(ATEnvironmentTopBean.getAir_tips());
                        imgBegin.setVisibility(View.VISIBLE);
                        imgTo.setVisibility(View.VISIBLE);
                        switch (ATEnvironmentTopBean.getWeatherCode()) {
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
                                imgEnd.setImageResource(ATResourceUtils.getResIdByName(String.format(getString(R.string.at_weather_)
                                        , ATEnvironmentTopBean.getWeatherCode()), ATResourceUtils.ResourceType.DRAWABLE));
                                break;
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