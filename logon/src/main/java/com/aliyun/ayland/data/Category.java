package com.aliyun.ayland.data;

/**
 * @author guikong on 18/4/8.
 */
public class Category {
    /**
     * productModel : ilop
     * netType : NET_WIFI
     * categoryKey : SmartCity
     * productKey : a1FiafrhlWF
     * categoryName : 智能城市
     * productName : 智能家居-dev-25545512
     */

    private String productModel;
    private String categoryUrl;
    private String netType;
    private String categoryKey;
    private String productKey;
    private String categoryName;
    private String productName;

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

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }
}
