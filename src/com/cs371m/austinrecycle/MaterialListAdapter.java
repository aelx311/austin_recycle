package com.cs371m.austinrecycle;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MaterialListAdapter extends ArrayAdapter<MaterialItem> {
	private static final String TAG = "MaterialListAdapter.java";
	
	private final Context _context;
	private final int _layoutResourceId;
	private ArrayList<MaterialItem> _itemArray;
	private MaterialItem _data;
	
	static class ViewHolder {
		public ImageView _icon;
		public TextView _name;
		public CheckBox _checked;
	}
	
	public MaterialListAdapter(Context context, int layoutResourceId, ArrayList<MaterialItem> itemArray) {
		super(context, layoutResourceId, itemArray);
		_context = context;
		_layoutResourceId = layoutResourceId;
		_itemArray = itemArray;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "begin MaterialListAdapter getView");
		View rowView = convertView;
		if(rowView == null) {
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(_layoutResourceId, parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder._icon = (ImageView) rowView.findViewById(R.id.material_icon);
			viewHolder._name = (TextView) rowView.findViewById(R.id.material_name);
//			viewHolder._checked = (CheckBox) rowView.findViewById(R.id.material_checkbox);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		_data = _itemArray.get(position);
		holder._icon.setImageResource(_data.getIcon());
		Log.d(TAG, "icon: " + _data.getIcon());
		holder._name.setText(_data.getName());
		Log.d(TAG, "name: " + _data.getName());
//		holder._checked.setChecked(_data.getChecked());
//		Log.d(TAG, "checked: " + _data.getChecked());
		
		Log.d(TAG, "end MaterialListAdapter getView");
		return rowView;
	}
}
