package com.cs317m.austinrecycle;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FacilityDetailsActivity extends Activity {

	private static final String TAG = "FacilityDetailsActivity.java";
	private ArrayList<FacilityItem> _facilityItem;
	private FacilityItem _data;
	private int _position;
	private Button _dialButton;
	private Button _directionButton;
	private TextView _facilityName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.facility_details_activity);
		_facilityItem = this.getIntent().getParcelableArrayListExtra("SELECTED_FACILITY");
		_position = this.getIntent().getIntExtra("SELECTED_POSITION", -1);
		_data = _facilityItem.get(_position);
		
		Log.d(TAG, "_facilityItem: " + _facilityItem.size());
		Log.d(TAG, "_position: " + _position);
		
		_dialButton = (Button) this.findViewById(R.id.dial_button);
		_directionButton = (Button) this.findViewById(R.id.direction_button);
		_facilityName = (TextView) this.findViewById(R.id.facility_name);
		
		_dialButton.setText("Call: " + _data.getPhoneNum());
		_facilityName.setText(_data.getName());
		
		final Uri phoneNumber = Uri.parse("tel:"+_data.getPhoneNum());
		_dialButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent dialer = new Intent(android.content.Intent.ACTION_CALL, phoneNumber);
				FacilityDetailsActivity.this.startActivity(dialer);
			}
		});
		
		final Uri direction = Uri.parse("geo:"+_data.getAddrLat() +","+ _data.getAddrLong() + "?z=19");
		_directionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent googleMaps = new Intent(android.content.Intent.ACTION_VIEW, direction);
				FacilityDetailsActivity.this.startActivity(googleMaps);
			}
		});
	}
}
