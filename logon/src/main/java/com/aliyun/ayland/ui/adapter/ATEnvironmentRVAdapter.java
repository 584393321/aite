package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEnvironmentBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATEnvironmentRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATEnvironmentBean> list = new ArrayList<>();
    private int current_position = 0;

    public ATEnvironmentRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_environment, parent, false);
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

    public void setLists(List<ATEnvironmentBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llContent;
        private TextView mTvName, mTvUnit;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            llContent = itemView.findViewById(R.id.ll_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvUnit = itemView.findViewById(R.id.tv_unit);
        }

        public void setData(int position) {
            if (current_position == position) {
                mTvName.setTextColor(mContext.getResources().getColor(R.color._333333));
                mTvUnit.setVisibility(View.VISIBLE);
            } else {
                mTvName.setTextColor(mContext.getResources().getColor(R.color._999999));
                mTvUnit.setVisibility(View.GONE);
            }
            mTvName.setText(list.get(position).getTemp());
            mTvUnit.setText(list.get(position).getUnit());

            llContent.setOnClickListener(view -> {
                current_position = position;
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(view, list.get(position), position);
            });
        }
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}