package com.aliyun.ayland.data;

public class ATShareSpaceProjectBean {
    /**
     * billing : 1
     * createPerson : 1000009
     * createTime : 2021-01-07T15:30:48
     * customerCode : 10001
     * id : 62
     * ifDelete : 0
     * projectName : web测试新增订单
     * remark : web添加
     * updateTime : 2021-01-07T15:30:48
     * villageCode : 10001
     */

    private int billing;
    private int createPerson;
    private String createTime;
    private int customerCode;
    private int id;
    private int ifDelete;
    private String projectName;
    private String remark;
    private String updateTime;
    private int villageCode;

    public int getBilling() {
        return billing;
    }

    public void setBilling(int billing) {
        this.billing = billing;
    }

    public int getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(int createPerson) {
        this.createPerson = createPerson;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIfDelete() {
        return ifDelete;
    }

    public void setIfDelete(int ifDelete) {
        this.ifDelete = ifDelete;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(int villageCode) {
        this.villageCode = villageCode;
    }
}