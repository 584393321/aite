package com.aliyun.ayland.data;


import java.util.ArrayList;
import java.util.List;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceAccessBean {
    /**
     * annualDate : 1563288241000
     * bizType : 3
     * bizInfo : 车牌号
     * bizTypeList : ["CAR","CAR_IN","CAR_OUT"]
     * createTime : 1563288241000
     * deviceId : 我是设备id
     * deviceType : 106
     * effectiveDate : 1563288241000
     * id : 3
     * name : 我是停车系统设备1号
     * productionDate : 1563288241000
     * regularDate : 1563288241000
     * updateTime : 1563288241000
     * villageId : 100007
     */

    private String annualDate;
    private int bizType;
    private String bizInfo;
    private String createTime;
    private String iotId;
    private int deviceType;
    private String effectiveDate;
    private int id;
    private String name;
    private String productionDate;
    private String regularDate;
    private String updateTime;
    private int villageId;
    private ArrayList<String> bizTypeList;

    public String getAnnualDate() {
        return annualDate;
    }

    public void setAnnualDate(String annualDate) {
        this.annualDate = annualDate;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getBizInfo() {
        return bizInfo;
    }

    public void setBizInfo(String bizInfo) {
        this.bizInfo = bizInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getRegularDate() {
        return regularDate;
    }

    public void setRegularDate(String regularDate) {
        this.regularDate = regularDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public ArrayList<String> getBizTypeList() {
        return bizTypeList;
    }

    public void setBizTypeList(ArrayList<String> bizTypeList) {
        this.bizTypeList = bizTypeList;
    }

    @Override
    public String toString() {
        return "ATDeviceAccessBean{" +
                "annualDate=" + annualDate +
                ", bizType='" + bizType + '\'' +
                ", bizInfo='" + bizInfo + '\'' +
                ", createTime=" + createTime +
                ", iotId='" + iotId + '\'' +
                ", deviceType=" + deviceType +
                ", effectiveDate=" + effectiveDate +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", productionDate=" + productionDate +
                ", regularDate=" + regularDate +
                ", updateTime=" + updateTime +
                ", villageId=" + villageId +
                ", bizTypeList=" + bizTypeList +
                '}';
    }
}
