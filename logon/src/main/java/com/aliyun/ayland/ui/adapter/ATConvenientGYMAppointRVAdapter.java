package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATGymRecordBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATConvenientGYMAppointRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATGymRecordBean> list = new ArrayList<>();

    public ATConvenientGYMAppointRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_appoint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATGymRecordBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTvAppointTime, mTvAddress, mTvSubject, mTvPlan;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mTvAppointTime = itemView.findViewById(R.id.tv_appoint_time);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvSubject = itemView.findViewById(R.id.tv_subject);
            mTvPlan = itemView.findViewById(R.id.tv_plan);
        }

        public void setData(int position) {
            mTvAppointTime.setText(list.get(position).getCreateTimeStr());
            mTvAddress.setText(list.get(position).getGymName());
            mTvSubject.setText(list.get(position).getProjectName());
            mTvPlan.setText(list.get(position).getAppointmentDay() + "  " + list.get(position).getAppointmentTime());
        }
    }
}