package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATFamilyMemberCareBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATOldYoungCareFindLocationAdapter extends RecyclerView.Adapter{
    private Activity mContext;
    private List<ATFamilyMemberCareBean> list = new ArrayList<>();

    public ATOldYoungCareFindLocationAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_old_young_care_find_location, parent, false);
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

    public void setLists(List<ATFamilyMemberCareBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvType;
        private RelativeLayout mRlContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvType = itemView.findViewById(R.id.tv_type);
            mRlContent = itemView.findViewById(R.id.rl_content);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getNickname());
            if (list.get(position).getPersonType() == 1) {
                mTvType.setText("老人");
            } else if (list.get(position).getPersonType() == 0) {
                mTvType.setText("小孩");
            } else {
                mTvType.setText("普通");
            }
            mRlContent.setOnClickListener(view -> mOnItemClickListener.onItemClick(view,position));
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}