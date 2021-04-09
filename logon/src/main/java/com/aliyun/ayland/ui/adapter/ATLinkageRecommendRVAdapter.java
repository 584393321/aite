package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneTemplateBean;
import com.aliyun.ayland.ui.activity.ATLinkageRecommendActivity;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageRecommendRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATSceneTemplateBean> list = new ArrayList<>();

    public ATLinkageRecommendRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_recommend, parent, false);
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

    public void setLists(List<ATSceneTemplateBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTextView;
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTextView = itemView.findViewById(R.id.textView);
            img = itemView.findViewById(R.id.img);
        }

        public void setData(int position) {
            mTextView.setText(list.get(position).getTemplateName());
            switch (position) {
                case 0:
                    img.setImageResource(R.drawable.at_home_ld_cqzm);
                    break;
                case 1:
                    img.setImageResource(R.drawable.at_home_ld_hjms);
                    break;
                case 2:
                    img.setImageResource(R.drawable.at_home_ld_ljms);
                    break;
                case 3:
                    img.setImageResource(R.drawable.at_home_ld_smms);
                    break;
                case 4:
                    img.setImageResource(R.drawable.at_home_ld_plms);
                    break;
                case 5:
                    img.setImageResource(R.drawable.at_home_ld_ylms);
                    break;
            }
            mRlContent.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, ATLinkageRecommendActivity.class)
                    .putExtra("sceneName", list.get(position).getTemplateName())
                    .putExtra("templateId", list.get(position).getId())));
        }
    }
}