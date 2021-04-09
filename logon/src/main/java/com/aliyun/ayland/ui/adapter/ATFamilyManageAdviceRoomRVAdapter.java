package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATRoomBean1;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ATFamilyManageAdviceRoomRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private HashSet<Integer> checkSet = new HashSet<>();
    private List<ATRoomBean1> list = new ArrayList<>();

    public ATFamilyManageAdviceRoomRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_manage_advice_room, parent, false);
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

    public void setList(List<ATRoomBean1> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public HashSet<Integer> getCheckSet() {
        return checkSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoomName;
        private ATSwitchButton switchButton;
        private LinearLayout llSwitchButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            tvRoomName = itemView.findViewById(R.id.tv_room_name);
            switchButton = itemView.findViewById(R.id.switchButton);
            llSwitchButton = itemView.findViewById(R.id.ll_switchButton);
        }

        public void setData(int position) {
            tvRoomName.setText(list.get(position).getName());
            switchButton.setCheckedImmediately(checkSet.contains(position));
            llSwitchButton.setOnClickListener(view -> {
                if (checkSet.contains(position)) {
                    checkSet.remove(position);
                } else {
                    checkSet.add(position);
                }
                notifyItemChanged(position);
            });
        }
    }
}
