package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;

public class ATRoomPicRVAdapter extends RecyclerView.Adapter {
    private String[] room_type;
    private Activity mContext;
    private int select = -1;

    public ATRoomPicRVAdapter(Activity context, String[] room_type) {
        mContext = context;
        this.room_type = room_type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_room_pic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return room_type.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
        }

        public void setData(int position) {
            mTvName.setText(ATResourceUtils.getResIdByName(room_type[position], ATResourceUtils.ResourceType.STRING));
            if (position == select) {
                mTvName.setTextColor(mContext.getResources().getColor(R.color.white));
//                mImg.setImageResource(ATResourceUtils.getResIdByName("at_room_" + room_type[position] + "_c", ATResourceUtils.ResourceType.DRAWABLE));
                mTvName.setBackground(mContext.getResources().getDrawable(R.drawable.shape_15px_fda448));
            } else {
                mTvName.setTextColor(mContext.getResources().getColor(R.color._666666));
//                mImg.setImageResource(ATResourceUtils.getResIdByName("at_room_" + room_type[position] + "_b", ATResourceUtils.ResourceType.DRAWABLE));
                mTvName.setBackground(mContext.getResources().getDrawable(R.drawable.shape_15px_f2f2f2));
            }

            mTvName.setOnClickListener((view) -> {
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
