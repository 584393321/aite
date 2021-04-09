package com.aliyun.ayland.data;


import java.util.HashSet;

public class ATEventIntegerSet {
    private String clazz;
    private HashSet<Integer> set;

    public ATEventIntegerSet(String clazz, HashSet<Integer> set) {
        this.clazz = clazz;
        this.set = set;
    }

    public String getClazz() {
        return clazz;
    }

    public HashSet<Integer> getSet() {
        return set;
    }
}