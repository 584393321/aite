package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneBean;
import com.anthouse.xuhui.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ATMonitorRecordRVAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<ATSceneBean> list = new ArrayList<>();

    public ATMonitorRecordRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_monitor_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setList(List<ATSceneBean> mCameraRecordList, int offset) {
        if(offset == 0)
            list.clear();
        list.addAll(mCameraRecordList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvCameraName;
        private TextView mTvCameraStartTime;
        private TextView mTvCameraRecordTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvCameraName = itemView.findViewById(R.id.tv_camera_name);
            mTvCameraStartTime = itemView.findViewById(R.id.tv_camera_start_time);
            mTvCameraRecordTime = itemView.findViewById(R.id.tv_camera_record_time);
        }

        public void setData(int position) {
//            mTvCameraName.setText(list.get(position).getName());
//            String startTime = list.get(position).getStart_time().substring(0, 19);
//            String endTime = list.get(position).getEnd_time().substring(0, 19);
//
//            mTvCameraStartTime.setText(startTime.substring(5));
//            mTvCameraRecordTime.setText(getTimeExpend(startTime, endTime));
        }
    }

    private String getTimeExpend(String startTime, String endTime) {
        long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差

        String longHours = longExpend / (60 * 60 * 1000) + ""; //根据时间差来计算小时数
        String longMinutes = longExpend % (60 * 60 * 1000) / (60 * 1000) + "";  //根据时间差来计算分钟数
        String longSecond = longExpend % (60 * 60 * 1000) % (60 * 1000) / 1000 + "";  //根据时间差来计算分钟数

        if (longHours.length() == 1)
            longHours = 0 + longHours;
        if (longMinutes.length() == 1)
            longMinutes = 0 + longMinutes;
        if (longSecond.length() == 1)
            longSecond = 0 + longSecond;
        return longHours + mContext.getString(R.string.at_hour) + longMinutes + mContext.getString(R.string.at_minute) + longSecond+ mContext.getString(R.string.at_second);
    }

    private long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return returnMillis;
    }
}