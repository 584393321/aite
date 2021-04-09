package com.aliyun.ayland.data;

public class ATVisualIntercomRecordBean {
    /**
     * address : 测试小区2栋2单元
     * customerCode : 10002
     * deviceName : 太川云对讲
     * iotId : gwPifGzZ0tkXM8DguKjL000000
     * sipNumber : sip:1kGaqtW4931jtos35UMZQJ@47.116.75.112:6050
     * times : 2021-01-05 17:19:45
     */

    private String address;
    private int customerCode;
    private String deviceName;
    private String iotId;
    private String sipNumber;
    private String times;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getSipNumber() {
        return sipNumber;
    }

    public void setSipNumber(String sipNumber) {
        this.sipNumber = sipNumber;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
