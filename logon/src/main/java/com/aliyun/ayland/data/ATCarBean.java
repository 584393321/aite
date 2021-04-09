package com.aliyun.ayland.data;

public class ATCarBean {
    /**
     * code : 1
     * createTime : 1563312751000
     * effectiveDate : 1563312537000
     * effectiveStatus : 1
     * ifElectro : 1
     * ifResident : 1
     * licence : 粤-C55559
     * personCode : 1132916889676607488
     * remark : 我是备注
     * vehicleType : 101
     * villageId : 100007
     */

    private String id;
    private long createTime;
    private long effectiveDate;
    private int effectiveStatus;
    private int ifElectro;
    private int ifResident;
    private String licence;
    private String personCode;
    private String parkCode;
    private String remark;
    private int vehicleType;
    private int villageId;
    /**
     * brand : 1
     * color : 黄色
     * drivingLicense : 我是图片
     * model : 车型
     * parkName : 恒基-龙光汇
     * updateTime : 1573109813000
     */

    private String brand;
    private String color;
    private String drivingLicense;
    private String model;
    private String parkName;
    private long updateTime;

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String code) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public int getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(int effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public int getIfElectro() {
        return ifElectro;
    }

    public void setIfElectro(int ifElectro) {
        this.ifElectro = ifElectro;
    }

    public int getIfResident() {
        return ifResident;
    }

    public void setIfResident(int ifResident) {
        this.ifResident = ifResident;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
