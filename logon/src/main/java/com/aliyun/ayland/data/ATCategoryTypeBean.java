package com.aliyun.ayland.data;

public class ATCategoryTypeBean {

    /**
     * categoryName : 台灯2
     * createPerson : 100009
     * createTime : 1583119632000
     * id : 3
     * ifDelete : 0
     * updatePerson : 100009
     * updateTime : 1583119632000
     * villageId : 100003
     */

    private String categoryName;
    private String createPerson;
    private long createTime;
    private int id;
    private int ifDelete;
    private String updatePerson;
    private long updateTime;
    private int villageId;

    public ATCategoryTypeBean(String categoryName, int id) {
        this.categoryName = categoryName;
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }
}
