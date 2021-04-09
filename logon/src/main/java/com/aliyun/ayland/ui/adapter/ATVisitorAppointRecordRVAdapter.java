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

public class ATVisitorAppointRecordRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATVisitorReservateBean> list = new ArrayList<>();

    public ATVisitorAppointRecordRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_visitor_appoint_record, parent, false);
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

    public void setLists(List<ATVisitorReservateBean> list, int pageNum) {
        if (pageNum == 1)
            this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llParkFee, llContent;
        private TextView mTvName, mTvStatus, mTvPhone, mTvVisiteTime, mTvLeaveTime, mTvTripMode, mTvIntermediary, mTvStartTime, mTvParkFee;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            llContent = itemView.findViewById(R.id.ll_content);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            mTvPhone = itemView.findViewById(R.id.tv_phone);
            mTvVisiteTime = itemView.findViewById(R.id.tv_visite_time);
            mTvStartTime = itemView.findViewById(R.id.tv_start_time);
            mTvLeaveTime = itemView.findViewById(R.id.tv_leave_time);
            mTvParkFee = itemView.findViewById(R.id.tv_park_fee);
            llParkFee = itemView.findViewById(R.id.ll_park_fee);
            mTvTripMode = itemView.findViewById(R.id.tv_trip_mode);
            mTvIntermediary = itemView.findViewById(R.id.tv_intermediary);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getVisitorName());
            mTvStartTime.setText(TextUtils.isEmpty(list.get(position).getActualStartTime()) ? "无" : list.get(position).getActualStartTime());
            mTvLeaveTime.setText(TextUtils.isEmpty(list.get(position).getActualEndTime()) ? "无" : list.get(position).getActualEndTime());
            mTvPhone.setText(TextUtils.isEmpty(list.get(position).getVisitorTel()) ? "无" : list.get(position).getVisitorTel());
            if (list.get(position).getIntermediary() == 1) {
                mTvIntermediary.setVisibility(View.VISIBLE);
            } else {
                mTvIntermediary.setVisibility(View.GONE);
            }
            if (-1 == list.get(position).getVisitorStatus()) {
                mTvStatus.setText(mContext.getString(R.string.at_no_visited));
                llContent.setOnClickListener(view1 -> mContext.startActivity(new Intent(mContext, ATVisitorAppointResultActivity.class)
                        .putExtra("id", list.get(position).getId())));
            } else {
                mTvStatus.setText(mContext.getString(R.string.at_visited));
                llContent.setOnClickListener(v -> {

                });
            }
            if (list.get(position).getHasCar() == -1) {
                llParkFee.setVisibility(View.GONE);
                mTvTripMode.setText(mContext.getString(R.string.at_walk));
            } else {
                llParkFee.setVisibility(View.VISIBLE);
                mTvTripMode.setText(mContext.getString(R.string.at_car) + " " + list.get(position).getCarNumber());
            }
            mTvParkFee.setText("0元");
            mTvVisiteTime.setText(list.get(position).getReservationStartTime() + "  至  " + list.get(position).getReservationEndTime());
        }
    }
}