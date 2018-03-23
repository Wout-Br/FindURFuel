package com.example.gebruiker.findurfuel.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Wout Briels on 2/03/2018.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String GASSTATIONS_URL =
            "https://maps.googleapis.com/maps/api/place/textsearch/json";
            // "https://maps.googleapis.com/maps/api/place/textsearch/json?query=gas+station+Hasselt&key=AIzaSyCwlLTGT9UiZqiUpHQIfWJyoaVEVSG3S80";


    //private static final String format = "json";
    private static final String key = "AIzaSyCwlLTGT9UiZqiUpHQIfWJyoaVEVSG3S80";


    final static String QUERY_PARAM = "query";
    //final static String FORMAT_PARAM = "output";
    final static String APIKEY_PARAM = "key";


    public static URL buildUrl(String locationInput) {
        String locationUrl = "gas+station+" + locationInput;
        Uri builtUri = Uri.parse(GASSTATIONS_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationUrl)
                .appendQueryParameter(APIKEY_PARAM, key)
                //.appendQueryParameter(UNITS_PARAM, units)
                //.appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();
        //Uri builtUri = Uri.parse(GASSTATIONS_URL);      // Weglaten (dit is volledige API)

                URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
