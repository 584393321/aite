package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATEnvironmentBean;
import com.aliyun.ayland.data.ATEnvironmentOutsideBottom;
import com.aliyun.ayland.data.ATEnvironmentTopBean;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATEnvironmentRVAdapter;
import com.aliyun.ayland.widget.ATIndexHorizontalScrollView;
import com.aliyun.ayland.widget.ATSmoothnessLayoutManage;
import com.aliyun.ayland.widget.ATToday24HourView;
import com.aliyun.ayland.widget.popup.ATEnvironmentTimePopup;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATEnvironmentCommunityFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATEnvironmentRVAdapter mATEnvironmentRVAdapter;
    private ATHouseBean mATHouseBean;
    private int category = 1, timeInterval = 1;
    private double max = 0, min = 0;
    private ATEnvironmentTimePopup mATEnvironmentTimePopup;
    private ATToday24HourView today24HourView;
    private ATIndexHorizontalScrollView indexHorizontalScrollView;
    private RecyclerView recyclerView;
    private TextView tvTime, tvNoData, tvTemp, tvWet, tvPm, tvNoise;
    private List<String> mTime = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_environment_community;
    }

    @Override
    protected void findView(View view) {
        today24HourView = view.findViewById(R.id.today24HourView);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTime = view.findViewById(R.id.tv_time);
        tvNoData = view.findViewById(R.id.tv_no_data);
        tvTemp = view.findViewById(R.id.tv_temp);
        tvWet = view.findViewById(R.id.tv_wet);
        tvPm = view.findViewById(R.id.tv_pm);
        tvNoise = view.findViewById(R.id.tv_noise);
        indexHorizontalScrollView = view.findViewById(R.id.indexHorizontalScrollView);
        EventBus.getDefault().register(this);
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger ATEventInteger) {
        if ("ConvenientShareGYMActivity2".equals(ATEventInteger.getClazz())) {
            tvTime.setText(mTime.get(ATEventInteger.getPosition()));
            timeInterval = ATEventInteger.getPosition() == 0 ? 1 : (ATEventInteger.getPosition() == 1 ? 3 : 4);
            getOutdoorEnvironmentHistoryData();
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
        request();
    }

    public void request() {
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
        tvTime.setOnClickListener(view -> mATEnvironmentTimePopup.showPopupWindow());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTHISTORYDATA:
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
                    case ATConstants.Config.SERVER_URL_GETOUTDOORENVIRONMENTDATA:
                        ATEnvironmentTopBean ATEnvironmentTopBean = gson.fromJson(jsonResult.getJSONObject("data").getString("environmentData"), ATEnvironmentTopBean.class);
                        tvTemp.setText(ATEnvironmentTopBean.getTem());
                        tvWet.setText(ATEnvironmentTopBean.getHumidity() + "  ");
                        tvPm.setText(ATEnvironmentTopBean.getAir_pm25());
                        tvNoise.setText(ATEnvironmentTopBean.getSoundDecibelValue() + "dB");
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