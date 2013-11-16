package com.cs371m.austinrecycle;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CheckboxListAdapter extends BaseAdapter implements OnClickListener {
	 
	/** The inflator used to inflate the XML layout */
	private LayoutInflater inflator;
 
	/** A list containing some sample data to show. */
	private ArrayList<SampleData> dataList;
	
 
	public CheckboxListAdapter(LayoutInflater inflator, ArrayList<SampleData> itemList) {
		super();
		this.inflator = inflator;
 
		dataList = itemList;

	}
 
	@Override
	public int getCount() {
		return dataList.size();
	}
 
	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		CheckBox _checkBox = null;
 
		// We only create the view if its needed
		if (view == null) {
			view = inflator.inflate(R.layout.material_list_view, null);
 
			// Set the click listener for the checkbox
			_checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
		}
 
		SampleData data = (SampleData) this.getItem(position);
 
		// Set the example text and the state of the checkbox
//		CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
		_checkBox.setChecked(data.isSelected());
		// We tag the data object to retrieve it on the click listener.
		_checkBox.setTag(data);
 
//		TextView tv = (TextView) view.findViewById(R.id.textView1);
//		tv.setText(data.getName());
 
		return view;
	}
 
//	@Override
	/** Will be called when a checkbox has been clicked. */
	public void onClick(View view) {
		SampleData data = (SampleData) view.getTag();
		data.setSelected(((CheckBox) view).isChecked());
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}