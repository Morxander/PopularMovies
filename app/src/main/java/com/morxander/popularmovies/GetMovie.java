package com.morxander.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by morxander on 6/26/15.
 */
public class GetMovie extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        String movieId = params[0];

        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are avaiable at OWM's forecast API page, at
        Uri builtUri = Uri.parse(Settings.movieApiUrl).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(Settings.apiVarKey, Settings.apiVarValue)
                .build();
        return Settings.getLinkContent(builtUri);
    }

    @Override
    protected void onPostExecute(String jsonString) {
        try {
            if(jsonString != null) {
                JSONObject movieObject = new JSONObject(jsonString);
                MovieDetails.movie.setTitle(movieObject.getString("title"));
                MovieDetails.movie.setReleaseDate(movieObject.getString("release_date"));
                MovieDetails.movie.setDuration(movieObject.getInt("runtime"));
                MovieDetails.movie.setVoteAverage(movieObject.getInt("vote_average"));
                MovieDetails.movie.setPosterPath(movieObject.getString("poster_path"));
                MovieDetails.movie.setOverview(movieObject.getString("overview"));
                MovieDetails.movie_duration.setText(movieObject.getInt("runtime") + "M");
                MovieDetails.movie_duration.setVisibility(View.VISIBLE);
                MovieDetails.movie_year.setVisibility(View.VISIBLE);
                MovieDetails.movie_rate.setVisibility(View.VISIBLE);
                MovieDetails.ratingBar.setVisibility(View.VISIBLE);
                MovieDetails.movie_duration.setVisibility(View.VISIBLE);
                MovieDetails.black_line.setVisibility(View.VISIBLE);
                MovieDetails.movie_overview.setVisibility(View.VISIBLE);
                MovieDetails.progress.dismiss();
            }else{
                Log.v(Settings.LOG_TAG,"Nullllllllllllllllllllllll");
            }
//            MovieDetails.PlaceholderFragment.setValues();
        } catch (JSONException e) {
            MovieDetails.progress.dismiss();
            e.printStackTrace();
        }
        super.onPostExecute(jsonString);
    }
}
