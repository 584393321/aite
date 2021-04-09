package com.aliyun.ayland.data;

public class ATDeviceListBean {
    /**
     * iotId : 0bIyYcyFgQWJTPvtP0Gk000100
     * belongsToHomelink : false
     * productKey : a1nMXUhT65e
     * deviceName : 1323432748
     */

    private String iotId;
    private boolean belongsToHomelink;
    private String productKey;
    private String deviceName;
    private boolean request;

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public boolean isBelongsToHomelink() {
        return belongsToHomelink;
    }

    public void setBelongsToHomelink(boolean belongsToHomelink) {
        this.belongsToHomelink = belongsToHomelink;
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

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }
}
