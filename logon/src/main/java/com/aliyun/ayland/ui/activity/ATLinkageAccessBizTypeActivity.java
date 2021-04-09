package com.aliyun.ayland.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATBizTypeBean;
import com.aliyun.ayland.ui.adapter.ATBizTypeRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageAccessBizTypeActivity extends ATBaseActivity {
    private ATBizTypeRVAdapter mATBizTypeRVAdapter;
    private int current_position = 0;
    private List<ATBizTypeBean> bizTypeList = new ArrayList<>();
    private ATMyTitleBar titlebar;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_recycleview;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        recyclerView = findViewById(R.id.recyclerView);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        JSONObject params = JSONObject.parseObject(getIntent().getStringExtra("params"));
        boolean replace = getIntent().getBooleanExtra("replace", false);
        String content = getIntent().getStringExtra("content");
        ATBizTypeBean ATBizTypeBean1 = new ATBizTypeBean();
        ATBizTypeBean ATBizTypeBean2 = new ATBizTypeBean();
        ATBizTypeBean ATBizTypeBean3 = new ATBizTypeBean();
        switch (getIntent().getIntExtra("bizType", 101)){
            case 101:
                ATBizTypeBean1.setBizType("FACE");
                ATBizTypeBean1.setBizTypeCN("进和出");
                bizTypeList.add(ATBizTypeBean1);
                titlebar.setTitle(getString(R.string.at_person_access));
                break;
            case 102:
                ATBizTypeBean1.setBizType("ENTRANCE_IN");
                ATBizTypeBean1.setBizTypeCN("进");
                ATBizTypeBean2.setBizType("ENTRANCE_OUT");
                ATBizTypeBean2.setBizTypeCN("出");
                ATBizTypeBean3.setBizType("ENTRANCE");
                ATBizTypeBean3.setBizTypeCN("进和出");
                bizTypeList.add(ATBizTypeBean1);
                bizTypeList.add(ATBizTypeBean2);
                bizTypeList.add(ATBizTypeBean3);
                titlebar.setTitle(getString(R.string.at_person_access));
                break;
            case 103:
                ATBizTypeBean1.setBizType("CAR_IN");
                ATBizTypeBean1.setBizTypeCN("进");
                ATBizTypeBean2.setBizType("CAR_OUT");
                ATBizTypeBean2.setBizTypeCN("出");
                ATBizTypeBean3.setBizType("CAR");
                ATBizTypeBean3.setBizTypeCN("进和出");
                bizTypeList.add(ATBizTypeBean1);
                bizTypeList.add(ATBizTypeBean2);
                bizTypeList.add(ATBizTypeBean3);
                titlebar.setTitle(getString(R.string.at_vehicle_access));
                break;
            case 109:
                break;
        }
        if (replace){
            for (int i = 0; i < bizTypeList.size(); i++) {
                if(content.split(" ")[1].equals(bizTypeList.get(i).getBizTypeCN())){
                    current_position = i;
                    break;
                }
            }
            content = content.split(" ")[0];
        }
        String finalContent = content;
        titlebar.setRightButtonText(getString(R.string.at_done));

        titlebar.setRightClickListener(() -> {
            params.put("bizType", bizTypeList.get(current_position).getBizType());
            setResult(RESULT_OK, getIntent()
                    .putExtra("content", finalContent + " " + bizTypeList.get(current_position).getBizTypeCN())
                    .putExtra("params", params.toJSONString()));
            finish();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mATBizTypeRVAdapter = new ATBizTypeRVAdapter(this);
        recyclerView.setAdapter(mATBizTypeRVAdapter);
        mATBizTypeRVAdapter.setOnItemClickListener((view, position) -> {
            current_position = position;
        });
        mATBizTypeRVAdapter.setList(bizTypeList, current_position);
    }
}