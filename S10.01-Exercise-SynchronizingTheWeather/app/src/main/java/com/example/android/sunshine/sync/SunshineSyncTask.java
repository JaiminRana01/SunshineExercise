package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  COMPLETED (1) Create a class called SunshineSyncTask
public class SunshineSyncTask {
    //  COMPLETED (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
    synchronized public static void syncWeather(Context context) {
//      COMPLETED (3) Within syncWeather, fetch new weather data
        try {
            URL weatherRequestUrl = NetworkUtils.getUrl(context);

            /* Use the url to retrieve the JSON */
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            /* parse the JSON into a list of weather values */
            ContentValues[] weatherValues = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherValues != null && weatherValues.length != 0) {
                /* get a handle on the content resolver to delete and insert data */
                ContentResolver sunshineContentResolver = context.getContentResolver();

                //      COMPLETED (4) If we have valid results, delete the old data and insert the new
                sunshineContentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null);

                sunshineContentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}