/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aliyun.ayland.base.autolayout.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.ayland.base.autolayout.ATAutoLayoutInfo;
import com.aliyun.ayland.base.autolayout.attr.ATHeightAttr;
import com.aliyun.ayland.base.autolayout.attr.ATMarginAttr;
import com.aliyun.ayland.base.autolayout.attr.ATMarginBottomAttr;
import com.aliyun.ayland.base.autolayout.attr.ATMarginLeftAttr;
import com.aliyun.ayland.base.autolayout.attr.ATMarginRightAttr;
import com.aliyun.ayland.base.autolayout.attr.ATMarginTopAttr;
import com.aliyun.ayland.base.autolayout.attr.ATPaddingAttr;
import com.aliyun.ayland.base.autolayout.attr.ATPaddingBottomAttr;
import com.aliyun.ayland.base.autolayout.attr.ATPaddingLeftAttr;
import com.aliyun.ayland.base.autolayout.attr.ATPaddingRightAttr;
import com.aliyun.ayland.base.autolayout.attr.ATPaddingTopAttr;
import com.aliyun.ayland.base.autolayout.attr.ATTextSizeAttr;
import com.aliyun.ayland.base.autolayout.attr.ATWidthAttr;
import com.aliyun.ayland.base.autolayout.config.AutoLayoutConifg;
import com.anthouse.xuhui.R;

public class AutoLayoutHelper {
    private final ViewGroup mHost;

    private static final int[] LL = new int[]{ //
            android.R.attr.textSize,
            android.R.attr.padding,//
            android.R.attr.paddingLeft,//
            android.R.attr.paddingTop,//
            android.R.attr.paddingRight,//
            android.R.attr.paddingBottom,//
            android.R.attr.layout_width,//
            android.R.attr.layout_height,//
            android.R.attr.layout_margin,//
            android.R.attr.layout_marginLeft,//
            android.R.attr.layout_marginTop,//
            android.R.attr.layout_marginRight,//
            android.R.attr.layout_marginBottom,//
    };

    private static final int INDEX_TEXT_SIZE = 0;
    private static final int INDEX_PADDING = 1;
    private static final int INDEX_PADDING_LEFT = 2;
    private static final int INDEX_PADDING_TOP = 3;
    private static final int INDEX_PADDING_RIGHT = 4;
    private static final int INDEX_PADDING_BOTTOM = 5;
    private static final int INDEX_WIDTH = 6;
    private static final int INDEX_HEIGHT = 7;
    private static final int INDEX_MARGIN = 8;
    private static final int INDEX_MARGIN_LEFT = 9;
    private static final int INDEX_MARGIN_TOP = 10;
    private static final int INDEX_MARGIN_RIGHT = 11;
    private static final int INDEX_MARGIN_BOTTOM = 12;

    /**
     * move to other place?
     */
    private static AutoLayoutConifg mAutoLayoutConifg;

    public AutoLayoutHelper(ViewGroup host) {
        mHost = host;
        if (mAutoLayoutConifg == null) {
            initAutoLayoutConfig(host);
        }
    }

    private void initAutoLayoutConfig(ViewGroup host) {
        mAutoLayoutConifg = AutoLayoutConifg.getInstance();
        mAutoLayoutConifg.init(host.getContext());
    }

    public void adjustChildren() {
        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof AutoLayoutParams) {
                ATAutoLayoutInfo info =
                        ((AutoLayoutParams) params).getATAutoLayoutInfo();
                if (info != null) {
                    info.fillAttrs(view);
                }
            }
        }
    }

    public static ATAutoLayoutInfo getAutoLayoutInfo(Context context, AttributeSet attrs) {
        AutoLayoutConifg.getInstance().checkParams();
        ATAutoLayoutInfo info = new ATAutoLayoutInfo();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLayout_Layout);
        int baseWidth = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_basewidth, 0);
        int baseHeight = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_baseheight, 0);
        a.recycle();

        TypedArray array = context.obtainStyledAttributes(attrs, LL);

        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = array.getIndex(i);
            String val = array.getString(index);
            if (!isPxVal(val)) continue;
            int pxVal = 0;
            try {
                pxVal = array.getDimensionPixelOffset(index, 0);
            } catch (Exception ignore) {
                continue;
            }
            switch (index) {
                case INDEX_TEXT_SIZE:
                    info.addAttr(new ATTextSizeAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING:
                    info.addAttr(new ATPaddingAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_LEFT:
                    info.addAttr(new ATPaddingLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_TOP:
                    info.addAttr(new ATPaddingTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_RIGHT:
                    info.addAttr(new ATPaddingRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_BOTTOM:
                    info.addAttr(new ATPaddingBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_WIDTH:
                    info.addAttr(new ATWidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_HEIGHT:
                    info.addAttr(new ATHeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN:
                    info.addAttr(new ATMarginAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_LEFT:
                    info.addAttr(new ATMarginLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_TOP:
                    info.addAttr(new ATMarginTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_RIGHT:
                    info.addAttr(new ATMarginRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_BOTTOM:
                    info.addAttr(new ATMarginBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
            }
        }
        array.recycle();
        return info;
    }

    private static boolean isPxVal(String val) {
        return val.endsWith("px");
    }

    public interface AutoLayoutParams {
        ATAutoLayoutInfo getATAutoLayoutInfo();
    }
}
