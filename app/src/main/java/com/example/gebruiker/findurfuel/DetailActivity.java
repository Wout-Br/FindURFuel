package com.example.gebruiker.findurfuel;

import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gebruiker.findurfuel.data.GasStationContract;
import com.example.gebruiker.findurfuel.utilities.LogoUtils;

/**
 * Created by Wout Briels on 6/03/2018.
 */

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] GASSTATION_DETAIL_PROJECTION = {
            GasStationContract.GasStationEntry.COLUMN_NAME,
            GasStationContract.GasStationEntry.COLUMN_ADDRESS,
            GasStationContract.GasStationEntry.COLUMN_HEIGHT,
            GasStationContract.GasStationEntry.COLUMN_WIDTH,
            GasStationContract.GasStationEntry.COLUMN_LAT,
            GasStationContract.GasStationEntry.COLUMN_LNG,
            GasStationContract.GasStationEntry.COLUMN_OPEN,
            GasStationContract.GasStationEntry.COLUMN_RATING
    };

    public static final int INDEX_GASSTATION_NAME = 0;
    public static final int INDEX_GASSTATION_ADDRESS = 1;
    public static final int INDEX_GASSTATION_HEIGHT = 2;
    public static final int INDEX_GASSTATION_WIDTH = 3;
    public static final int INDEX_GASSTATION_LAT = 4;
    public static final int INDEX_GASSTATION_LNG = 5;
    public static final int INDEX_GASSTATION_OPEN = 6;
    public static final int INDEX_GASSTATION_RATING = 7;

    private static final int ID_DETAIL_LOADER = 200;

    //private String details;

    private Uri gUri;

    private TextView nameTextView;
    private TextView addressTextView;
    private TextView heightTextView;
    private TextView widthTextView;
    private TextView latTextView;
    private TextView lngTextView;
    private TextView openTextView;
    private TextView ratingTextView;
    private ImageView iconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameTextView = (TextView) findViewById(R.id.name);
        addressTextView = (TextView) findViewById(R.id.address);
        heightTextView = (TextView) findViewById(R.id.height);
        widthTextView = (TextView) findViewById(R.id.width);
        latTextView = (TextView) findViewById(R.id.lat);
        lngTextView = (TextView) findViewById(R.id.lng);
        openTextView = (TextView) findViewById(R.id.open);
        ratingTextView = (TextView) findViewById(R.id.rating);
        iconImageView = (ImageView) findViewById(R.id.imageView);

        gUri = getIntent().getData();
        if (gUri == null) throw new NullPointerException("URI can't be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intentToOpenSettings = new Intent(this, SettingsActivity.class);
            startActivity(intentToOpenSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_DETAIL_LOADER:
                return new CursorLoader(this, gUri, GASSTATION_DETAIL_PROJECTION,
                        null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        // *** Gas station name *** //
        String name = data.getString(INDEX_GASSTATION_NAME);
        String splitWord[] = name.split(" ");
        if (splitWord.length > 3) {
            nameTextView.setTextSize(22);
        } else {
            nameTextView.setTextSize(26);
        }
        nameTextView.setTextColor(Color.BLUE);
        nameTextView.setText(name);

        // *** Gas station address *** //
        String address = data.getString(INDEX_GASSTATION_ADDRESS);
        addressTextView.setText(address);

        // *** Gas station height *** //
        String height = data.getString(INDEX_GASSTATION_HEIGHT);
        heightTextView.setText(height);

        // *** Gas station width *** //
        String width = data.getString(INDEX_GASSTATION_WIDTH);
        widthTextView.setText(width);

        // *** Gas station latitude *** //
        String lat = data.getString(INDEX_GASSTATION_LAT);
        latTextView.setText(lat);

        // *** Gas station longitude *** //
        String lng = data.getString(INDEX_GASSTATION_LNG);
        lngTextView.setText(lng);

        // *** Gas station open *** //
        String open = data.getString(INDEX_GASSTATION_OPEN);
        if (open.equals("OPEN")) {
            openTextView.setTextColor(Color.GREEN);
        }
        else if (open.equals("CLOSED")) {
            openTextView.setTextColor(Color.RED);
        }
        openTextView.setText(open);

        // *** Gas station rating *** //
        String rating = data.getString(INDEX_GASSTATION_RATING);
        ratingTextView.setTextColor(Color.rgb(255, 64, 129));
        ratingTextView.setText(rating);

        // *** Gas station icon *** //
        int logoId = LogoUtils.getLogoForGasStation(name);
        iconImageView.setImageResource(logoId);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
