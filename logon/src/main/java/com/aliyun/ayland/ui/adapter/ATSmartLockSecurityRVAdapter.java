package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDoorAlarmRecordBean;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATSmartLockSecurityRVAdapter extends RecyclerView.Adapter{
    private Activity mContext;
    private List<ATDoorAlarmRecordBean> list = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ld_ico_smart_door)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATSmartLockSecurityRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_smart_lock_security, parent, false);
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

    public void setList(List<ATDoorAlarmRecordBean> list, int startNum) {
        if (startNum == 0)
            this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvTime, mTvContent;
        private ImageView mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mImg = itemView.findViewById(R.id.img);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getDeviceName());
            mTvTime.setText(list.get(position).getEventTime());
            mTvContent.setText(list.get(position).getDescription());
            Glide.with(mContext)
                    .load(list.get(position).getImage())
                    .apply(options)
                    .into(mImg);
        }

    }

//    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;
//
//    public static interface ATOnRecyclerViewItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
//    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
}
