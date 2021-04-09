package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATMessageCenterBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.ui.adapter.ATMessageCenterRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ATMessageCenterActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private ATMessageCenterRVAdapter mATMessageCenterRVAdapter;
    private ATMyTitleBar titlebar;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_recycleview_sml;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    private void init() {
        titlebar.setTitle(getString(R.string.at_message_center));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATMessageCenterRVAdapter = new ATMessageCenterRVAdapter(this);
        recyclerView.setAdapter(mATMessageCenterRVAdapter);
        List<ATMessageCenterBean> list = new ArrayList<>();
        ATMessageCenterBean mATMessageCenterBean1 = new ATMessageCenterBean("设备", "灯已经打开", "2019-5-30", "deng");
        ATMessageCenterBean mATMessageCenterBean2 = new ATMessageCenterBean("通知", "灯已经打开", "2019-5-30", "tongzhi");
        ATMessageCenterBean mATMessageCenterBean3 = new ATMessageCenterBean("社区服务", "缴费到期", "2019-5-30", "fangzi");
        list.add(mATMessageCenterBean1);
        list.add(mATMessageCenterBean2);
        list.add(mATMessageCenterBean3);
        mATMessageCenterRVAdapter.setLists(list);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:

                        break;
                }
            } else {
                closeBaseProgressDlg();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}