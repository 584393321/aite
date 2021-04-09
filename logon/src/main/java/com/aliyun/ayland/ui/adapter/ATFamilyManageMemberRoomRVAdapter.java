package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEventInteger2;
import com.aliyun.ayland.data.ATFamilyManageRoomBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyManageMemberRoomRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATFamilyManageRoomBean> list = new ArrayList<>();

    public ATFamilyManageMemberRoomRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_manage_member_room, parent, false);
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

    public void setLists(List<ATFamilyManageRoomBean> roomList) {
        this.list.clear();
        this.list.addAll(roomList);
        notifyDataSetChanged();
    }

    public void notifyItem(int current_position, int status) {
        list.get(current_position).setCanSee(status);
        notifyItemChanged(current_position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private ATSwitchButton switchButton;
        private LinearLayout llSwitchButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            switchButton = itemView.findViewById(R.id.switchButton);
            llSwitchButton = itemView.findViewById(R.id.ll_switchButton);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getName());
            switchButton.setCheckedImmediately(list.get(position).getCanSee() == 1);
            llSwitchButton.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, list.get(position).getCanSee() == 1 ? 0 : 1, position));
        }
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}