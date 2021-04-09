package com.aliyun.ayland.data;


import java.util.List;

/**
 * @author guikong on 18/4/12.
 */
public class ATMonitorDeviceBean {
    /**
     * regularCheck : 101
     * deviceId : WsgGqKamyo4oHwGQ0z0d000100
     * annualCheck : 101
     * id : 14
     * deviceType : 109
     * buildingCode : 100003
     * effectiveStatus : true
     * name : 我是摄像头1号
     * regularDate : 1563954121000
     * villageId : 100007
     * bizType : 1
     * code : 1000006
     * bizInfo : DEFAULT
     * productionDate : 1563954121000
     * rebootState : 1
     * pcs : 1
     * annualDate : 1563954121000
     * bizTypeList : ["FACE"]
     * updateTime : 1563954121000
     * createTime : 1563954121000
     * effectiveDate : 1563954121000
     * ifSe : false
     */

    private int regularCheck;
    private String deviceId;
    private int annualCheck;
    private int id;
    private int deviceType;
    private int buildingCode;
    private boolean effectiveStatus;
    private String name;
    private long regularDate;
    private int villageId;
    private int bizType;
    private int code;
    private String bizInfo;
    private long productionDate;
    private int rebootState;
    private int pcs;
    private long annualDate;
    private long updateTime;
    private long createTime;
    private long effectiveDate;
    private boolean ifSe;
    private List<String> bizTypeList;

    public int getRegularCheck() {
        return regularCheck;
    }

    public void setRegularCheck(int regularCheck) {
        this.regularCheck = regularCheck;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getAnnualCheck() {
        return annualCheck;
    }

    public void setAnnualCheck(int annualCheck) {
        this.annualCheck = annualCheck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(int buildingCode) {
        this.buildingCode = buildingCode;
    }

    public boolean isEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(boolean effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRegularDate() {
        return regularDate;
    }

    public void setRegularDate(long regularDate) {
        this.regularDate = regularDate;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBizInfo() {
        return bizInfo;
    }

    public void setBizInfo(String bizInfo) {
        this.bizInfo = bizInfo;
    }

    public long getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(long productionDate) {
        this.productionDate = productionDate;
    }

    public int getRebootState() {
        return rebootState;
    }

    public void setRebootState(int rebootState) {
        this.rebootState = rebootState;
    }

    public int getPcs() {
        return pcs;
    }

    public void setPcs(int pcs) {
        this.pcs = pcs;
    }

    public long getAnnualDate() {
        return annualDate;
    }

    public void setAnnualDate(long annualDate) {
        this.annualDate = annualDate;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
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

    public boolean isIfSe() {
        return ifSe;
    }

    public void setIfSe(boolean ifSe) {
        this.ifSe = ifSe;
    }

    public List<String> getBizTypeList() {
        return bizTypeList;
    }

    public void setBizTypeList(List<String> bizTypeList) {
        this.bizTypeList = bizTypeList;
    }
}
