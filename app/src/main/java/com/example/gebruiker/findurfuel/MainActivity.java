package com.example.gebruiker.findurfuel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gebruiker.findurfuel.data.GasStationContract;
import com.example.gebruiker.findurfuel.data.GasStationDbHelper;
import com.example.gebruiker.findurfuel.data.GasStationPreferences;
import com.example.gebruiker.findurfuel.utilities.GasStationJsonUtils;
import com.example.gebruiker.findurfuel.utilities.NetworkUtils;

import java.net.URL;

import static com.example.gebruiker.findurfuel.data.GasStationContract.GasStationEntry.COLUMN_NAME;

public class MainActivity extends AppCompatActivity implements GasStationDetailsAdapter.GasStationDetailsOnClickHandler,
        LoaderManager.LoaderCallbacks<String[]>, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private  GasStationDetailsAdapter gasStationDetailsAdapter;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;
    private static final int DETAILS_LOADER_ID = 0;
    private static boolean UPDATED_PREFERENCES = false;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        // Bundle om informatie te bewaren als app gedestroyed wordt!!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        gasStationDetailsAdapter = new GasStationDetailsAdapter(this);

        recyclerView.setAdapter(gasStationDetailsAdapter);

        errorMessage = (TextView) findViewById(R.id.error_message);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        int loaderId = DETAILS_LOADER_ID;
        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        GasStationDbHelper gasStationDbHelper = new GasStationDbHelper(this);
        sqLiteDatabase = gasStationDbHelper.getWritableDatabase();

        Cursor cursor = getAllDetails();
    }

    @Override
    public void onClick(String detailsPerStation) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, detailsPerStation);
        startActivity(intentToStartDetailActivity);
    }

    private void showGasStationDataView() {
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_settings):
                Intent intentForSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentForSettings);
                break;
            case  (R.id.menu_refresh):
                invalidateData();
                getSupportLoaderManager().restartLoader(DETAILS_LOADER_ID, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] gasStationdata = null;

            @Override
            protected void onStartLoading() {
                if (gasStationdata != null) {
                    deliverResult(gasStationdata);
                } else {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {
                String location = GasStationPreferences.getPreferredLocation(MainActivity.this);
                URL gasStationRequestUrl = NetworkUtils.buildUrl(location);

                try {
                    String jsonGasStationResponse = NetworkUtils.getResponseFromHttpUrl(gasStationRequestUrl);
                    String[] screenGasStationData = GasStationJsonUtils.getSimpleGasStationDetails(MainActivity.this, jsonGasStationResponse);
                    return screenGasStationData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {
                gasStationdata = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        gasStationDetailsAdapter.setGasStationData(data);
        if (data != null) {
            showGasStationDataView();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {
        // Not used but needed for program to work//
    }

    private void invalidateData() { gasStationDetailsAdapter.setGasStationData(null); }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        UPDATED_PREFERENCES = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (UPDATED_PREFERENCES) {
            getSupportLoaderManager().restartLoader(DETAILS_LOADER_ID, null, this);
            UPDATED_PREFERENCES = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private Cursor getAllDetails() {
        return sqLiteDatabase.query(GasStationContract.GasStationEntry.TABLE_NAME, null, null, null, null, null, COLUMN_NAME);
    }
}