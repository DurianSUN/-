package cn.com.lab.studyclient.data;

import android.os.Parcel;
import android.os.Parcelable;

public class StudyTitleListDataEntry implements Parcelable {

	private long id = 0;
	private String title = null;
	private String content = null;
	private String visitor = null;
	private String time = null;
	private int recommendStar = 0;
	private String webUrl = null;

	public StudyTitleListDataEntry(long id, String title, String content,
			String visitor, String time, int recommendStar, String webUrl) {

		this.id = id;
		this.title = title;
		this.content = content;
		this.visitor = visitor;
		this.time = time;
		this.recommendStar = recommendStar;
		this.webUrl = webUrl;

	}

	public StudyTitleListDataEntry(Parcel source) {

		id = source.readLong();
		title = source.readString();
		content = source.readString();
		visitor = source.readString();
		time = source.readString();
		recommendStar = source.readInt();
		webUrl = source.readString();

	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getVisitor() {
		return visitor;
	}

	public String getTime() {
		return time;
	}

	public int getRecommendStar() {
		return recommendStar;
	}

	public String getWebUrl() {
		return webUrl;
	}

	@Override
	public int describeContents() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(visitor);
		dest.writeString(time);
		dest.writeInt(recommendStar);
		dest.writeString(webUrl);

	}

	public static final Parcelable.Creator<StudyTitleListDataEntry> CREATOR = new Creator<StudyTitleListDataEntry>() {

		@Override
		public StudyTitleListDataEntry[] newArray(int size) {
			return new StudyTitleListDataEntry[size];
		}

		@Override
		public StudyTitleListDataEntry createFromParcel(Parcel source) {
			return new StudyTitleListDataEntry(source);
		}
	};

}
