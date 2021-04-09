package com.aliyun.ayland.widget.magicindicator;

import android.support.v4.view.ViewPager;

import com.aliyun.ayland.widget.magicindicator.ATMagicIndicator;

/**
 * 简化和ViewPager绑定
 * Created by hackware on 2016/8/17.
 */

public class ATViewPagerHelper {
    public static void bind(final ATMagicIndicator ATMagicIndicator, ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ATMagicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                ATMagicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ATMagicIndicator.onPageScrollStateChanged(state);
            }
        });
    }
}
