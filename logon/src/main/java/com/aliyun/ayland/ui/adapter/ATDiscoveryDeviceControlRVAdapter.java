package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDiscoveryDeviceFirstLevelBean;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATDiscoveryDeviceControlRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATDiscoveryDeviceFirstLevelBean> list = new ArrayList<>();
    private int select = 0;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .error(R.drawable.at_home_ico_shebeigongyong);

    public ATDiscoveryDeviceControlRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_discovery_device_control, parent, false);
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

    public void setLists(List<ATDiscoveryDeviceFirstLevelBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView imgDeviceBg;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            this.itemView = itemView;
            tvName = itemView.findViewById(R.id.tv_name);
            imgDeviceBg = itemView.findViewById(R.id.img_device_bg);
        }

        public void setData(int position) {
            Glide.with(mContext).load(list.get(position).getImageUrl()).apply(options).into(imgDeviceBg);
            tvName.setText(list.get(position).getCategoryName());
            if (position == select) {
                tvName.setTextColor(mContext.getResources().getColor(R.color._86523C));
                itemView.setBackgroundColor(Color.WHITE);
            } else {
                tvName.setTextColor(mContext.getResources().getColor(R.color._999999));
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }

            itemView.setOnClickListener(view1 -> {
                mOnItemClickListener.onItemClick(position);
                setSelectItem(position);
            });
            itemView.setTag(position);
        }
    }

    public void setSelectItem(int select) {
        this.select = select;
        notifyDataSetChanged();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}