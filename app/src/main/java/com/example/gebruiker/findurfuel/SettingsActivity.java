package com.example.gebruiker.findurfuel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by gebruiker on 28/02/2018.
 */

public class SettingsActivity extends AppCompatActivity {

    private TextView settingsTextView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsTextView1 = (TextView) findViewById(R.id.settings_textview1);

        settingsTextView1.append("\nSettings");
    }
}
