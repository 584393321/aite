package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.data.ATDeviceTslDataType;
import com.aliyun.ayland.interfaces.ATOnEPLItemClickListener;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyMonitorRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATDeviceBean> list = new ArrayList<>();
    private List<Integer> sensitivity = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.home_ld_ico_camera)
            .diskCacheStrategy(DiskCacheStrategy.ALL);
    private Drawable drawable;

    public ATFamilyMonitorRVAdapter(Activity context) {
        mContext = context;
        drawable = context.getResources().getDrawable(R.drawable.atico_junp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_monitor, parent, false);
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

    public void setLists(List<ATDeviceBean> list) {
        this.sensitivity.clear();
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llAlarmSwitch, llMotionSwitch, llMotionSensitivity;
        private TextView mTvName, mTvOffLine, tvMotionSensitivity;
        private ImageView img;
        private RelativeLayout rlContent;
        private ATSwitchButton sbAlarmSwitch, sbMotionSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            rlContent = itemView.findViewById(R.id.rl_content);
            llAlarmSwitch = itemView.findViewById(R.id.ll_alarm_switch);
            llMotionSwitch = itemView.findViewById(R.id.ll_motion_switch);
            llMotionSensitivity = itemView.findViewById(R.id.ll_motion_sensitivity);
            img = itemView.findViewById(R.id.img);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvOffLine = itemView.findViewById(R.id.tv_off_line);
            tvMotionSensitivity = itemView.findViewById(R.id.tv_motion_sensitivity);
            sbAlarmSwitch = itemView.findViewById(R.id.sb_alarm_switch);
            sbMotionSwitch = itemView.findViewById(R.id.sb_motion_switch);
        }

        public void setData(int position) {
            if (list.get(position).getStatus() != 1) {
                mTvOffLine.setText(String.format(mContext.getString(R.string.at_status_), mContext.getString(R.string.at_off_line)));
                mTvOffLine.setTextColor(mContext.getResources().getColor(R.color._E85D3E));
            } else {
                mTvOffLine.setText(String.format(mContext.getString(R.string.at_status_), mContext.getString(R.string.at_normal)));
                mTvOffLine.setTextColor(mContext.getResources().getColor(R.color._EBB080));
            }
            mTvName.setText(TextUtils.isEmpty(list.get(position).getNickName())
                    ? list.get(position).getProductName() : list.get(position).getNickName());
            Glide.with(mContext).load(list.get(position).getProductImage()).apply(options).into(img);
            llMotionSensitivity.setOnClickListener(view -> mOnItemClickListener.onItemClick(position, 2,
                    sensitivity.get(position)));
            if (list.get(position).getAttributes() != null)
                for (ATDeviceTslDataType atDeviceTslDataType : list.get(position).getAttributes()) {
                if ("AlarmSwitch".equals(atDeviceTslDataType.getAttribute())) {
                    sbAlarmSwitch.setCheckedImmediately("1".equals(atDeviceTslDataType.getValue()));
                }
                if ("MotionDetectSensitivity".equals(atDeviceTslDataType.getAttribute())) {
                    sensitivity.add(Integer.parseInt(atDeviceTslDataType.getValue()));
                    if ("0".equals(atDeviceTslDataType.getValue())) {
                        sbMotionSwitch.setCheckedImmediately(false);
                        llMotionSensitivity.setClickable(false);
                        tvMotionSensitivity.setText(mContext.getString(R.string.at_close));
                        tvMotionSensitivity.setTextColor(mContext.getResources().getColor(R.color._999999));
                        tvMotionSensitivity.setCompoundDrawablePadding(0);
                        tvMotionSensitivity.setCompoundDrawables(null, null, null, null);
                    } else {
                        sbMotionSwitch.setCheckedImmediately(true);
                        llMotionSensitivity.setClickable(true);
                        tvMotionSensitivity.setText(ATResourceUtils.getString(ATResourceUtils.getResIdByName(
                                String.format(mContext.getString(R.string.at_level_), atDeviceTslDataType.getValue()), ATResourceUtils.ResourceType.STRING)));
                        tvMotionSensitivity.setTextColor(mContext.getResources().getColor(R.color._4181EB));
                        tvMotionSensitivity.setCompoundDrawablePadding(0);
                        tvMotionSensitivity.setCompoundDrawables(null, null, drawable, null);
                    }
                }
            }
            sbAlarmSwitch.setClickable(false);
            sbMotionSwitch.setClickable(false);
            llAlarmSwitch.setOnClickListener(view -> mOnItemClickListener.onItemClick(position, 0,
                    sbAlarmSwitch.isChecked() ? 0 : 1));
            llMotionSwitch.setOnClickListener(view -> mOnItemClickListener.onItemClick(position, 1,
                    sbMotionSwitch.isChecked() ? 0 : 1));
            rlContent.setOnClickListener(view -> mOnItemClickListener.onItemClick(position));
        }
    }

    private ATOnEPLItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnEPLItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}