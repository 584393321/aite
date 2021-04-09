package com.aliyun.ayland.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 订阅的vo类
 * @author Jack
 */
public class ATSubScribeBean implements Serializable, Parcelable {
	
	private static final long serialVersionUID = 1L;
	/**板块id*/
	@Expose
	@SerializedName("tagid")
	public String fid;
	/**板块名称*/
	@Expose
	@SerializedName("tagname")
	public String tag;
	public boolean isSelected;


	public static final Creator<ATSubScribeBean> CREATOR = new Creator<ATSubScribeBean>() {
		public ATSubScribeBean createFromParcel(Parcel in) {
			ATSubScribeBean bean = new ATSubScribeBean();
			bean.fid = in.readString();
			bean.tag = in.readString();
			return bean;
		}

		public ATSubScribeBean[] newArray(int size) {
			return new ATSubScribeBean[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.fid);
		dest.writeString(this.tag);
	}

	@Override
	public boolean equals(Object o) {
		return fid.equals(((ATSubScribeBean) o).fid);
	}

	public ATSubScribeBean() {
		super();
	}

	public ATSubScribeBean(String fid, String tag, boolean isSelected) {
		super();
		this.fid = fid;
		this.tag = tag;
		this.isSelected = isSelected;
	}
}