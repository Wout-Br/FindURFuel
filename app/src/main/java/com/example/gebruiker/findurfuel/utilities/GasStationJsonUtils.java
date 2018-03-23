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

    public static String[] getSimpleGasStationDetails(Context context, String detailsJsonString) throws JSONException {

        final  String GS_LIST = "results";
        final String GS_ADDRESS = "formatted_address";
        final String GS_NAME = "name";
        final String GS_FUELTYPE = "types";
        //final String GS_ICON = "icon";
        // rating bijvoegen



        String[] parsedGasStationData = null;

        JSONObject detailsJson = new JSONObject(detailsJsonString);

        /*if (detailsJson.has(GS_MESSAGE_CODE)) {
            int errorCode = detailsJson.getInt(GS_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK;
                break;

                case HttpURLConnection.HTTP_NOT_FOUND;
                    return null;
                default:
                    return null;
            }
        }*/

        JSONArray gasStationArray = detailsJson.getJSONArray(GS_LIST);

        parsedGasStationData = new String[gasStationArray.length()];

        for (int i = 0 ; i < gasStationArray.length() ; i++) {
            JSONObject singleGasStation = gasStationArray.getJSONObject(i);
            String name = singleGasStation.getString(GS_NAME);
            String fuelType = singleGasStation.getString(GS_FUELTYPE);
            String address = singleGasStation.getString(GS_ADDRESS);

            parsedGasStationData[i] = name + "\n" + address;
        }
        return parsedGasStationData;
    }
}
