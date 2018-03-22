package com.example.gebruiker.findurfuel.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wout Briels on 22/03/2018.
 */

public class GasStationDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gasStation.db";
    private static final int DATABASE_VERSION = 1;

    public GasStationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_GASSTATION_DETAILS = "CREATE DETAILS" + GasStationContract.GasStationEntry.GASSTATION_NAME + " (" +
                GasStationContract.GasStationEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT? " +
                GasStationContract.GasStationEntry.GASSTATION_ADDRESS + "REAL, " +
                GasStationContract.GasStationEntry.GASSTATION_LAT + "REAL, " +
                GasStationContract.GasStationEntry.GASSTATION_LNG + "REAL, " +
                GasStationContract.GasStationEntry.GASSTATION_HEIGTH + "REAL, " +
                GasStationContract.GasStationEntry.GASSTATION_WIDTH + "REAL, " +
                GasStationContract.GasStationEntry.GASSTATION_RATING + "REAL, " +
                GasStationContract.GasStationEntry.GASSTATION_OPEN + "REAL, " + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_GASSTATION_DETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP DETAILS IF EXISTS " + GasStationContract.GasStationEntry.GASSTATION_NAME);
        onCreate(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }
}
