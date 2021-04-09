package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class FamilyMemberBean implements Parcelable{
    private String avatarUrl;
    private String householdtype;
    private int ifAdmin;
    private String nickname;
    private String personCode;
    private String birthDate;
    private String firstname;
    private String idNumber;
    private String inDate;
    private String lastname;
    private String openid;
    private String phone;
    private int sex;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHouseholdtype() {
        return householdtype;
    }

    public void setHouseholdtype(String householdtype) {
        this.householdtype = householdtype;
    }

    public int getIfAdmin() {
        return ifAdmin;
    }

    public void setIfAdmin(int ifAdmin) {
        this.ifAdmin = ifAdmin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(avatarUrl);
        out.writeString(householdtype);
        out.writeInt(ifAdmin);
        out.writeString(nickname);
        out.writeString(personCode);
        out.writeString(birthDate);
        out.writeString(firstname);
        out.writeString(idNumber);
        out.writeString(inDate);
        out.writeString(lastname);
        out.writeString(openid);
        out.writeString(phone);
        out.writeInt(sex);
    }

    public static final Creator<FamilyMemberBean> CREATOR = new Creator<FamilyMemberBean>() {
        @Override
        public FamilyMemberBean[] newArray(int size) {
            return new FamilyMemberBean[size];
        }

        @Override
        public FamilyMemberBean createFromParcel(Parcel in) {
            return new FamilyMemberBean(in);
        }
    };

    private FamilyMemberBean(Parcel in) {
        avatarUrl = in.readString();
        householdtype = in.readString();
        ifAdmin = in.readInt();
        nickname = in.readString();
        personCode = in.readString();
        birthDate = in.readString();
        firstname = in.readString();
        idNumber = in.readString();
        inDate = in.readString();
        lastname = in.readString();
        openid = in.readString();
        phone = in.readString();
        sex = in.readInt();
    }
}
