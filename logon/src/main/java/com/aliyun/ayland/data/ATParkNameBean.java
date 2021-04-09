package com.aliyun.ayland.data;

public class ATParkNameBean {
    private String parkName;
    private String id;

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ATParkNameBean{" +
                "parkName='" + parkName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
