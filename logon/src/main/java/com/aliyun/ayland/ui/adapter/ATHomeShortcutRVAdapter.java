package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneManualAutoBean;
import com.aliyun.ayland.data.ATShortcutBean;
import com.aliyun.ayland.widget.ATCircleLoadingAnimotion;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ATHomeShortcutRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATShortcutBean> list = new ArrayList<>();
    private Handler handler = new Handler();

    public ATHomeShortcutRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_home_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    public void setIsShowing(int isShowing, int current_position) {
        list.get(current_position).setIsShowing(isShowing);
        notifyItemChanged(current_position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATShortcutBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent, mLLStartSuccess;
        private TextView mTvName;
        private ImageView mImg;
        private ATCircleLoadingAnimotion mCircleBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            mLLStartSuccess = itemView.findViewById(R.id.ll_start_success);
            mCircleBar = itemView.findViewById(R.id.circle_bar);
            mImg = itemView.findViewById(R.id.img);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getItemName());
            if (position == list.size() - 1)
                mImg.setImageResource(R.drawable.at_icon_s_gengduobianji);
            else
                Glide.with(mContext).load(list.get(position).getItemIcon()).into(mImg);
            if (list.get(position).getShortcutType() == 1 && list.get(position).getOperateType() == 2 &&
                    list.get(position).getAttributes() != null && list.get(position).getAttributes().size() != 0) {
                if ("1".equals(list.get(position).getAttributes().get(0).getValue())) {
                    mLlContent.setBackground(mContext.getResources().getDrawable(R.drawable.selector_home_shortcut));
                } else {
                    mLlContent.setBackground(mContext.getResources().getDrawable(R.drawable.selector_18px_ffffff_eeeeee));
                }
            }
            if (list.get(position).getShortcutType() == 2) {
                if (list.get(position).getIsShowing() == ATSceneManualAutoBean.SHOWSUCCESS) {
                    clearAnimotion(this);
                    mLLStartSuccess.setVisibility(View.VISIBLE);
                    handler.postDelayed(() -> {
                        list.get(position).setIsShowing(ATSceneManualAutoBean.NOTSHOW);
                        notifyItemChanged(position);
                    }, 800);
                } else if (list.get(position).getIsShowing() == ATSceneManualAutoBean.SHOWING) {
                    showAnimotion(this);
                    mLLStartSuccess.setVisibility(View.GONE);
                } else {
                    clearAnimotion(this);
                    mLLStartSuccess.setVisibility(View.GONE);
                }
            }
            mCircleBar.setAnimotionEnd(() -> {
                mImg.setVisibility(View.VISIBLE);
                list.get(position).setIsShowing(ATSceneManualAutoBean.NOTSHOW);
                mLlContent.setBackground(mContext.getResources().getDrawable(R.drawable.selector_18px_ffffff_eeeeee));
            });
            mLlContent.setOnClickListener(view -> {
                if (list.get(position).getShortcutType() == 2) {
                    if (list.get(position).getIsShowing() != ATSceneManualAutoBean.NOTSHOW)
                        return;
                    list.get(position).setIsShowing(ATSceneManualAutoBean.SHOWING);
                    showAnimotion(this);
                }
                mOnItemClickListener.onItemClick(view, position);
            });
        }
    }

    private void showAnimotion(ViewHolder viewHolder) {
        viewHolder.mCircleBar.setVisibility(View.VISIBLE);
        viewHolder.mCircleBar.animotionStart(3000);
        viewHolder.mImg.setVisibility(View.GONE);
        viewHolder.mLlContent.setBackground(mContext.getResources().getDrawable(R.drawable.selector_home_shortcut));
    }

    private void clearAnimotion(ViewHolder viewHolder) {
        viewHolder.mCircleBar.setVisibility(View.GONE);
        viewHolder.mCircleBar.animotionStop();
        viewHolder.mLlContent.setBackground(mContext.getResources().getDrawable(R.drawable.selector_18px_ffffff_eeeeee));
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
