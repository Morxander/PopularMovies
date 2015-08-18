package com.morxander.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

import com.activeandroid.query.Select;
import com.morxander.popularmovies.db.models.MovieDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by morxander on 6/26/15.
 */
public class GetMovies extends AsyncTask<Object, Void, String> {
    String LOG_TAG = "Morad";
    private MainJobsInterface listener;
    String no_overview;
    @Override
    protected String doInBackground(Object... params) {
        if (params.length == 0) {
            return null;
        }
        String sortingOrder = params[0].toString();
        String sortingCriteria = params[1].toString();
        this.listener = (MainJobsInterface) params[2];
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
                listener.onConnectionError();
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
                no_overview = "No Overview found";
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");
                listener.clearImagesUrls();
                listener.clearMoviesList();
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
                        movieItem.setOverview(no_overview);
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
                        listener.addToImagesUrls(Utils.image_not_found);
                        movieItem.setPosterPath(Utils.image_not_found);
                    } else {
                        listener.addToImagesUrls(Utils.imageBaseURL + Utils.imageSize185 + movie.getString("poster_path"));
                    }
                    listener.addToMoviesList(movieItem);
                    movieItem = null;
                    listener.notifyAdapter();
                }
             // If the user selected favorites movies
            } else if (jsonString == "fav") {
                List<MovieDB> movieDB = new Select().from(MovieDB.class).execute();
                listener.clearImagesUrls();
                listener.clearMoviesList();
                for (int i = 0; i < movieDB.size(); i++) {
                    Movie movieItem = new Movie();
                    MovieDB movie = movieDB.get(i);
                    movieItem.setTitle(movie.getTitle());
                    movieItem.setMovieId(movie.getMovie_pid());
                    movieItem.setBackdropPath(movie.getPoster());
                    if (movie.getOverview() == "null") {
                        movieItem.setOverview(no_overview);
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
                        listener.addToImagesUrls(Utils.image_not_found);
                        movieItem.setPosterPath(Utils.image_not_found);
                    } else {
                        listener.addToImagesUrls(Utils.imageBaseURL + Utils.imageSize185 + movie.getPoster());
                    }
                    listener.addToMoviesList(movieItem);
                    movieItem = null;
                    listener.notifyAdapter();
                }
            } else {
                listener.somethingWrongHappened();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}