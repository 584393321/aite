package com.aliyun.ayland.widget.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.aliyun.ayland.widget.banner.transformer.ATAccordionTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATBackgroundToForegroundTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATCubeInTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATCubeOutTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATDefaultTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATDepthPageTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATFlipHorizontalTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATFlipVerticalTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATForegroundToBackgroundTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATRotateDownTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATRotateUpTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATScaleInOutTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATScaleRightTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATScaleTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATStackTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATTabletTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATZoomInTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATZoomOutSlideTransformer;
import com.aliyun.ayland.widget.banner.transformer.ATZoomOutTranformer;

public class ATTransformer {
    public static Class<? extends PageTransformer> Default = ATDefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = ATAccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = ATBackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ATForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = ATCubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = ATCubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = ATDepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = ATFlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = ATFlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = ATRotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = ATRotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ATScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Scale = ATScaleTransformer.class;
    public static Class<? extends PageTransformer> ScaleRight = ATScaleRightTransformer.class;
    public static Class<? extends PageTransformer> Stack = ATStackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = ATTabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ATZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ATZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ATZoomOutSlideTransformer.class;
}
