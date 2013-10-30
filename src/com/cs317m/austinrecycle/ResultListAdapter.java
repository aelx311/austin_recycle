package com.cs317m.austinrecycle;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultListAdapter extends ArrayAdapter<FacilityItem> {
	private static final String TAG = "ResultListAdapter.java";
	
	private Context _context;
	private int _layoutResourceId;
	private ArrayList<FacilityItem> _item;
	private TextView _facility_name;
	private TextView _facility_address;
	private HashMap<String, Integer> _materialIcons;
	private ImageView _icon;
	
	public ResultListAdapter(Context context, int layoutResourceId, ArrayList<FacilityItem> item) {
		super(context, layoutResourceId, item);
		_context = context;
		_layoutResourceId = layoutResourceId;
		_item = item;
		_materialIcons = new HashMap<String, Integer>();
		
		// Make sure the object is passed correctly
//		Log.d(TAG, "BEGIN PRINTING _facilitiesItem");
//    	for(FacilityItem x : _item) {
//    		Log.d(TAG, x.getName());
//    	}
//    	Log.d(TAG, "END PRINTING _facilitiesItem");
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "in getView");
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(_layoutResourceId, parent, false);
			convertView.setBackground(null);
			_facility_name = (TextView) convertView.findViewById(R.id.facility_name);
			_facility_address = (TextView) convertView.findViewById(R.id.facility_address);
			_materialIcons.put("oil", R.id.oil_icon);
			_materialIcons.put("oil_filter", R.id.oil_filter_icon);
			_materialIcons.put("fluids", R.id.fluids_icon);
			_materialIcons.put("tires", R.id.tires_icon);
			_materialIcons.put("batteries", R.id.batteries_icon);
			_materialIcons.put("newspapers", R.id.newspapers_icon);
			_materialIcons.put("scrap_metal", R.id.scrap_metal_icon);
			_materialIcons.put("aluminum", R.id.aluminum_icon);
		}
		
		FacilityItem data = _item.get(position);
		String addr_human = data.getAddrHuman();
		ArrayList<String> accepts = data.getAccepts();
		
		try {
			JSONObject address = new JSONObject(addr_human);
			String addr = address.getString("address");
	        String city = address.getString("city");
	        String state = address.getString("state");
	        String zip = address.getString("zip");
	        
			for(String iconKey : accepts) {
				int iconId = _materialIcons.get(iconKey);
				if(_materialIcons.containsKey(iconKey)) {
					_icon = (ImageView) convertView.findViewById(iconId);
					_icon.setVisibility(View.VISIBLE);
				}
			}
			
			convertView.setBackgroundColor(Color.WHITE);
	        
			String textToDisplay = addr + ", " + city + ", " + state + ", " + zip + ".";
			
			_facility_name.setText((position+1) + ". " + data.getName());
			_facility_address.setText(textToDisplay);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return convertView;
	}
}
