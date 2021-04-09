package com.aliyun.ayland.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author guikong on 18/4/12.
 */
public class ATCommunityBean implements Parcelable {

    /**
     * communityName : 大家好我是娱乐类社群板块2号
     * communityPeopleNum : 1
     * communityStatus : 2
     * communitySynopsis : ？还我手套
     * communityType : 1
     * createPerson : 1140430804614561792
     * createPersonBuildingCode : 100033
     * createPersonName : 灭霸
     * createPersonPhone : 12345
     * createPersonVillageId : 100007
     * createTime : 1565941334000
     * id : 3
     * updatePerson : 1140430804614561792
     * updateTime : 1565941334000
     */

    private String communityName;
    private int communityPeopleNum;
    private int communityStatus;
    private String communitySynopsis;
    private int communityType;
    private String createPerson;
    private int createPersonBuildingCode;
    private String createPersonName;
    private String createPersonPhone;
    private int createPersonVillageId;
    private long createTime;
    private int id;
    private String updatePerson;
    private long updateTime;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public int getCommunityPeopleNum() {
        return communityPeopleNum;
    }

    public void setCommunityPeopleNum(int communityPeopleNum) {
        this.communityPeopleNum = communityPeopleNum;
    }

    public int getCommunityStatus() {
        return communityStatus;
    }

    public void setCommunityStatus(int communityStatus) {
        this.communityStatus = communityStatus;
    }

    public String getCommunitySynopsis() {
        return communitySynopsis;
    }

    public void setCommunitySynopsis(String communitySynopsis) {
        this.communitySynopsis = communitySynopsis;
    }

    public int getCommunityType() {
        return communityType;
    }

    public void setCommunityType(int communityType) {
        this.communityType = communityType;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public int getCreatePersonBuildingCode() {
        return createPersonBuildingCode;
    }

    public void setCreatePersonBuildingCode(int createPersonBuildingCode) {
        this.createPersonBuildingCode = createPersonBuildingCode;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getCreatePersonPhone() {
        return createPersonPhone;
    }

    public void setCreatePersonPhone(String createPersonPhone) {
        this.createPersonPhone = createPersonPhone;
    }

    public int getCreatePersonVillageId() {
        return createPersonVillageId;
    }

    public void setCreatePersonVillageId(int createPersonVillageId) {
        this.createPersonVillageId = createPersonVillageId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(communityName);
        out.writeString(communitySynopsis);
        out.writeString(createPerson);
        out.writeString(createPersonName);
        out.writeString(createPersonPhone);
        out.writeString(updatePerson);
        out.writeInt(communityPeopleNum);
        out.writeInt(communityStatus);
        out.writeInt(communityType);
        out.writeInt(createPersonBuildingCode);
        out.writeInt(createPersonVillageId);
        out.writeInt(id);
        out.writeLong(createTime);
        out.writeLong(updateTime);
    }

    public static final Creator<ATCommunityBean> CREATOR = new Creator<ATCommunityBean>() {
        @Override
        public ATCommunityBean[] newArray(int size) {
            return new ATCommunityBean[size];
        }

        @Override
        public ATCommunityBean createFromParcel(Parcel in) {
            return new ATCommunityBean(in);
        }
    };

    public ATCommunityBean() {

    }

    private ATCommunityBean(Parcel in) {
        communityName = in.readString();
        communitySynopsis = in.readString();
        createPerson = in.readString();
        createPersonName = in.readString();
        createPersonPhone = in.readString();
        updatePerson = in.readString();
        communityPeopleNum = in.readInt();
        communityStatus = in.readInt();
        communityType = in.readInt();
        createPersonBuildingCode = in.readInt();
        createPersonVillageId = in.readInt();
        id = in.readInt();
        createTime = in.readInt();
        updateTime = in.readInt();
    }
}