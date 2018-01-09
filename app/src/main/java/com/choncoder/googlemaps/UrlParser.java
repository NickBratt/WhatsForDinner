package com.choncoder.googlemaps;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nick on 12/30/2017.
 */

public class UrlParser {

    private static final String TAG = "UrlParser";

    public String parseUrl(String urlParam) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(urlParam);

            // create http connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            // read url data
            inputStream = urlConnection.getInputStream();
            Log.d(TAG, "parseUrl: inputstream");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
                Log.d(TAG, "parseUrl: bufferedreader");
            }

            data = stringBuffer.toString();
            Log.d("UrlParser", data.toString());
            bufferedReader.close();
        } catch (Exception e){
            Log.e("Exception", e.toString());
        } finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
