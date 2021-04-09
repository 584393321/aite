package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATSceneManualTitle;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATLinkageIconRVAdapter;
import com.anthouse.xuhui.R;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.List;

public class ATLinkageIconActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private String sceneId;
    private ATSceneManualTitle mATSceneManualTitle;
    private List<String> mSceneIconList;
    private ATLinkageIconRVAdapter mATLinkageIconRVAdapter;
    private int select_position = -1;
    private RecyclerView rvRoomPic;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_icon;
    }

    @Override
    protected void findView() {
        rvRoomPic = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);

        if (TextUtils.isEmpty(ATGlobalApplication.getAllSceneIcon())) {
            linkageImageList();
        } else {
            mSceneIconList = gson.fromJson(ATGlobalApplication.getAllSceneIcon(), new TypeToken<List<String>>() {
            }.getType());
            if (!TextUtils.isEmpty(mATSceneManualTitle.getScene_icon()))
                for (int i = 0; i < mSceneIconList.size(); i++) {
                    if (mATSceneManualTitle.getScene_icon().equals(mSceneIconList.get(i))) {
                        select_position = i;
                        break;
                    }
                }
            mATLinkageIconRVAdapter.setList(mSceneIconList, select_position);
        }
    }

    public void linkageImageList() {
        showBaseProgressDlg();
        JSONObject jsonObject = new JSONObject();
        mPresenter.request(ATConstants.Config.SERVER_URL_LINKAGEIMAGELIST, jsonObject);
    }

    public void baseinfoUpdate() {
        showBaseProgressDlg();
//
        JSONObject operator = new JSONObject();
        operator.put("hid", ATGlobalApplication.getOpenId());
        operator.put("hidType", "OPEN");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sceneId", mATSceneManualTitle.getScene_id());
        jsonObject.put("name", mATSceneManualTitle.getName());
        jsonObject.put("icon", mSceneIconList.get(select_position));
        jsonObject.put("description", "");
        jsonObject.put("operator", operator);
        jsonObject.put("iotToken", ATGlobalApplication.getIoTToken());
        mPresenter.request(ATConstants.Config.SERVER_URL_BASEINFOUPDATE, jsonObject);
    }

    private void init() {
        mATSceneManualTitle = getIntent().getParcelableExtra("ATSceneManualTitle");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvRoomPic.setLayoutManager(gridLayoutManager);
        mATLinkageIconRVAdapter = new ATLinkageIconRVAdapter(this);
        mATLinkageIconRVAdapter.setOnItemClickListener((view, position) -> {
            select_position = position;
            if (TextUtils.isEmpty(mATSceneManualTitle.getScene_id())) {
                setResult(RESULT_OK, new Intent().putExtra("scene_icon", mSceneIconList.get(position)));
                finish();
            } else {
                baseinfoUpdate();
            }
        });
        rvRoomPic.setAdapter(mATLinkageIconRVAdapter);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_LINKAGEIMAGELIST:
                        ATGlobalApplication.setAllSceneIcon(jsonResult.getString("data"));
                        mSceneIconList = gson.fromJson(jsonResult.getString("data"), new TypeToken<List<String>>() {
                        }.getType());
                        if (!TextUtils.isEmpty(mATSceneManualTitle.getScene_icon()))
                            for (int i = 0; i < mSceneIconList.size(); i++) {
                                if (mATSceneManualTitle.getScene_icon().equals(mSceneIconList.get(i))) {
                                    select_position = i;
                                    break;
                                }
                            }
                        mATLinkageIconRVAdapter.setList(mSceneIconList, select_position);
                        break;
                    case ATConstants.Config.SERVER_URL_BASEINFOUPDATE:
                        setResult(RESULT_OK, new Intent().putExtra("scene_icon", mSceneIconList.get(select_position)));
                        finish();
                        break;
                }
            } else {
                showToast(jsonResult.getString("message"));
                select_position = -1;
                mATLinkageIconRVAdapter.setList(mSceneIconList, select_position);
            }
            closeBaseProgressDlg();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}