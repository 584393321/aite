package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATCarLogBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATCarRecordRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATCarLogBean> list = new ArrayList<>();

    public ATCarRecordRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_car_record, parent, false);
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

    public void setLists(List<ATCarLogBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPark, tvTime, tvPlateNumber, tvDirection;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            tvPark = itemView.findViewById(R.id.tv_park);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPlateNumber = itemView.findViewById(R.id.tv_plate_number);
            tvDirection = itemView.findViewById(R.id.tv_direction);
        }

        public void setData(int position) {
            tvPark.setText(list.get(position).getParkName());
            tvTime.setText(list.get(position).getCreateTime());
            tvPlateNumber.setText(list.get(position).getPlateNumber());
            tvDirection.setText(list.get(position).getDirection() == 0 ? "进" : "出");
        }
    }
}