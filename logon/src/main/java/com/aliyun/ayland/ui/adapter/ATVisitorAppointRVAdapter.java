package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATVisitorReservateBean;
import com.aliyun.ayland.ui.activity.ATVisitorAppointResultActivity;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATVisitorAppointRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATVisitorReservateBean> list = new ArrayList<>();

    public ATVisitorAppointRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_visitor_appoint, parent, false);
        return  new ViewHolder(view);
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

    public void setLists(List<ATVisitorReservateBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTvAppointTime, mTvName, mTvTel, mTvVisiteTime, mTvLeaveTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mTvAppointTime = itemView.findViewById(R.id.tv_appoint_time);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvTel = itemView.findViewById(R.id.tv_tel);
            mTvVisiteTime = itemView.findViewById(R.id.tv_visite_time);
            mTvLeaveTime = itemView.findViewById(R.id.tv_leave_time);
        }

        public void setData(int position) {
            mTvAppointTime.setText(list.get(position).getCreateTime());
            mTvName.setText(list.get(position).getVisitorName());
            mTvTel.setText(TextUtils.isEmpty(list.get(position).getVisitorTel()) ? "无" : list.get(position).getVisitorTel());
            mTvVisiteTime.setText(list.get(position).getReservationStartTime());
            mTvLeaveTime.setText(list.get(position).getReservationEndTime());
            mLlContent.setOnClickListener(view1 -> mContext.startActivity(new Intent(mContext, ATVisitorAppointResultActivity.class)
                    .putExtra("id", list.get(position).getId())));
        }
    }
}