package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.anthouse.xuhui.R;

import java.util.List;

public class ATTextRVAdapter extends RecyclerView.Adapter{
    private String clazz;
    private boolean flag;
    private Context mContext;
    private List<String> list;

    public ATTextRVAdapter(Activity context, List<String> list, boolean flag, String clazz) {
        mContext = context;
        this.list = list;
        this.flag = flag;
        this.clazz = clazz;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_parkname_flowlayout, parent, false);
        TextHolder textHolder = new TextHolder(view);
        return textHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextHolder textHolder = (TextHolder) holder;
        textHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TextHolder extends RecyclerView.ViewHolder {
        private TextView tvText;

        public TextHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
        public void setData(int position) {
            tvText.setText(list.get(position));
            if(flag && position == list.size() - 1){
                tvText.setText("");
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.at_icon_delete_32dp);
                tvText.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
//                imageView.setVisibility(View.VISIBLE);
            }
            tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, tvText.getText().toString());
                }
            });
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String text);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
