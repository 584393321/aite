package com.aliyun.ayland.data;


/**
 * @author guikong on 18/4/12.
 */
public class HouseBean {
    /**
     * area : 闵行区
     * city : 上海
     * code : 100068
     * iotSpaceId : 19bad4c5f3de48ea8c8f710dd0239517
     * name : A3室
     * province : 上海
     * rootSpaceId : cf116fe134cf40de84305df240a56426
     * villageFullName : 上海-闵行区-恒基龙光汇中心
     * villageId : 100007
     * villageName : 恒基龙光汇中心
     */

    private String area;
    private String city;
    private int code;
    private String iotSpaceId;
    private String name;
    private String province;
    private String rootSpaceId;
    private String villageFullName;
    private int villageId;
    private String villageName;
    private String houseAddress;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRootSpaceId() {
        return rootSpaceId;
    }

    public void setRootSpaceId(String rootSpaceId) {
        this.rootSpaceId = rootSpaceId;
    }

    public String getVillageFullName() {
        return villageFullName;
    }

    public void setVillageFullName(String villageFullName) {
        this.villageFullName = villageFullName;
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

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ",\"iotSpaceId\":" + iotSpaceId +
                ",\"name\":" + name +
                ",\"rootSpaceId\":" + rootSpaceId +
                ",\"villageId\":" + villageId +
                "}";
    }
}
