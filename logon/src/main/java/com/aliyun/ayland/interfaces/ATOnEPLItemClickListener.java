package com.aliyun.ayland.interfaces;

public interface ATOnEPLItemClickListener<T> {
    void onItemClick(int groupPosition);
    void onItemClick(int groupPosition, int childPosition);
    void onItemClick(int groupPosition, int childPosition, int status);
}