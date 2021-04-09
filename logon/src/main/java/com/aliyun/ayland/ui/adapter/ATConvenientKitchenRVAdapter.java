package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATKitchenProjectBean.TimeBean;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATConvenientKitchenRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<TimeBean> list = new ArrayList<>();
    private int selected_position = -1;

    public ATConvenientKitchenRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_kitchen, parent, false);
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

    public void setLists(List<TimeBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView tvName, tvTime, tvPrice;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

        public void setData(int position) {
            tvName.setText(list.get(position).getItemName());
            tvTime.setText(list.get(position).getItemTime());
            tvPrice.setText(String.format(mContext.getString(R.string.at_price_), list.get(position).getItemPrice()));
            if (list.get(position).getUseStatus() != -1)
                if (position == selected_position) {
                    tvName.setTextColor(mContext.getResources().getColor(R.color._EBB080));
                    tvTime.setTextColor(mContext.getResources().getColor(R.color._EBB080));
                    tvPrice.setTextColor(mContext.getResources().getColor(R.color._EBB080));
                } else {
                    tvName.setTextColor(mContext.getResources().getColor(R.color._666666));
                    tvTime.setTextColor(mContext.getResources().getColor(R.color._666666));
                    tvPrice.setTextColor(mContext.getResources().getColor(R.color._666666));
                }
            else {
                tvName.setTextColor(mContext.getResources().getColor(R.color._CCCCCC));
                tvTime.setTextColor(mContext.getResources().getColor(R.color._CCCCCC));
                tvPrice.setTextColor(mContext.getResources().getColor(R.color._CCCCCC));
            }

            mRlContent.setOnClickListener(view -> {
                if (list.get(position).getUseStatus() == -1)
                    return;
                selected_position = position;
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(view, position);
            });
        }
    }

    private ATOnRVItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRVItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
