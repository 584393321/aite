package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATScoreDetailBean implements Parcelable{
    private String name;
    private String score;
    private boolean haveDone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHaveDone() {
        return haveDone;
    }

    public void setHaveDone(boolean haveDone) {
        this.haveDone = haveDone;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeString(score);
        out.writeBoolean(haveDone);
    }

    public static final Creator<ATScoreDetailBean> CREATOR = new Creator<ATScoreDetailBean>() {
        @Override
        public ATScoreDetailBean[] newArray(int size) {
            return new ATScoreDetailBean[size];
        }

        @Override
        public ATScoreDetailBean createFromParcel(Parcel in) {
            return new ATScoreDetailBean(in);
        }
    };

    public ATScoreDetailBean() {
    }

    private ATScoreDetailBean(Parcel in) {
        name = in.readString();
        score = in.readString();
        haveDone = in.readBoolean();
    }
}