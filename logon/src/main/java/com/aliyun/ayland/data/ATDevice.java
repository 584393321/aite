package com.aliyun.ayland.data;


/**
 * @author guikong on 18/4/12.
 */
public class ATDevice {
    public String pk;
    public String dn;
    public String token;
    public String iotId;
    public String netType;

    // homelink demo
    public String roomId;

    @Override
    public String toString() {
        return "ATDevice{" +
                "pk='" + pk + '\'' +
                ", dn='" + dn + '\'' +
                ", token='" + token + '\'' +
                ", iotId='" + iotId + '\'' +
                ", netType='" + netType + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
