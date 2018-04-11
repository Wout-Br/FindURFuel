package com.example.gebruiker.findurfuel;

import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gebruiker.findurfuel.data.GasStationContract;
import com.example.gebruiker.findurfuel.utilities.FakeDetailsUtils;

public class MainActivity extends AppCompatActivity implements DetailsAdapter.GasStationDetailsOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    // String of details showing in main activity list
    public static final String[] MAIN_DETAILS_PROJECTION = {
            GasStationContract.GasStationEntry.COLUMN_NAME,
            GasStationContract.GasStationEntry.COLUMN_ADDRESS,
    };

    public static final int INDEX_GASSTATION_NAME = 0;
    public static final int INDEX_GASSTATION_ADDRESS = 1;

    private RecyclerView recyclerView;
    private int position = RecyclerView.NO_POSITION;
    private DetailsAdapter detailsAdapter;
    private ProgressBar loadingIndicator;
    private static final int DETAILS_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        // Bundle om informatie te bewaren als app gedestroyed wordt!!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FakeDetailsUtils.insertFakeData(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        detailsAdapter = new DetailsAdapter(this,this);

        recyclerView.setAdapter(detailsAdapter);

        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        showLoading();

        int loaderId = DETAILS_LOADER_ID;
        LoaderManager.LoaderCallbacks<Cursor> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
    }

    @Override
    public void onClick(String detailsPerStation) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, detailsPerStation);
        startActivity(intentToStartDetailActivity);
    }

    private void showGasStationDataView() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    /*private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }*/

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
                getSupportLoaderManager().restartLoader(DETAILS_LOADER_ID, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case DETAILS_LOADER_ID:
                Uri detailsQueryUri = GasStationContract.GasStationEntry.CONTENT_URI;
                String sortOrder = GasStationContract.GasStationEntry.COLUMN_NAME;

                return new CursorLoader(this, detailsQueryUri, MAIN_DETAILS_PROJECTION,
                                        null, null, sortOrder);
            default:
                throw new RuntimeException("Loader is not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        detailsAdapter.swapCursor(data);
        if (position == RecyclerView.NO_POSITION) {
            position = 0;
        }
        recyclerView.smoothScrollToPosition(position);
        if (data.getCount() != 0) {
            showGasStationDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        detailsAdapter.swapCursor(null);
    }


    /*@Override
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
    }*/
}