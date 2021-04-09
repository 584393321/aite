package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATRankingListBean.UserRankInfoBean;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author guikong on 18/4/8.
 */

public class ATRankingListTitleRvViewHolder extends ATSettableViewHolder {
    private TextView tvName, tvRanking, tvStep, tvNumber;
    private ImageView imgLike, imgUser;
    private ATOnRecyclerViewItemClickListener mOnItemClickListener;
    private Context mContext;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.at_pho_s_mine_touxiang)
            .error(R.drawable.at_pho_s_mine_touxiang);

    public ATRankingListTitleRvViewHolder(View view, ATOnRecyclerViewItemClickListener mOnItemClickListener) {
        super(view);
        mContext = view.getContext();
        tvName = view.findViewById(R.id.tv_name);
        tvRanking = view.findViewById(R.id.tv_ranking);
        tvStep = view.findViewById(R.id.tv_step);
        tvNumber = view.findViewById(R.id.tv_number);
        imgLike = view.findViewById(R.id.img_like);
        imgUser = view.findViewById(R.id.img_user);
        this.mOnItemClickListener = mOnItemClickListener;
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (object instanceof UserRankInfoBean) {
            tvName.setText(((UserRankInfoBean) object).getNickname());
            tvRanking.setText(String.valueOf(((UserRankInfoBean) object).getRankNum()));
            tvStep.setText(String.valueOf(((UserRankInfoBean) object).getCalorieNum()));
            tvNumber.setText(String.valueOf(((UserRankInfoBean) object).getAgree()));
            Glide.with(mContext).load(((UserRankInfoBean)object).getAvatarUrl()).apply(options).into(imgUser);
            imgLike.setImageResource(((UserRankInfoBean) object).getAgreeStatus() == 0 ? R.drawable.at_ic_h_yyjs_shape_n : R.drawable.at_ic_h_yyjs_shape_h);
            imgLike.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, ((UserRankInfoBean) object).getId()));
        }
    }
}