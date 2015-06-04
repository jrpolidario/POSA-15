package com.example.jules.weatherapplication.operations;

import android.view.View;

import com.example.jules.weatherapplication.activities.MainActivity;

/**
 * This class defines all the acronym-related operations.
 */
public interface WeatherOps {
    /**
     * Initiate the service binding protocol.
     */
    public void bindService();

    /**
     * Initiate the service unbinding protocol.
     */
    public void unbindService();

    /*
     * Initiate the synchronous weather lookup when the user presses
     * the "Weather Sync" button.
     */
    public void getCurrentWeatherSync(String location);

    /*
     * Initiate the asynchronous weather lookup when the user presses
     * the "Weather Async" button.
     */
    public void getCurrentWeatherAsync(String location);

    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChanged(MainActivity activity);
}
