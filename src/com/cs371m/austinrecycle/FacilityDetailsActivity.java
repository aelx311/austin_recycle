package com.cs371m.austinrecycle;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;

import com.cs317m.austinrecycle.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FacilityDetailsActivity extends FragmentActivity {
	private static final String TAG = "FacilityDetailsActivity.java";
	
	private ArrayList<FacilityItem> _facilityItemArray;
	private FacilityItem _data;
	private int _position;
	private double _current_lat;
	private double _current_long;
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
		 
		// Get details of the selected facility from previous activity
		_facilityItemArray = this.getIntent().getParcelableArrayListExtra("SELECTED_FACILITY");
		_position = this.getIntent().getIntExtra("SELECTED_POSITION", -1);
		_current_lat = this.getIntent().getDoubleExtra("CURRENT_LAT", 0);
		_current_long = this.getIntent().getDoubleExtra("CURRENT_LONG", 0);
		_data = _facilityItemArray.get(_position);
		
		// Get the latitude and longitude of the facility
		_facility_lat = Double.valueOf(_data.getAddrLat());
		_facility_long = Double.valueOf(_data.getAddrLong());
		_facility_location = new LatLng(_facility_lat, _facility_long);
		
		// Set up SupportMapFragment
		_mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        _mapView.setMyLocationEnabled(true);
        _mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(_facility_location, 19));
        _mapView.addMarker(new MarkerOptions().title(_data.getName()).position(_facility_location));
		
		Log.d(TAG, "_facilityItemArray: " + _facilityItemArray.size());
		Log.d(TAG, "_position: " + _position);
		
		_dialButton = (Button) this.findViewById(R.id.dial_button);
		_directionButton = (Button) this.findViewById(R.id.direction_button);
		_facilityName = (TextView) this.findViewById(R.id.facility_name);
		
		_dialButton.setText("Call: " + _data.getPhoneNum());
		_facilityName.setText(_data.getName());
		
		// Bring up dialing screen
		final Uri phoneNumber = Uri.parse("tel:"+_data.getPhoneNum());
		_dialButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent dialer = new Intent(android.content.Intent.ACTION_CALL, phoneNumber);
				FacilityDetailsActivity.this.startActivity(dialer);
			}
		});
		
		// Bring up Google Maps to start navigating
		final Uri direction = Uri.parse("http://maps.google.com/maps?saddr="+ _current_lat + ","
																			+ _current_long +
																	"&daddr="+ _data.getAddrLat() +","
																			+_data.getAddrLong());
		_directionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent googleMaps = new Intent(android.content.Intent.ACTION_VIEW, direction);
				googleMaps.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				FacilityDetailsActivity.this.startActivity(googleMaps);
			}
		});
	}
}
