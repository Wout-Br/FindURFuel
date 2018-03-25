package com.example.gebruiker.findurfuel.data;

import android.provider.BaseColumns;

/**
 * Created by Wout Briels on 23/03/2018.
 */

public class GasStationContract {

    public static class GasStationEntry implements BaseColumns {
        public static final String TABLE_NAME = "gas_station";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "formatted_address";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LNG = "lng";
        public static final String COLUMN_OPEN = "open_now";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WIDTH = "width";
    }
}
