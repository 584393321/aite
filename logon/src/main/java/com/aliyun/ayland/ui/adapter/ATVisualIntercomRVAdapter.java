package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATVisualIntercomRecordBean;
import com.anthouse.xuhui.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ATVisualIntercomRVAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private List<ATVisualIntercomRecordBean> list = new ArrayList<>();

    public ATVisualIntercomRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_visual_intercom, parent, false);
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

    public void setList(List<ATVisualIntercomRecordBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvAddress, mTvEventTime;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvEventTime = itemView.findViewById(R.id.tv_eventTime);
        }

        public void setData(int position) {
            mTvAddress.setText(list.get(position).getAddress());
            if (TextUtils.isEmpty(list.get(position).getTimes())) {
                mTvEventTime.setText(mContext.getString(R.string.at_unknown));
            } else {
                mTvEventTime.setText(list.get(position).getTimes());
            }
        }
    }
}