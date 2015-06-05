package com.example.jules.weatherapplication.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jules.weatherapplication.R;
import com.example.jules.weatherapplication.aidl.WeatherData;
import com.example.jules.weatherapplication.operations.WeatherOps;
import com.example.jules.weatherapplication.operations.WeatherOpsImpl;
import com.example.jules.weatherapplication.utils.RetainedFragmentManager;
import com.example.jules.weatherapplication.utils.Utils;
import com.example.jules.weatherapplication.utils.WeatherDataArrayAdapter;

import java.util.List;

public class MainActivity extends LifecycleLoggingActivity {

    /**
     * Used to retain the ImageOps state between runtime configuration
     * changes.
     */
    protected final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(this.getFragmentManager(),
                    TAG);

    /**
     * Provides weather-related operations.
     */
    private WeatherOps mWeatherOps;

    /**
     * The ListView that will display the results to the user.
     */
    protected ListView mWeathersListView;

    /**
     * Location entered by the user.
     */
    protected EditText mLocationEditText;

    /**
     * A custom ArrayAdapter used to display the list of AcronymData
     * objects.
     */
    protected WeatherDataArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherOps = new WeatherOpsImpl(this);

        mWeathersListView = (ListView) findViewById(R.id.weathersListView);

        // Store the EditText that holds the urls entered by the user
        // (if any).
        mLocationEditText = (EditText) findViewById(R.id.locationEditText);

        // Create a local instance of our custom Adapter for our
        // ListView.
        mAdapter = new WeatherDataArrayAdapter(this);

        // Set the adapter to the ListView.
        mWeathersListView.setAdapter(mAdapter);

        // Handle any configuration change.
        handleConfigurationChanges();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        handleConfigurationChanges();
    }

    public void getCurrentWeatherSync(View v) {
        final String location = mLocationEditText.getText().toString();
        mWeatherOps.getCurrentWeatherSync(location);
    }

    public void getCurrentWeatherAsync(View v) {
        final String location = mLocationEditText.getText().toString();
        mWeatherOps.getCurrentWeatherAsync(location);
    }

    /**
     * Handle hardware reconfigurations, such as rotating the display.
     */
    protected void handleConfigurationChanges() {
        // If this method returns true then this is the first time the
        // Activity has been created.
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG,
                    "First time onCreate() call");

            // Create the WeatherOps object one time.
            mWeatherOps = new WeatherOpsImpl(this);

            // Store the WeatherOps into the RetainedFragmentManager.
            mRetainedFragmentManager.put("WEATHER_OPS_STATE",
                    mWeatherOps);

            // Initiate the service binding protocol (which may be a
            // no-op, depending on which type of DownloadImages*Service is
            // used).
            mWeatherOps.bindService();
        } else {
            // The RetainedFragmentManager was previously initialized,
            // which means that a runtime configuration change
            // occured.

            Log.d(TAG,
                    "Second or subsequent onCreate() call");

            // Obtain the WeatherOps object from the
            // RetainedFragmentManager.
            mWeatherOps =
                    mRetainedFragmentManager.get("WEATHER_OPS_STATE");

            // This check shouldn't be necessary under normal
            // circumtances, but it's better to lose state than to
            // crash!
            if (mWeatherOps == null) {
                // Create the WeatherOps object one time.  The "true"
                // parameter instructs WeatherOps to use the
                // DownloadImagesBoundService.
                mWeatherOps = new WeatherOpsImpl(this);

                // Store the WeatherOps into the RetainedFragmentManager.
                mRetainedFragmentManager.put("WEATHER_OPS_STATE",
                        mWeatherOps);

                // Initiate the service binding protocol (which may be
                // a no-op, depending on which type of
                // DownloadImages*Service is used).
                mWeatherOps.bindService();
            } else
                // Inform it that the runtime configuration change has
                // completed.
                mWeatherOps.onConfigurationChanged(this);
        }
    }

    /**
     * Display the results to the screen.
     *
     * @param results
     *            List of Results to be displayed.
     */
    public void displayResults(List<WeatherData> results,
                               String errorMessage) {
        if (results == null || results.size() == 0)
            Utils.showToast(this,
                    errorMessage);
        else {
            Log.d(TAG,
                    "displayResults() with number of weathers = "
                            + results.size());

            // Set/change data set.
            mAdapter.clear();
            mAdapter.addAll(results);
            mAdapter.notifyDataSetChanged();
        }
    }
}
