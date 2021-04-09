package com.aliyun.ayland.data;

public class ATGymRecordBean {
    private String appointmentDay;
    private String appointmentHour;
    private String appointmentTime;
    private String createPerson;
    private String createTime;
    private String createTimeStr;
    private String gymId;
    private String gymName;
    private String id;
    private String personCode;
    private String price;
    private String projectId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "ATGymRecordBean{" +
                "appointmentDay='" + appointmentDay + '\'' +
                ", appointmentHour='" + appointmentHour + '\'' +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", createPerson='" + createPerson + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", gymId='" + gymId + '\'' +
                ", gymName='" + gymName + '\'' +
                ", id='" + id + '\'' +
                ", personCode='" + personCode + '\'' +
                ", price='" + price + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
