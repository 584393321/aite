package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATCommunityTypeBean implements Parcelable {

    /**
     * createTime : 1566979946000
     * id : 1
     * typeName : 广场
     */

    private long createTime;
    private int id;
    private String typeName;

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeLong(createTime);
        out.writeInt(id);
        out.writeString(typeName);
    }

    public static final Creator<ATCommunityTypeBean> CREATOR = new Creator<ATCommunityTypeBean>() {
        @Override
        public ATCommunityTypeBean[] newArray(int size) {
            return new ATCommunityTypeBean[size];
        }

        @Override
        public ATCommunityTypeBean createFromParcel(Parcel in) {
            return new ATCommunityTypeBean(in);
        }
    };

    private ATCommunityTypeBean(Parcel in) {
        createTime = in.readLong();
        id = in.readInt();
        typeName = in.readString();
    }
}
