package com.example.gebruiker.findurfuel.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Wout Briels on 23/03/2018.
 */

public class GasStationContract {

    public static final String CONTENT_AUTHORITY = "com.example.gebruiker.findurfuel";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_GASSTATION = "gas_stations";

    public static class GasStationEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GASSTATION).build();

        public static final String TABLE_NAME = "gas_station";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "formatted_address";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LNG = "lng";
        public static final String COLUMN_OPEN = "open_now";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WIDTH = "width";

        public static Uri buildDetailsUriWithName(String name) {
            return CONTENT_URI.buildUpon().appendPath(name).build();
        }
    }
}
