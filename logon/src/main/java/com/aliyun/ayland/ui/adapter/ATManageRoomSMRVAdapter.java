package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ATManageRoomSMRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATRoomBean1> list = new ArrayList<>();
    private SwipeMenuRecyclerView mMenuRecyclerView;

    public ATManageRoomSMRVAdapter(SwipeMenuRecyclerView menuRecyclerView, Activity context) {
        this.mMenuRecyclerView = menuRecyclerView;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_smrv_manage_room, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mMenuRecyclerView = mMenuRecyclerView;
        mMenuRecyclerView.setItemViewSwipeEnabled(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATRoomBean1> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int adapterPosition) {
        this.list.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvRoomName;
        private ImageView mImgRight, mImgDelete;
        private SwipeMenuRecyclerView mMenuRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvRoomName = itemView.findViewById(R.id.tv_room_name);
            mImgDelete = itemView.findViewById(R.id.img_delete);
            mImgRight = itemView.findViewById(R.id.img_right);
            mImgDelete.setOnClickListener(view ->
                    mMenuRecyclerView.smoothOpenRightMenu(getAdapterPosition())
            );
            mImgRight.setOnTouchListener((view, motionEvent) -> {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        mMenuRecyclerView.startDrag(ViewHolder.this);
                        break;
                    }
                }
                return false;
            });
        }

        public void setData(int position) {
            mTvRoomName.setText(list.get(position).getName());
        }
    }
}