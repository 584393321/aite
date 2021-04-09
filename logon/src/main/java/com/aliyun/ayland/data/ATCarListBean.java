package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATCarListBean implements Parcelable {
    /**
     * carImage : http://anthouse-oss.oss-cn-shenzhen.aliyuncs.com/carpass2021/01/081000022-134811003-1610084891003.jpg
     * carType : 路线
     * color : 橘色
     * createTime : 2021-01-08 13:48:11
     * customerCode : 10002
     * id : 11
     * identityId : 4264567895258
     * ifDelete : 0
     * mobile : 15537856351
     * personCode : 1000022
     * plateNumber : 辽FDZXGX
     * spaceId : 1
     * status : 1
     * userName : 熬夜
     * villageCode : 10193
     */

    private String carImage;
    private String carType;
    private String color;
    private String createTime;
    private int customerCode;
    private int id;
    private String identityId;
    private int ifDelete;
    private String mobile;
    private int personCode;
    private String plateNumber;
    private int spaceId;
    private int status;
    private String userName;
    private int villageCode;
    private String brand;
    private String parkName;
    private int buildingCode;
    private String buildingName;

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public int getIfDelete() {
        return ifDelete;
    }

    public void setIfDelete(int ifDelete) {
        this.ifDelete = ifDelete;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getPersonCode() {
        return personCode;
    }

    public void setPersonCode(int personCode) {
        this.personCode = personCode;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(int villageCode) {
        this.villageCode = villageCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public int getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(int buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(carImage);
        out.writeString(carType);
        out.writeString(color);
        out.writeString(createTime);
        out.writeInt(customerCode);
        out.writeInt(id);
        out.writeString(identityId);
        out.writeInt(ifDelete);
        out.writeString(mobile);
        out.writeInt(personCode);
        out.writeString(plateNumber);
        out.writeInt(spaceId);
        out.writeInt(status);
        out.writeString(userName);
        out.writeInt(villageCode);
        out.writeString(brand);
        out.writeString(parkName);
        out.writeInt(buildingCode);
        out.writeString(buildingName);
    }

    public static final Creator<ATCarListBean> CREATOR = new Creator<ATCarListBean>() {
        @Override
        public ATCarListBean[] newArray(int size) {
            return new ATCarListBean[size];
        }

        @Override
        public ATCarListBean createFromParcel(Parcel in) {
            return new ATCarListBean(in);
        }
    };


    public ATCarListBean() {
    }

    private ATCarListBean(Parcel in) {
        carImage = in.readString();
        carType = in.readString();
        color = in.readString();
        createTime = in.readString();
        customerCode = in.readInt();
        id = in.readInt();
        identityId = in.readString();
        ifDelete = in.readInt();
        mobile = in.readString();
        personCode = in.readInt();
        plateNumber = in.readString();
        spaceId = in.readInt();
        status = in.readInt();
        userName = in.readString();
        villageCode = in.readInt();
        brand = in.readString();
        parkName = in.readString();
        buildingCode = in.readInt();
        buildingName = in.readString();
    }
}