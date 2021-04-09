package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATAccessRecordBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATAccessRecordRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATAccessRecordBean> list = new ArrayList<>();

    public ATAccessRecordRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_access_record, parent, false);
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

    public void setLists(List<ATAccessRecordBean> list, int page) {
        if (page == 0)
            this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvParkLot, mTvTime, mTvName, mTvInOut;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvParkLot = itemView.findViewById(R.id.tv_park_lot);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvInOut = itemView.findViewById(R.id.tv_in_out);
        }

        public void setData(int position) {
            mTvParkLot.setText(list.get(position).getParkcode());
            mTvName.setText(list.get(position).getLicence());
            mTvTime.setText(list.get(position).getTime());
            if ("in".equals(list.get(position).getApproach())) {
                mTvInOut.setText("进");
            } else {
                mTvInOut.setText("出");
            }
        }
    }
}