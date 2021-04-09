package com.aliyun.ayland.data;

/**
 * @author guikong on 18/4/8.
 */
public class ATProduct {
    public long id;

    public String productKey;
    public String name;
    public String description;
    @SuppressWarnings("unused")
    public String productModel;

    @SuppressWarnings("unused")
    public long gmtCreate;
    @SuppressWarnings("unused")
    public long gmtModified;

    @SuppressWarnings("unused")
    public Long categoryId;
    @SuppressWarnings("unused")
    public String categoryKey;
    @SuppressWarnings("unused")
    public String categoryName;

    /**
     * 入网类型:
     * <br/>
     * 取值范围:
     * 0:Lora
     * 3:WiFi
     * 4:Zigbee
     * 5:BT
     * 6:Cellular(GPRS,NB-IoT)
     * 7:Ethernet
     */
    @SuppressWarnings("unused")
    public int netType;
    /**
     * 节点类型:
     * <br/>
     * 取值范围:0:ATDevice, 1:Gateway
     */
    @SuppressWarnings("unused")
    public int nodeType;

    /**
     * 租户的唯一标识符
     */
    @SuppressWarnings("unused")
    public String tenantId;
}
