package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATShareSpaceTimeBean;
import com.anthouse.xuhui.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ATShareGardenTimeSelectedRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATShareSpaceTimeBean> list = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#####0.00");

    public ATShareGardenTimeSelectedRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_subject_selected, parent, false);
        return new ATShareGardenTimeSelectedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ATShareGardenTimeSelectedViewHolder ATShareGardenTimeSelectedViewHolder = (ATShareGardenTimeSelectedViewHolder) holder;
        ATShareGardenTimeSelectedViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<ATShareSpaceTimeBean> getList() {
        return list;
    }

    public void setMap(HashMap<String, List<ATShareSpaceTimeBean>> timeMap, HashMap<String, HashSet<Integer>> timeSelectedMap) {
        list.clear();
        for (String s : timeSelectedMap.keySet()) {
            for (Integer integer : timeSelectedMap.get(s)) {
                ATShareSpaceTimeBean atShareSpaceTimeBean = timeMap.get(s).get(integer);
                atShareSpaceTimeBean.setAppointmentHour(s);
                atShareSpaceTimeBean.setPosition(integer);
                list.add(atShareSpaceTimeBean);
            }
        }
        Collections.sort(list, (o1, o2) -> o1.getTimeslot().compareTo(o2.getTimeslot()));
        notifyDataSetChanged();
    }

    public class ATShareGardenTimeSelectedViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgDelete;
        private TextView mTvTime, mTvPrice;

        private ATShareGardenTimeSelectedViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mImgDelete = itemView.findViewById(R.id.img_delete);
        }

        public void setData(int position) {
            mTvTime.setText(list.get(position).getTimeslot());
            mTvPrice.setText(String.format(mContext.getResources().getString(R.string.at_price_), df.format(list.get(position).getUnitPrice())));
            mImgDelete.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(list.get(getAdapterPosition()));
                list.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(ATShareSpaceTimeBean atShareSpaceTimeBean);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}