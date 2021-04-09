package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATFamilyMenberBean implements Parcelable {

    /**
     * birthDate : 1995-02-06
     * firstname : lin
     * householdtype : 102
     * idNumber : 325436576576487100002
     * ifAdmin : 1
     * lastname : 17717810650
     * nickname : 17717810650
     * openid : 1242386385666252800
     * personCode : 100002
     */

    private String birthDate;
    private String firstname;
    private String householdtype;
    private String idNumber;
    private int ifAdmin;
    private String lastname;
    private String nickname;
    private String openid;
    private String personCode;
    private int sex;

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

    public String getHouseholdtype() {
        return householdtype;
    }

    public void setHouseholdtype(String householdtype) {
        this.householdtype = householdtype;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public int getIfAdmin() {
        return ifAdmin;
    }

    public void setIfAdmin(int ifAdmin) {
        this.ifAdmin = ifAdmin;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

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
        out.writeString(householdtype);
        out.writeInt(ifAdmin);
        out.writeString(nickname);
        out.writeString(personCode);
        out.writeString(birthDate);
        out.writeString(firstname);
        out.writeString(idNumber);
        out.writeString(lastname);
        out.writeString(openid);
        out.writeInt(sex);
    }

    public static final Creator<ATFamilyMenberBean> CREATOR = new Creator<ATFamilyMenberBean>() {
        @Override
        public ATFamilyMenberBean[] newArray(int size) {
            return new ATFamilyMenberBean[size];
        }

        @Override
        public ATFamilyMenberBean createFromParcel(Parcel in) {
            return new ATFamilyMenberBean(in);
        }
    };

    private ATFamilyMenberBean(Parcel in) {
        householdtype = in.readString();
        ifAdmin = in.readInt();
        nickname = in.readString();
        personCode = in.readString();
        birthDate = in.readString();
        firstname = in.readString();
        idNumber = in.readString();
        lastname = in.readString();
        openid = in.readString();
        sex = in.readInt();
    }
}
