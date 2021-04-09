package com.aliyun.ayland.data;

public class ATSIPEnterBean {
    /**
     * buildingName : 测试小区2栋2单元601室
     * entranceGuardType : 105
     * equipmentCode : 1000451
     * equipmentSubtype : 102
     * iotId : fmEAb6BfHPrMcpb10wzL000000
     * name : 云对讲2
     */

    private String buildingName;
    private int entranceGuardType;
    private int equipmentCode;
    private int equipmentSubtype;
    private String iotId;
    private String name;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getEntranceGuardType() {
        return entranceGuardType;
    }

    public void setEntranceGuardType(int entranceGuardType) {
        this.entranceGuardType = entranceGuardType;
    }

    public int getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(int equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public int getEquipmentSubtype() {
        return equipmentSubtype;
    }

    public void setEquipmentSubtype(int equipmentSubtype) {
        this.equipmentSubtype = equipmentSubtype;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
