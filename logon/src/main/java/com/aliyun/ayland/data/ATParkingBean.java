package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATParkingBean implements Parcelable {
    /**
     * parkcode : 190808155418
     * parkname : 滨海隽城商业广场
     * totalspace : 109
     * buyoutspace : 0
     * publicspace : 109
     */

    private String parkcode;
    private String parkname;
    private String totalspace;
    private String buyoutspace;
    private String publicspace;
    private String currentSpace;

    public String getParkcode() {
        return parkcode;
    }

    public void setParkcode(String parkcode) {
        this.parkcode = parkcode;
    }

    public String getParkname() {
        return parkname;
    }

    public void setParkname(String parkname) {
        this.parkname = parkname;
    }

    public String getTotalspace() {
        return totalspace;
    }

    public void setTotalspace(String totalspace) {
        this.totalspace = totalspace;
    }

    public String getBuyoutspace() {
        return buyoutspace;
    }

    public void setBuyoutspace(String buyoutspace) {
        this.buyoutspace = buyoutspace;
    }

    public String getPublicspace() {
        return publicspace;
    }

    public void setPublicspace(String publicspace) {
        this.publicspace = publicspace;
    }

    public String getCurrentSpace() {
        return currentSpace;
    }

    public void setCurrentSpace(String currentSpace) {
        this.currentSpace = currentSpace;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(parkcode);
        out.writeString(parkname);
        out.writeString(totalspace);
        out.writeString(buyoutspace);
        out.writeString(publicspace);
        out.writeString(currentSpace);
    }

    public static final Creator<ATParkingBean> CREATOR = new Creator<ATParkingBean>() {
        @Override
        public ATParkingBean[] newArray(int size) {
            return new ATParkingBean[size];
        }

        @Override
        public ATParkingBean createFromParcel(Parcel in) {
            return new ATParkingBean(in);
        }
    };

    private ATParkingBean(Parcel in) {
        parkcode = in.readString();
        parkname = in.readString();
        totalspace = in.readString();
        buyoutspace = in.readString();
        publicspace = in.readString();
        currentSpace = in.readString();
    }

    @Override
    public String toString() {
        return "ATParkingBean{" +
                "parkcode='" + parkcode + '\'' +
                ", parkname='" + parkname + '\'' +
                ", totalspace=" + totalspace +
                ", buyoutspace=" + buyoutspace +
                ", publicspace=" + publicspace +
                ", currentSpace=" + currentSpace +
                '}';
    }
}