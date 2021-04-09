package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATFamilySensorsBean.SensorsBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATFamilySensorsRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<SensorsBean> list = new ArrayList<>();

    public ATFamilySensorsRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_sensors, parent, false);
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

    public void setLists(List<SensorsBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            textView = itemView.findViewById(R.id.textView);
        }

        public void setData(int position) {
            textView.setText(list.get(position).getName());
            if(list.get(position).getStatus() == 0){
                //没有
                textView.setTextColor(mContext.getResources().getColor(R.color._9F837F));
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_27px_d4c5c3));
            }else {
                //拥有
                textView.setTextColor(mContext.getResources().getColor(R.color._9F837F));
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_27px_ffffff));
            }
        }
    }
}
