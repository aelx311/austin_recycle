package com.cs317m.austinrecycle;

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
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity.java";
	
	private EditText _materialEditText;
	private ListView _listView;
	private MaterialListAdapter _adapter;
	
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
	}

	private void popChooseMaterialDialog() {
		Log.d(TAG, "in popChooseMaterialDialog");
		final AlertDialog.Builder materialDialogBuilder = new AlertDialog.Builder(this);
		materialDialogBuilder.setTitle("Please select materials");
		LayoutInflater inflater = this.getLayoutInflater();
		final View popupLayout = inflater.inflate(R.layout.material_listview, null);
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
				dialog.dismiss();
			}
		});
		
		// Read material name and icon
		String[] rawData = this.getResources().getStringArray(R.array.list_material_name);
		TypedArray icons = this.getResources().obtainTypedArray(R.array.list_material_icon);
		MaterialItem[] materialItem = new MaterialItem[rawData.length];
		for(int i=0; i<rawData.length; ++i) {
			materialItem[i] = new MaterialItem(icons.getResourceId(i, 0), rawData[i], false);
		}
		icons.recycle();
		_adapter = new MaterialListAdapter(this, R.layout.material_list_item, materialItem);
		_listView.setAdapter(_adapter);
		
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