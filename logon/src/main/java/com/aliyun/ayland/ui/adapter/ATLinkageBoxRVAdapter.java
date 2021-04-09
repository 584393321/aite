package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATAllAppointmentBean;
import com.aliyun.ayland.ui.activity.ATLinkageBoxToActivity;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

import static com.aliyun.ayland.ui.activity.ATLinkageBoxActivity.REQUEST_CODE_LINKAGE_BOX;

public class ATLinkageBoxRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATAllAppointmentBean> list = new ArrayList<>();

    public ATLinkageBoxRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_box, parent, false);
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

    public void setLists(List<ATAllAppointmentBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvName = itemView.findViewById(R.id.tv_name);
        }

        public void setData(int position) {
            mTvName.setText(String.format(mContext.getString(R.string.at_space_), list.get(position).getDay().substring(5, 10), list.get(position).getTypeName()
                    , list.get(position).getProjectName(), list.get(position).getAppointmentTime()));
            mRlContent.setOnClickListener(view -> {
                mContext.startActivityForResult(new Intent(mContext, ATLinkageBoxToActivity.class).putExtra("appointment",list.get(position)), REQUEST_CODE_LINKAGE_BOX);
            });
        }
    }
}