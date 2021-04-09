package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATRecommendSceneBean;
import com.aliyun.ayland.ui.activity.ATRecommendSceneDetailActivity;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATRecommendSceneRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATRecommendSceneBean> list = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_tjcj_bg_pm)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATRecommendSceneRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_recommend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATRecommendSceneBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTextView;
        private ImageView mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTextView = itemView.findViewById(R.id.textView);
            mImg = itemView.findViewById(R.id.img);
        }

        public void setData(int position) {
            Glide.with(mContext).load(list.get(position).getRecommendSceneIcon()).apply(options).into(mImg);
            mTextView.setText(list.get(position).getRecommendSceneName());
            mRlContent.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, ATRecommendSceneDetailActivity.class)
                    .putExtra("recommendSceneType", list.get(position).getRecommendSceneType())
                    .putExtra("recommendSceneStatus", list.get(position).getRecommendSceneStatus())
                    .putExtra("recommendSceneId",list.get(position).getRecommendSceneId())));
        }
    }
}
