package com.example.jules.weatherapplication.jsonweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;

import com.example.jules.weatherapplication.jsonweather.JsonWeather;
import com.example.jules.weatherapplication.jsonweather.Main;
import com.example.jules.weatherapplication.jsonweather.Sys;
import com.example.jules.weatherapplication.jsonweather.Wind;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {

    /**
     * Used for logging purposes.
     */
    private final String TAG =
            this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
            throws IOException {

        // Create a JsonReader for the inputStream.
        try (JsonReader reader =
                     new JsonReader(new InputStreamReader(inputStream,
                             "UTF-8"))) {
            // Log.d(TAG, "Parsing the results returned as an array");

            // Handle the array returned from the Weather Service.
            return parseJsonWeathers(reader);
        }
    }

//    /**
//     * Parse a Json stream and convert it into a List of JsonWeather
//     * objects.
//     */
//    public List<JsonWeather> parseWeatherServiceResults(JsonReader reader)
//            throws IOException {
//
//        reader.beginArray();
//        try {
//            // If the weather wasn't expanded return null;
//            if (reader.peek() == JsonToken.END_ARRAY)
//                return null;
//
//            // Create a JsonWeather object for each element in the
//            // Json array.
//            return parseJsonWeathers(reader);
//        } finally {
//            reader.endArray();
//        }
//    }

    public List<JsonWeather> parseJsonWeathers(JsonReader reader)
            throws IOException {
        reader.beginObject();

        List<JsonWeather> jsonWeathers = new ArrayList<JsonWeather>();

        JsonWeather jsonWeather = new JsonWeather();

        try {
            outerloop:
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case JsonWeather.name_JSON:
                        jsonWeather.setName(reader.nextString());
                        break;
                    case JsonWeather.wind_JSON:
                        jsonWeather.setWind(parseWeatherWind(reader));
                        break;
                    case JsonWeather.main_JSON:
                        jsonWeather.setMain(parseWeatherMain(reader));
                        break;
                    case JsonWeather.sys_JSON:
                        jsonWeather.setSys(parseWeatherSys(reader));
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        } finally {
            reader.endObject();
        }

        jsonWeathers.add(jsonWeather);

        return jsonWeathers;
    }

//    /**
//     * Parse a Json stream and convert it into a List of JsonWeather
//     * objects.
//     */
//    public List<JsonWeather> parseWeatherLongFormArray(JsonReader reader)
//            throws IOException {
//
//        // Log.d(TAG, "reading lfs elements");
//
//        reader.beginArray();
//
//        try {
//            List<JsonWeather> weathers = new ArrayList<JsonWeather>();
//
//            while (reader.hasNext())
//                weathers.add(parseWeather(reader));
//
//            return weathers;
//        } finally {
//            reader.endArray();
//        }
//    }

    /**
     * Parse a Json stream and return a JsonWeather object.
     */
//    public JsonWeather parseWeather(JsonReader reader, WeatherJson)
//            throws IOException {
//
//        reader.beginObject();
//
//        JsonWeather weather = new JsonWeather();
//
//        try {
//            while (reader.hasNext()) {
//                String name = reader.nextName();
//                switch (name) {
//                    case JsonWeather.lf_JSON:
//                        weather.setLongForm(reader.nextString());
//                        // Log.d(TAG, "reading lf " + weather.getLongForm());
//                        break;
//                    case JsonWeather.freq_JSON:
//                        weather.setFreq(reader.nextInt());
//                        // Log.d(TAG, "reading freq " + weather.getFreq());
//                        break;
//                    case JsonWeather.since_JSON:
//                        weather.setSince(reader.nextInt());
//                        // Log.d(TAG, "reading since " + weather.getSince());
//                        break;
//                    default:
//                        reader.skipValue();
//                        // Log.d(TAG, "ignoring " + name);
//                        break;
//                }
//            }
//        } finally {
//            reader.endObject();
//        }
//        return weather;
//    }

    public Main parseWeatherMain(JsonReader reader) throws IOException {
        reader.beginObject();

        Main main = new Main();
        try {
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case Main.temp_JSON:
                        main.setTemp(reader.nextDouble());
                        break;
                    case Main.humidity_JSON:
                        main.setHumidity(reader.nextLong());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        } finally {
            reader.endObject();
        }
        return main;
    }

    public Wind parseWeatherWind(JsonReader reader) throws IOException {
        reader.beginObject();

        Wind wind = new Wind();
        try {
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case Wind.speed_JSON:
                        wind.setSpeed(reader.nextDouble());
                        break;
                    case Wind.deg_JSON:
                        wind.setDeg(reader.nextDouble());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        } finally {
            reader.endObject();
        }
        return wind;
    }

    public Sys parseWeatherSys(JsonReader reader) throws IOException {
        reader.beginObject();

        Sys sys = new Sys();
        try {
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case Sys.sunrise_JSON:
                        sys.setSunrise(reader.nextLong());
                        break;
                    case Sys.sunset_JSON:
                        sys.setSunset(reader.nextLong());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
        } finally {
            reader.endObject();
        }
        return sys;
    }
}
