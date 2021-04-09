package com.aliyun.ayland.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATParkingBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATVehicleCheckRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATVehicleCheckActivity extends ATBaseActivity implements ATMainContract.View {
    private static final int MSG_SINGAL = 1001;
    private static final String MSG_KEY = "ATMainActivity.MSG_KEY";
    private ATMainPresenter mPresenter;
    private ATVehicleCheckRVAdapter mATVehicleCheckRVAdapter;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SINGAL:
                    refreshListView(msg.getData().getString(MSG_KEY));
                    break;
                default:
                    break;
            }
        }
    };
    private List<ATParkingBean> mATParkingBeanList = new ArrayList<>();
    private List<ATParkingBean> mParkingSearchList = new ArrayList<>();
    private ATMyTitleBar titlebar;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private RelativeLayout rlSearch;
    private TextView tvHint, tvCancel;
    private EditText etSearchContent;


    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_vehicle_check;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        rlSearch = findViewById(R.id.rl_search);
        etSearchContent = findViewById(R.id.et_search_content);
        tvHint = findViewById(R.id.tv_hint);
        tvCancel = findViewById(R.id.tv_cancel);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
        findParkingData();
    }

    private void findParkingData() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FINDPARKINGDATA, jsonObject);
    }

    private void init() {
        titlebar.setTitle(getString(R.string.at_vehicle_check));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATVehicleCheckRVAdapter = new ATVehicleCheckRVAdapter(this);
        recyclerView.setAdapter(mATVehicleCheckRVAdapter);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
            findParkingData();
        });

        rlSearch.setOnClickListener((View.OnClickListener) view -> {
            if (View.GONE == etSearchContent.getVisibility()) {
                tvHint.setVisibility(View.GONE);
                etSearchContent.setVisibility(View.VISIBLE);
                etSearchContent.setFocusableInTouchMode(true);
                etSearchContent.setFocusable(true);
                etSearchContent.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) etSearchContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etSearchContent, 0);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        etSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Message msg = new Message();
                msg.what = MSG_SINGAL;
                Bundle data = new Bundle();
                data.putString(MSG_KEY, s.toString());
                msg.setData(data);
                myHandler.sendMessage(msg);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //根据搜索字符（串）更新ListView显示数据
    private void refreshListView(String searchString) {
        mParkingSearchList.clear();
        for (int i = 0; i < mATParkingBeanList.size(); i++) {
            if (mATParkingBeanList.get(i).getParkname().toLowerCase().contains(searchString.toLowerCase())) {
                mParkingSearchList.add(mATParkingBeanList.get(i));
            }
        }
        mATVehicleCheckRVAdapter.setLists(mParkingSearchList);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_FINDPARKINGDATA:
                        mATParkingBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATParkingBean>>() {
                        }.getType());
                        if (TextUtils.isEmpty(etSearchContent.getText().toString()))
                            mATVehicleCheckRVAdapter.setLists(mATParkingBeanList);
                        else {
                            mParkingSearchList.clear();
                            for (int i = 0; i < mATParkingBeanList.size(); i++) {
                                if (mATParkingBeanList.get(i).getParkname().toLowerCase().contains(etSearchContent.getText().toString().toLowerCase())) {
                                    mParkingSearchList.add(mATParkingBeanList.get(i));
                                }
                            }
                            mATVehicleCheckRVAdapter.setLists(mParkingSearchList);
                        }
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
            }
            closeBaseProgressDlg();
            smartRefreshLayout.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
