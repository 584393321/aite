package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageIconRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private int select = -1;
    private List<String> list = new ArrayList<>();

    public ATLinkageIconRVAdapter(Activity context) {
        mContext = context;
    }

    public void setList(List<String> list, int selectPosition) {
        this.list.clear();
        this.list.addAll(list);
        select = selectPosition;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_icon, parent, false);
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
        private LinearLayout mLlContent;
        private ImageView mImg;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mImg = itemView.findViewById(R.id.img);
        }

        public void setData(int position) {
            Glide.with(mContext).load(list.get(position)).into(mImg);
            if (position == select) {
                mLlContent.setBackground(mContext.getResources().getDrawable(R.drawable.shape_12px_3pxf5cea7_fff0e1));
            } else {
                mLlContent.setBackground(mContext.getResources().getDrawable(R.drawable.shape_12px_3pxf0f0f0));
            }

            mLlContent.setOnClickListener((view) -> {
                mOnItemClickListener.onItemClick(view, position);
                setSelectItem(position);
            });
            itemView.setTag(position);
        }
    }

    public void setSelectItem(int select) {
        this.select = select;
        notifyDataSetChanged();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
