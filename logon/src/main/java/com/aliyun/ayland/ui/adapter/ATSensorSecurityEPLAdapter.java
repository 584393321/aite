package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSensorDeploymentBean;
import com.aliyun.ayland.interfaces.ATOnEPLItemClickListener;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATSensorSecurityEPLAdapter extends BaseExpandableListAdapter {
    private List<ATSensorDeploymentBean> list = new ArrayList<>();
    private Context context;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    // 是否有用户习惯
    public ATSensorSecurityEPLAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ATSensorDeploymentBean> list) {
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
        return list.get(groupPosition).getData().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) { return list.get(groupPosition).getData().get(childPosition); }

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
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_sensor_security_group, parent, false);
            holder.mTvName = convertView.findViewById(R.id.tv_name);
            holder.mTvNumber = convertView.findViewById(R.id.tv_number);
            holder.imgAdd = convertView.findViewById(R.id.img_add);
            holder.imgDown = convertView.findViewById(R.id.img_down);
            holder.mRlGroup = convertView.findViewById(R.id.rl_group);
            convertView.setTag(holder);
            ATAutoUtils.autoSize(convertView);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(list.get(groupPosition).getName());
        holder.mTvNumber.setText(String.format(context.getString(R.string.at_amount_), list.get(groupPosition).getSize()));
        holder.imgDown.setImageResource(R.drawable.atico_xiala);
        if (isExpanded) {
            holder.imgDown.setImageResource(R.drawable.atico_junp_shouhui);
            holder.mRlGroup.setBackgroundResource(R.drawable.shape_top_12px_f5f5f5);
        } else {
            holder.imgDown.setImageResource(R.drawable.atico_xiala);
            holder.mRlGroup.setBackgroundResource(R.drawable.shape_12px_f5f5f5);
        }
        if (list.get(groupPosition).getSize() == 0) {
            holder.imgAdd.setVisibility(View.VISIBLE);
            holder.imgDown.setVisibility(View.GONE);
            holder.imgAdd.setOnClickListener(view -> mOnItemClickListener.onItemClick(groupPosition));
            holder.mRlGroup.setBackgroundResource(R.drawable.shape_12px_f5f5f5);
        } else {
            holder.imgDown.setVisibility(View.VISIBLE);
            holder.imgAdd.setVisibility(View.GONE);
            holder.imgDown.setOnClickListener(view -> mOnItemClickListener.onItemClick(groupPosition, 0));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_sensor_security_child, parent, false);
            holder.mTvName = convertView.findViewById(R.id.tv_name);
            holder.img = convertView.findViewById(R.id.img);
            holder.llSwitchButton = convertView.findViewById(R.id.ll_switchButton);
            holder.switchButton = convertView.findViewById(R.id.switchButton);
            holder.mTvLocation = convertView.findViewById(R.id.tv_location);
            convertView.setTag(holder);
            ATAutoUtils.autoSize(convertView);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(list.get(groupPosition).getData().get(childPosition).getProductName());
        holder.mTvLocation.setText(list.get(groupPosition).getData().get(childPosition).getSpaceName());

        holder.switchButton.setCheckedImmediately(1 == list.get(groupPosition).getData().get(childPosition).getDeployType());
        holder.switchButton.setClickable(false);
        holder.llSwitchButton.setOnClickListener(view -> mOnItemClickListener.onItemClick(groupPosition, childPosition,
                list.get(groupPosition).getData().get(childPosition).getDeployType() == 1 ? 0 : 1));

        Glide.with(context).load(list.get(groupPosition).getData().get(childPosition).getProductImage()).apply(options).into(holder.img);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupViewHolder {
        TextView mTvName, mTvNumber;
        ImageView imgDown, imgAdd;
        RelativeLayout mRlGroup;
    }

    private static class ChildViewHolder {
        TextView mTvName, mTvLocation;
        ATSwitchButton switchButton;
        LinearLayout llSwitchButton;
        ImageView img;
    }

    private ATOnEPLItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnEPLItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}