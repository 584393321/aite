package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.aliyun.ayland.data.ATEventInteger2;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.ui.activity.ATDiscoveryDeviceActivity;
import com.aliyun.ayland.ui.activity.ATIntelligentMonitorActivity;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.aliyun.iot.aep.component.router.Router;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ATEquipmentRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATDeviceBean> list = new ArrayList<>();
    private int specs;
    private long clickTime = 0;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATEquipmentRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_equipment_card, parent, false);
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
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setCheck(List<ATDeviceBean> list, int current_position) {
        this.list.clear();
        this.list.addAll(list);
        notifyItemChanged(current_position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent, mLlAdd, mLlAddReduce, mLlCurtain, mLlCurtainClose, mLlCurtainStop, mLlCurtainOpen, mLlReduce, mLlIncrease;
        private TextView mTvName, mTvCenter, mTvSpecs, mTvStatus;
        private ImageView mImgDevice;
        private RelativeLayout rlContent;
        private View mView;
        private ATSwitchButton mSwitchview;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            rlContent = itemView.findViewById(R.id.rl_content);
            mLlAdd = itemView.findViewById(R.id.ll_add);
            mLlCurtain = itemView.findViewById(R.id.ll_curtain);
            mSwitchview = itemView.findViewById(R.id.switchview);
            mView = itemView.findViewById(R.id.view);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvSpecs = itemView.findViewById(R.id.tv_specs);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            mImgDevice = itemView.findViewById(R.id.img_device);
            mLlCurtainClose = itemView.findViewById(R.id.ll_curtain_close);
            mLlCurtainStop = itemView.findViewById(R.id.ll_curtain_stop);
            mLlCurtainOpen = itemView.findViewById(R.id.ll_curtain_open);
        }

        public void setData(int position) {
            if (position == list.size() - 1) {
                mTvName.setText("添加设备");
                mLlAdd.setVisibility(View.VISIBLE);
                mLlContent.setVisibility(View.GONE);
                mLlAdd.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, ATDiscoveryDeviceActivity.class)));
            } else {
                mLlAdd.setVisibility(View.GONE);
                mLlContent.setVisibility(View.VISIBLE);
                if (list.get(position).getAttributes().size() > 0) {
                    mTvSpecs.setText(list.get(position).getAttributes().get(0).getName());
                    final int i = position;
                    if (!TextUtils.isEmpty(list.get(position).getAttributes().get(0).getDataType()))
                        switch (list.get(position).getAttributes().get(0).getDataType()) {
                            case "BOOL":
                                mSwitchview.setVisibility(View.VISIBLE);
                                mLlCurtain.setVisibility(View.GONE);
//                                mSwitchview.setOnCheckedChangeListener((compoundButton, b) -> {
//                                    //保留
//                                });
                                mSwitchview.setCheckedImmediately("1".equals(list.get(position).getAttributes().get(0).getValue()));
                                mSwitchview.setOnTouchListener((view, motionEvent) -> {
                                    if ((System.currentTimeMillis() - clickTime) > 500) {
                                        clickTime = System.currentTimeMillis();
                                        EventBus.getDefault().post(new ATEventInteger2("ATEquipmentActivity", i, mSwitchview.isChecked() ? 0 : 1));
                                    }
                                    return true;
                                });
//                                mSwitchview.setOnCheckedChangeListener((compoundButton, b) ->
//                                        EventBus.getDefault().post(new ATEventInteger2("ATEquipmentActivity", i, b ? 1 : 0)));
                                break;
                            case "ENUM":
                                if ("a1Z9cgRNvul".equals(list.get(position).getProductKey())) {
                                    //窗帘
                                    mSwitchview.setVisibility(View.GONE);
                                    mLlCurtain.setVisibility(View.VISIBLE);

                                    mLlCurtainClose.setOnClickListener(view -> EventBus.getDefault().post(new ATEventInteger2("ATEquipmentActivity", i, 0)));
                                    mLlCurtainOpen.setOnClickListener(view -> EventBus.getDefault().post(new ATEventInteger2("ATEquipmentActivity", i, 1)));
                                    mLlCurtainStop.setOnClickListener(view -> EventBus.getDefault().post(new ATEventInteger2("ATEquipmentActivity", i, 2)));
                                }
                                break;
                            case "INT":
                            case "DOUBLE":
                            case "FLOAT":
                                mSwitchview.setVisibility(View.GONE);
                                mLlCurtain.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                } else {
                    mTvSpecs.setText("");
                    mSwitchview.setVisibility(View.GONE);
                    mLlCurtain.setVisibility(View.GONE);
                }
                if (list.get(position).getStatus() != 1) {
                    mView.setVisibility(View.VISIBLE);
                    rlContent.setBackground(mContext.getResources().getDrawable(R.drawable.shape_18px_99fafaf9));
                    mTvSpecs.setText(mContext.getString(R.string.at_off_line));
                    mTvStatus.setText(mContext.getString(R.string.at_off));
                } else {
                    rlContent.setBackground(mContext.getResources().getDrawable(R.drawable.selector_18px_ffffff_eeeeee));
                    mView.setVisibility(View.GONE);
                    mTvSpecs.setText(mContext.getString(R.string.at_online));
                    mTvStatus.setText(mContext.getString(R.string.at_on));
                }
                mTvName.setText(TextUtils.isEmpty(list.get(position).getNickName())
                        ? list.get(position).getProductName() : list.get(position).getNickName());
                Glide.with(mContext).load(list.get(position).getProductImage()).apply(options).into(mImgDevice);
                mLlContent.setOnClickListener(view -> {
                    if (ATConstants.ProductKey.CAMERA_HAIKANG.equals(list.get(position).getProductKey())
                            || ATConstants.ProductKey.CAMERA_AITE.equals(list.get(position).getProductKey())
                            || ATConstants.ProductKey.CAMERA_XIAOMIYAN.equals(list.get(position).getProductKey())
                            || ATConstants.ProductKey.CAMERA_IVP.equals(list.get(position).getProductKey())
                            || "Camera".equals(list.get(position).getCategoryKey())) {
                        mContext.startActivity(new Intent(mContext, ATIntelligentMonitorActivity.class)
                                .putExtra("productKey", list.get(position).getProductKey())
                                .putExtra("iotId", list.get(position).getIotId())
                                .putExtra("status", "device"));
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("iotId", list.get(position).getIotId());
                        Router.getInstance().toUrl(mContext, "link://router/" + list.get(position).getProductKey(), bundle);
                    }
                });
            }
        }
    }
}
