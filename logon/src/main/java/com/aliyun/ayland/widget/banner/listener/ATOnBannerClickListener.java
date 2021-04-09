package com.aliyun.ayland.widget.banner.listener;


import java.util.List;

/**
 * 旧版接口，由于返回的下标是从1开始，下标越界而废弃（因为有人使用所以不能直接删除）
 */
@Deprecated
public interface ATOnBannerClickListener {
    void onBannerClick(List datas, int position);
}
