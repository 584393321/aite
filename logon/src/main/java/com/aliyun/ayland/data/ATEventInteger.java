package com.aliyun.ayland.data;


public class ATEventInteger {
    private String clazz;
    private int position;

    public ATEventInteger(String flag, int count) {
        this.clazz = flag;
        this.position = count;
    }

    public String getClazz() {
        return clazz;
    }

    public int getPosition() {
        return position;
    }
}
