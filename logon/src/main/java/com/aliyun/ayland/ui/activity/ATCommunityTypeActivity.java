package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATCommunityTypeBean;
import com.aliyun.ayland.ui.adapter.ATCommunityTypeRVAdapter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import java.util.ArrayList;

public class ATCommunityTypeActivity extends ATBaseActivity {
    private ATMyTitleBar titlebar;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_recycleview1;
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
        titlebar.setTitle(getString(R.string.at_community_style));
        ArrayList<ATCommunityTypeBean> mATCommunityTypeBeanList = getIntent().getParcelableArrayListExtra("CommunityTypeBeanList");
        ATCommunityTypeRVAdapter mATCommunityTypeRVAdapter = new ATCommunityTypeRVAdapter(this, mATCommunityTypeBeanList, getIntent().getIntExtra("position", 0));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(mATCommunityTypeRVAdapter);
        mATCommunityTypeRVAdapter.setOnItemClickListener((view, o, position) -> {
            setResult(RESULT_OK, new Intent().putExtra("position", position));
            finish();
        });
    }
}