package com.example.jules.weatherapplication.services;

import android.util.Log;

import com.example.jules.weatherapplication.aidl.WeatherData;
import com.example.jules.weatherapplication.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WeatherServiceCached extends LifecycleLoggingService {
    private HashMap<String, HashMap<String, Object>> mCache;

    public static final long DEFAULT_CACHING_DURATION = 10000; //10 seconds

    @Override
    public void onCreate()
    {
        mCache = new HashMap<String, HashMap<String, Object>>();
    }

    protected boolean isCachedWeatherDataTooOld(String location) {
        HashMap hashMapOnLocation = (HashMap) mCache.get(location);
        Long lastTimeChecked = (long) hashMapOnLocation.get("LAST_TIME_CHECKED");

        if (lastTimeChecked == 0) {
            // If Not Yet Checked
            return true;
        } else {
            long currentTime = new Date().getTime();
            long millisecondsElapsed = currentTime - lastTimeChecked;

            if (millisecondsElapsed > DEFAULT_CACHING_DURATION) {
                Log.i(TAG, "Cache too old. Caching new data.");
                return true;
            }
            else
                return false;
        }
    }

    protected boolean isLocationAlreadyInCache(String location) {
        if (mCache.containsKey(location)) {
            Log.i(TAG, "Location already in cache.");
            return true;
        }
        else {
            Log.i(TAG, "Location not yet part of cache. Now added to cache.");
            return false;
        }
    }

    protected boolean isGetFromCache(String location) {
        if (isLocationAlreadyInCache(location)) {
            if (!isCachedWeatherDataTooOld(location))
                return true;
            else
                return false;
        }
        else {
            // Else if location not yet in cache, add to cache
            mCache.put(location, new HashMap<String, Object>());
            return false;
        }
    }

    protected List<WeatherData> getWeatherData(String location){
        List<WeatherData> returnedWeatherDataList;

        if (isGetFromCache(location)) {
            Log.i(TAG, "Getting data from cache.");
            returnedWeatherDataList = (ArrayList<WeatherData>) mCache.get(location).get("WEATHER_DATA");
        }
        else {
            Log.i(TAG, "Fetching new data.");
            returnedWeatherDataList = Utils.getResults(location);
            Log.i(TAG, "Caching the fetched data.");
            mCache.get(location).put("WEATHER_DATA", returnedWeatherDataList);
            mCache.get(location).put("LAST_TIME_CHECKED", new Date().getTime());
        }
        return returnedWeatherDataList;
    }
}
