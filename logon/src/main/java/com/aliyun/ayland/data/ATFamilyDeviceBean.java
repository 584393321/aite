package com.aliyun.ayland.data;


/**
 * @author guikong on 18/4/12.
 */
public class ATFamilyDeviceBean {
    /**
     * iotId : 8W4rkLgzPCSN8PEAQhBq000100
     * productImage : http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631370511.png
     * productKey : a1bPGo41MBP
     * deviceName : D3616849846165
     * productName : 艾特紧急按钮
     */

    private String iotId;
    private String productImage;
    private String productKey;
    private String deviceName;
    private String productName;
    private String categoryKey;
    private boolean isAdd;

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
