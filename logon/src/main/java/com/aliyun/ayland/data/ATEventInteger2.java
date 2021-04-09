package com.aliyun.ayland.data;


public class ATEventInteger2 {
    private String clazz;
    private int specs;
    private int position;

    public ATEventInteger2(String clazz, int position, int specs) {
        this.clazz = clazz;
        this.position = position;
        this.specs = specs;
    }

    public String getClazz() {
        return clazz;
    }

    public int getPosition() {
        return position;
    }

    public int getSpecs() {
        return specs;
    }
}
