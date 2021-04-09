package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATConvenientLifeCommunityBean implements Parcelable {

    /**
     * communityIcon : http://alisaas.atsmartlife.com/pic/communityImg/7/1568252336160.png
     * communityName : 大家好我是娱乐类社群板块6号
     * communitySynopsis : 看到我的车轮滚滚没？
     * huanxinGroupId : 93103099805699
     * id : 7
     * personNum : 2
     */

    private String communityIcon;
    private String communityName;
    private String communitySynopsis;
    private String huanxinGroupId;
    private int id;
    private String personNum;

    public String getCommunityIcon() {
        return communityIcon;
    }

    public void setCommunityIcon(String communityIcon) {
        this.communityIcon = communityIcon;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunitySynopsis() {
        return communitySynopsis;
    }

    public void setCommunitySynopsis(String communitySynopsis) {
        this.communitySynopsis = communitySynopsis;
    }

    public String getHuanxinGroupId() {
        return huanxinGroupId;
    }

    public void setHuanxinGroupId(String huanxinGroupId) {
        this.huanxinGroupId = huanxinGroupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(communityIcon);
        out.writeString(communityName);
        out.writeString(communitySynopsis);
        out.writeString(huanxinGroupId);
        out.writeString(personNum);
        out.writeInt(id);
    }

    public static final Creator<ATConvenientLifeCommunityBean> CREATOR = new Creator<ATConvenientLifeCommunityBean>() {
        @Override
        public ATConvenientLifeCommunityBean[] newArray(int size) {
            return new ATConvenientLifeCommunityBean[size];
        }

        @Override
        public ATConvenientLifeCommunityBean createFromParcel(Parcel in) {
            return new ATConvenientLifeCommunityBean(in);
        }
    };


    public ATConvenientLifeCommunityBean() {

    }

    private ATConvenientLifeCommunityBean(Parcel in) {
        communityIcon = in.readString();
        communityName = in.readString();
        communitySynopsis = in.readString();
        huanxinGroupId = in.readString();
        personNum = in.readString();
        id = in.readInt();
    }
}
