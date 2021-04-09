package com.aliyun.ayland.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author guikong on 18/4/8.
 */
public abstract class ATSettableViewHolder extends RecyclerView.ViewHolder {

    ATSettableViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setData(Object object, int position, int count);
}
