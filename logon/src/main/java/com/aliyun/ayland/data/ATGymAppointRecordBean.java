package com.aliyun.ayland.data;

public class ATGymAppointRecordBean {

    /**
     * appointmentDay : 2019-09-04
     * appointmentHour : 06:00~12:00
     * appointmentStatus : 2
     * appointmentTime : 11:30~12:00
     * createPerson : 1140807701130522624
     * createTime : 1567566477000
     * createTimeStr : 2019-09-04 11:07
     * gymId : 1
     * gymName : 健身房
     * id : 0
     * personCode : 1140807701130522624
     * price : 20.0
     * projectId : 1
     * projectName : 跑步
     * useStatus : 2
     */

    private String appointmentDay;
    private String appointmentHour;
    private int appointmentStatus;
    private String appointmentTime;
    private String createPerson;
    private long createTime;
    private String createTimeStr;
    private int gymId;
    private String gymName;
    private int id;
    private String personCode;
    private double price;
    private int projectId;
    private String projectName;
    private int useStatus;
    private String actualEndTime;
    private String actualEndTimeStr;
    private String actualStartTime;
    private String actualStartTimeStr;

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

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
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

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }
}