package com.aliyun.ayland.data;

import android.support.annotation.NonNull;

/**
 * @author guikong on 18/4/12.
 */
public class ATHouseBean {
    /**
     * buildingCode : 10004
     * iotSpaceId : 4481e72bef124b9aa2aad94ef72e09d3
     * name : 01室
     * rootSpaceId : 8b68220084b2465ebecf0ac3c0eccc74
     * villageId : 10005
     * villageName : 艾特智能总部
     */

    private String buildingCode;
    private String iotSpaceId;
    private String name;
    private String rootSpaceId;
    private int villageId;
    private String villageName;
    private String houseAddress;
    private String houseState;
    private String area;
    private String city;
    private String province;

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getIotSpaceId() {
        return iotSpaceId;
    }

    public void setIotSpaceId(String iotSpaceId) {
        this.iotSpaceId = iotSpaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRootSpaceId() {
        return rootSpaceId;
    }

    public void setRootSpaceId(String rootSpaceId) {
        this.rootSpaceId = rootSpaceId;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getHouseState() {
        return houseState;
    }

    public void setHouseState(String houseState) {
        this.houseState = houseState;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "{" +
                "\"iotSpaceId\":" + iotSpaceId +
                ",\"rootSpaceId\":" + rootSpaceId +
                ",\"buildingCode\":" + buildingCode +
                ",\"houseAddress\":" + houseAddress +
                ",\"name\":" + name +
                ",\"villageId\":" + villageId +
                ",\"villageName\":" + villageName +
                ",\"area\":" + area +
                ",\"city\":" + city +
                ",\"province\":" + province +
                "}";
    }
}