package com.morxander.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.morxander.popularmovies.db.models.MovieDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by morxander on 6/26/15.
 */
public class GetMovies extends AsyncTask<String, Void, String> {
    String LOG_TAG = "Morad";

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String sortingOrder = params[0];
        String sortingCriteria = params[1];
        String linkParameter = sortingCriteria + "." + sortingOrder;
        // If the sortingCriteria is not favorites movies
        if (!sortingCriteria.equals("fav")) {
            Uri builtUri = Uri.parse(Utils.baseApiUrl).buildUpon()
                    .appendQueryParameter(Utils.sortingVarKey, linkParameter)
                    .appendQueryParameter(Utils.apiVarKey, Utils.apiVarValue)
                    .build();
            String response;
            try {
                response = Utils.getLinkContent(builtUri);
                return response;
            } catch (Exception e) {
                MainActivity.PlaceholderFragment.toast.setText("Connection Error");
                MainActivity.PlaceholderFragment.toast.setDuration(Toast.LENGTH_SHORT);
                MainActivity.PlaceholderFragment.toast.show();
                return null;
            }
        } else {
            // If the user selected favorites movies we will return 'fav' to handle it in the @onPostExecute function
            return "fav";
        }

    }

    @Override
    protected void onPostExecute(String jsonString) {
        super.onPostExecute(jsonString);
        try {
            // If it's not favorite movies and the jsonstring not null
            if (jsonString != null && !jsonString.equals("fav")) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");
                MainActivity.imagesUrls.clear();
                MainActivity.moviesArrayList.clear();
                for (int i = 0; i <= moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    Movie movieItem = new Movie();
                    movieItem.setTitle(movie.getString("title"));
                    movieItem.setMovieId(movie.getInt("id"));
                    movieItem.setAdult(movie.getBoolean("adult"));
                    movieItem.setBackdropPath(movie.getString("backdrop_path"));
                    movieItem.setOriginalTitle(movie.getString("original_title"));
                    movieItem.setLanguage(movie.getString("original_language"));
                    if (movie.getString("overview") == "null") {
                        movieItem.setOverview(MainActivity.no_overview);
                    } else {
                        movieItem.setOverview(movie.getString("overview"));
                    }
                    if (movie.getString("release_date") == "null") {
                        movieItem.setReleaseDate("Unknown Release Date");
                    } else {
                        movieItem.setReleaseDate(movie.getString("release_date"));
                    }
                    movieItem.setPopularity(movie.getDouble("popularity"));
                    movieItem.setVoteAverage(movie.getInt("vote_average"));
                    movieItem.setPosterPath(movie.getString("poster_path"));
                    if (movie.getString("poster_path") == "null") {
                        MainActivity.imagesUrls.add(Utils.image_not_found);
                        movieItem.setPosterPath(Utils.image_not_found);
                    } else {
                        MainActivity.imagesUrls.add(Utils.imageBaseURL + Utils.imageSize185 + movie.getString("poster_path"));
                    }
                    MainActivity.moviesArrayList.add(movieItem);
                    movieItem = null;
                    MainActivity.PlaceholderFragment.imageAdapter.notifyDataSetChanged();
                }
             // If the user selected favorites movies
            } else if (jsonString == "fav") {
                List<MovieDB> movieDB = new Select().from(MovieDB.class).execute();
                MainActivity.imagesUrls.clear();
                MainActivity.moviesArrayList.clear();
                for (int i = 0; i < movieDB.size(); i++) {
                    Movie movieItem = new Movie();
                    MovieDB movie = movieDB.get(i);
                    movieItem.setTitle(movie.getTitle());
                    movieItem.setMovieId(movie.getMovie_pid());
                    movieItem.setBackdropPath(movie.getPoster());
                    if (movie.getOverview() == "null") {
                        movieItem.setOverview(MainActivity.no_overview);
                    } else {
                        movieItem.setOverview(movie.getOverview());
                    }
                    if (movie.getRelase_date() == "null") {
                        movieItem.setReleaseDate("Unknown Release Date");
                    } else {
                        movieItem.setReleaseDate(movie.getRelase_date());
                    }
                    movieItem.setPopularity(movie.getPopularity());
                    movieItem.setVoteAverage(movie.getVote_average());
                    movieItem.setPosterPath(movie.getPoster());
                    if (movie.getPoster() == "null") {
                        MainActivity.imagesUrls.add(Utils.image_not_found);
                        movieItem.setPosterPath(Utils.image_not_found);
                    } else {
                        MainActivity.imagesUrls.add(Utils.imageBaseURL + Utils.imageSize185 + movie.getPoster());
                    }
                    MainActivity.moviesArrayList.add(movieItem);
                    movieItem = null;
                    MainActivity.PlaceholderFragment.imageAdapter.notifyDataSetChanged();
                }
            } else {
                MainActivity.PlaceholderFragment.toast.setText("Something Wrong Happend");
                MainActivity.PlaceholderFragment.toast.setDuration(Toast.LENGTH_SHORT);
                MainActivity.PlaceholderFragment.toast.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}