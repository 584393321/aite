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
import com.aliyun.ayland.data.ATCategoryTypeBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATSpeedyLifeTextRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATCategoryTypeBean> list = new ArrayList<>();
    private int select = 0;

    public ATSpeedyLifeTextRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_speedy_life_text, parent, false);
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

    public void setLists(List<ATCategoryTypeBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private View view;
        private RelativeLayout rlContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTextView = itemView.findViewById(R.id.text);
            view = itemView.findViewById(R.id.view);
            rlContent = itemView.findViewById(R.id.rl_content);
        }

        public void setData(int position) {
            if (select == position) {
                mTextView.setTextColor(mContext.getResources().getColor(R.color._EAA520));
                view.setVisibility(View.VISIBLE);
            } else {
                mTextView.setTextColor(mContext.getResources().getColor(R.color._666666));
                view.setVisibility(View.GONE);
            }
            mTextView.setText(list.get(position).getCategoryName());
            rlContent.setOnClickListener(view -> {
                select = position;
                mOnItemClickListener.onItemClick(view, list.get(position), position);
                notifyDataSetChanged();
            });
        }
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
