package com.aliyun.ayland.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseFragment;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATAccessRecordHumanBean;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATUserFaceRecordRVAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;

import java.util.List;

public class ATUserFaceRecordFragment extends ATBaseFragment implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATUserFaceRecordRVAdapter mUserFaceCheckRvAdapter;
    private int pageNum = 0;
    private String openId = null;
    private boolean idStatus = false;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private ATHouseBean mATHouseBean;

    @Override
    protected int getLayoutId() {
        return R.layout.at_fragment_user_face_record;
    }

    @Override
    protected void findView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(getActivity());
    }

    public void queryVisitorRecord(int pageNum) {
        this.pageNum = pageNum;
        queryVisitorRecord();
    }

    private void queryVisitorRecord() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", idStatus ? openId : ATGlobalApplication.getOpenId());
        jsonObject.put("pageSize", 10);
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("mediaType", "face");
        jsonObject.put("buildingCode", mATHouseBean.getBuildingCode());
        jsonObject.put("villageId", mATHouseBean.getVillageId());
        mPresenter.request(ATConstants.Config.SERVER_URL_QUERYVISITORRECORD, jsonObject);
    }

    private void init() {
        openId = getActivity().getIntent().getStringExtra("openId");
        mATHouseBean = gson.fromJson(ATGlobalApplication.getHouse(), ATHouseBean.class);
        idStatus = !TextUtils.isEmpty(openId);
        mUserFaceCheckRvAdapter = new ATUserFaceRecordRVAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mUserFaceCheckRvAdapter);

        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            refreshLayout.finishLoadMore(2000);
            pageNum += 10;
            queryVisitorRecord();
        });
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                if (ATConstants.Config.SERVER_URL_QUERYVISITORRECORD.equals(url)) {
                    List<ATAccessRecordHumanBean> aTAccessRecordHumanBeanList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<ATAccessRecordHumanBean>>() {
                    }.getType());
                    if (aTAccessRecordHumanBeanList != null) {
                        mUserFaceCheckRvAdapter.setLists(aTAccessRecordHumanBeanList, pageNum);
                        if (aTAccessRecordHumanBeanList.size() == 0)
                            pageNum--;
                        if (aTAccessRecordHumanBeanList.size() < 10) {
                            smartRefreshLayout.setNoMoreData(true);
                        } else {
                            smartRefreshLayout.setNoMoreData(false);
                        }
                    } else {
                        showToast(jsonResult.getString("message"));
                    }
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