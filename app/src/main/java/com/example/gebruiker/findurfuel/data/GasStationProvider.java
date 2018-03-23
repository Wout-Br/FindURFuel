package com.example.gebruiker.findurfuel.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Wout Briels on 22/03/2018.
 */

public class GasStationProvider extends ContentProvider {

    private GasStationDbHelper gasStationDbHelper;

    public static final int CODE_GASSTATION = 100;
    public static final int CODE_GASSTATION_WITH_NAME = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GasStationContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, GasStationContract.PATH_GASSTATION, CODE_GASSTATION);
        uriMatcher.addURI(authority, GasStationContract.PATH_GASSTATION + "/#", CODE_GASSTATION_WITH_NAME);
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

        switch (uriMatcher.match(uri)) {
            case CODE_GASSTATION:
                sqLiteDatabase.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues contentValue : values) {
                        long gasStationData = contentValue.getAsLong(GasStationContract.GasStationEntry.COLUMN_NAME);

                        if (!GasStation)
                    }
                }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case CODE_GASSTATION_WITH_NAME: {
                String normalizedUtcNameString = uri.getLastPathSegment();
                String[] selectionArguments = new String[] {normalizedUtcNameString};
                cursor = gasStationDbHelper.getReadableDatabase().query(
                        GasStationContract.GasStationEntry.TABLE_NAME, projection,
                        GasStationContract.GasStationEntry.COLUMN_NAME + " = ? ",
                        selectionArguments, null, null, sortOrder);
                break;
            }
            case CODE_GASSTATION: {
                cursor = gasStationDbHelper.getReadableDatabase().query(
                        GasStationContract.GasStationEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
