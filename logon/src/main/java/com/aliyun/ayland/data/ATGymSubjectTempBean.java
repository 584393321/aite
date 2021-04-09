package com.aliyun.ayland.data;

import java.util.List;

public class ATGymSubjectTempBean {
    private String appointmentDay;
    private String appointmentHour;
    private String appointmentTime;
    private String projectId;
    private String price;
    private String projectName;
    private String tempKey;
    private List<String> tempValue;

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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTempKey() {
        return tempKey;
    }

    public void setTempKey(String tempKey) {
        this.tempKey = tempKey;
    }

    public List<String> getTempValue() {
        return tempValue;
    }

    public void setTempValue(List<String> tempValue) {
        this.tempValue = tempValue;
    }

    @Override
    public String toString() {
        return "ATGymSubjectTempBean{" +
                "appointmentDay='" + appointmentDay + '\'' +
                ", appointmentHour='" + appointmentHour + '\'' +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", projectId='" + projectId + '\'' +
                ", price=" + price +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}