package com.aliyun.ayland.data;


public class ATEventString {
    private String clazz;
    private String value;

    public ATEventString(String clazz, String value) {
        this.clazz = clazz;
        this.value = value;
    }

    public String getClazz() {
        return clazz;
    }

    public String getValue() {
        return value;
    }
}
