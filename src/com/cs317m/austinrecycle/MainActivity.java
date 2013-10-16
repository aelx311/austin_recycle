package com.cs317m.austinrecycle;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private EditText _materialList;
	private ListView _materialListView;
	private List<String> _materials;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_materials = new ArrayList<String>();
		_materials.add("Oil");
		_materials.add("Aluminum");
		
		_materialList = (EditText) this.findViewById(R.id.materials_editText);
		_materialList.setInputType(DEFAULT_KEYS_DISABLE);
		_materialList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.setContentView(R.layout.material_list);
			}
		});
		
		_materialListView = (ListView) this.findViewById(R.id.material_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
