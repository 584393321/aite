package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATHouseBean;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATChangeHouseRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATHouseBean> list = new ArrayList<>();

    public ATChangeHouseRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_change_house, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ATHouseBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvChange, tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            tvName = itemView.findViewById(R.id.tv_house_name);
            tvChange = itemView.findViewById(R.id.tv_change);
            tvAddress = itemView.findViewById(R.id.tv_address);
        }

        public void setData(int position) {
            tvName.setText(list.get(position).getName());
            tvAddress.setText(list.get(position).getVillageName());
            tvChange.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position));
        }
    }

    private ATOnRVItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRVItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}