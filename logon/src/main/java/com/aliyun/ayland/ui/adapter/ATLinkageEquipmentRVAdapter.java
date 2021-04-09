package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDeviceBean;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageEquipmentRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATDeviceBean> list;
    private int specs;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATLinkageEquipmentRVAdapter(Activity context, ArrayList<ATDeviceBean> deviceList) {
        mContext = context;
        this.list = deviceList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_equipment, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTvText;
        private ImageView mImgDevice;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mTvText = itemView.findViewById(R.id.text);
            mImgDevice = itemView.findViewById(R.id.img);
        }

        public void setData(int position) {
            mLlContent.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(view, position);
            });
            mTvText.setText(TextUtils.isEmpty(list.get(position).getNickName()) ? list.get(position).getProductName()
                    : list.get(position).getNickName());
            Glide.with(mContext).load(list.get(position).getProductImage()).apply(options).into(mImgDevice);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
