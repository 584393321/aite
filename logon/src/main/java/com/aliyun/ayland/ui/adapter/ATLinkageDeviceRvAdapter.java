package com.aliyun.ayland.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.ayland.data.ATBrightnessLightBean;
import com.aliyun.ayland.data.ATRecommendTemplateActionBean;
import com.aliyun.ayland.ui.viewholder.ATLinkageDeviceContentRvViewHolder;
import com.aliyun.ayland.ui.viewholder.ATLinkageDeviceTitleRvViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ATLinkageDeviceRvAdapter extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;
    private List<Object> data = new ArrayList<>();
    private HashSet<Integer> set = new HashSet<>();

    public void setList(List<ATRecommendTemplateActionBean> aTRecommendTemplateActionBeanList) {
        data.clear();
        for (ATRecommendTemplateActionBean atRecommendTemplateActionBean : aTRecommendTemplateActionBeanList) {
            data.add(atRecommendTemplateActionBean);
            for (ATBrightnessLightBean atBrightnessLightBean : atRecommendTemplateActionBean.getDeviceDtoList()) {
                atBrightnessLightBean.setAction(atRecommendTemplateActionBean.getActionType());
                data.add(atBrightnessLightBean);
            }
        }
        notifyDataSetChanged();
    }

    public List<Object> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof ATRecommendTemplateActionBean) {
            return TYPE_TITLE;
        } else {
            return TYPE_CONTENT;
        }
    }

    @NonNull
    @Override
    public ATSettableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (TYPE_TITLE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_linkage_device_title, parent, false);
            return new ATLinkageDeviceTitleRvViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.at_item_rv_linkage_device_content, parent, false);
            return new ATLinkageDeviceContentRvViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(ATSettableViewHolder holder, int position) {
        Object item = data.get(position);
        holder.setData(item, position, data.size());
    }

    public HashSet<Integer> getSet() {
        return set;
    }
}