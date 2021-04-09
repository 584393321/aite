package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.data.ATUserFaceCheckBean;
import com.aliyun.ayland.data.ATUserFaceCheckListBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATUserFaceCheckRvAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATUserFaceCheckFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATUserFaceCheckRvAdapter mATUserFaceCheckRvAdapter;
    private List<ATUserFaceCheckListBean> allUserFaceCheckList = new ArrayList<>();
    private ATHouseBean mATHouseBean;
    private RecyclerView rvHomeDevice;
    private String openId = null, personCode = null;
    private boolean idStatus = false;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_home_inner;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ATEventInteger ATEventInteger) {
        if ("ATUserFaceCheckFragment".equals(ATEventInteger.getClazz())) {
            showBaseProgressDlg();
            addUserFaceVillage((ATUserFaceCheckBean) mATUserFaceCheckRvAdapter.getData().get(ATEventInteger.getPosition()));
        }
    }

    @Override
    protected void findView(View view) {
        rvHomeDevice = view.findViewById(R.id.rv_home_device);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
    }

    private void faceVillageList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", idStatus ? openId : ATGlobalApplication.getOpenId());
        mPresenter.request(ATConstants.Config.SERVER_URL_FACEVILLAGELIST, jsonObject);
    }

    private void addUserFaceVillage(ATUserFaceCheckBean ATUserFaceCheckBean) {
        JSONObject jsonObject = new JSONObject();
        JSONArray scopeIdList = new JSONArray();
        scopeIdList.add(ATUserFaceCheckBean.getDeviceId());
        JSONArray userIdList = new JSONArray();
        userIdList.add(idStatus ? openId : ATGlobalApplication.getOpenId());
        jsonObject.put("scopeIdList", scopeIdList);
        jsonObject.put("userIdList", userIdList);
        jsonObject.put("scopeType", "IOT_ID");
        jsonObject.put("userType", "OPEN");
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        jsonObject.put("deviceType", TextUtils.isEmpty(ATUserFaceCheckBean.getDeviceType()) ? 1 : 2);
        mPresenter.request(ATConstants.Config.SERVER_URL_ADDUSERFACEVILLAGE, jsonObject);
    }

    private void init() {
        openId = getActivity().getIntent().getStringExtra("openId");
        personCode = getActivity().getIntent().getStringExtra("personCode");
        idStatus = !TextUtils.isEmpty(openId);
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        mATUserFaceCheckRvAdapter = new ATUserFaceCheckRvAdapter();
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        rvHomeDevice.setLayoutManager(layoutManager1);
        rvHomeDevice.setHasFixedSize(true);
        rvHomeDevice.setAdapter(mATUserFaceCheckRvAdapter);
    }

    public void setList(List<ATUserFaceCheckListBean> allUserFaceCheckList) {
        mATUserFaceCheckRvAdapter.setList(allUserFaceCheckList);
    }

    public void setJsonResult(org.json.JSONObject jsonResult) {
        try {
            allUserFaceCheckList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATUserFaceCheckListBean>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mATUserFaceCheckRvAdapter.setList(allUserFaceCheckList);
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
                    case ATConstants.Config.SERVER_URL_ADDUSERFACEVILLAGE:
                        faceVillageList();
                        break;
                    case ATConstants.Config.SERVER_URL_FACEVILLAGELIST:
                        setJsonResult(jsonResult);
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