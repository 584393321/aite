package com.aliyun.ayland.data;

public class ATGymSubjectTimeBean {
    private String time;
    private int useStatus;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    @Override
    public String toString() {
        return "ATGymSubjectTimeBean{" +
                "time='" + time + '\'' +
                ", useStatus=" + useStatus +
                '}';
    }
}
