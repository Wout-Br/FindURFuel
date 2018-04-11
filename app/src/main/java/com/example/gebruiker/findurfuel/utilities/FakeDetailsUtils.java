package com.example.gebruiker.findurfuel.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.gebruiker.findurfuel.data.GasStationContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wout Briels on 11/04/2018.
 */

public class FakeDetailsUtils {

    private static String[] names = {"Shell", "Q8", "Esso", "Total", "Peeterman"};
    private static String[] addresses = {"Steenweg", "Bosstraat", "Kerkweg", "Vogelzangstraat", "Kasseiweg"};

    private static ContentValues createDetailContentValues(int i) {
        ContentValues detailValues = new ContentValues();
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_NAME, names[i]);
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_ADDRESS, addresses[i]);
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_HEIGHT, 300);
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_WIDTH, 500);
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_LAT, 50.9433871);
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_LNG, 5.348641199999999);
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_OPEN, "open");
        detailValues.put(GasStationContract.GasStationEntry.COLUMN_RATING, 4);

        return detailValues;
    }

    public static void insertFakeData (Context context) {
        List<ContentValues> fakeDetails = new ArrayList<ContentValues>();

        for (int i=0 ; i<5 ; i++) {
            fakeDetails.add(FakeDetailsUtils.createDetailContentValues(i));
        }

        context.getContentResolver().bulkInsert(GasStationContract.GasStationEntry.CONTENT_URI, fakeDetails.toArray(new ContentValues[5]));
    }
}
