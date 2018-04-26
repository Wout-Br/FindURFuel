package com.example.gebruiker.findurfuel.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Wout Briels on 26/04/2018.
 */

public class FindURFuelSyncIntentService extends IntentService {

    public FindURFuelSyncIntentService() {
        super("FindURFuelIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FindURFuelSyncTask.syncGasStations(this);
    }
}
