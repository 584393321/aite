package com.aliyun.ayland.data;

import com.google.gson.annotations.SerializedName;

public class ATFamilyAppointRecordBean {

    /**
     * appointmentDay : 2019-09-03
     * appointmentHour : 18:00~24:00
     * appointmentStatus : 2
     * appointmentTime : 19:30~20:00
     * childrenRoomId : 1
     * childrenRoomName : 亲子房1号
     * createPerson : 1140807701130522624
     * createTime : 1567501357000
     * createTimeStr : 2019-09-03 17:02
     * id : 0
     * personCode : 1140807701130522624
     * price : 20
     * projectId : 1
     * projectName : 亲子盒子
     * useStatus : 2
     */

    private String appointmentDay;
    private String appointmentHour;
    private int appointmentStatus;
    private String appointmentTime;
    private int childrenRoomId;
    private String childrenRoomName;
    private String createPerson;
    private long createTime;
    private String createTimeStr;
    private int id;
    private String personCode;
    private float price;
    private int projectId;
    private String projectName;
    private int useStatus;
    private String actualEndTime;
    private String actualEndTimeStr;
    private String actualStartTime;
    private String actualStartTimeStr;

    public String getAppointmentDay() {
        return appointmentDay;
    }

    public void setAppointmentDay(String appointmentDay) {
        this.appointmentDay = appointmentDay;
    }

    public String getAppointmentHour() {
        return appointmentHour;
    }

    public void setAppointmentHour(String appointmentHour) {
        this.appointmentHour = appointmentHour;
    }

    public int getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getChildrenRoomId() {
        return childrenRoomId;
    }

    public void setChildrenRoomId(int childrenRoomId) {
        this.childrenRoomId = childrenRoomId;
    }

    public String getChildrenRoomName() {
        return childrenRoomName;
    }

    public void setChildrenRoomName(String childrenRoomName) {
        this.childrenRoomName = childrenRoomName;
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

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getActualEndTimeStr() {
        return actualEndTimeStr;
    }

    public void setActualEndTimeStr(String actualEndTimeStr) {
        this.actualEndTimeStr = actualEndTimeStr;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualStartTimeStr() {
        return actualStartTimeStr;
    }

    public void setActualStartTimeStr(String actualStartTimeStr) {
        this.actualStartTimeStr = actualStartTimeStr;
    }

}