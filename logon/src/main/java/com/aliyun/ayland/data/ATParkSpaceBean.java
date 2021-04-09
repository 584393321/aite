package com.aliyun.ayland.data;

public class ATParkSpaceBean {

    /**
     * parkcode : 1000302
     * qureytime : 1565148600004
     * current_space : 555
     */

    private String parkcode;
    private String qureytime;
    private String current_space;

    public String getParkcode() {
        return parkcode;
    }

    public void setParkcode(String parkcode) {
        this.parkcode = parkcode;
    }

    public String getQureytime() {
        return qureytime;
    }

    public void setQureytime(String qureytime) {
        this.qureytime = qureytime;
    }

    public String getCurrent_space() {
        return current_space;
    }

    public void setCurrent_space(String current_space) {
        this.current_space = current_space;
    }

    @Override
    public String toString() {
        return "ATParkSpaceBean{" +
                "parkcode='" + parkcode + '\'' +
                ", qureytime='" + qureytime + '\'' +
                ", current_space=" + current_space +
                '}';
    }
}
