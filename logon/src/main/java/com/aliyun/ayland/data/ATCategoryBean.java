package com.aliyun.ayland.data;

public class ATCategoryBean {
    /**
     * categoryKey : SmartCity
     * categoryName : 智能城市
     * state : 0
     * superId : 0
     */

    private String categoryKey;
    private String categoryName;
    private int state;
    private int superId;

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }
}
