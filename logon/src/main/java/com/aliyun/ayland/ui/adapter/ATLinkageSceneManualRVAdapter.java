package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEventString;
import com.aliyun.ayland.data.ATSceneBean;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.aliyun.ayland.ui.fragment.ATLinkageFragment.REQUEST_CODE_EDIT_LINKAGE;

public class ATLinkageSceneManualRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATSceneBean> list = new ArrayList<>();

    public ATLinkageSceneManualRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_scene, parent, false);
        LinkageSceneManualHolder linkageSceneManualHolder = new LinkageSceneManualHolder(view);
        return linkageSceneManualHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LinkageSceneManualHolder linkageSceneManualHolder = (LinkageSceneManualHolder) holder;
        linkageSceneManualHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATSceneBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class LinkageSceneManualHolder extends RecyclerView.ViewHolder {
        private ImageView mImgIcon;
        private TextView mTvName, tvKeyword;
        private RelativeLayout mRlContent;

        private LinkageSceneManualHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mImgIcon = itemView.findViewById(R.id.img_icon);
            mTvName = itemView.findViewById(R.id.tv_name);
            tvKeyword = itemView.findViewById(R.id.tv_keyword);
            mRlContent = itemView.findViewById(R.id.rl_content);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getName());
            mRlContent.setOnClickListener((view) -> {
                mContext.startActivityForResult(new Intent(mContext, ATLinkageAddActivity.class)
                        .putExtra("sceneId", list.get(position).getSceneId()), REQUEST_CODE_EDIT_LINKAGE);
            });
            mImgIcon.setOnClickListener((view) -> {
                EventBus.getDefault().post(new ATEventString("ATLinkageFamilyFragment", list.get(position).getSceneId()));
            });
        }
    }
}
