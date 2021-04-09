package com.aliyun.ayland.data;

/**
 * @author guikong on 18/4/12.
 */
public class ATLoginBean {
    private String authCode;
    private String code;
    private String openid;
    private String message;
    private ATHouseBean house;
    private String avatarUrl;
    private String nickName;
    private boolean hasHouse;
    private String personCode;
    private String huanxinUserName;
    private String huanxinPassword;
    private String email;
    private String houseState;
    /**
     * expiresIn : 7776000
     * phone : 15537876353
     * nickname : 155****6353
     * accessToken : app_user_c20ad4d76fe97759aa27a0c99bff6710
     * username : 15537876353
     */

    private int expiresIn;
    private String phone;
    private String nickname;
    private String accessToken;
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHouseState() {
        return houseState;
    }

    public void setHouseState(String houseState) {
        this.houseState = houseState;
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

    public ATHouseBean getHouse() {
        return house;
    }

    public void setHouse(ATHouseBean house) {
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
                ",\"house\":" + house +
                ",\"avatarUrl\":" + avatarUrl +
                ",\"nickName\":" + nickName +
                ",\"hasHouse\":" + hasHouse +
                ",\"personCode\":" + personCode +
                ",\"houseState\":" + houseState +
                ",\"huanxinUserName\":" + huanxinUserName +
                ",\"huanxinPassword\":" + huanxinPassword +
                "}";
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}