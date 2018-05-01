package com.example.gebruiker.findurfuel.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.gebruiker.findurfuel.data.GasStationContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Wout Briels on 2/03/2018.
 */

public class GasStationJsonUtils {
    private static final String TAG = GasStationJsonUtils.class.getSimpleName();

    private static final  String GS_LIST = "results";
    private static final String GS_NAME = "name";
    private static final String GS_ADDRESS = "formatted_address";
    private static final String GS_PHOTOS = "photos";
    private static final String GS_HEIGHT = "height";
    private static final String GS_WIDTH = "width";
    private static final String GS_GEOMETRY = "geometry";
    private static final String GS_LOCATION = "location";
    private static final String GS_LAT = "lat";
    private static final String GS_LNG = "lng";
    private static final String GS_OPENING_HOURS = "opening_hours";
    private static final String GS_OPEN = "open_now";
    private static final String GS_RATING = "rating";


    public static ContentValues[] getJsonGasStationContentValues(Context context, String detailsJsonString) throws JSONException {

        JSONObject detailsJson = new JSONObject(detailsJsonString);
        JSONArray gasStationArray = detailsJson.getJSONArray(GS_LIST);

        ContentValues[] gasStationContentValues = new ContentValues[gasStationArray.length()];

        for (int i = 0 ; i < gasStationArray.length() ; i++) {
            JSONObject singleGasStation = gasStationArray.getJSONObject(i);
            String name = singleGasStation.getString(GS_NAME);
            String address = singleGasStation.getString(GS_ADDRESS);

            String rating = "";
            try {
                rating = singleGasStation.getDouble(GS_RATING)+"";
            }
            catch (Exception e) {
                Log.i(TAG, "Rating not found in api!");
            }

            JSONObject geometryObject = singleGasStation.getJSONObject(GS_GEOMETRY);
            JSONObject locationObject = geometryObject.getJSONObject(GS_LOCATION);
            String lat = "";
            String lng = "";
            try {
                lat = locationObject.getDouble(GS_LAT) + "";
                lng = locationObject.getDouble(GS_LNG) + "";
            }
            catch (Exception e) {
                Log.i(TAG, "Latitude and longitude not found in api!");
            }


            String height = "";
            String width = "";
            try {
                JSONObject photosObject = singleGasStation.getJSONArray(GS_PHOTOS).getJSONObject(0);
                height = photosObject.getDouble(GS_HEIGHT) + "";
                width = photosObject.getDouble(GS_WIDTH) + "";
            }
            catch (Exception e) {
                Log.i(TAG, "Height and width not found in api!");
            }

            String open_now = "";
            try {
                JSONObject openingHoursObject = singleGasStation.getJSONObject(GS_OPENING_HOURS);
                boolean open_now_value = openingHoursObject.getBoolean(GS_OPEN);
                if (open_now_value) {
                    open_now = "OPEN";
                } else {
                    open_now = "CLOSED";
                }
            }
            catch (Exception e) {
                Log.i(TAG, "Open_now not found in api!");
            }


            ContentValues cv = new ContentValues();
            cv.put(GasStationContract.GasStationEntry.COLUMN_NAME, name);
            cv.put(GasStationContract.GasStationEntry.COLUMN_ADDRESS, address);
            cv.put(GasStationContract.GasStationEntry.COLUMN_LAT, lat);
            cv.put(GasStationContract.GasStationEntry.COLUMN_LNG, lng);
            cv.put(GasStationContract.GasStationEntry.COLUMN_HEIGHT, height);
            cv.put(GasStationContract.GasStationEntry.COLUMN_WIDTH, width);
            cv.put(GasStationContract.GasStationEntry.COLUMN_RATING, rating);
            cv.put(GasStationContract.GasStationEntry.COLUMN_OPEN, open_now);

            gasStationContentValues[i] = cv;
        }
        return gasStationContentValues;
    }
}
