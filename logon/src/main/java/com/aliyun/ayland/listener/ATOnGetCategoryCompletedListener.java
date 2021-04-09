package com.aliyun.ayland.listener;


import com.aliyun.ayland.data.Category;

import java.util.List;

/**
 * @author guikong on 18/4/8.
 */
public interface ATOnGetCategoryCompletedListener {
    void onSuccess(int count, List<Category> categories);

    void onFailed(Exception e);

    void onFailed(int code, String message, String localizedMsg);
}
