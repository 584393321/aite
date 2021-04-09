package com.aliyun.ayland.data;

public class ATAuthCodeBean {

    /**
     * authCode : 4v795u
     * code : 200
     * hasHouse : true
     * message : success
     * house : {"buildingCode":10004,"iotSpaceId":"4481e72bef124b9aa2aad94ef72e09d3","name":"01室","rootSpaceId":"8b68220084b2465ebecf0ac3c0eccc74","villageId":10005,"villageName":"艾特智能总部"}
     * personCode : 1000022
     */

    private String authCode;
    private String code;
    private boolean hasHouse;
    private String message;
    private ATHouseBean house;
    private int personCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHasHouse() {
        return hasHouse;
    }

    public void setHasHouse(boolean hasHouse) {
        this.hasHouse = hasHouse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ATHouseBean getHouse() {
        return house;
    }

    public void setHouse(ATHouseBean house) {
        this.house = house;
    }

    public int getPersonCode() {
        return personCode;
    }

    public void setPersonCode(int personCode) {
        this.personCode = personCode;
    }
}