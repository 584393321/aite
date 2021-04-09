package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATCommunityTypeBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATCommunityTypeRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATCommunityTypeBean> list;
    private int current_position;

    public ATCommunityTypeRVAdapter(Activity context, ArrayList<ATCommunityTypeBean> list, int current_position) {
        mContext = context;
        this.list = list;
        this.current_position = current_position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_community_type, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTextView = itemView.findViewById(R.id.textView);
        }

        public void setData(int position) {
            mTextView.setText(list.get(position).getTypeName());
            if (current_position == position) {
                mTextView.setTextColor(mContext.getResources().getColor(R.color._EBB080));
                mTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_51px2pxebb080));
            } else {
                mTextView.setTextColor(mContext.getResources().getColor(R.color._666666));
                mTextView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_51px2pxeeeeee));
            }
            mTextView.setOnClickListener(view -> {
                if (current_position != position) {
                    current_position = position;
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(view, null, position);
                }
            });
        }
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}