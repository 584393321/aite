package com.aliyun.ayland.data;

public class ATFamilyMemberCareBean {
    private String nickname;
    private String openid;
    private String personCode;
    private int householdtype;
    private int ifEnable;
    private int personType;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public int getHouseholdtype() {
        return householdtype;
    }

    public void setHouseholdtype(int householdtype) {
        this.householdtype = householdtype;
    }

    public int getIfEnable() {
        return ifEnable;
    }

    public void setIfEnable(int ifEnable) {
        this.ifEnable = ifEnable;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }
}
