package com.example.gebruiker.findurfuel.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Wout Briels on 2/03/2018.
 */

public class GasStationJsonUtils {

    public static String[] getJsonGasStationDetails(Context context, String detailsJsonString) throws JSONException {

        final  String GS_LIST = "results";
        final String GS_NAME = "name";
        final String GS_ADDRESS = "formatted_address";
        final String GS_HEIGHT = "height";
        final String GS_WIDTH = "width";
        final String GS_LAT = "lat";
        final String GS_LNG = "lng";
        final String GS_OPEN = "open_now";
        final String GS_RATING = "rating";
        final String GS_ICON = "icon";



        String[] parsedGasStationData = null;

        JSONObject detailsJson = new JSONObject(detailsJsonString);
        JSONArray gasStationArray = detailsJson.getJSONArray(GS_LIST);

        parsedGasStationData = new String[gasStationArray.length()];

        for (int i = 0 ; i < gasStationArray.length() ; i++) {
            JSONObject singleGasStation = gasStationArray.getJSONObject(i);
            String name = singleGasStation.getString(GS_NAME);
            String address = singleGasStation.getString(GS_ADDRESS);

            parsedGasStationData[i] = name + "\n" + address;
        }
        return parsedGasStationData;
    }
}
