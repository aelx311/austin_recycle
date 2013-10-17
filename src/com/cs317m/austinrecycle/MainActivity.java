package com.cs317m.austinrecycle;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private EditText _materialList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_materialList = (EditText) this.findViewById(R.id.materials_editText);
		_materialList.setInputType(DEFAULT_KEYS_DISABLE);
		_materialList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popMaterialDialog();
			}
		});
	}

	private void popMaterialDialog() {
		final AlertDialog.Builder materialBuilder = new AlertDialog.Builder(this);
		materialBuilder.setTitle("Please select materials");
		
		LayoutInflater inflater = this.getLayoutInflater();
		final View materialListItemLayout = inflater.inflate(R.layout.material_list_item, null);
		materialBuilder.setView(materialListItemLayout);
		
		final AlertDialog materialDialog = materialBuilder.create();
		materialDialog.show();
		
		// create a list view
		// create list to store materials
		materialList = ()
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
