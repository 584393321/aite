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
import com.aliyun.ayland.data.ATFamilyAppointHistoryBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyAppointHistoryRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATFamilyAppointHistoryBean> list = new ArrayList<>();

    public ATFamilyAppointHistoryRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_appoint_history, parent, false);
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

    public void setLists(List<ATFamilyAppointHistoryBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTvAddress, mTvAppointTime, mTvPlan;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvAppointTime = itemView.findViewById(R.id.tv_appoint_time);
            mTvPlan = itemView.findViewById(R.id.tv_plan);
        }

        public void setData(int position) {
            mTvAppointTime.setText(list.get(position).getCreateTimeStr());
            mTvAddress.setText(list.get(position).getChildrenRoomName());
            mTvPlan.setText(list.get(position).getAppointmentDay() + "  " + list.get(position).getAppointmentTime());
//            mLlContent.setOnClickListener(view1 -> mContext.startActivity(new Intent(mContext, VisitorAppointResultActivityAT.class)
//                    .putExtra("id", list.get(position).getId())));
        }
    }
}