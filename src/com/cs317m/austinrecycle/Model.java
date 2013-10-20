package com.cs317m.austinrecycle;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Model {
    String TAG = "Model";
    HttpURLConnection _conn;
    
    public Model()
    {
        try
        {
            _conn.setReadTimeout(10000);
            _conn.setConnectTimeout(15000);
            _conn.setRequestMethod("GET");
            _conn.setDoInput(true);         // Yes we can receive responses
            _conn.setDoOutput(true);        // Yes we can send requests
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }
    }
    
    /**
     * Makes an HTTP request to the given url_string and returns the JSON string
     * response. Returns null if it fails.
     */
    private String getResponse(String url_string)
    {
        try
        {
            URL url = new URL(url_string);
            _conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(_conn.getInputStream());
            
            return convertStreamToString(in);
        }
        catch (Exception e) 
        {
            Log.e(TAG, e.toString());
            return null;
        }
    }
    
    /**
     * Forms the appropriate URL based on the given materials list, makes the
     * request, and returns the response.
     */
    public String getFacilities(String[] materials)
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
        String response = getResponse(url_string);
        
        // Parse the JSON String response into a JSONArray of JSONObjects
        try 
        {
            JSONArray resp_array = new JSONArray(response);
            for(int i = 0; i < resp_array.length(); ++i)
            {
                JSONObject obj_i = resp_array.getJSONObject(i);
                String facility_name = obj_i.getString("business_name");
                String facility_zone = obj_i.getString("zone");
                String facility_zip = obj_i.getString("zip_code");
                
                String addr_obj_string = obj_i.getString("address");
                JSONObject addr_obj = new JSONObject(addr_obj_string);
                String latitude = addr_obj.getString("latitude");
                
                Log.v(TAG, facility_name + " is in zone \"" + facility_zone + 
                      "\" at zipcode " + facility_zip + " and latitude " + latitude);
                // TODO: Determine where to parse + sort this array. If we do it
                // here then getFacilities should return the JSONArray, not String.
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.toString());
        }

        return response;
    }
    
    /**
     * Return a String of the given input stream. Trick works by using the
     * scanner such that it returns the whole stream as one token string.
     */
    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
