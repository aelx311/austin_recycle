/**
 * A class to store data about a recycling facility that is easier to work with than a JSON string.
 */

package com.cs317m.austinrecycle;

public class FacilityItem {
	private String _name;
	private String _addr_lat;   // Address Latitude
	private String _addr_long;  // Address Longitude
	private String _addr_human; // Human Readable Address
	private String _phone_num;
	
	public FacilityItem(String name, String addr_lat, String addr_long, 
			             String addr_human, String phone_num) {
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
}
