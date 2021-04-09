package com.aliyun.ayland.data;

public class ATCarLogBean {
    /**
     * createTime : 1609914479187
     * customerCode : 10003
     * direction : 1
     * id : d4c29920-5cb3-415e-82ce-75a85573359f
     * parkSpaceId : 2
     * plateNumber : 粤J32459
     * versionCode : 1
     */

    private String createTime;
    private int customerCode;
    private int direction;
    private String id;
    private String parkSpaceId;
    private String plateNumber;
    private int versionCode;
    private String parkName;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParkSpaceId() {
        return parkSpaceId;
    }

    public void setParkSpaceId(String parkSpaceId) {
        this.parkSpaceId = parkSpaceId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }
}