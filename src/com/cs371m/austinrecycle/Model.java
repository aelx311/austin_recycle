package com.cs371m.austinrecycle;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Model {
    String TAG = "Model";
    HttpURLConnection _conn;
    
    // Must know user's location to sort facilities
    double _user_lat;
    double _user_long;
    
    public Model(double user_lat, double user_long)
    {
    	// Save user location
    	_user_lat = user_lat;
    	_user_long = user_long;
    	Log.d(TAG, "Model user location initialized to {" + _user_lat + ", " + _user_long + "}");
    }
    
    /**
     * Makes an HTTP request to the given url_string and returns the JSON string
     * response. Returns null if it fails.
     */
    private String getResponse(String url_string)
    {
        try
        {
        	// Initialize connection object
            URL url = new URL(url_string);
            _conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(_conn.getInputStream());
            
            return convertStreamToString(in);
        }
        catch (Exception e) 
        {
            Log.e(TAG, "Error getting response", e);
            return null;
        }
    }
    
    /**
     * Forms the appropriate URL based on the given materials list, makes the
     * request, and returns the response.
     */
    public ArrayList<FacilityItem> getFacilities(String[] materials)
    {
        Log.d(TAG, "entering getFacilities()");
        // Create HTTP Request URL from materials
        String url_string = "https://data.austintexas.gov/resource/qzi7-nx8g.json";
        for (int i = 0; i < materials.length; ++i)
        {
            if (i == 0) url_string += "?" + materials[i] + "=Yes";
            else url_string += "&" + materials[i] + "=Yes";
        }
        // Make the request over the network and get the response
        Log.d(TAG, "request: " + url_string);
        String response = getResponse(url_string);
        //Log.d(TAG, "response: " + response);
        
        // Parse the JSON String response into a JSONArray of JSONObjects
        ArrayList<FacilityItem> facilities = new ArrayList<FacilityItem>();
        try 
        {
            JSONArray resp_array = new JSONArray(response);
            
            for(int i = 0; i < resp_array.length(); ++i)
            {
                JSONObject obj_i = resp_array.getJSONObject(i);
                Iterator<?> itr = obj_i.keys();
                ArrayList<String> accepts = new ArrayList<String>();
                while(itr.hasNext()) {
                	String key = (String) itr.next();
                	if(obj_i.getString(key).equals("Yes")) {
                		accepts.add(key);
                	}
                }
                
                String name = obj_i.getString("business_name");
                String phone_num = obj_i.getString("phone");
                
                String addr_obj_string = obj_i.getString("address");
                                
                JSONObject addr_obj = new JSONObject(addr_obj_string);
                String addr_human = addr_obj.getString("human_address");
                
                String addr_lat = addr_obj.getString("latitude");
                String addr_long = addr_obj.getString("longitude");
                
                FacilityItem facility_i = new FacilityItem(name, addr_lat, addr_long, addr_human, phone_num, accepts);
                facilities.add(facility_i);
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, "Error parsing response", e);
        }
        
        for(FacilityItem i : facilities)
        {
        	Log.d(TAG, i.getName() + ": " + i.getAccepts());
        }

        // Sort facilities by distance from the user
        facilities = calculateDistances(facilities);
        Collections.sort(facilities, new FacilityComparator());

        
        return facilities;
    }
    
    /**
     * Return a String of the given input stream. Trick works by using the
     * scanner such that it returns the whole stream as one token string.
     */
    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
    /**
     * Calculate the distance between the user's location and each facility
     * in the given facilities ArrayList and record it into each component 
     * FacilityItem.
     */
    private ArrayList<FacilityItem> calculateDistances(ArrayList<FacilityItem> facilities) {
    	int num_facilities = facilities.size();
    	Log.d(TAG, "num_facilities: " + num_facilities);
    	int chunk_start_i = 0;
    	int chunk_end_i = 0;
    	int num_processed = 0;
    	
		// Distance API can only handle 50 elements at a time - so if there are
    	// more than 50 elements in facilities we need to break them up into
    	// chunks.
    	while (num_processed < num_facilities) {
    		int num_remaining = num_facilities - num_processed;
			chunk_start_i = num_processed;

    		// Formulate the request string for the Google Distance Matrix web API
        	String request_url = "http://maps.googleapis.com/maps/api/distancematrix/json?";
        	request_url += "origins=" + _user_lat + "," + _user_long;
        	request_url += "&destinations=";
        	
        	// Calculate chunk boundary indices into facilities list
    		if (num_remaining > 50) {
    			chunk_end_i = num_processed + 49;
    			num_processed += 50;          
    		}
    		else {
    			chunk_end_i = num_facilities - 1;
    			num_processed = num_facilities;
    		}	
    		
    		// Plug this chunk's facilities into the request as destinations
    		for (int i = chunk_start_i; i <= chunk_end_i; ++i) {
        		request_url += facilities.get(i).getAddrLat() + "," + 
        					   facilities.get(i).getAddrLong();
        		request_url += (i != chunk_end_i) ? "|" : "";
        	}
	    	request_url += "&sensor=false&units=imperial";
	    	
	    	// Make the chunk request over the network and get the response
	        String response = getResponse(request_url);
	        
	        // Parse chunk result and write the distance into each FacilityItem
	        try {
	        	// Drill down into JSON
	            JSONObject resp_obj = new JSONObject(response);
	            JSONArray rows = resp_obj.getJSONArray("rows");
	            JSONObject row0 = rows.getJSONObject(0);
	            JSONArray elements = row0.getJSONArray("elements");
	            
	            // Assemble an array of distances that mirrors the facilities array
	            for(int i = 0; i < elements.length(); ++i) {
	            	JSONObject element = elements.getJSONObject(i);
	            	JSONObject distance = element.getJSONObject("distance");
	            	int meters = distance.getInt("value");
	            	facilities.get(chunk_start_i + i).setDistance(meters);
	            }
	        }
	        catch (JSONException e) {
	            Log.e(TAG, "Error parsing distance response", e);
	        }
    	}
        
        return facilities;
    }
    
    /**
     * Sorts two FacilityItems based on distance from user's location
     */
    private class FacilityComparator implements Comparator<FacilityItem> {
    	public int compare(FacilityItem item1, FacilityItem item2) {
    		int dist1 = item1.getDistance();
    		int dist2 = item2.getDistance();
    		
    		if(dist1 == dist2) return 0;
    		else if(dist1 < dist2) return -1;
    		else return 1;
    	}
    }
}
