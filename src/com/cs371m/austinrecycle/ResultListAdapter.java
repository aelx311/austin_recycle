package com.cs371m.austinrecycle;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
	private HashMap<String, Integer> _materialIcons; // To lookup material icon IDs from strings
	private FacilityItem _data;
	
	// For creating efficient references to View objects - ViewHolder pattern
	// Goal: Reduce calls to findViewById()
	static class ViewHolder {
		public TextView _facility_name;
		public TextView _facility_dist;
		public TextView _facility_address;
		public ImageView _oil_icon;
		public ImageView _oil_filter_icon;
		public ImageView _fluids_icon;
		public ImageView _tires_icon;
		public ImageView _batteries_icon;
		public ImageView _newspapers_icon;
		public ImageView _scrap_metal_icon;
		public ImageView _aluminum_icon;
	}
	
	public ResultListAdapter(Context context, int layoutResourceId, ArrayList<FacilityItem> item) {
		super(context, layoutResourceId, item);
		_context = context;
		_layoutResourceId = layoutResourceId;
		_item = item;
	}
	
	// Need to override getView to display something more complicated than a TextView
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "in getView");
		// Re-populate already-allocated views if possible for efficiency
		View rowView = convertView;
		// If no View obj to reuse, allocate one and assign it ViewHolder reference
		if(rowView == null) {
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(_layoutResourceId, parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder._facility_name = (TextView) rowView.findViewById(R.id.facility_name);
			viewHolder._facility_dist = (TextView) rowView.findViewById(R.id.facility_dist);
			viewHolder._facility_address = (TextView) rowView.findViewById(R.id.facility_address);
			viewHolder._oil_icon = (ImageView) rowView.findViewById(R.id.oil_icon);
			viewHolder._oil_filter_icon = (ImageView) rowView.findViewById(R.id.oil_filter_icon);
			viewHolder._fluids_icon = (ImageView) rowView.findViewById(R.id.fluids_icon);
			viewHolder._tires_icon = (ImageView) rowView.findViewById(R.id.tires_icon);
			viewHolder._batteries_icon = (ImageView) rowView.findViewById(R.id.batteries_icon);
			viewHolder._newspapers_icon = (ImageView) rowView.findViewById(R.id.newspapers_icon);
			viewHolder._scrap_metal_icon = (ImageView) rowView.findViewById(R.id.scrap_metal_icon);
			viewHolder._aluminum_icon = (ImageView) rowView.findViewById(R.id.aluminum_icon);
			rowView.setTag(viewHolder);
		}
		
		// Get data to populate the View object
		ViewHolder holder = (ViewHolder) rowView.getTag();
		_data = _item.get(position);
		String addr_human = _data.getAddrHuman();
		ArrayList<String> accepts = _data.getAccepts();
		
		// Re-format the address string into something prettier
		try {
			JSONObject address = new JSONObject(addr_human);
			String addr = address.getString("address");
	        String city = address.getString("city");
	        String state = address.getString("state");
	        String zip = address.getString("zip");

			String textToDisplay = addr + ", " + city + ", " + state + ", " + zip + ".";
			
			holder._facility_name.setText((position+1) + ". " + _data.getName().trim());
			
			float dist_miles = (float)_data.getDistance()/(float)1609.34;
			String miles = String.format("%.1f", dist_miles);
			holder._facility_dist.setText(miles + " mi");
			holder._facility_address.setText(textToDisplay);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.d(TAG, _data.getName());
		Log.d(TAG, _data.getAccepts()+"");
		
		holder._oil_icon.setVisibility(View.GONE);
		holder._oil_filter_icon.setVisibility(View.GONE);
		holder._fluids_icon.setVisibility(View.GONE);
		holder._tires_icon.setVisibility(View.GONE);
		holder._batteries_icon.setVisibility(View.GONE);
		holder._newspapers_icon.setVisibility(View.GONE);
		holder._scrap_metal_icon.setVisibility(View.GONE);
		holder._aluminum_icon.setVisibility(View.GONE);
		
		for(String iconKey : accepts) {
			Log.d(TAG, iconKey);
			if(iconKey.equals("oil")) {
				holder._oil_icon.setVisibility(View.VISIBLE);
			}
			
			if(iconKey.equals("oil_filter")) {
				holder._oil_filter_icon.setVisibility(View.VISIBLE);
			}
			
			if(iconKey.equals("fluids")) {
				holder._fluids_icon.setVisibility(View.VISIBLE);
			}
			
			if(iconKey.equals("tires")) {
				holder._tires_icon.setVisibility(View.VISIBLE);
			}
			
			if(iconKey.equals("batteries")) {
				holder._batteries_icon.setVisibility(View.VISIBLE);
			}
			
			if(iconKey.equals("newspapers")) {
				holder._newspapers_icon.setVisibility(View.VISIBLE);
			}
			
			if(iconKey.equals("scrap_metal")) {
				holder._scrap_metal_icon.setVisibility(View.VISIBLE);
			}
			
			if(iconKey.equals("aluminum")) {
				holder._aluminum_icon.setVisibility(View.VISIBLE);
			}
		}
		return rowView;
	}
}
