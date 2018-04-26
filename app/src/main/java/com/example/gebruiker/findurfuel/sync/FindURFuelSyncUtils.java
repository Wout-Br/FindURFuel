package com.example.gebruiker.findurfuel.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.example.gebruiker.findurfuel.data.GasStationContract;

/**
 * Created by Wout Briels on 26/04/2018.
 */

public class FindURFuelSyncUtils {

    private static boolean initialized;

    synchronized public static void initialize(final Context context) {
        if (initialized) {
            return;
        }
        initialized = true;

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Uri detailsQueryUri = GasStationContract.GasStationEntry.CONTENT_URI;
                String[] projectionColumns = {GasStationContract.GasStationEntry._ID};
                String selectionStatement = GasStationContract.GasStationEntry.COLUMN_NAME;

                Cursor cursor = context.getContentResolver().query(detailsQueryUri,
                        projectionColumns,  selectionStatement, null, null);

                if (cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
                return null;
            }
        }.execute();
    }

    public static void startImmediateSync(final Context context) {
        Intent intent = new Intent(context, FindURFuelSyncIntentService.class);
        context.startService(intent);
    }
}
