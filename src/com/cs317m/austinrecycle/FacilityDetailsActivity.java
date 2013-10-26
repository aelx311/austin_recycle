package com.cs317m.austinrecycle;

import java.util.ArrayList;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

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
	
	private ArrayList<FacilityItem> _facilityItemArray;
	private FacilityItem _data;
	private int _position;
	private double _facility_lat;
	private double _facility_long;
	private LatLng _facility_location;
	private Button _dialButton;
	private Button _directionButton;
	private TextView _facilityName;
	private GoogleMap _mapView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.facility_details_activity);
		 
		_facilityItemArray = this.getIntent().getParcelableArrayListExtra("SELECTED_FACILITY");
		_position = this.getIntent().getIntExtra("SELECTED_POSITION", -1);
		_data = _facilityItemArray.get(_position);
		
		_mapView = ((MapFragment) this.getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		_facility_lat = Double.valueOf(_data.getAddrLat());
		_facility_long = Double.valueOf(_data.getAddrLong());
		_facility_location = new LatLng(_facility_lat, _facility_long);

		_mapView.setMyLocationEnabled(true);
		_mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(_facility_location, 17));
		_mapView.addMarker(new MarkerOptions()
                .title(_data.getName())
//                .snippet("The most populous city in Australia.")
                .position(_facility_location));
		
		Log.d(TAG, "_facilityItemArray: " + _facilityItemArray.size());
		Log.d(TAG, "_position: " + _position);
		
		_dialButton = (Button) this.findViewById(R.id.dial_button);
		_directionButton = (Button) this.findViewById(R.id.direction_button);
		_facilityName = (TextView) this.findViewById(R.id.facility_name);
		
		_directionButton.setText("Get direction");
		
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
		
		final Uri direction = Uri.parse("geo:"+_data.getAddrLat() +","+ _data.getAddrLong() + "?z=17");
		_directionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent googleMaps = new Intent(android.content.Intent.ACTION_VIEW, direction);
				FacilityDetailsActivity.this.startActivity(googleMaps);
			}
		});
	}
}
