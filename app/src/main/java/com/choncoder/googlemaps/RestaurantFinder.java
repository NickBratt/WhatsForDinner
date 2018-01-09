package com.choncoder.googlemaps;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Nick on 12/24/2017.
 */

public class RestaurantFinder extends AsyncTask<Object, String, String> {

    private static final String TAG = "RestaurantFinder";

    private String placesData;
    private HashMap<String, String> googlePlace = new HashMap<>();

    public HashMap<String, String> getGooglePlace() {
        return googlePlace;
    }

    GoogleMap mMap;
    String url;


    @Override
    protected String doInBackground(Object... objects) {

        try{
            Log.d(TAG, "doInBackground: doInBackground has been called");
            mMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            UrlParser urlParser = new UrlParser();
            placesData = urlParser.parseUrl(url);
            Log.d(TAG, "doInBackground: doInBackground Exit");
        }catch(Exception e){
            Log.e("Exception", e.getMessage());
        }
        return placesData;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: Entered");
        List<HashMap<String, String>> nearbyRestaurants = null;
        DataParser dataParser = new DataParser();
        nearbyRestaurants = dataParser.parse(s);
        ShowNearbyRestaurants(nearbyRestaurants);
        Log.d(TAG, "onPostExecute: Exit");
    }

    private void ShowNearbyRestaurants(List<HashMap<String, String>> nearbyRestaurants){
        for (int i = 0; i < nearbyRestaurants.size(); i++){
            Log.d(TAG, "ShowNearbyRestaurants: method called");
            MarkerOptions markerOptions = new MarkerOptions();
            googlePlace = nearbyRestaurants.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11f));
        }
    }
}
