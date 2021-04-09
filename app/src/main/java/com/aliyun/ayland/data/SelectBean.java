package com.aliyun.ayland.data;

public class SelectBean {
    /**
     * key : 1_1_3
     * value : 1期1区3栋
     */

    private String key;
    private String value;

    public SelectBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectBuildingBean{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}