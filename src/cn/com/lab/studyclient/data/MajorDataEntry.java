package cn.com.lab.studyclient.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MajorDataEntry implements Parcelable {

	private long id = 0;
	private String name = null;

	public MajorDataEntry(long id, String name) {

		this.id = id;
		this.name = name;

	}

	public MajorDataEntry(Parcel source) {

		id = source.readLong();
		name = source.readString();

	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int describeContents() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(name);
		;

	}

	public static final Parcelable.Creator<MajorDataEntry> CREATOR = new Creator<MajorDataEntry>() {

		@Override
		public MajorDataEntry[] newArray(int size) {
			return new MajorDataEntry[size];
		}

		@Override
		public MajorDataEntry createFromParcel(Parcel source) {
			return new MajorDataEntry(source);
		}

	};

}
