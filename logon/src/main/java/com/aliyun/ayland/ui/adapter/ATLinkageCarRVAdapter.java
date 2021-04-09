package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATCarBean;
import com.aliyun.ayland.data.ATCarListBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageCarRVAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ATCarListBean> list = new ArrayList<>();
    private int selectPosition = -1;

    public ATLinkageCarRVAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_car, parent, false);
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

    public void setList(List<ATCarListBean> list, int selectPosition) {
        this.selectPosition = selectPosition;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvCarNumber, mTvParkName;
        private CheckBox mCbSelect;
        private RelativeLayout mRlContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvCarNumber = itemView.findViewById(R.id.tv_car_number);
            mTvParkName = itemView.findViewById(R.id.tv_park_name);
            mCbSelect = itemView.findViewById(R.id.cb_select);
            mRlContent = itemView.findViewById(R.id.rl_content);
        }

        public void setData(int position) {
            mTvCarNumber.setText(list.get(position).getPlateNumber());
            mTvParkName.setText(list.get(position).getParkName());
            mRlContent.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(view, position);
                selectPosition = position;
                notifyDataSetChanged();
            });
            if (selectPosition == position) {
                mCbSelect.setChecked(true);
            } else {
                mCbSelect.setChecked(false);
            }
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}