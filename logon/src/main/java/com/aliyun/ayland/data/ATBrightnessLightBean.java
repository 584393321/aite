package com.aliyun.ayland.data;

import java.util.List;

public class ATBrightnessLightBean {
    /**
     * attributes : [{"attribute":"Brightness","value":"100"},{"attribute":"Brightness_2","value":"100"},{"attribute":"CountDown","value":"{}"},{"attribute":"CountDownList","value":"{}"},{"attribute":"LightSwitch","value":"0"},{"attribute":"LightSwitch_2","value":"0"},{"attribute":"LocalTimer","value":"[]"}]
     * categoryKey : Light
     * deviceName : 832400000061
     * iotId : ProGLP1QZwWVw6VJxTk6000100
     * iotSpaceId : 09283fd59f434080874abb60972315ab
     * nickName : 调光灯
     * productImage : http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559630650729.png
     * productKey : a1NHU7sfE6X
     * productName : 艾特双路调光灯
     * status : 1
     */

    private int action;
    private String categoryKey;
    private String deviceName;
    private String iotId;
    private String iotSpaceId;
    private String nickName;
    private String productImage;
    private String productKey;
    private String productName;
    private int status;
    private List<AttributesBean> attributes;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getIotSpaceId() {
        return iotSpaceId;
    }

    public void setIotSpaceId(String iotSpaceId) {
        this.iotSpaceId = iotSpaceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<AttributesBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributesBean> attributes) {
        this.attributes = attributes;
    }

    public static class AttributesBean {
        /**
         * attribute : Brightness
         * value : 100
         */

        private String attribute;
        private String value;

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}