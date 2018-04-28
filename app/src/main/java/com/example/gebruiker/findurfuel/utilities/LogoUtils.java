package com.example.gebruiker.findurfuel.utilities;

import android.util.Log;

import com.example.gebruiker.findurfuel.R;

/**
 * Created by Wout Briels on 26/04/2018.
 */

public class LogoUtils {

    private static final String TAG = LogoUtils.class.getSimpleName();

    public static int getLogoForGasStation(String name) {
        String splitWord[] = name.split(" ", 2);
        String firstWord = splitWord[0].toLowerCase();
        Log.d(TAG, name);

        if (firstWord.equals("shell")) {
            return R.drawable.shell_logo;
        }
        else if (firstWord.equals("q8") || firstWord.equals("bouts")) {
            return R.drawable.q8_logo;
        }
        else if (firstWord.equals("total")) {
            return R.drawable.total_logo;
        }
        else if (firstWord.equals("esso")) {
            return R.drawable.esso_logo;
        }
        else if (firstWord.equals("lukoil")) {
            return R.drawable.lukoil_logo;
        }
        else if (firstWord.equals("avia")) {
            return R.drawable.avia_logo;
        }
        else if (firstWord.equals("dats")) {
            return R.drawable.dats24_logo;
        }
        return R.drawable.default_logo;
    }
}
