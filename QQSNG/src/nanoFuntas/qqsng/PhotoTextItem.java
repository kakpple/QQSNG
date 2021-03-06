package nanoFuntas.qqsng;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class defines data type of PhotoTextItem.
 * photo, name and score are member of PhotoTextItem data type.
 */
public class PhotoTextItem implements Parcelable{	
	private Bitmap mPhoto = null;
	private String mName = null;
	private String mScore = null;	
	private boolean mSelectable = true;
	
	public PhotoTextItem(){
	}
	
	public PhotoTextItem(Bitmap photo, String name, String score){
		this.mPhoto = photo;
		this.mName = name;
		this.mScore = score;
	}
	
	public PhotoTextItem(Parcel in){
		mPhoto = in.readParcelable(getClass().getClassLoader());
		mName = in.readString();
		mScore = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(mPhoto, flags);
		dest.writeString(mName);
		dest.writeString(mScore);
	}
	
	public static final Parcelable.Creator<PhotoTextItem> CREATOR = new Parcelable.Creator<PhotoTextItem>() {
		@Override
		public PhotoTextItem createFromParcel(Parcel source) {
			return new PhotoTextItem(source);
		}

		@Override
		public PhotoTextItem[] newArray(int size) {
			return new PhotoTextItem[size];
		}
	};
	
	public boolean isSelectable(){
		return mSelectable;
	}
	
	public void setSelectable(boolean selectable){
		this.mSelectable = selectable;
	}
	
	public void setPhoto(Bitmap photo){
		this.mPhoto = photo;
	}
	
	public Bitmap getPhoto(){
		return mPhoto;
	}
	
	public void setName(String name){
		this.mName = name;
	}
	
	public String getName(){
		return mName;
	}
	
	public void setScore(String score){
		this.mScore = score;
	}
	
	public String getScore(){
		return mScore;
	}
}
