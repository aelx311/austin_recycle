package com.cs317m.austinrecycle;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultListAdapter extends ArrayAdapter<FacilityItem> {
	private static final String TAG = "ResultListAdapter.java";
	
	private Context _context;
	private int _layoutResourceId;
	private ArrayList<FacilityItem> _item;
	private TextView _facility_name;
	private TextView _facility_address;
	
	public ResultListAdapter(Context context, int layoutResourceId, ArrayList<FacilityItem> item) {
		super(context, layoutResourceId, item);
		_context = context;
		_layoutResourceId = layoutResourceId;
		_item = item;
		
		Log.d(TAG, "_item size: " + _item.size());
		// Make sure the object is passed correctly
		Log.d(TAG, "BEGIN PRINTING _facilitiesItem");
    	for(FacilityItem x : _item) {
    		Log.d(TAG, x.getName());
    	}
    	Log.d(TAG, "END PRINTING _facilitiesItem");
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "in getView");
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(_layoutResourceId, parent, false);
		}
		_facility_name = (TextView) convertView.findViewById(R.id.facility_name);
		_facility_address = (TextView) convertView.findViewById(R.id.facility_address);
		
		FacilityItem data = _item.get(position);
		
		// Set alternating background color
		if(position%2 == 0) {
			convertView.setBackgroundColor(android.graphics.Color.LTGRAY);
		}
		else {
			convertView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
		}
		_facility_name.setText(data.getName());
		_facility_address.setText(data.getAddrHuman());
		
		return convertView;
	}
}
