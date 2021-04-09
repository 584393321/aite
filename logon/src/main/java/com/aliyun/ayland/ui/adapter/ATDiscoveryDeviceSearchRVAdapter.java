package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.Category;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.utils.ATAddDeviceScanHelper;
import com.aliyun.iot.aep.component.router.Router;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.espressif.android.v1.EspTouchActivity;

import java.util.ArrayList;
import java.util.List;

public class ATDiscoveryDeviceSearchRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<Category> list = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .error(R.drawable.at_home_ico_shebeigongyong);

    public ATDiscoveryDeviceSearchRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_local_device_category, parent, false);
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

    public void setLists(List<Category> list, int page) {
        if (page == 1)
            this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView imgDevice;
        private TextView tvDevice;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            this.itemView = itemView;
            imgDevice = itemView.findViewById(R.id.img_device);
            tvDevice = itemView.findViewById(R.id.tv_device);
        }

        public void setData(int position) {
            tvDevice.setText(list.get(position).getProductName());
            Glide.with(mContext)
                    .load(list.get(position).getCategoryUrl())
                    .apply(options)
                    .into(imgDevice);

            itemView.setOnClickListener(v -> {
                if (ATConstants.ProductKey.ESP_TOUCH.equals(list.get(position).getProductKey())) {
                    mContext.startActivityForResult(new Intent(mContext, EspTouchActivity.class), 10001);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("productKey", list.get(position).getProductKey());
//                    bundle.putString("deviceName", list.get(position).getProductName());
                    Router.getInstance().toUrlForResult((Activity) v.getContext(), ATConstants.RouterUrl.PLUGIN_ID_DEVICE_CONFIG,
                            ATAddDeviceScanHelper.REQUEST_CODE_CONFIG_WIFI, bundle);
                }
            });
        }
    }
}