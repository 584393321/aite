package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.data.ATCommunityDynamicBean;
import com.aliyun.ayland.ui.activity.ATPhotoViewActivity;
import com.aliyun.ayland.widget.ATMyCircleImageView;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.aliyun.ayland.widget.ninegridimageview.ATNineGridImageView;
import com.aliyun.ayland.widget.ninegridimageview.ATNineGridImageViewAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class ATCommentEPLAdapter extends BaseExpandableListAdapter {
    private Drawable drawableLike, drawableNormal, drawableJoin, drawableJoined;
    private List<ATCommunityDynamicBean> list = new ArrayList<>();
    private Context context;
    private int pageIndex = 1;
    private HashSet<Integer> set = new HashSet<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_pho_s_mine_touxiang)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATCommentEPLAdapter(Context context) {
        this.context = context;
        drawableLike = context.getResources().getDrawable(R.drawable.at_icon_bj_like_s);
        drawableLike.setBounds(0, 0, drawableLike.getIntrinsicWidth(), drawableLike.getIntrinsicHeight());
        drawableNormal = context.getResources().getDrawable(R.drawable.at_icon_bj_like);
        drawableNormal.setBounds(0, 0, drawableNormal.getIntrinsicWidth(), drawableNormal.getIntrinsicHeight());
        drawableJoin = context.getResources().getDrawable(R.drawable.at_ic_h_guanzhu);
        drawableJoin.setBounds(0, 0, drawableJoin.getIntrinsicWidth(), drawableJoin.getIntrinsicHeight());
        drawableJoined = context.getResources().getDrawable(R.drawable.at_ic_h_yiguanzhu);
        drawableJoined.setBounds(0, 0, drawableJoined.getIntrinsicWidth(), drawableJoined.getIntrinsicHeight());
    }

    public void setList(List<ATCommunityDynamicBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void notifyItemChanged(int current_group_position) {
        this.list.get(current_group_position).setUserCommunityStatus(1);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getCommentList() == null ? 0 : list.get(groupPosition).getCommentList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getCommentList() == null ? null : list.get(groupPosition).getCommentList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    boolean isLike = false;

    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_dynamic_group, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(list.get(groupPosition).getAvatarUrl())
                .apply(options)
                .into(groupHolder.mImgPortrait);
        groupHolder.mTvName.setText(list.get(groupPosition).getCreatePersonName());
        groupHolder.mTvTime.setText(list.get(groupPosition).getCreateTimeStr());
        groupHolder.mTvContent.setText(list.get(groupPosition).getContent());
        groupHolder.mTvLike.setText(String.valueOf(list.get(groupPosition).getAgreeNum()));
        if (!TextUtils.isEmpty(list.get(groupPosition).getImage_list())) {
            groupHolder.mNineGridImageView.setVisibility(View.VISIBLE);
            List<String> imgList = new ArrayList<>();
            Collections.addAll(imgList, list.get(groupPosition).getImage_list().split(","));
            groupHolder.mNineGridImageView.setImagesData(imgList);
        } else {
            groupHolder.mNineGridImageView.setVisibility(View.GONE);
        }

        if (set.contains(groupPosition)) {
            groupHolder.mLlTitle.setVisibility(View.VISIBLE);
        } else {
            groupHolder.mLlTitle.setVisibility(View.GONE);
        }
        if (0 == list.get(groupPosition).getAgreeStatus()) {
            groupHolder.mTvLike.setCompoundDrawables(drawableNormal, null, null, null);
            groupHolder.mTvLike.setTextColor(context.getResources().getColor(R.color._D8D8D8));
        } else {
            groupHolder.mTvLike.setCompoundDrawables(drawableLike, null, null, null);
            groupHolder.mTvLike.setTextColor(context.getResources().getColor(R.color._EBB080));
        }
        groupHolder.mTvWriteComment.setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(groupPosition, -3);
        });
        groupHolder.mImgComplaint.setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(groupPosition, -4);
        });
        if (list.get(groupPosition).getUserCommunityStatus() == 0) {
            groupHolder.mTvTogether.setCompoundDrawables(null, null, drawableJoin, null);
        } else {
            groupHolder.mTvTogether.setCompoundDrawables(null, null, drawableJoined, null);
        }
        groupHolder.mTvTogether.setText(String.format(context.getString(R.string.at_come_from_), list.get(groupPosition).getFromCommunityName()));
        groupHolder.mTvTogether.setOnClickListener(view -> {
            if (list.get(groupPosition).getUserCommunityStatus() == 0)
                mOnItemClickListener.onItemClick(groupPosition, -5);
        });
        groupHolder.mTvComment.setText(list.get(groupPosition).getCommentList() == null ? "0" : list.get(groupPosition).getCommentList().size() + "");
        groupHolder.mTvComment.setOnClickListener(view -> {
            if (groupHolder.mLlTitle.getVisibility() == View.GONE) {
                groupHolder.mLlTitle.setVisibility(View.VISIBLE);
                set.add(groupPosition);
            } else {
                groupHolder.mLlTitle.setVisibility(View.GONE);
                set.remove(groupPosition);
            }
            mOnItemClickListener.onItemClick(groupPosition, -2);
        });
        groupHolder.mTvLike.setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(groupPosition, -1);
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.at_item_epl_dynamic_child, viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(list.get(groupPosition).getCommentList().get(childPosition).getBeCommentPerson())) {
            childHolder.mTvComment.setText(getSpannableStr(list.get(groupPosition).getCommentList().get(childPosition).getCommentPersonName()
                    , list.get(groupPosition).getCommentList().get(childPosition).getContent()));
        } else {
            childHolder.mTvComment.setText(getSpannableStr1(list.get(groupPosition).getCommentList().get(childPosition).getCommentPersonName()
                    , list.get(groupPosition).getCommentList().get(childPosition).getBeCommentPersonName()
                    , list.get(groupPosition).getCommentList().get(childPosition).getContent()));
        }
        childHolder.mTvComment.setOnClickListener(view -> {
            mOnItemClickListener.onItemClick(groupPosition, childPosition);
        });
        return convertView;
    }

    private SpannableString getSpannableStr1(String str1, String str2, String str3) {
        SpannableString s = new SpannableString(str1 + " 回复 " + str2 + "：" + str3);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF333333")), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF666666")), str1.length(), str1.length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF333333")), str1.length() + 4, str1.length() + 4 + str2.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF666666")), str1.length() + 4 + str2.length() + 1, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private SpannableString getSpannableStr(String str1, String str2) {
        SpannableString s = new SpannableString(str1 + "：" + str2);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF333333")), 0, str1.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF666666")), str1.length() + 1, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder {
        private ATMyCircleImageView mImgPortrait;
        private ImageView mImgComplaint;
        private TextView mTvName, mTvTime, mTvTogether, mTvContent, mTvComment, mTvLike, mTvWriteComment;
        private LinearLayout mLlTitle;
        private ATNineGridImageView mNineGridImageView;

        private GroupHolder(View view) {
            mImgPortrait = view.findViewById(R.id.img_portrait);
            mTvName = view.findViewById(R.id.tv_name);
            mTvTime = view.findViewById(R.id.tv_time);
            mTvTogether = view.findViewById(R.id.tv_together);
            mTvContent = view.findViewById(R.id.tv_content);
            mNineGridImageView = view.findViewById(R.id.nineGridImageView);
            mTvComment = view.findViewById(R.id.tv_comment);
            mTvLike = view.findViewById(R.id.tv_like);
            mImgComplaint = view.findViewById(R.id.img_complaint);
            mTvWriteComment = view.findViewById(R.id.tv_write_comment);
            mLlTitle = view.findViewById(R.id.ll_title);
            mNineGridImageView.setAdapter(mAdapter);
        }
    }

    private class ChildHolder {
        private TextView mTvComment;

        private ChildHolder(View view) {
            mTvComment = view.findViewById(R.id.tv_comment);
        }
    }


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(int groupPosition, int clickPosition);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private ATNineGridImageViewAdapter<String> mAdapter = new ATNineGridImageViewAdapter<String>() {
        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String imgUrl) {
            if (!imgUrl.equals("")) {
//                Picasso.with(context).load(imgUrl).into(imageView);
                Object tag = new Object();
                Picasso.with(context).load(imgUrl)
//                        .placeholder(imageView.getDrawable())
                        .placeholder(R.color.white)
                        .error(R.drawable.at_hsh_pic_tpjzsb_x).tag(tag)
                        .resize(200, 200).centerCrop().config(Bitmap.Config.RGB_565)
//                        .memoryPolicy(NO_CACHE, NO_STORE)
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {
                                int size = Math.min(source.getWidth(), source.getHeight());
                                int x = (source.getWidth() - size) / 2;
                                int y = (source.getHeight() - size) / 2;
                                Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
                                if (result != source) {
                                    source.recycle();
                                }
                                return result;
                            }

                            @Override
                            public String key() {
                                return "square()";
                            }
                        }).into(imageView);
            }
        }

        @Override
        protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
            ArrayList<String> data = new ArrayList<>(list);
            if (list.size() == 5 && data.size() == 4) {
                if (index == 0) {
                    setIndex(context, index, data);
                } else if (index == 1) {
                    setIndex(context, index, data);
                } else if (index == 3) {
                    setIndex(context, 2, data);
                } else if (index == 4) {
                    setIndex(context, 3, data);
                }
            } else {
                setIndex(context, index, data);
            }
        }
    };

    private void setIndex(Context context, int index, ArrayList<String> data) {
        Intent intent = new Intent(context, ATPhotoViewActivity.class);
        intent.putExtra("index", index);
        intent.putStringArrayListExtra("data", data);
        context.startActivity(intent);
    }

}