package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATRankingListBean.RankListBean;
import com.aliyun.ayland.interfaces.ATOnRVItemClickListener;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author guikong on 18/4/8.
 */

public class ATRankingListContentRvViewHolder extends ATSettableViewHolder {
    private TextView tvName, tvRanking, tvStep, tvNumber;
    private ImageView imgLike, imgUser;
    private ATOnRecyclerViewItemClickListener mOnItemClickListener;
    private Context mContext;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.at_pho_s_mine_touxiang)
            .error(R.drawable.at_pho_s_mine_touxiang);

    public ATRankingListContentRvViewHolder(View view, ATOnRecyclerViewItemClickListener mOnItemClickListener) {
        super(view);
        mContext = view.getContext();
        tvName = view.findViewById(R.id.tv_name);
        tvRanking = view.findViewById(R.id.tv_ranking);
        tvStep = view.findViewById(R.id.tv_step);
        tvNumber = view.findViewById(R.id.tv_number);
        imgUser = view.findViewById(R.id.img_user);
        imgLike = view.findViewById(R.id.img_like);
        this.mOnItemClickListener = mOnItemClickListener;
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if(object instanceof RankListBean) {
            tvName.setText(((RankListBean)object).getNickname());
            tvRanking.setText(((RankListBean)object).getRankNum() == 0 ? "-" : String.valueOf(((RankListBean)object).getRankNum()));
            tvStep.setText(((RankListBean)object).getCalorieNum() == 0 ? "-" : String.valueOf(((RankListBean)object).getCalorieNum()));
            tvNumber.setText(String.valueOf(((RankListBean)object).getAgree()));
            Glide.with(mContext).load(((RankListBean)object).getAvatarUrl()).apply(options).into(imgUser);
            imgLike.setImageResource(((RankListBean) object).getAgreeStatus() == 0 ? R.drawable.at_ic_h_yyjs_shape_n : R.drawable.at_ic_h_yyjs_shape_h);
            imgLike.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, ((RankListBean) object).getId()));
        }
    }
}