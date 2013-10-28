package com.cs317m.austinrecycle;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity.java";
	
	private EditText _materialEditText;
	private Button _searchButton;
	private AutoCompleteTextView _locationAutoCompleteTextView;
	private ListView _listView;
	private MaterialListAdapter _adapter;
	private String[] _materialNames;
	private TypedArray _icons;
	private ArrayList<MaterialItem> _materialItemArray;
	private Geocoder _geocoder;
	private double _current_lat;
	private double _current_long;
	private ProgressDialog _progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_materialItemArray = new ArrayList<MaterialItem>();
		_geocoder = new Geocoder(this);

		_materialEditText = (EditText) this.findViewById(R.id.materials_editText);
		_materialEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_materialEditText.setText("");
				_materialItemArray.clear();
				popChooseMaterialDialog();
			}
		});
		 
		_searchButton = (Button) this.findViewById(R.id.search_button);
		_searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(_materialEditText.getText().toString().equals("")) {
					Toast.makeText(MainActivity.this, "Please select at least ONE material", Toast.LENGTH_SHORT).show();
				}
				else if(_locationAutoCompleteTextView.getText().toString().equals("")) {
					Toast.makeText(MainActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
				}
				else {
					_progressDialog = new ProgressDialog(MainActivity.this);
					_progressDialog.setTitle("Searching");
					_progressDialog.setMessage("Searching for locations...");
					_progressDialog.show();
					// Get the latitude and longitude of current location
					try {
						String currentAddress = _locationAutoCompleteTextView.getText().toString();
						List<Address> returnedAddress = _geocoder.getFromLocationName(currentAddress, 1);
						_current_lat = returnedAddress.get(0).getLatitude();
						_current_long = returnedAddress.get(0).getLongitude();
					}
					catch(IOException e) {
						Log.e(TAG, "Error occured in Geocoder: ", e);
					}
					
					// SelectedMaterial is converted to lower case and replaced whitespace with underscore to match the database field name
					String selectedMaterial = _materialEditText.getText().toString().toLowerCase().replace(' ', '_');
					
					// Convert to String array to pass as parameter
					String[] selectedMaterialArray = selectedMaterial.split(",");
					
					// Needs to create a new task every time
					new NetworkRequestTask().execute(selectedMaterialArray);	
				}
			}
		});
		
		// Location AutoComplete using suggestions from Google Location API
		_locationAutoCompleteTextView = (AutoCompleteTextView) this.findViewById(R.id.location_autoCompleteTextView);
		_locationAutoCompleteTextView.setThreshold(1);
		_locationAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
		});
		_locationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//				String str = (String) adapterView.getItemAtPosition(position);
				// TODO: create a special case for "Current location"
				_locationAutoCompleteTextView.dismissDropDown();
			}
		});
	}
	
	/*
	 * Display list of materials
	 * Selected materials will be removed from the list
	 */
	private void popChooseMaterialDialog() {
		Log.d(TAG, "entering popChooseMaterialDialog");
		
		// Create an Alert Dialog
		final AlertDialog.Builder materialDialogBuilder = new AlertDialog.Builder(this);
		
		// Set title of Alert Dialog
		materialDialogBuilder.setTitle("Please select materials");
		
		// Set the view of Alert Dialog to custom ListView
		LayoutInflater inflater = this.getLayoutInflater();
		final View popupLayout = inflater.inflate(R.layout.material_list_view, null);
		_listView = (ListView) popupLayout.findViewById(R.id.material_listView);
		materialDialogBuilder.setView(popupLayout);
		
		// Dialog CANCEL button
		materialDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				_materialEditText.setText("");
				dialog.dismiss();
			}
		});
		
		// Dialog DONE button
		materialDialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		// Read material names and icons from arrays.xml
		_materialNames = this.getResources().getStringArray(R.array.list_material_name);
		_icons = this.getResources().obtainTypedArray(R.array.list_material_icon);

		// Store MaterialItem into ArrayList
		for(int i=0; i<_materialNames.length; ++i) {
			_materialItemArray.add(new MaterialItem(_icons.getResourceId(i, 0), _materialNames[i], false));
		}
		_icons.recycle();
		
		// Setup custom adapter
		_adapter = new MaterialListAdapter(this, R.layout.material_list_item, _materialItemArray);
		_listView.setAdapter(_adapter);
		_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				// Grab the clicked item's name before removing the item from
				String clickedMaterial = _materialItemArray.get(position).getName();
				String oldString = _materialEditText.getText().toString();
				
				// Remove clicked item from the adapter's data array and update
				// => It gets removed from the dialog
				_materialItemArray.remove(position);
				_adapter.notifyDataSetChanged();		
				
				// Format into comma separated string
				String newString = oldString.equals("") ? clickedMaterial : oldString + "," + clickedMaterial;
				_materialEditText.setText(newString);
			}
		});
		
		// Display dialog
		final AlertDialog materialListDialog = materialDialogBuilder.create();
		materialListDialog.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    /**
     * Class to run HTTP network requests in a worker thread. Necessary to
     * keep the UI interactive.
     * 
     * Types specified are <Argument Type, Progress Update Type, Return Type>
     */
    private class NetworkRequestTask extends AsyncTask<String, Integer, ArrayList<FacilityItem>> {
    	
    	@Override
        protected ArrayList<FacilityItem> doInBackground(String... materials) {
            Model m = new Model(_current_lat, _current_long);
            return m.getFacilities(materials);
        }
        
        /** 
         * Invoked in asynchronously in MainActivity when the network request 
         * has finished and doInBackground returns its result.
         */
    	@Override
        protected void onPostExecute(ArrayList<FacilityItem> facilities) {
        	_progressDialog.dismiss();
        	// Starting the ResultListActivity
        	Intent resultIntent = new Intent(MainActivity.this, ResultListActivity.class);
        	resultIntent.putParcelableArrayListExtra("RETURNED_RESULT", (ArrayList<? extends Parcelable>) facilities);
        	resultIntent.putExtra("CURRENT_LAT", _current_lat);
        	resultIntent.putExtra("CURRENT_LONG", _current_long);
			MainActivity.this.startActivity(resultIntent);
        }
    }
    
    /**
     * Asynchronously get place suggestions from Google
     */
    private class PlacesTask extends AsyncTask<String, Void, ArrayList<String>>
    {
        private static final String TAG = "LocationAutoCompleteAdapter.java";
        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String OUT_JSON = "/json";
        private static final String API_KEY = "AIzaSyCnLUmKZNvy5P7R2p1RJw2fd4VGNRbcJBU";
        
        protected ArrayList<String> doInBackground(String... input) {
			Log.d(TAG, "Async PlacesTask doInBackground(): ");

    		ArrayList<String> resultList = null;
            
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?sensor=true");
                sb.append("&key=" + API_KEY);
                sb.append("&components=country:us");
                sb.append("&input=" + URLEncoder.encode(input[0], "utf8"));
                
                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                
                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            }
            catch (MalformedURLException e) {
                Log.e(TAG, "Error processing Places API URL", e);
                return resultList;
            }
            catch (IOException e) {
                Log.e(TAG, "Error connecting to Places API", e);
                return resultList;
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                
                // Extract the Place descriptions from the results
                resultList = new ArrayList<String>(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Cannot process JSON results", e);
            }
            
            return resultList;
    	}
    	
    	protected void onPostExecute(ArrayList<String> resultList) {
    		Log.d(TAG, "Async PlacesTask onPostExecute(): ");
    		LocationAutoCompleteAdapter locAdapter = new LocationAutoCompleteAdapter(MainActivity.this, R.layout.location_list_item, resultList);
    		_locationAutoCompleteTextView.setAdapter(locAdapter);
    		_locationAutoCompleteTextView.showDropDown();
    	}	
    }
}
