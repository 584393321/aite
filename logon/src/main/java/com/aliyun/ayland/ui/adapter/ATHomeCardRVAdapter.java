package com.aliyun.ayland.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.aliyun.ayland.data.ATApplicationBean;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.ui.activity.ATEnvironmentActivity;
import com.aliyun.ayland.ui.activity.ATEquipmentActivity;
import com.aliyun.ayland.ui.activity.ATFamilyMonitorActivity;
import com.aliyun.ayland.ui.activity.ATFamilySecurityActivity;
import com.aliyun.ayland.ui.activity.ATLinkageActivity;
import com.aliyun.ayland.ui.activity.ATOldYoungCareActivity;
import com.aliyun.ayland.ui.activity.ATPublicSecurityMainActivity;
import com.aliyun.ayland.ui.activity.ATSFMainActivity;
import com.aliyun.ayland.ui.activity.ATTmallVoiceWizardActivity;
import com.aliyun.ayland.ui.activity.ATUserFaceActivity;
import com.aliyun.ayland.ui.activity.ATVehicleCheckActivity;
import com.aliyun.ayland.ui.activity.ATVehiclePassageActivity;
import com.aliyun.ayland.ui.activity.ATVisitorRecordActivity;
import com.aliyun.ayland.ui.activity.ATVisualIntercomActivity;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.utils.ATToastUtils;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ATHomeCardRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATApplicationBean> list = new ArrayList<>();
    private Dialog dialog;

    public ATHomeCardRVAdapter(Activity context) {
        mContext = context;
        initDialog();
    }

    @SuppressLint("InflateParams")
    private void initDialog() {
        dialog = new Dialog(mContext, R.style.nameDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_dialog_sf, null, false);
        ((TextView) view.findViewById(R.id.tv_title)).setText(mContext.getString(R.string.at_sf_tip2));
        TextView textView = view.findViewById(R.id.tv_sure);
        textView.setText(mContext.getString(R.string.at_sf_to_sf));
        view.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> dialog.dismiss());
        textView.setOnClickListener(view2 -> {
            dialog.dismiss();
            mContext.startActivity(new Intent(mContext, ATSFMainActivity.class));
        });
        dialog.setContentView(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_home_application, parent, false);
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

    public void setLists(List<ATApplicationBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvName;
        private ImageView mImg;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            mImg = itemView.findViewById(R.id.img);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getApplicationName());
            mImg.setImageResource(ATResourceUtils.getResIdByName("atbg_" + position, ATResourceUtils.ResourceType.DRAWABLE));
            mRlContent.setOnClickListener(view -> {
                switch (list.get(position).getApplicationIdentification()) {
                    case "app_park_select":
                        //车位查看
                        mContext.startActivity(new Intent(mContext, ATVehicleCheckActivity.class));
                        break;
                    case "app_my_car":
                        //我家的车
//                        mContext.startActivity(new Intent(mContext, ATVehiclePassageActivity.class));
                        break;
                    case "app_visitor_appointment":
                        //访客预约
                        mContext.startActivity(new Intent(mContext, ATVisitorRecordActivity.class));
                        break;
                    case "app_face_access":
                        //人脸通行
//                        mContext.startActivity(new Intent(mContext, ATUserFaceActivity.class));
                        break;
                    case "app_access_record":
                        //通行记录
                        mContext.startActivity(new Intent(mContext, ATVehiclePassageActivity.class));
                        break;
                    case "app_video_intercom":
                        //云对讲
                        mContext.startActivity(new Intent(mContext, ATVisualIntercomActivity.class));
                        break;
                    case "app_my_equipment":
                        //我的设备
                        if (TextUtils.isEmpty(ATGlobalApplication.getHouseState())
                                || "101".equals(ATGlobalApplication.getHouseState())
                                || "102".equals(ATGlobalApplication.getHouseState())
                                || "103".equals(ATGlobalApplication.getHouseState())) {
                            ATToastUtils.shortShow(mContext.getString(R.string.at_sf_tip1));
                        } else if ("104".equals(ATGlobalApplication.getHouseState())) {
                            dialog.show();
                        } else
                            mContext.startActivity(new Intent(mContext, ATEquipmentActivity.class));
                        break;
                    case "app_scene_linkage":
                        //生活场景
                        if (TextUtils.isEmpty(ATGlobalApplication.getHouseState())
                                || "101".equals(ATGlobalApplication.getHouseState())
                                || "102".equals(ATGlobalApplication.getHouseState())
                                || "103".equals(ATGlobalApplication.getHouseState())) {
                            ATToastUtils.shortShow(mContext.getString(R.string.at_sf_tip1));
                        } else if ("104".equals(ATGlobalApplication.getHouseState())) {
                            dialog.show();
                        } else
                            mContext.startActivity(new Intent(mContext, ATLinkageActivity.class));
                        break;
                    case "app_home_security":
                        //家庭安防
                        if (TextUtils.isEmpty(ATGlobalApplication.getHouseState())
                                || "101".equals(ATGlobalApplication.getHouseState())
                                || "102".equals(ATGlobalApplication.getHouseState())
                                || "103".equals(ATGlobalApplication.getHouseState())) {
                            ATToastUtils.shortShow(mContext.getString(R.string.at_sf_tip1));
                        } else if ("104".equals(ATGlobalApplication.getHouseState())) {
                            dialog.show();
                        } else
                            mContext.startActivity(new Intent(mContext, ATFamilySecurityActivity.class));
                        break;
                    case "app_public_security":
                        //公区安防
                        mContext.startActivity(new Intent(mContext, ATPublicSecurityMainActivity.class));
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
                    case "app_intelligent_environment":
                        //智慧环境
                        mContext.startActivity(new Intent(mContext, ATEnvironmentActivity.class));
                        break;
                    case "app_voice_genie":
                        //语音精灵
//                        mContext.startActivity(new Intent(mContext, ATTmallWizardActivity.class));
                        mContext.startActivity(new Intent(mContext, ATTmallVoiceWizardActivity.class));
                        break;
                }
            });
        }
    }
}