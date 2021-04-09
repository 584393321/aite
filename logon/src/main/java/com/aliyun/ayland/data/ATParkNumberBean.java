package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_parknumber")
public class ATParkNumberBean implements Parcelable {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String park_number;
	
	@DatabaseField
	private long create_time;

	public ATParkNumberBean(){
		
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel o, int flags) {
		o.writeInt(id);
		o.writeString(park_number);
		o.writeLong(create_time);
	}
	
	public ATParkNumberBean(Parcel in){
		id = in.readInt();
		park_number = in.readString();
		create_time = in.readLong();
	}
	
	public static final Creator<ATParkNumberBean> CREATOR = new Creator<ATParkNumberBean>() {
		@Override
		public ATParkNumberBean[] newArray(int size) {
			return new ATParkNumberBean[size];
		}

		@Override
		public ATParkNumberBean createFromParcel(Parcel in) {
			return new ATParkNumberBean(in);
		}
	};
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPark_number() {
		return park_number;
	}

	public void setPark_number(String park_number) {
		this.park_number = park_number;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	@Override
	public String toString() {
		return "AlaramBean [id=" + id + ", room_name=" + park_number + ", create_time=" + create_time+ "]";
	}
}
