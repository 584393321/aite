package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATTmallSceneBean implements Parcelable {

    /**
     * relationStatus : 1
     * sceneId : 3d2f8484b659426e8d337c0488f6e946
     * sceneName : 回家模式
     */

    private int relationStatus;
    private String sceneId;
    private String sceneName;

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(relationStatus);
        out.writeString(sceneId);
        out.writeString(sceneName);
    }

    public static final Creator<ATTmallSceneBean> CREATOR = new Creator<ATTmallSceneBean>() {
        @Override
        public ATTmallSceneBean[] newArray(int size) {
            return new ATTmallSceneBean[size];
        }

        @Override
        public ATTmallSceneBean createFromParcel(Parcel in) {
            return new ATTmallSceneBean(in);
        }
    };

    private ATTmallSceneBean(Parcel in) {
        relationStatus = in.readInt();
        sceneId = in.readString();
        sceneName = in.readString();
    }
}
