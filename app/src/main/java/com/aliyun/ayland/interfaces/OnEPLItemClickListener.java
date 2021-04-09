package com.aliyun.ayland.interfaces;

public  interface OnEPLItemClickListener<T> {
    void onItemClick(int groupPosition, int childPosition);
    void onItemClick(int groupPosition, int childPosition, int status);
}