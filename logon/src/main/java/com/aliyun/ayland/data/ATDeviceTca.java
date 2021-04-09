package com.aliyun.ayland.data;


/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceTca {
    private String categoryType;
    private String identifier;
    private String rwType;
    private String abilityType;
    private boolean isStd;
    private String name;
    private String abilityId;
    private boolean required;

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRwType() {
        return rwType;
    }

    public void setRwType(String rwType) {
        this.rwType = rwType;
    }

    public String getAbilityType() {
        return abilityType;
    }

    public void setAbilityType(String abilityType) {
        this.abilityType = abilityType;
    }

    public boolean isStd() {
        return isStd;
    }

    public void setStd(boolean std) {
        isStd = std;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(String abilityId) {
        this.abilityId = abilityId;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "ATDeviceTca{" +
                "categoryType='" + categoryType + '\'' +
                ", identifier='" + identifier + '\'' +
                ", rwType='" + rwType + '\'' +
                ", abilityType='" + abilityType + '\'' +
                ", isStd=" + isStd +
                ", name='" + name + '\'' +
                ", abilityId='" + abilityId + '\'' +
                ", required=" + required +
                '}';
    }
}
