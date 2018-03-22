package com.example.gebruiker.findurfuel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by gebruiker on 6/03/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private String details;
    private TextView gasStationDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        gasStationDisplay = (TextView) findViewById(R.id.details_textview);

        Intent intentForActivity = getIntent();

        if (intentForActivity != null) {
            if (intentForActivity.hasExtra(Intent.EXTRA_TEXT)) {        // Controleren of er wel degelijk extra is!!
                details = intentForActivity.getStringExtra(Intent.EXTRA_TEXT);
                gasStationDisplay.setText(details);
            }
        }
    }
}
