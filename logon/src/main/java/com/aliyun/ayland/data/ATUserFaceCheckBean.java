package com.aliyun.ayland.data;


public class ATUserFaceCheckBean {
    /**
     * faceStatus : 1
     * villageName : 测试小区
     * villageId : 100021
     * deviceName : 我是另一个小区的设备
     * deviceId : 我是另一个小区的设备id
     */

    private int faceStatus;
    private String villageName;
    private int villageId;
    private String deviceName;
    private String deviceId;
    private String deviceAddress;
    /**
     * deviceType : AIBOX
     * iotId : mJP3SVxTJFIkYKphNv1D000100
     * createTime : 1575528158000
     * groupId : 100003
     * id : 2
     */

    private String deviceType;
    private String iotId;
    private long createTime;
    private String groupId;
    private int id;

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public int getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(int faceStatus) {
        this.faceStatus = faceStatus;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}