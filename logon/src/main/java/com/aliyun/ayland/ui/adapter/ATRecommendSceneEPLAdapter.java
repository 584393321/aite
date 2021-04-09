package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneActionBean;
import com.aliyun.ayland.interfaces.ATOnEPLItemClickListener;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATRecommendSceneEPLAdapter extends BaseExpandableListAdapter {
    private List<ATSceneActionBean> list = new ArrayList<>();
    private Context context;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    // 是否有用户习惯
    public ATRecommendSceneEPLAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ATSceneActionBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getDeviceList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getDeviceList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_recommend_scene_group, parent, false);
            holder.mTvName = convertView.findViewById(R.id.tv_name);
            holder.mImg = convertView.findViewById(R.id.img);
            convertView.setTag(holder);
            ATAutoUtils.autoSize(convertView);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(list.get(groupPosition).getActionName());
        Glide.with(context).load(list.get(groupPosition).getActionIcon()).apply(options).into(holder.mImg);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_recommend_scene_child, parent, false);
            holder.mTvName = convertView.findViewById(R.id.tv_name);
            holder.mImg = convertView.findViewById(R.id.img);
            holder.switchview = convertView.findViewById(R.id.switchview);
            convertView.setTag(holder);
            ATAutoUtils.autoSize(convertView);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(list.get(groupPosition).getDeviceList().get(childPosition).getDeviceName());
        Glide.with(context).load(list.get(groupPosition).getDeviceList().get(childPosition).getDeviceIcon()).apply(options).into(holder.mImg);
        holder.switchview.setCheckedImmediately(1 == list.get(groupPosition).getDeviceList().get(childPosition).getDeviceStatus());
        holder.switchview.setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(groupPosition, childPosition,
                    list.get(groupPosition).getDeviceList().get(childPosition).getDeviceStatus()  == 1 ? 0 : 1);
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupViewHolder {
        TextView mTvName;
        ImageView mImg;
    }

    private class ChildViewHolder {
        TextView mTvName;
        ImageView mImg;
        ATSwitchButton switchview;
    }

    private ATOnEPLItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnEPLItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}