package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATCaringRecordBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATCaringRecordRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATCaringRecordBean> list = new ArrayList<>();
    private String typeRecord;

    public ATCaringRecordRVAdapter(Activity context, String typeRecord) {
        mContext = context;
        this.typeRecord = typeRecord;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_caring_record, parent, false);
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

    public void setLists(List<ATCaringRecordBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTvEventTime, mTvAddress, mTvPersonName, mTvPersonType;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mTvEventTime = itemView.findViewById(R.id.tv_eventTime);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvPersonName = itemView.findViewById(R.id.tv_personName);
            mTvPersonType = itemView.findViewById(R.id.tv_personType);
        }

        public void setData(int position) {
            mTvEventTime.setText(list.get(position).getEventTime());
            mTvAddress.setText("oldyoung".equals(typeRecord) ? list.get(position).getDeviceName() : list.get(position).getAddress());
            mTvPersonName.setText(list.get(position).getPersonName());
            if (list.get(position).getPersonType() == 1) {
                mTvPersonType.setText("老人");
            } else if (list.get(position).getPersonType() == 0) {
                mTvPersonType.setText("小孩");
            } else {
                mTvPersonType.setText("普通");
            }
            mLlContent.setOnClickListener(view -> mOnItemClickListener.onItemClick(position));
        }
    }

    private ATPublicSecurityCameraRVAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ATPublicSecurityCameraRVAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

