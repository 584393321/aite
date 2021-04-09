package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.ui.activity.ATLinkageDeviceActivity;
import com.aliyun.ayland.ui.activity.ATMainActivity;
import com.aliyun.ayland.ui.activity.ATManageRoomActivity;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ATHomeControlRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATRoomBean1> list = new ArrayList<>();
    private int select = 0;
    private String allDevice = "";

    public ATHomeControlRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_home_control_rv, parent, false);
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

    public void setLists(List<ATRoomBean1> list, String allDevice) {
        this.list.clear();
        this.list.addAll(list);
        this.allDevice = allDevice;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            this.itemView = itemView;
            tvName = itemView.findViewById(R.id.tv_name);
        }

        public void setData(int position) {
            tvName.setText(list.get(position).getName());

            if (position != list.size() - 1 || mContext instanceof ATLinkageDeviceActivity) {
                if (position == select) {
                    tvName.setTextColor(mContext.getResources().getColor(R.color._E9A866));
                    itemView.setBackgroundColor(Color.WHITE);
                } else {
                    tvName.setTextColor(mContext.getResources().getColor(R.color._999999));
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                }
            }
            itemView.setOnClickListener(v -> {
                if (mContext instanceof ATMainActivity) {
                    if (position == list.size() - 1) {
                        mContext.startActivity(new Intent(mContext, ATManageRoomActivity.class).putExtra("allDevice", allDevice)
                                .putExtra("mRoomNameList", (Serializable) list));
                    } else {
                        EventBus.getDefault().post(new ATEventInteger("ATHomeInnerATFragment", position));
                        setSelectItem(position);
                    }
                } else {
                    EventBus.getDefault().post(new ATEventInteger("ATLinkageDeviceActivity", position));
                    setSelectItem(position);
                }
            });
            itemView.setTag(position);
        }
    }

    public void setSelectItem(int select) {
        this.select = select;
        notifyDataSetChanged();
    }
}