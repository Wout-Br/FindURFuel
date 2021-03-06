package com.example.gebruiker.findurfuel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gebruiker.findurfuel.data.GasStationContract;
import com.example.gebruiker.findurfuel.sync.FindURFuelSyncUtils;

public class MainActivity extends AppCompatActivity implements DetailsAdapter.GasStationDetailsOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    // String of details showing in main activity list
    public static final String[] MAIN_DETAILS_PROJECTION = {
            GasStationContract.GasStationEntry.COLUMN_NAME,
            GasStationContract.GasStationEntry.COLUMN_OPEN,
            GasStationContract.GasStationEntry.COLUMN_RATING,
            // In projection for unique element in database when clicked on item.
            GasStationContract.GasStationEntry.COLUMN_ADDRESS,
    };

    public static final int INDEX_GASSTATION_NAME = 0;
    public static final int INDEX_GASSTATION_OPEN = 1;
    public static final int INDEX_GASSTATION_RATING = 2;
    public static final int INDEX_GASSTATION_ADDRESS = 3;

    private RecyclerView recyclerView;
    private int position = RecyclerView.NO_POSITION;
    private DetailsAdapter detailsAdapter;
    private ProgressBar loadingIndicator;
    private static final int DETAILS_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        // Bundle to safe data when app destroyed!!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isConnected(this)) {
            buildDialog(this).show();
        }

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

        FindURFuelSyncUtils.initialize(this);
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public AlertDialog.Builder buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Make sure wifi or mobile data is turned on to get current data!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }

    @Override
    public void onClick(String address) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        Uri uriForItemClicked = GasStationContract.GasStationEntry.buildDetailsUriWithAddress(address);
        intentToStartDetailActivity.setData(uriForItemClicked);
        startActivity(intentToStartDetailActivity);
    }

    private void showGasStationDataView() {
        recyclerView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
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
                Log.i(TAG, "onOptionsItemSelected: settings");
                Intent intentForSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentForSettings);
                break;
            case  (R.id.menu_refresh):
                Log.i(TAG, "onOptionsItemSelected: refresh");
                showLoading();
                FindURFuelSyncUtils.startImmediateSync(this);
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
}