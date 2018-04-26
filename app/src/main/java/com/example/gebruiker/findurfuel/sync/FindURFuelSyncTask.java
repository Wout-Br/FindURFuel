package com.example.gebruiker.findurfuel.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.gebruiker.findurfuel.data.GasStationContract;
import com.example.gebruiker.findurfuel.utilities.GasStationJsonUtils;
import com.example.gebruiker.findurfuel.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Wout Briels on 26/04/2018.
 */

public class FindURFuelSyncTask {

    synchronized public static void syncGasStations(Context context) {

        try {
            URL gasStationUrl = NetworkUtils.buildUrl(context);
            String jsonGasStationResponse = NetworkUtils.getResponseFromHttpUrl(gasStationUrl);

            ContentValues[] gasStationContentValues = GasStationJsonUtils.getJsonGasStationContentValues(context, jsonGasStationResponse);

            if (gasStationContentValues != null && gasStationContentValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(GasStationContract.GasStationEntry.CONTENT_URI, null, null);
                contentResolver.bulkInsert(GasStationContract.GasStationEntry.CONTENT_URI, gasStationContentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
