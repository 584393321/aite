package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATApplicationBean;
import com.aliyun.ayland.ui.activity.ATEnvironmentActivity;
import com.aliyun.ayland.ui.activity.ATEquipmentActivity;
import com.aliyun.ayland.ui.activity.ATFamilyMonitorActivity;
import com.aliyun.ayland.ui.activity.ATFamilySecurityActivity;
import com.aliyun.ayland.ui.activity.ATLinkageActivity;
import com.aliyun.ayland.ui.activity.ATOldYoungCareActivity;
import com.aliyun.ayland.ui.activity.ATPublicSecurityMainActivity;
import com.aliyun.ayland.ui.activity.ATTmallVoiceWizardActivity;
import com.aliyun.ayland.ui.activity.ATUserFaceActivity;
import com.aliyun.ayland.ui.activity.ATVehicleCheckActivity;
import com.aliyun.ayland.ui.activity.ATVehiclePassageActivity;
import com.aliyun.ayland.ui.activity.ATVisitorRecordActivity;
import com.aliyun.ayland.ui.activity.ATVisualIntercomActivity;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ATHomeShortcutEditRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATApplicationBean> list = new ArrayList<>();
    private SwipeMenuRecyclerView mMenuRecyclerView;
    private boolean edit;

    public ATHomeShortcutEditRVAdapter(SwipeMenuRecyclerView menuRecyclerView, Activity context, boolean edit) {
        this.mMenuRecyclerView = menuRecyclerView;
        this.edit = edit;
        mContext = context;
    }

    public List<ATApplicationBean> getList() {
        return list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_application, parent, false);
        ShortcutViewHolder shortcutViewHolder = new ShortcutViewHolder(view);
        shortcutViewHolder.mMenuRecyclerView = mMenuRecyclerView;
        mMenuRecyclerView.setItemViewSwipeEnabled(false);
        return shortcutViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShortcutViewHolder shortcutViewHolder = (ShortcutViewHolder) holder;
        shortcutViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATApplicationBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ShortcutViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvName;
        private ImageView mImg, mImgDelete;
        private SwipeMenuRecyclerView mMenuRecyclerView;

        private ShortcutViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            mImg = itemView.findViewById(R.id.img);
            mImgDelete = itemView.findViewById(R.id.img_add_delete);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getApplicationName());
            Glide.with(mContext).load(list.get(position).getApplicationIcon()).into(mImg);
            if (edit) {
                mImgDelete.setVisibility(View.VISIBLE);
                mRlContent.setOnTouchListener((view, motionEvent) -> {
                    int action = motionEvent.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN: {
                            mMenuRecyclerView.startDrag(ShortcutViewHolder.this);
                            break;
                        }
                    }
                    return false;
                });
            } else {
                mImgDelete.setVisibility(View.GONE);
                mRlContent.setOnClickListener(view -> {
                    switch (list.get(position).getApplicationIdentification()) {
                        case "app_park_select":
                            //车位查看
                            mContext.startActivity(new Intent(mContext, ATVehicleCheckActivity.class));
                            break;
                        case "app_my_car":
                            //我家的车
                            mContext.startActivity(new Intent(mContext, ATVehiclePassageActivity.class));
                            break;
                        case "app_visitor_appointment":
                            //访客预约
                            mContext.startActivity(new Intent(mContext, ATVisitorRecordActivity.class));
                            break;
                        case "app_face_access":
                            //人脸通行
                            mContext.startActivity(new Intent(mContext, ATUserFaceActivity.class));
                            break;
                        case "app_access_record":
                            //通行记录
                            mContext.startActivity(new Intent(mContext, ATVehiclePassageActivity.class));
                            break;
                        case "app_video_intercom":
                            //云对讲
                            mContext.startActivity(new Intent(mContext, ATVisualIntercomActivity.class));
                            break;
                        case "app_home_security":
                            //家庭安防
                            mContext.startActivity(new Intent(mContext, ATFamilySecurityActivity.class));
                            break;
                        case "app_scene_linkage":
                            //生活场景
                            mContext.startActivity(new Intent(mContext, ATLinkageActivity.class));
                            break;
                        case "app_wisdom_health":
                            //智慧健康
                            break;
                        case "app_care":
                            //老幼关怀
                            mContext.startActivity(new Intent(mContext, ATOldYoungCareActivity.class));
                            break;
                        case "app_home_monitor":
                            //家庭监控
                            mContext.startActivity(new Intent(mContext, ATFamilyMonitorActivity.class));
                            break;
                        case "app_my_equipment":
                            //我的设备
                            mContext.startActivity(new Intent(mContext, ATEquipmentActivity.class));
                            break;
                        case "app_intelligent_environment":
                            //智慧环境
                            mContext.startActivity(new Intent(mContext, ATEnvironmentActivity.class));
                            break;
                        case "app_voice_genie":
                            //语音精灵
//                            mContext.startActivity(new Intent(mContext, ATTmallWizardActivity.class));
                            mContext.startActivity(new Intent(mContext, ATTmallVoiceWizardActivity.class));
                            break;
                        case "app_public_security":
                            //公区安防
                            mContext.startActivity(new Intent(mContext, ATPublicSecurityMainActivity.class));
                            break;
                    }
                });
            }
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}