package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATFamilyMemberCareBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyMemberCareRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATFamilyMemberCareBean> list = new ArrayList<>();

    public ATFamilyMemberCareRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_member_care, parent, false);
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

    public void setEnable(int enable, int position) {
        this.list.get(position).setIfEnable(enable);
        notifyItemChanged(position);
    }

    public void setLists(List<ATFamilyMemberCareBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvType;
        private ATSwitchButton mATSwitchButton;
        private LinearLayout llContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvType = itemView.findViewById(R.id.tv_type);
            mATSwitchButton = itemView.findViewById(R.id.switchview);
            llContent = itemView.findViewById(R.id.ll_content);
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
            if (list.get(position).getIfEnable() == 1) {
                mATSwitchButton.setChecked(true);
            } else {
                mATSwitchButton.setChecked(false);
            }
            mATSwitchButton.setClickable(false);
            llContent.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, mATSwitchButton.isChecked() ? 0 : 1, position));
        }
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}