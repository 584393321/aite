package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author guikong on 18/4/8.
 */
public class ATSceneManualTitle implements Parcelable {
    // well, no field
    private boolean auto;
    private String name;
    private String scene_id;
    private String scene_icon;

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScene_id() {
        return scene_id;
    }

    public void setScene_id(String scene_id) {
        this.scene_id = scene_id;
    }

    public String getScene_icon() {
        return scene_icon;
    }

    public void setScene_icon(String scene_icon) {
        this.scene_icon = scene_icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeByte((byte) (auto ? 1 : 0));
        out.writeString(name);
        out.writeString(scene_id);
        out.writeString(scene_icon);
    }

    public static final Creator<ATSceneManualTitle> CREATOR = new Creator<ATSceneManualTitle>() {
        @Override
        public ATSceneManualTitle[] newArray(int size) {
            return new ATSceneManualTitle[size];
        }

        @Override
        public ATSceneManualTitle createFromParcel(Parcel in) {
            return new ATSceneManualTitle(in);
        }
    };

    public ATSceneManualTitle() {

    }

    public ATSceneManualTitle(Parcel in) {
        auto = in.readByte() != 0;
        name = in.readString();
        scene_id = in.readString();
        scene_icon = in.readString();
    }
}
