package com.cs317m.austinrecycle;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
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
	private AutoCompleteTextView _locationAutoCompleteTextViewt;
	private ListView _listView;
	private MaterialListAdapter _adapter;

	private Button _searchButton;
	private String[] _materialNames;
	private TypedArray _icons;
	private ArrayList<MaterialItem> _materialItem;
	private ArrayList<FacilityItem> _facilitiesItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_facilitiesItem = new ArrayList<FacilityItem>();
		_materialItem = new ArrayList<MaterialItem>();
		
		_materialEditText = (EditText) this.findViewById(R.id.materials_editText);
		_materialEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_materialEditText.setText("");
				_materialItem.clear();
				popChooseMaterialDialog();
			}
		});
		
		_searchButton = (Button) this.findViewById(R.id.search_button);
		_searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// SelectedMaterial is converted to lower case and replaced whitespace with underscore to match the database field name
				String selectedMaterial = _materialEditText.getText().toString().toLowerCase().replace(' ', '_');
				Log.d(TAG, "INSIDE BUTTON ONCLICK = " +selectedMaterial);
				// Convert to String array to pass as parameter
				String[] selectedMaterialArray = selectedMaterial.split(",");
				new NetworkRequestTask().execute(selectedMaterialArray);
			}
		});
		

		/*
		 * Location AutoComplete using suggestions from Google Location API
		 * TODO: Get API key
		 * 
		 */
		_locationAutoCompleteTextViewt = (AutoCompleteTextView) this.findViewById(R.id.location_autoCompleteTextView);
		_locationAutoCompleteTextViewt.setAdapter(new LocationAutoCompleteAdapter(this, R.layout.location_list_item));
		_locationAutoCompleteTextViewt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Log.d(TAG, _locationAutoCompleteTextViewt.getText().toString());
			}
		});
	}

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
		
		// Read material name
		_materialNames = this.getResources().getStringArray(R.array.list_material_name);
		
		// Read material icon
		_icons = this.getResources().obtainTypedArray(R.array.list_material_icon);
		
		// Store MaterialItem into ArrayList
		for(int i=0; i<_materialNames.length; ++i) {
			_materialItem.add(new MaterialItem(_icons.getResourceId(i, 0), _materialNames[i], false));
		}
		_icons.recycle();
		
		// Create instance of custom adapter
		_adapter = new MaterialListAdapter(this, R.layout.material_list_item, _materialItem);
		
		// Set ListView with custom adapter
		_listView.setAdapter(_adapter);
	
		// Set AdapterView listener
		_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				_materialItem.remove(position);
				_adapter.notifyDataSetChanged();
				
				// Get the string at the clicked position
				String clickedMaterial = _materialNames[position];
				String oldString = _materialEditText.getText().toString();
				
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
     * @param  A String array of materials for the request
     * @return JSON string
     */
    private class NetworkRequestTask extends AsyncTask<String, Integer, ArrayList<FacilityItem>>
    {
        protected ArrayList<FacilityItem> doInBackground(String... materials) {
            Model m = new Model();
            return m.getFacilities(materials);
        }
        
        /** 
         * Invoked in asynchronously in MainActivity when the network 
         * request has finished and doInBackground returns its result.
         */
        protected void onPostExecute(ArrayList<FacilityItem> facilities) {
        	_facilitiesItem = facilities;
        }
    }
}
