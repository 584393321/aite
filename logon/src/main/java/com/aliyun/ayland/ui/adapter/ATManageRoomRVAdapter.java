package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATManageRoomRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATRoomBean1> list = new ArrayList<>();

    public ATManageRoomRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_manage_room, parent, false);
        return new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvRoomName;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvRoomName = itemView.findViewById(R.id.tv_room_name);
        }

        public void setData(int position) {
            mTvRoomName.setText(list.get(position).getName());
//            mImgRoom.setImageResource(ATResourceUtils.getResIdByName("at_room_at_" + list.get(position).getType() + "_a", ATResourceUtils.ResourceType.DRAWABLE));
            mTvRoomName.setOnClickListener((view) -> {
                if (lis != null) {
                    lis.onItemClick(view, position);
                }
            });
        }
    }

    private ATOnRVItemClickListener lis;

    public void setOnRVClickListener(ATOnRVItemClickListener lis) {
        this.lis = lis;
    }
}
