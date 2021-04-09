package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATScoreDetailBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATScoreDetailRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATScoreDetailBean> list = new ArrayList<>();
    ;

    public ATScoreDetailRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //根据返回类型选择不同item
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_score_detail, parent, false);
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

    public void setLists(List<ATScoreDetailBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, tvScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            tvScore = itemView.findViewById(R.id.tv_score);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getName());
            if(list.get(position).isHaveDone()){
                mTvName.setTextColor(mContext.getResources().getColor(R.color._333333));
                tvScore.setText(list.get(position).getScore());
                tvScore.setTextColor(mContext.getResources().getColor(R.color._EBB080));
            }else {
                mTvName.setTextColor(mContext.getResources().getColor(R.color._BBBBBB));
                tvScore.setText(mContext.getResources().getString(R.string.at_undone));
                tvScore.setTextColor(mContext.getResources().getColor(R.color._BBBBBB));
            }
        }
    }
}