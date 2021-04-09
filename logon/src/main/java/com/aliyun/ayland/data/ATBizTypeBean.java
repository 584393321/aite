package com.aliyun.ayland.data;


/**
 * @author guikong on 18/4/12.
 */
public class ATBizTypeBean {
    private String bizType;
    private String bizTypeCN;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizTypeCN() {
        return bizTypeCN;
    }

    public void setBizTypeCN(String bizTypeCN) {
        this.bizTypeCN = bizTypeCN;
    }

    @Override
    public String toString() {
        return "ATBizTypeBean{" +
                "bizType='" + bizType + '\'' +
                ", bizTypeCN='" + bizTypeCN + '\'' +
                '}';
    }
}