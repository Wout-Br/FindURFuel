package com.example.gebruiker.findurfuel.sync;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Wout Briels on 26/04/2018.
 */

public class FindURFuelSyncUtils {

    public static void startImmediateSync(final Context context) {
        Intent intent = new Intent(context, FindURFuelSyncIntentService.class);
        context.startService(intent);
    }
}
