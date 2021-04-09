package com.aliyun.ayland.data;

public class ATShareSpaceAppBean {
    /**
     * id : 14
     * name : 健身方
     * mobile :
     * adress : 虹旭小区1区1栋1单元1层1室1001
     * thirdBuildingId : null
     * discount : null
     * villageCode : 10001
     * roomCode : 10143
     * customerCode : 10001
     * createPerson : null
     * createTime : 2020-12-29T11:17:57
     * updateTime : 2020-12-29T11:17:57
     * updatePerson : null
     * ifDelete : 0
     */

    private int id;
    private String name;
    private String mobile;
    private String adress;
    private String thirdBuildingId;
    private String discount;
    private int villageCode;
    private int roomCode;
    private int customerCode;
    private String createPerson;
    private String createTime;
    private String updateTime;
    private String updatePerson;
    private int ifDelete;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Object getThirdBuildingId() {
        return thirdBuildingId;
    }

    public void setThirdBuildingId(String thirdBuildingId) {
        this.thirdBuildingId = thirdBuildingId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(int villageCode) {
        this.villageCode = villageCode;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public int getIfDelete() {
        return ifDelete;
    }

    public void setIfDelete(int ifDelete) {
        this.ifDelete = ifDelete;
    }
}