package com.aliyun.ayland.data;


/**
 * @author guikong on 18/4/12.
 */
public class ATPublicDeviceBean {
    /**
     * buildingCode : 100003
     * code : 1000006
     * deviceId : WsgGqKamyo4oHwGQ0z0d000100
     * imageUrl : http://alisaas.atsmartlife.com/pic/deviceLinkage/home_ld_ico_Camera.png
     * name : 我是摄像头1号
     */

    private int buildingCode;
    private int code;
    private String deviceId;
    private String imageUrl;
    private String name;
    private String categoryKey;
    private boolean isAdd;

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public int getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(int buildingCode) {
        this.buildingCode = buildingCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
