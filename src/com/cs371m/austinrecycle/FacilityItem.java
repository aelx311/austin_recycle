/**
 * A class to store data about a recycling facility that is easier to work with than a JSON string.
 */

package com.cs371m.austinrecycle;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class FacilityItem implements Parcelable{
	
	private static final String TAG = "FacilityItem";
	private String _name;
	private String _addr_lat;   // Address Latitude
	private String _addr_long;  // Address Longitude
	private String _addr_human; // Human Readable Address
	private String _phone_num;
	private ArrayList<String> _accepts;
	
	public FacilityItem(String name, String addr_lat, String addr_long, String addr_human, String phone_num, ArrayList<String> accepts) {
		_name = name;
		_addr_lat = addr_lat;
		_addr_long = addr_long;
		_addr_human = addr_human;
		_phone_num = phone_num;
		_accepts = accepts;
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
	
	public ArrayList<String> getAccepts() {
		return _accepts;
	}
	
	/*
	 * In order to pass objects between activities, the objects have to be Parcelable
	 * Parceling part
	 */
	@SuppressWarnings("unchecked")
	public FacilityItem(Parcel in) {
		
		this._name = in.readString();
		this._addr_lat = in.readString();
		this._addr_long = in.readString();
		this._addr_human = in.readString();
		this._phone_num = in.readString();
		this._accepts = (ArrayList<String>) in.readSerializable();
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_name);
		dest.writeString(_addr_lat);
		dest.writeString(_addr_long);
		dest.writeString(_addr_human);
		dest.writeString(_phone_num);
		dest.writeSerializable(_accepts);
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
