package com.example.gebruiker.findurfuel.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by Wout Briels on 25/03/2018.
 */

public class GasStationProvider extends ContentProvider {

    private GasStationDbHelper gasStationDbHelper;
    public static final int CODE_GASSTATION = 100;
    public static final int CODE_GASSTATION_BY_NAME = 101;
    private static final UriMatcher gUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GasStationContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, GasStationContract.PATH_GASSTATION, CODE_GASSTATION);
        uriMatcher.addURI(authority, GasStationContract.PATH_GASSTATION + "/*", CODE_GASSTATION_BY_NAME);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        gasStationDbHelper = new GasStationDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase sqLiteDatabase = gasStationDbHelper.getWritableDatabase();

        switch (gUriMatcher.match(uri)) {
            case CODE_GASSTATION:
                sqLiteDatabase.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues contentValue : values) {
                        long _id = sqLiteDatabase.insert(GasStationContract.GasStationEntry.TABLE_NAME, null, contentValue);

                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (gUriMatcher.match(uri)) {
            case CODE_GASSTATION: {
                cursor = gasStationDbHelper.getReadableDatabase().query(GasStationContract.GasStationEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case CODE_GASSTATION_BY_NAME: {
                String address = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{address};
                cursor = gasStationDbHelper.getReadableDatabase().query(GasStationContract.GasStationEntry.TABLE_NAME,
                        projection, GasStationContract.GasStationEntry.COLUMN_ADDRESS + " =? ",
                        selectionArguments, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Implementation needed!!");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        throw new RuntimeException("Implementation needed!!");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;

        if (selection == null) {
            selection = "1";
        }

        switch (gUriMatcher.match(uri)) {
            case CODE_GASSTATION:
                numRowsDeleted = gasStationDbHelper.getWritableDatabase().delete(
                        GasStationContract.GasStationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        throw new RuntimeException("Implementation needed!!");
    }
}
