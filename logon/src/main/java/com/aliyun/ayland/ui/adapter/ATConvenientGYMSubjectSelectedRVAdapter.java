package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATGymSubjectTempBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ATConvenientGYMSubjectSelectedRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private String price = "";
    private List<ATGymSubjectTempBean> list = new ArrayList<>();

    public ATConvenientGYMSubjectSelectedRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_subject_selected, parent, false);
        return new ConvenientGYMSubjectSelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConvenientGYMSubjectSelectedViewHolder convenientGYMSubjectSelectedViewHolder = (ConvenientGYMSubjectSelectedViewHolder) holder;
        convenientGYMSubjectSelectedViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setMap(String gymIdappointmentDay, Map<String, List<String>> map) {
        list.clear();
        for (String s : map.keySet()) {
            if (gymIdappointmentDay.equals(s.split(" ")[0] + " " + s.split(" ")[1]))
                for (String s1 : map.get(s)) {
                    ATGymSubjectTempBean ATGymSubjectTempBean = new ATGymSubjectTempBean();
                    ATGymSubjectTempBean.setAppointmentTime(s1);
                    ATGymSubjectTempBean.setTempKey(s);
                    ATGymSubjectTempBean.setTempValue(map.get(s));
                    ATGymSubjectTempBean.setProjectName(s.split(" ")[4]);
                    ATGymSubjectTempBean.setPrice(s.split(" ")[5]);
                    list.add(ATGymSubjectTempBean);
                }
        }
        notifyDataSetChanged();
    }

    public class ConvenientGYMSubjectSelectedViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgDelete;
        private TextView mTvTime, mTvPrice;

        private ConvenientGYMSubjectSelectedViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mImgDelete = itemView.findViewById(R.id.img_delete);
        }

        public void setData(int position) {
            mTvTime.setText(list.get(position).getAppointmentTime() + " " + list.get(position).getProjectName());
            mTvPrice.setText(String.format(mContext.getResources().getString(R.string.at_price_), list.get(position).getPrice()));
            mImgDelete.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(view, list.get(getAdapterPosition()).getTempKey(), list.get(getAdapterPosition()).getAppointmentTime());
                list.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String key, String appointment);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
