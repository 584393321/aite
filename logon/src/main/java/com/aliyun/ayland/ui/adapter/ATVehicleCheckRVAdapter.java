package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATParkingBean;
import com.aliyun.ayland.ui.activity.ATVehicleCheckDetailActivity;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATVehicleCheckRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATParkingBean> list = new ArrayList<>();

    public ATVehicleCheckRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_vehicle_check, parent, false);
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

    public void setLists(List<ATParkingBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTextView,mTvEmpty, mTvTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mTextView = itemView.findViewById(R.id.textView);
            mTvEmpty = itemView.findViewById(R.id.tv_empty);
            mTvTotal = itemView.findViewById(R.id.tv_total);
        }

        public void setData(int position) {
            mTextView.setText(list.get(position).getParkname());
            mTvEmpty.setText(list.get(position).getCurrentSpace());
            mTvTotal.setText(list.get(position).getTotalspace());
            mLlContent.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, ATVehicleCheckDetailActivity.class)
                    .putExtra("parkingBean", list.get(position))));
        }
    }
}
