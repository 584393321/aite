package com.aliyun.ayland.data;

public class ATEventAccessRecord {
    private String clazz;
    private String approach;
    private String startTime;
    private String endTime;

    public ATEventAccessRecord(String clazz, String approach, String startTime, String endTime) {
        this.clazz = clazz;
        this.approach = approach;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getClazz() {
        return clazz;
    }

    public String getApproach() {
        return approach;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
