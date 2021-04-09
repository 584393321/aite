package com.aliyun.ayland.data;


public class ATEventBoolean {
    private String clazz;
    private int position;
    private boolean flag;

    public ATEventBoolean(String clazz, int position, boolean flag) {
        this.clazz = clazz;
        this.position = position;
        this.flag = flag;
    }

    public String getClazz() {
        return clazz;
    }

    public int getPosition() {
        return position;
    }

    public boolean isFlag() {
        return flag;
    }
}
