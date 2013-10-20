package com.cs317m.austinrecycle;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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
	private String[] _materialNames;
	private TypedArray _icons;
	private MaterialItem[] _materialItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_materialEditText = (EditText) this.findViewById(R.id.materials_editText);
		_materialEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popChooseMaterialDialog();
			}
		});
		
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
		Log.d(TAG, "in popChooseMaterialDialog");
		final AlertDialog.Builder materialDialogBuilder = new AlertDialog.Builder(this);
		materialDialogBuilder.setTitle("Please select materials");
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
				// TODO Send selected materials back to EditText
				AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
						String clickedMaterial = _materialNames[position];
						_materialEditText.setText(clickedMaterial);
						Log.d(TAG, "position: "+position);
					}
				};
				_listView.setOnItemClickListener(listListener);
				dialog.dismiss();
			}
		});
		
		// Read material name and icon
		_materialNames = this.getResources().getStringArray(R.array.list_material_name);
		_icons = this.getResources().obtainTypedArray(R.array.list_material_icon);
		_materialItem = new MaterialItem[_materialNames.length];
		for(int i=0; i<_materialNames.length; ++i) {
			_materialItem[i] = new MaterialItem(_icons.getResourceId(i, 0), _materialNames[i], false);
		}
		_icons.recycle();
		_adapter = new MaterialListAdapter(this, R.layout.material_list_item, _materialItem);
		_listView.setAdapter(_adapter);
		
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
}