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
import com.aliyun.ayland.data.ATShareSpaceTimeBean;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.anthouse.xuhui.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ATShareGardenTimeRVAdapter extends RecyclerView.Adapter {
    private Activity context;
    private List<ATShareSpaceTimeBean> list = new ArrayList<>();
    private HashSet<Integer> set = new HashSet<>();
    private DecimalFormat df = new DecimalFormat("#####0.00");

    public ATShareGardenTimeRVAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.at_item_rv_share_garden_time, parent, false);
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

    public void setLists(List<ATShareSpaceTimeBean> list, HashSet<Integer> set) {
        this.list.clear();
        this.list.addAll(list);
        this.set.clear();
        this.set.addAll(set);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView tvTime, tvPrice;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

        public void setData(int position) {
            tvTime.setText(list.get(position).getTimeslot());
            tvPrice.setText(df.format(list.get(position).getUnitPrice()));
            
            if(list.get(position).getAppointmentStatus() == 1){
                //不可预约
                tvTime.setTextColor(context.getResources().getColor(R.color._CCCCCC));
                tvPrice.setTextColor(context.getResources().getColor(R.color._CCCCCC));
                mRlContent.setBackground(context.getResources().getDrawable(R.drawable.shape_12pxffffff));
                mRlContent.setOnClickListener(view-> {
                });
            }else {
                tvTime.setTextColor(context.getResources().getColor(R.color._666666));
                tvPrice.setTextColor(context.getResources().getColor(R.color._86523C));
                if(set.contains(position)){
                    //已选中
                    mRlContent.setBackground(context.getResources().getDrawable(R.drawable.shape_12px_3pxe2b3a1_fffaf8));
                    mRlContent.setOnClickListener(view-> {
                        set.remove(position);
                        notifyItemChanged(position);
                        mOnItemClickListener.onItemClick(view, position);
                    });
                }else {
                    //未选中
                    mRlContent.setBackground(context.getResources().getDrawable(R.drawable.shape_12pxffffff));
                    mRlContent.setOnClickListener(view-> {
                        set.add(position);
                        notifyItemChanged(position);
                        mOnItemClickListener.onItemClick(view, position);
                    });
                }
            }
        }
    }

    private ATOnRVItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRVItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}