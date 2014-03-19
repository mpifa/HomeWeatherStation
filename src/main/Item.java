package main;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
	private String name;
	private int sensorType;
	private int drawableID;

	public Item(String name, int sensorType, int drawableID) {
		this.name = name;
		this.sensorType = sensorType;
		this.drawableID = drawableID;
	}

	public String getName() {
		return this.name;
	}

	public int getDrawableID() {
		return this.drawableID;
	}

	public int getSensorType() {
		return this.sensorType;
	}

	public Item(Parcel in) {
		this.name = new String();
		this.sensorType = 0;
		this.drawableID = 0;
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in) {
		this.name = in.readString();
		this.sensorType = in.readInt();
		this.drawableID = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(sensorType);
		dest.writeInt(drawableID);
	}

	public static final Parcelable.Creator<Item> CREATOR =
			   new Parcelable.Creator<Item>(){

			    @Override
			    public Item createFromParcel(Parcel source) {
			     return new Item(source);
			    }

			    @Override
			    public Item[] newArray(int size) {
			     return new Item[size];
			    }
			 };
}
