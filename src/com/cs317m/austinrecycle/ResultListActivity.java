package com.cs317m.austinrecycle;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ResultListActivity extends ListActivity {
	private static final String TAG = "ResultListActivity.java";
	
	private ListView _listView;
	private ArrayList<FacilityItem> _facilityItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_listView = this.getListView();
		_facilityItem = this.getIntent().getParcelableArrayListExtra("RETURNED_RESULT");
		
		// Make sure the object is passed correctly
		Log.d(TAG, "BEGIN PRINTING _facilitiesItem");
    	for(FacilityItem x : _facilityItem) {
    		Log.d(TAG, x.getName());
    	}
    	Log.d(TAG, "END PRINTING _facilitiesItem");
    	
		_listView.setAdapter(new ResultListAdapter(this, R.layout.result_list_item, _facilityItem));
		_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Intent showFacilityDetails = new Intent(ResultListActivity.this, FacilityDetailsActivity.class);
				showFacilityDetails.putParcelableArrayListExtra("SELECTED_FACILITY", (ArrayList<? extends Parcelable>) _facilityItem);
				showFacilityDetails.putExtra("SELECTED_POSITION", position);
				ResultListActivity.this.startActivity(showFacilityDetails);
				Log.d(TAG, "Clicked position: " + position);
			}
		});
	}
}
