package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATCarBean;
import com.aliyun.ayland.ui.activity.ATMyCarDetailActivity;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATSFLinkageRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATCarBean> list = new ArrayList<>();

    public ATSFLinkageRVAdapter(Activity context) {
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

    public void setLists(List<ATCarBean> list) {
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
            tvName.setText(list.get(position).getLicence());
            tvRoom.setText(list.get(position).getLicence());
            tvHandUp.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, ATMyCarDetailActivity.class)
                        .putExtra("id", list.get(position).getId()).putExtra("licence", list.get(position).getLicence())));
        }
    }
}