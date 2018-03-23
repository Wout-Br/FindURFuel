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
        final String SQL_CREATE_GASSTATION_TABLE = "CREATE DETAILS" + GasStationContract.GasStationEntry.TABLE_NAME + " (" +
                GasStationContract.GasStationEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT? " +
                GasStationContract.GasStationEntry.COLUMN_NAME + "TEXT NOT NULL, " +
                GasStationContract.GasStationEntry.COLUMN_ADDRESS + "TEXT NOT NULL, " +
                GasStationContract.GasStationEntry.COLUMN_LAT + "DOUBLE NOT NULL, " +
                GasStationContract.GasStationEntry.COLUMN_LNG + "DOUBLE NOT NULL, " +
                GasStationContract.GasStationEntry.COLUMN_HEIGTH + "DOUBLE NOT NULL, " +
                GasStationContract.GasStationEntry.COLUMN_WIDTH + "DOUBLE NOT NULL, " +
                GasStationContract.GasStationEntry.COLUMN_RATING + "DOUBLE NOT NULL, " +
                GasStationContract.GasStationEntry.COLUMN_OPEN + "BOOLEAN NOT NULL, " + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_GASSTATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GasStationContract.GasStationEntry.COLUMN_NAME);
        onCreate(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }
}
