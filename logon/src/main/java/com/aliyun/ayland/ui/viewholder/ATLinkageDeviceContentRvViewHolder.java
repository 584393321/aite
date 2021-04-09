package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATBrightnessLightBean;
import com.aliyun.ayland.ui.adapter.ATLinkageDeviceRvAdapter;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashSet;

/**
 * @author guikong on 18/4/8.
 */

public class ATLinkageDeviceContentRvViewHolder extends ATSettableViewHolder {
    private TextView tvName, tvRanking, tvStep, tvNumber;
    private ImageView img;
    private ATSwitchButton switchButton;
    private Context mContext;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);
    private ATLinkageDeviceRvAdapter atLinkageDeviceRvAdapter;

    public ATLinkageDeviceContentRvViewHolder(View view, ATLinkageDeviceRvAdapter atLinkageDeviceRvAdapter) {
        super(view);
        mContext = view.getContext();
        tvName = itemView.findViewById(R.id.tv_name);
        img = itemView.findViewById(R.id.img);
        switchButton = itemView.findViewById(R.id.switchButton);
        this.atLinkageDeviceRvAdapter = atLinkageDeviceRvAdapter;
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if(object instanceof ATBrightnessLightBean) {
            //灯
            Glide.with(mContext)
                    .load(((ATBrightnessLightBean)object).getProductImage())
                    .apply(options)
                    .into(img);
            switchButton.setChecked(atLinkageDeviceRvAdapter.getSet().contains(position));
            switchButton.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    atLinkageDeviceRvAdapter.getSet().add(position);
                }else {
                    atLinkageDeviceRvAdapter.getSet().remove(position);
                }
            });
            tvName.setText(TextUtils.isEmpty(((ATBrightnessLightBean)object).getNickName()) ? ((ATBrightnessLightBean)object).getProductName()
                    : ((ATBrightnessLightBean)object).getNickName());
        }
    }
}