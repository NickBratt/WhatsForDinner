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
import java.util.Random;

/**
 * Created by Nick on 12/24/2017.
 */

public class RestaurantFinder extends AsyncTask<Object, String, String> {

    private static final String TAG = "RestaurantFinder";

    private String placesData;
    private GoogleMap mMap;
    private String url;
    private int num = 0;

    public RestaurantFinder(int num){
        this.num = num;
    }

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
        DataParser dataParser = new DataParser();
        List<HashMap<String, String>> nearbyRestaurants = dataParser.parse(s);
        ShowNearbyRestaurants(nearbyRestaurants);
        Log.d(TAG, "onPostExecute: Exit");
    }

    private void ShowNearbyRestaurants(List<HashMap<String, String>> nearbyRestaurants){
        if (num == 0) {
            for (int i = 0; i < nearbyRestaurants.size(); i++) {
                Log.d(TAG, "ShowNearbyRestaurants: method called");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyRestaurants.get(i);
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
        } else if (num == 1){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> singleRest;
            Random r = new Random();
            mMap.clear();
            singleRest = nearbyRestaurants.get(r.nextInt(nearbyRestaurants.size()) + 0);
            double lat = Double.parseDouble(singleRest.get("lat"));
            double lng = Double.parseDouble(singleRest.get("lng"));
            String placeName = singleRest.get("place_name");
            String vicinity = singleRest.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
        } else {
            System.out.println("Not valid input for constructor.");
        }
    }

}
