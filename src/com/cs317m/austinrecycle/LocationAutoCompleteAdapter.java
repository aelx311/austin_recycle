package com.cs317m.austinrecycle;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

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