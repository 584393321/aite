package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATAccessRecordSelectRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<String> list = new ArrayList<>();
    private int select = 0;

    public ATAccessRecordSelectRVAdapter(Activity context) {
        mContext = context;
        list.add(mContext.getString(R.string.at_total));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_access_record_select, parent, false);
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

    public void setLists(List<String> list) {
        this.list.clear();
        this.list.add(mContext.getString(R.string.at_total));
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTextView = itemView.findViewById(R.id.textView);
        }

        public void setData(int position) {
            mTextView.setText(list.get(position));
            mTextView.setOnClickListener(view -> {
                if(select != position){
                    select = position;
                    notifyDataSetChanged();
                }
            });
            if(select == position){
                mTextView.setSelected(true);
            }else {
                mTextView.setSelected(false);
            }
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}