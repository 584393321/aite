package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSFDeviceListBean;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATSFDeviceRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATSFDeviceListBean> list = new ArrayList<>();

    public ATSFDeviceRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_sf, parent, false);
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

    public void setLists(List<ATSFDeviceListBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHandUp, tvName, tvRoom;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            tvHandUp = itemView.findViewById(R.id.tv_hand_up);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRoom = itemView.findViewById(R.id.tv_room);
        }

        public void setData(int position) {
            tvName.setText(list.get(position).getProductName());
            tvRoom.setText(list.get(position).getSpaceName());
            tvHandUp.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position));
        }
    }

    private ATOnRVItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRVItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}