package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.base.autolayout.ATAutoAppBarLayout;
import com.aliyun.ayland.base.autolayout.ATAutoCollapsingToolbarLayout;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATScoreDetailBean;
import com.aliyun.ayland.ui.adapter.ATScoreDetailRVAdapter;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATScoreDetailActivity extends ATBaseActivity {
    private RecyclerView recyclerView;
    private TextView tvBack, tvTitle, tvRight;
    private List<ATScoreDetailBean> list = new ArrayList<>();
    private ATAutoCollapsingToolbarLayout collapsingToolbarLayout;
    private ATAutoAppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private RelativeLayout rlContent;
    private LinearLayout llRight;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_score_detail;
    }

    @Override
    protected void findView() {
        tvBack = findViewById(R.id.tv_back);
        tvTitle = findViewById(R.id.tv_title);
        rlContent = findViewById(R.id.rl_content);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        mainContent = findViewById(R.id.main_content);
        recyclerView = findViewById(R.id.recyclerView);
        llRight = findViewById(R.id.ll_right);
        tvRight = findViewById(R.id.tv_right);
        init();
    }

    @Override
    protected void initPresenter() {

    }

    private void init() {
        ATSystemStatusBarUtils.init(this, false);

        ATAutoUtils.auto(collapsingToolbarLayout);
        ATAutoUtils.auto(appBarLayout);
        ATAutoUtils.auto(mainContent);
        tvBack.setOnClickListener(view -> finish());
        llRight.setOnClickListener(view -> startActivity(new Intent(this, ATScoreRuleActivity.class)));
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            rlContent.getBackground().setAlpha(255 * verticalOffset / (-appBarLayout.getTotalScrollRange()));
            if (rlContent.getBackground().getAlpha() == 0) {
                ATSystemStatusBarUtils.init(ATScoreDetailActivity.this, false);
                tvBack.setSelected(false);
                tvTitle.setTextColor(getResources().getColor(R.color.white));
                tvRight.setTextColor(getResources().getColor(R.color.white));
                tvRight.setBackground(getResources().getDrawable(R.drawable.shape_36px3pxffffff));
            } else if (rlContent.getBackground().getAlpha() == 255) {
                ATSystemStatusBarUtils.init(ATScoreDetailActivity.this, true);
                tvBack.setSelected(true);
                tvTitle.setTextColor(getResources().getColor(R.color._333333));
                tvRight.setTextColor(getResources().getColor(R.color._333333));
                tvRight.setBackground(getResources().getDrawable(R.drawable.shape_36px3px666666));
            }
        });

        ATScoreDetailBean ATScoreDetailBean = new ATScoreDetailBean();
        ATScoreDetailBean.setHaveDone(true);
        ATScoreDetailBean.setName("预约健身");
        ATScoreDetailBean.setScore("+10分");
        list.add(ATScoreDetailBean);
        ATScoreDetailBean = new ATScoreDetailBean();
        ATScoreDetailBean.setHaveDone(true);
        ATScoreDetailBean.setName("预约亲子盒子");
        ATScoreDetailBean.setScore("+10分");
        list.add(ATScoreDetailBean);
        ATScoreDetailBean = new ATScoreDetailBean();
        ATScoreDetailBean.setHaveDone(false);
        ATScoreDetailBean.setName("发广场动态");
        ATScoreDetailBean.setScore("");
        list.add(ATScoreDetailBean);
        ATScoreDetailBean = new ATScoreDetailBean();
        ATScoreDetailBean.setHaveDone(true);
        ATScoreDetailBean.setName("动态评论");
        ATScoreDetailBean.setScore("+5分");
        list.add(ATScoreDetailBean);
        ATScoreDetailBean = new ATScoreDetailBean();
        ATScoreDetailBean.setHaveDone(true);
        ATScoreDetailBean.setName("动态点赞");
        ATScoreDetailBean.setScore("+5分");
        list.add(ATScoreDetailBean);
        ATScoreDetailBean = new ATScoreDetailBean();
        ATScoreDetailBean.setHaveDone(true);
        ATScoreDetailBean.setName("加入群组");
        ATScoreDetailBean.setScore("+5分");
        list.add(ATScoreDetailBean);
        ATScoreDetailBean = new ATScoreDetailBean();
        ATScoreDetailBean.setHaveDone(true);
        ATScoreDetailBean.setName("发群组动态");
        ATScoreDetailBean.setScore("+5分");
        list.add(ATScoreDetailBean);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ATScoreDetailRVAdapter mATScoreDetailRVAdapter = new ATScoreDetailRVAdapter(this);
        recyclerView.setAdapter(mATScoreDetailRVAdapter);
        mATScoreDetailRVAdapter.setLists(list);
    }
}