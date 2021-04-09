package com.aliyun.ayland.listener;


import com.aliyun.ayland.data.ATProduct;

import java.util.List;

/**
 * @author guikong on 18/4/8.
 */
public interface ATOnGetProductCompletedListener {
    void onSuccess(int count, List<ATProduct> categories);

    void onFailed(Exception e);

    void onFailed(int code, String message, String localizedMsg);
}
