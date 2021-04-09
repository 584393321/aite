package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATCarBean;
import com.aliyun.ayland.data.ATCarListBean;
import com.aliyun.ayland.ui.activity.ATMyCarDetailActivity;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATMyCarRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATCarListBean> list = new ArrayList<>();

    public ATMyCarRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_my_car, parent, false);
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

    public void setLists(List<ATCarListBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTextView = itemView.findViewById(R.id.textView);
        }

        public void setData(int position) {
            mTextView.setText(list.get(position).getPlateNumber());
            mRlContent.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, ATMyCarDetailActivity.class)
                    .putExtra("ATCarListBean", list.get(position))));
        }
    }
}
