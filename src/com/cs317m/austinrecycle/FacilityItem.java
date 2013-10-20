/**
 * A class to store data about a recycling facility that is easier to work with than a JSON string.
 */

package com.cs317m.austinrecycle;

import android.os.Parcel;
import android.os.Parcelable;

public class FacilityItem implements Parcelable{
	private String _name;
	private String _addr_lat;   // Address Latitude
	private String _addr_long;  // Address Longitude
	private String _addr_human; // Human Readable Address
	private String _phone_num;
	
	public FacilityItem(String name, String addr_lat, String addr_long, String addr_human, String phone_num) {
		_name = name;
		_addr_lat = addr_lat;
		_addr_long = addr_long;
		_addr_human = addr_human;
		_phone_num = phone_num;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getAddrLat()
	{
		return _addr_lat;
	}
	
	public String getAddrLong()
	{
		return _addr_long;
	}
	
	public String getAddrHuman()
	{
		return _addr_human;
	}
	
	public String getPhoneNum()
	{
		return _phone_num;
	}
	
	/*
	 * In order to pass objects between activities, the objects have to be Parcleable
	 * Parceling part
	 */
	public FacilityItem(Parcel in) {
		String[] data = new String[5];
		in.readStringArray(data);
		this._name = data[0];
		this._addr_lat = data[1];
		this._addr_long = data[2];
		this._addr_human = data[3];
		this._phone_num = data[4];
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {this._name, this._addr_lat, this._addr_long, this._addr_human, this._phone_num});
	}
	
	public static final Parcelable.Creator<FacilityItem> CREATOR = new Parcelable.Creator<FacilityItem>() {

		@Override
		public FacilityItem createFromParcel(Parcel source) {
			return new FacilityItem(source);
		}

		@Override
		public FacilityItem[] newArray(int size) {
			return new FacilityItem[size];
		}
	};
}
