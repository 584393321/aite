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
import com.aliyun.ayland.data.ATShareSpaceAppBean;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATShareGardenAppRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATShareSpaceAppBean> list = new ArrayList<>();
    private int selected_position = 0;

    public ATShareGardenAppRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_share_garden_subject, parent, false);
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

    public void setLists(List<ATShareSpaceAppBean> list, int position) {
        this.list.clear();
        this.list.addAll(list);
        selected_position = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView textView;
        private View view;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            textView = itemView.findViewById(R.id.textView);
            view = itemView.findViewById(R.id.view);
        }

        public void setData(int position) {
            textView.setText(list.get(position).getName());
            if (selected_position == position) {
                textView.setTextColor(mContext.getResources().getColor(R.color._FDA448));
                view.setVisibility(View.VISIBLE);
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                view.setVisibility(View.GONE);
            }
            mRlContent.setOnClickListener(view -> {
                if (position != selected_position) {
                    mOnItemClickListener.onItemClick(view, position);
                    selected_position = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    private ATOnRVItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRVItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}