package com.example.gebruiker.findurfuel;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.gebruiker.findurfuel.utilities.GasStationJsonUtils;
import com.example.gebruiker.findurfuel.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements GasStationDetailsAdapter.GasStationDetailsOnClickHandler, LoaderManager.LoaderCallbacks<String[]> {

    private RecyclerView recyclerView;
    private  GasStationDetailsAdapter gasStationDetailsAdapter;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        loadGasStationData();
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] gasStationData = null;

            @Override
            protected void onStartLoading() {
                if (gasStationData != null) {
                    deliverResult(gasStationData);
                } else {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {
                String location = "Hasselt";
                URL gasStationRequestUrl = NetworkUtils.buildUrl(location);

                try {
                    String jsonGasStationResponse = NetworkUtils.getResponseFromHttpUrl(gasStationRequestUrl);
                    String[] screenGasStationData = GasStationJsonUtils.getSimpleGasSatationDetails(MainActivity.this, jsonGasStationResponse);
                    return screenGasStationData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {
                gasStationData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] strings) {
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void loadGasStationData() {
        showGasStationDataView();
        String location = "Hasselt";        // Moet geautomatiseerd worden!!
        new FetchGasstationTask().execute(location);
    }

    @Override
    public void onClick(String detailsPerStation) {
        //Toast.makeText(this, detailsPerStation, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Settings opened", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            break;
            case  (R.id.menu_refresh):
                Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
                gasStationDetailsAdapter.setGasStationData(null);
                loadGasStationData();
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchGasstationTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            String location = params[0];
            URL gasStationRequestUrl = NetworkUtils.buildUrl(location);


            try {
                String jsonGasStationResponse = NetworkUtils.getResponseFromHttpUrl(gasStationRequestUrl);
                String[] screenGasStationData = GasStationJsonUtils.getSimpleGasSatationDetails(MainActivity.this, jsonGasStationResponse);
                return screenGasStationData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] gasstationData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (gasstationData != null) {
                showGasStationDataView();
                gasStationDetailsAdapter.setGasStationData(gasstationData);
            }
            else {
                showErrorMessage();
            }
        }
    }
}
