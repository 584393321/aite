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
import com.aliyun.ayland.data.ATDiscoveryDeviceSecondLevelBean;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATDiscoveryDeviceRightRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
//    private List<Category> list = new ArrayList<>();
    private List<ATDiscoveryDeviceSecondLevelBean> list = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .error(R.drawable.at_home_ico_shebeigongyong);

    public ATDiscoveryDeviceRightRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_discovery_device_right, parent, false);
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

    public void setLists(List<ATDiscoveryDeviceSecondLevelBean> list, int pageNo) {
        if (pageNo == 1) {
            this.list.clear();
        }
//        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvDevice;
        private ImageView mImgDevice;
        private ImageView mImgJump;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvDevice = itemView.findViewById(R.id.tv_device);
            mImgDevice = itemView.findViewById(R.id.img_device);
            mImgJump = itemView.findViewById(R.id.img_jump);
        }

        public void setData(int position) {
            mTvDevice.setText(list.get(position).getCategoryName());
            Glide.with(mContext).load(list.get(position).getImageUrl()).apply(options).into(mImgDevice);
            mRlContent.setOnClickListener((view) -> {
                mOnItemClickListener.onItemClick(position);
            });
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}