package com.aliyun.ayland.data;


/**
 * @author sinyuk
 * @date 2018/6/25
 */
public class ATWeek {
    private String day;
    private int day_int;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDay_int() {
        return day_int;
    }

    public void setDay_int(int day_int) {
        this.day_int = day_int;
    }

    @Override
    public String toString() {
        return "Week{" +
                "day='" + day + '\'' +
                ", day_int=" + day_int +
                '}';
    }
}
