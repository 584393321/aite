package com.aliyun.ayland.widget.banner.loader;

import android.content.Context;
import android.widget.ImageView;


public abstract class ATATImageLoader implements ATImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
