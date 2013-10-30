package com.cs371m.austinrecycle;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

public class LocationAutoCompleteAdapter extends ArrayAdapter<String> {
    private ArrayList<String> resultList;
    
	public LocationAutoCompleteAdapter(Context context, int textViewResourceId, ArrayList<String> data) {
		super(context, textViewResourceId);
		resultList = data;
	}
    
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }
}