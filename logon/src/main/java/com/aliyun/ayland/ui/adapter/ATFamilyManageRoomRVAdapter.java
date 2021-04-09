package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATFamilyManageRoomBean;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyManageRoomRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATFamilyManageRoomBean> list = new ArrayList<>();
    private int can_see_size = 0;

    public ATFamilyManageRoomRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_manage_room, parent, false);
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

    public void setLists(List<ATFamilyManageRoomBean> canSeeRoomList, List<ATFamilyManageRoomBean> notSeeRoomList) {
        this.list.clear();
        this.list.addAll(canSeeRoomList);
        this.list.addAll(notSeeRoomList);
        can_see_size = canSeeRoomList.size();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvStatus = itemView.findViewById(R.id.tv_status);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getName());
            if (position < can_see_size) {
                mTvStatus.setTextColor(mContext.getResources().getColor(R.color._86523C));
                mTvStatus.setText(mContext.getString(R.string.at_can_see));
            } else {
                mTvStatus.setTextColor(mContext.getResources().getColor(R.color._CCCCCC));
                mTvStatus.setText(mContext.getString(R.string.at_not_see));
            }
        }
    }
}