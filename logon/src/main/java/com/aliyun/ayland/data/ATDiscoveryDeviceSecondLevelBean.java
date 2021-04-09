package com.aliyun.ayland.data;

public class ATDiscoveryDeviceSecondLevelBean {
    private String imageUrl;
    private String categoryKey;
    private String categoryName;
    private String productKey;
    private String productName;
    private String productModel;
    private String netType;
    private String categoryUrl;
    private int superId;
    private int state;
    private int categoryId;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public int getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "DiscoveryDeviceFirstLevelBean{" +
                "imageUrl='" + imageUrl + '\'' +
                ", categoryKey='" + categoryKey + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", superId='" + superId + '\'' +
                ", state='" + state + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", productKey='" + productKey + '\'' +
                ", productName='" + productName + '\'' +
                ", productModel='" + productModel + '\'' +
                ", netType='" + netType + '\'' +
                ", categoryUrl='" + categoryUrl + '\'' +
                '}';
    }
}

