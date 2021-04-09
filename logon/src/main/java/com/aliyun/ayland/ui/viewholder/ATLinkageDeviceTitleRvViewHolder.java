package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATRecommendTemplateActionBean;
import com.aliyun.ayland.ui.activity.ATDiscoveryDeviceActivity;
import com.anthouse.xuhui.R;

/**
 * @author guikong on 18/4/8.
 */

public class ATLinkageDeviceTitleRvViewHolder extends ATSettableViewHolder {
    private TextView tvName, tvStatus;
    private LinearLayout llAdd;
    private final Context context;

    public ATLinkageDeviceTitleRvViewHolder(View view) {
        super(view);
        context = view.getContext();
        tvName = view.findViewById(R.id.tv_name);
        llAdd = view.findViewById(R.id.ll_add);
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (object instanceof ATRecommendTemplateActionBean) {
            tvName.setText(((ATRecommendTemplateActionBean) object).getActionName());
            llAdd.setVisibility(((ATRecommendTemplateActionBean) object).getDeviceDtoList().size() == 0 ? View.VISIBLE : View.GONE);
            llAdd.setOnClickListener(v -> context.startActivity(new Intent(context, ATDiscoveryDeviceActivity.class)));
        }
    }
}