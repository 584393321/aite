package com.aliyun.ayland.widget;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

public class ATNoUnderLineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
