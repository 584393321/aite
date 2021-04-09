package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneManualAutoBean;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity1;
import com.aliyun.ayland.widget.ATCircleLoadingAnimotion;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.aliyun.ayland.ui.fragment.ATLinkageFragment.REQUEST_CODE_EDIT_LINKAGE;

public class ATLinkageSceneAutoRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATSceneManualAutoBean> list = new ArrayList<>();
    private int sceneType = 1;
    private Handler handler = new Handler();

    public ATLinkageSceneAutoRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_auto_scene, parent, false);
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

    public void setLists(List<ATSceneManualAutoBean> list, int sceneType) {
        this.sceneType = sceneType;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void setIsShowing(int isShowing, int current_position) {
        list.get(current_position).setIsShowing(isShowing);
        notifyItemChanged(current_position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgIcon, mImgScene, mImgStartSuccess;
        private TextView mTvName;
        private RelativeLayout mRlContent, mRlRight;
        private ATSwitchButton mSwitchview;
        private ATCircleLoadingAnimotion mCircleBar;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mImgIcon = itemView.findViewById(R.id.img_icon);
            mImgScene = itemView.findViewById(R.id.img_scene);
            mTvName = itemView.findViewById(R.id.tv_name);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mSwitchview = itemView.findViewById(R.id.switchview);
            mImgStartSuccess = itemView.findViewById(R.id.img_start_success);
            mCircleBar = itemView.findViewById(R.id.circle_bar);
            mRlRight = itemView.findViewById(R.id.rl_right);
            ATAutoUtils.autoSize(mSwitchview);
        }

        public void setData(int position) {
            Glide.with(mContext).load(list.get(position).getSceneIcon()).into(mImgScene);
            mTvName.setText(list.get(position).getSceneName());
            if (sceneType == 1) {
                mRlRight.setVisibility(View.VISIBLE);
                mSwitchview.setVisibility(View.GONE);
            } else {
                mRlRight.setVisibility(View.GONE);
                mSwitchview.setVisibility(View.VISIBLE);
            }
            mRlContent.setOnClickListener(view -> {
                mContext.startActivityForResult(new Intent(mContext, ATLinkageAddActivity1.class)
                        .putExtra("sceneId", list.get(position).getSceneId())
                        .putExtra("sceneType", list.get(position).getSceneType()), REQUEST_CODE_EDIT_LINKAGE);
            });
            mRlRight.setOnClickListener(view -> {
                // 显示动画
                list.get(position).setIsShowing(ATSceneManualAutoBean.SHOWING);
                showAnimotion(this);
                mOnItemClickListener.onItemClick(view, position);
            });

            if (list.get(position).getIsShowing() == ATSceneManualAutoBean.SHOWSUCCESS) {
                clearAnimotion(this);
                mImgStartSuccess.setVisibility(View.VISIBLE);
                mImgIcon.setVisibility(View.GONE);
                handler.postDelayed(() -> {
                    list.get(position).setIsShowing(ATSceneManualAutoBean.NOTSHOW);
                    notifyItemChanged(position);
                }, 800);
            } else if (list.get(position).getIsShowing() == ATSceneManualAutoBean.SHOWING) {
                showAnimotion(this);
                mImgStartSuccess.setVisibility(View.GONE);
                mImgIcon.setVisibility(View.VISIBLE);
            } else {
                clearAnimotion(this);
                mImgStartSuccess.setVisibility(View.GONE);
                mImgIcon.setVisibility(View.VISIBLE);
            }
            mSwitchview.setOnCheckedChangeListener(null);
            mSwitchview.setCheckedImmediately("1".equals(list.get(position).getDeployStatus()));
            mSwitchview.setOnCheckedChangeListener((buttonView, isChecked) -> list.get(position).setDeployStatus(isChecked ? "1" : "2"));
            mSwitchview.setOnCheckedChangeListener((compoundButton, b) -> mOnItemClickListener.onItemClick(getAdapterPosition(), b));
        }
    }

    private void showAnimotion(ViewHolder viewHolder) {
        viewHolder.mCircleBar.setVisibility(View.VISIBLE);
        viewHolder.mCircleBar.animotionStart(3000);
    }

    private void clearAnimotion(ViewHolder viewHolder) {
        viewHolder.mCircleBar.setVisibility(View.GONE);
        viewHolder.mCircleBar.animotionStop();
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);

        void onItemClick(int position, boolean check);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}