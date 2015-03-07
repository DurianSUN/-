package cn.com.lab.studyclient.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsTitleListDataEntry implements Parcelable {

	private long id = 0;
	private String title = null;
	private String content = null;
	private String time = null;
	private String imageUrl = null;
	private String webUrl = null;

	public NewsTitleListDataEntry(long id, String title, String content,
			String time, String imageUrl, String webUrl) {

		this.id = id;
		this.title = title;
		this.content = content;
		this.time = time;
		this.imageUrl = imageUrl;
		this.webUrl = webUrl;

	}

	public NewsTitleListDataEntry(Parcel source) {

		id = source.readLong();
		title = source.readString();
		content = source.readString();
		time = source.readString();
		imageUrl = source.readString();
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

	public String getTime() {
		return time;
	}

	public String getImageUrl() {
		return imageUrl;
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
		dest.writeString(time);
		dest.writeString(imageUrl);
		dest.writeString(webUrl);

	}

	public static final Parcelable.Creator<NewsTitleListDataEntry> CREATOR = new Creator<NewsTitleListDataEntry>() {

		@Override
		public NewsTitleListDataEntry[] newArray(int size) {
			return new NewsTitleListDataEntry[size];
		}

		@Override
		public NewsTitleListDataEntry createFromParcel(Parcel source) {
			return new NewsTitleListDataEntry(source);
		}

	};

}
