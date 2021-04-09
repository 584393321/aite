package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATDeviceTslOutputDataType;
import com.aliyun.ayland.ui.adapter.ATLinkageStatusToRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import java.util.List;

import static com.aliyun.ayland.ui.activity.ATLinkageAddActivity.REQUEST_CODE_ADD_CONDITION;

public class ATLinkageStatusToActivity extends ATBaseActivity {
    private List<ATDeviceTslOutputDataType> mATDeviceTslOutputDataTypeList;
    private ATMyTitleBar titlebar;
    private RecyclerView rvTca;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_linkage_status_choise;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        rvTca = findViewById(R.id.rv_tca);
        init();
    }

    @Override
    protected void initPresenter() {

    }

    private void init() {
        titlebar.setTitle(getIntent().getStringExtra("content"));

        JSONObject params = JSONObject.parseObject(getIntent().getStringExtra("params"));
        mATDeviceTslOutputDataTypeList = getIntent().getParcelableArrayListExtra("deviceTslOutputDataType");

        rvTca.setVisibility(View.VISIBLE);
        ATLinkageStatusToRVAdapter ATLinkageStatusToRVAdapter = new ATLinkageStatusToRVAdapter(this, mATDeviceTslOutputDataTypeList);
        rvTca.setLayoutManager(new LinearLayoutManager(this));
        rvTca.setAdapter(ATLinkageStatusToRVAdapter);

        ATLinkageStatusToRVAdapter.setOnRVClickListener((view, position) -> {
            params.put("propertyName", mATDeviceTslOutputDataTypeList.get(position).getIdentifier());
            startActivityForResult(getIntent().putExtra("dataType", mATDeviceTslOutputDataTypeList.get(position).getDataType().toJSONString())
                    .putExtra("params", params.toJSONString())
                    .putExtra("content", getIntent().getStringExtra("content") + " " + mATDeviceTslOutputDataTypeList.get(position).getName())
                    .setClass(ATLinkageStatusToActivity.this, ATLinkageStatusChoiseActivity.class), REQUEST_CODE_ADD_CONDITION);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_CONDITION) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}