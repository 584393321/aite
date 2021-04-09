package com.aliyun.ayland.data;


import android.content.Intent;

public class ATEventIntent {
    private String clazz;
    private Intent data;

    public ATEventIntent(String flag, Intent data) {
        this.clazz = flag;
        this.data = data;
    }

    public String getClazz() {
        return clazz;
    }

    public Intent getData() {
        return data;
    }
}
