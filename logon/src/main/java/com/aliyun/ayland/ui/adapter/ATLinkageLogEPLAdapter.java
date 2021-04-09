package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATLinkageLogBean;
import com.anthouse.xuhui.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ATLinkageLogEPLAdapter extends BaseExpandableListAdapter {
    private List<List<ATLinkageLogBean>> list = new ArrayList<>();
    private Context context;
    private Gson gson = new Gson();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    // 是否有用户习惯
    public ATLinkageLogEPLAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<List<ATLinkageLogBean>> list) {
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
        return list.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).get(childPosition);
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
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_linkage_log_group, parent, false);
            holder.mTvTime = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
            ATAutoUtils.autoSize(convertView);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(list.get(groupPosition).get(0).getCreateTime())) {
            holder.mTvTime.setText(getStringToWeek(list.get(groupPosition).get(0).getCreateTime()));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_linkage_log_child, parent, false);
            holder.mRlContent = convertView.findViewById(R.id.rl_content);
            holder.mTvName = convertView.findViewById(R.id.tv_name);
            holder.mTvStatus = convertView.findViewById(R.id.tv_status);
            holder.mTvTime = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
            ATAutoUtils.autoSize(convertView);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(list.get(groupPosition).get(childPosition).getSceneName());
        if (!TextUtils.isEmpty(list.get(groupPosition).get(childPosition).getCreateTime())) {
            holder.mTvTime.setText(list.get(groupPosition).get(childPosition).getCreateTime().split(" ")[1]);
        }
        holder.mTvStatus.setText(list.get(groupPosition).get(childPosition).getSuccess());
        if (context.getString(R.string.at_perform_success).equals(list.get(groupPosition).get(childPosition).getSuccess())) {
            holder.mTvStatus.setTextColor(context.getResources().getColor(R.color._999999));
        } else {
            holder.mTvStatus.setTextColor(context.getResources().getColor(R.color._86523C));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupViewHolder {
        TextView mTvTime;
    }

    private class ChildViewHolder {
        RelativeLayout mRlContent;
        TextView mTvName;
        TextView mTvStatus;
        TextView mTvTime;
    }

    private String getStringToWeek(String dateString) {
        Date date = new Date();
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int year = cd.get(Calendar.YEAR); //获取年份
        int month = cd.get(Calendar.MONTH) + 1; //获取月份
        int day = cd.get(Calendar.DAY_OF_MONTH); //获取日期
        int week = cd.get(Calendar.DAY_OF_WEEK); //获取星期
        String weekString;
        switch (week) {
            case Calendar.SUNDAY:
                weekString = "星期日";
                break;
            case Calendar.MONDAY:
                weekString = "星期一";
                break;
            case Calendar.TUESDAY:
                weekString = "星期二";
                break;
            case Calendar.WEDNESDAY:
                weekString = "星期三";
                break;
            case Calendar.THURSDAY:
                weekString = "星期四";
                break;
            case Calendar.FRIDAY:
                weekString = "星期五";
                break;
            default:
                weekString = "星期六";
                break;

        }
        return year + "-" + month + "-" + day + " " + weekString;
    }
}
