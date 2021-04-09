package com.aliyun.ayland.data;

import android.graphics.Point;
import android.graphics.Rect;

public class ATEnvironmentOutsideBottom {
    /**
     * num : 21.0
     * time : 00:00
     */

    private double num;
    private String time;
    private Rect windyBoxRect; //表示风力的box
    private Point tempPoint; //温度的点坐标

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public Rect getWindyBoxRect() {
        return windyBoxRect;
    }

    public void setWindyBoxRect(Rect windyBoxRect) {
        this.windyBoxRect = windyBoxRect;
    }

    public Point getTempPoint() {
        return tempPoint;
    }

    public void setTempPoint(Point tempPoint) {
        this.tempPoint = tempPoint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
