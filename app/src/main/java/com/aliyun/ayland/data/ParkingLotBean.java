package com.aliyun.ayland.data;

public class ParkingLotBean {
    /**
     * deviceid :
     * parkcode : 100001
     * parkname : 地下停车场11
     * thirdParkCode :
     * totalbookspace : 90
     * totalspace : 112
     * villageId : 100012
     */

    private String deviceid;
    private int parkcode;
    private String parkname;
    private String thirdParkCode;
    private int totalbookspace;
    private int totalspace;
    private int villageId;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public int getParkcode() {
        return parkcode;
    }

    public void setParkcode(int parkcode) {
        this.parkcode = parkcode;
    }

    public String getParkname() {
        return parkname;
    }

    public void setParkname(String parkname) {
        this.parkname = parkname;
    }

    public String getThirdParkCode() {
        return thirdParkCode;
    }

    public void setThirdParkCode(String thirdParkCode) {
        this.thirdParkCode = thirdParkCode;
    }

    public int getTotalbookspace() {
        return totalbookspace;
    }

    public void setTotalbookspace(int totalbookspace) {
        this.totalbookspace = totalbookspace;
    }

    public int getTotalspace() {
        return totalspace;
    }

    public void setTotalspace(int totalspace) {
        this.totalspace = totalspace;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }
}
