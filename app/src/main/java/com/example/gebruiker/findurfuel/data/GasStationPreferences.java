package com.example.gebruiker.findurfuel.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Wout Briels on 13/03/2018.
 */

public class GasStationPreferences {

    public static String getPreferredLocation (Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String locationByKey = "location";
        String defaultLocation = "Hasselt";
        return preferences.getString(locationByKey, defaultLocation);
    }
}
