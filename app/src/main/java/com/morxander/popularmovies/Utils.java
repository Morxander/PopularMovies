package com.morxander.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by morxander on 6/26/15.
 */
public class Utils {
    static public String baseApiUrl = "http://api.themoviedb.org/3/discover/movie";
    static public String movieApiUrl = "http://api.themoviedb.org/3/movie/";
    static public String sortingVarKey = "sort_by";
    static public String sortingVarValue = "popularity" + "." + "desc";
    static public String apiVarKey = "api_key";
    static public String apiVarValue = "8f3eba74c34816a84b86175fb6e12ec0";
    static public String imageBaseURL = "http://image.tmdb.org/t/p/";
    static public String imageSize92 = "w92";
    static public String imageSize154 = "w154";
    static public String imageSize185 = "w185";
    static public String imageSize342 = "w342";
    static public String LOG_TAG = "P_Movies";
    static public String image_not_found = "http://i.imgur.com/ey9lLmm.png";
    static InputStream inputStream;
    static StringBuffer buffer;
    static HttpURLConnection urlConnection;
    static BufferedReader reader;
    static String forecastJsonStr;

    public static String getLinkContent(Uri builtUri)
    {
        try {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            urlConnection = null;
            reader = null;

            // Will contain the raw JSON response as a string.
            forecastJsonStr = null;
            URL url = new URL(builtUri.toString());

            // Create the request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return forecastJsonStr;
    }

}
