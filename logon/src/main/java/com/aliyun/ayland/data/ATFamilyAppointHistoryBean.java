package com.aliyun.ayland.data;

public class ATFamilyAppointHistoryBean {

    /**
     * appointmentDay : 2019-09-03
     * appointmentHour : 12:00~18:00
     * appointmentStatus : 2
     * appointmentTime : 13:30~14:00
     * childrenRoomId : 1
     * childrenRoomName : 亲子房1号
     * createPerson : 1132916889676607488
     * createTime : 1567480744000
     * createTimeStr : 2019-09-03 11:19
     * id : 0
     * personCode : 1132916889676607488
     * price : 20.01
     * projectId : 1
     * projectName : 亲子盒子
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
    private double price;
    private int projectId;
    private String projectName;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
}
