package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATGymAppointRecordBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATGymAppointRecordRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATGymAppointRecordBean> list = new ArrayList<>();

    public ATGymAppointRecordRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_appoint_record, parent, false);
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

    public void setLists(List<ATGymAppointRecordBean> list, int pageNum) {
        if (pageNum == 0)
            this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTvName, mTvAddress, mTvPlan, mTvSubject, mTvGym;
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            img = itemView.findViewById(R.id.img);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvPlan = itemView.findViewById(R.id.tv_plan);
            mTvSubject = itemView.findViewById(R.id.tv_subject);
            mTvGym = itemView.findViewById(R.id.tv_gym);
        }

        public void setData(int position) {
            if (list.get(position).getUseStatus() == 1) {
                mTvName.setText(mContext.getString(R.string.at_have_done));
                img.setImageResource(R.drawable.at_icon_s_gxjsf_yidabiao);
            } else {
                mTvName.setText(mContext.getString(R.string.at_out_off_time));
                img.setImageResource(R.drawable.at_icon_s_gxjsf_bufenwancheng);
            }
            mTvAddress.setText(list.get(position).getGymName());
            mTvSubject.setText(list.get(position).getProjectName());
            mTvPlan.setText(list.get(position).getAppointmentDay() + "  " + list.get(position).getAppointmentTime());
            if (TextUtils.isEmpty(list.get(position).getActualStartTimeStr())) {
                mTvGym.setText(mContext.getString(R.string.at_unknown));
            } else {
                mTvGym.setText(list.get(position).getActualStartTimeStr()
                        + (TextUtils.isEmpty(list.get(position).getActualEndTimeStr()) ? "" : TextUtils.isEmpty(list.get(position).getActualEndTimeStr())));
            }
        }
    }
}