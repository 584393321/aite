package com.aliyun.ayland.data;

public class ATAccessRecordBean {
    /**
     * ifTemp : true
     * licence : 湘AK555U
     * createTime : 2019-08-13 11:33:05
     * deviceCode : 1000277_3
     * id : 648772
     * recordCode : 3c2deb5a959246f19ace63378fa1d7d2
     * time : 2019-08-13 11:33
     * parkcode : 1000277
     * approach : in
     * ownerName : 胡德云
     * ownerTel : 19918981522
     * cardNo : 31303030313939e6b998413347513231
     */

    private String ifTemp;
    private String licence;
    private String createTime;
    private String deviceCode;
    private int id;
    private String recordCode;
    private String time;
    private String parkcode;
    private String approach;
    private String ownerName;
    private String ownerTel;
    private String cardNo;

    public String getIfTemp() {
        return ifTemp;
    }

    public void setIfTemp(String ifTemp) {
        this.ifTemp = ifTemp;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getParkcode() {
        return parkcode;
    }

    public void setParkcode(String parkcode) {
        this.parkcode = parkcode;
    }

    public String getApproach() {
        return approach;
    }

    public void setApproach(String approach) {
        this.approach = approach;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String toString() {
        return "ATAccessRecordBean{" +
                "ifTemp='" + ifTemp + '\'' +
                ", licence='" + licence + '\'' +
                ", createTime='" + createTime + '\'' +
                ", deviceCode='" + deviceCode + '\'' +
                ", id=" + id +
                ", recordCode='" + recordCode + '\'' +
                ", time='" + time + '\'' +
                ", parkcode='" + parkcode + '\'' +
                ", approach='" + approach + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerTel='" + ownerTel + '\'' +
                ", cardNo='" + cardNo + '\'' +
                '}';
    }
}