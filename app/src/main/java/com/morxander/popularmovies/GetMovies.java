package com.morxander.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by morxander on 6/26/15.
 */
public class GetMovies extends AsyncTask<String, Void, String> {
    String LOG_TAG = "Morad";

    @Override
    protected String doInBackground(String... params) {
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        String sortingOrder = params[0];
        String sortingCriteria = params[1];
        String linkParameter = sortingCriteria + "." + sortingOrder;


        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are avaiable at OWM's forecast API page, at
        Uri builtUri = Uri.parse(Settings.baseApiUrl).buildUpon()
                .appendQueryParameter(Settings.sortingVarKey, linkParameter)
                .appendQueryParameter(Settings.apiVarKey, Settings.apiVarValue)
                .build();
        return Settings.getLinkContent(builtUri);
    }

    @Override
    protected void onPostExecute(String jsonString) {
        super.onPostExecute(jsonString);
        try {
            JSONObject moviesObject = new JSONObject(jsonString);
            JSONArray moviesArray = moviesObject.getJSONArray("results");
            MainActivity.imagesUrls.clear();
            MainActivity.moviesArrayList.clear();
//            MainActivity.imageAdapter = new MainActivity.ImageAdapter(MainActivity.);
            for (int i = 0; i <= moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
                Movie movieItem = new Movie();
                movieItem.setTitle(movie.getString("title"));
                movieItem.setMovieId(movie.getInt("id"));
                movieItem.setAdult(movie.getBoolean("adult"));
                movieItem.setBackdropPath(movie.getString("backdrop_path"));
                movieItem.setOriginalTitle(movie.getString("original_title"));
                movieItem.setLanguage(movie.getString("original_language"));
                movieItem.setOverview(movie.getString("overview"));
                movieItem.setReleaseDate(movie.getString("release_date"));
                movieItem.setPopularity(movie.getDouble("popularity"));
                movieItem.setVoteAverage(movie.getInt("vote_average"));
                movieItem.setPosterPath(movie.getString("poster_path"));
                MainActivity.moviesArrayList.add(movieItem);
                movieItem = null;
                Log.e(LOG_TAG, movie.getString("poster_path"));
                if (movie.getString("poster_path") == "null") {
                    MainActivity.imagesUrls.add("http://www.jordans.com/~/media/Jordans%20Redesign/No-image-found.jpg");
                } else {
                    MainActivity.imagesUrls.add(Settings.imageBaseURL + Settings.imageSize154 + movie.getString("poster_path"));
                }
                MainActivity.progress.dismiss();
                MainActivity.imageAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            MainActivity.progress.dismiss();
            e.printStackTrace();
        }
        Log.e("Morad", jsonString);
    }
}