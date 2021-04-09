package com.aliyun.ayland.data;


/**
 * @author guikong on 18/4/12.
 */
public class LoginBean {
    private String authCode;
    private String code;
    private String openid;
    private String message;
    private HouseBean house;
    private String avatarUrl;
    private String nickName;
    private boolean hasHouse;
    private String personCode;
    private String huanxinUserName;
    private String huanxinPassword;
    private String realName;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isHasHouse() {
        return hasHouse;
    }

    public void setHasHouse(boolean hasHouse) {
        this.hasHouse = hasHouse;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HouseBean getHouse() {
        return house;
    }

    public void setHouse(HouseBean house) {
        this.house = house;
    }

    public String getHuanxinUserName() {
        return huanxinUserName;
    }

    public void setHuanxinUserName(String huanxinUserName) {
        this.huanxinUserName = huanxinUserName;
    }

    public String getHuanxinPassword() {
        return huanxinPassword;
    }

    public void setHuanxinPassword(String huanxinPassword) {
        this.huanxinPassword = huanxinPassword;
    }

    @Override
    public String toString() {
        return "{" +
                "\"authCode\":" + authCode +
                ",\"code\":" + code +
                ",\"openid\":" + openid +
                ",\"message\":" + message +
                ",\"avatarUrl\":" + avatarUrl +
                ",\"nickName\":" + nickName +
                ",\"hasHouse\":" + hasHouse +
                ",\"personCode\":" + personCode +
                ",\"huanxinUserName\":" + huanxinUserName +
                ",\"huanxinPassword\":" + huanxinPassword +
                "}";
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
