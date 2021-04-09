package com.aliyun.ayland.widget.ninegridimageview;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * @author yueban
 * Date: 2017/9/19
 * Email: fbzhh007@gmail.com
 */
public interface ATItemImageLongClickListener<T> {
    boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<T> list);
}
