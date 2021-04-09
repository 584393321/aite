package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATMessageCenterBean;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.iot.aep.component.router.Router;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATMessageCenterRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATMessageCenterBean> list = new ArrayList<>();

    public ATMessageCenterRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_message_center, parent, false);
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

    public void setLists(List<ATMessageCenterBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private ImageView mImg;
        private TextView mTvName, mTvTime, mTvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mImg = itemView.findViewById(R.id.img);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvStatus = itemView.findViewById(R.id.tv_status);
        }

        public void setData(int position) {
            mTvTime.setText(list.get(position).getTime());
            mTvName.setText(list.get(position).getDevice());
            mTvStatus.setText(list.get(position).getStatus());
            mImg.setImageResource(ATResourceUtils.getResIdByName("at_" + list.get(position).getIcon(), ATResourceUtils.ResourceType.DRAWABLE));
            mRlContent.setOnClickListener(view -> {
                if (position == 0) {
                    Router.getInstance().toUrl(mContext, "link://router/devicenotices");
                } else {
                    Toast.makeText(mContext, "功能正在开发中，敬请期待", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}