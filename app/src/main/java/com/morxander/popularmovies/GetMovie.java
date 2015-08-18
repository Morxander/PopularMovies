package com.morxander.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by morxander on 6/26/15.
 */
public class GetMovie extends AsyncTask<Object, Void, String[]> {
    private DeatilsJobsInterface listener;
    @Override
    protected String[] doInBackground(Object... params) {
        if (params.length == 0) {
            return null;
        }
        // String Array will contain the moview details ,videos , reviews json
        String[] jsonValues = new String[3];
        String movieId = (String) params[0];
        listener = (DeatilsJobsInterface) params[1];
        try {
            // Showing the loading dialog
            listener.showLoading();
            // Getting the movie Deatils
            Uri movieDetails = Uri.parse(Utils.movieApiUrl).buildUpon()
                    .appendPath(movieId)
                    .appendQueryParameter(Utils.apiVarKey, Utils.apiVarValue)
                    .build();
            // Getting the movie videos
            Uri movieVideos = Uri.parse(Utils.movieApiUrl).buildUpon()
                    .appendPath(movieId)
                    .appendPath(Utils.movieVideosUrl)
                    .appendQueryParameter(Utils.apiVarKey, Utils.apiVarValue)
                    .build();
            // Getting the movie reviews
            Uri movieReviews = Uri.parse(Utils.movieApiUrl).buildUpon()
                    .appendPath(movieId)
                    .appendPath(Utils.movieReviewsUrl)
                    .appendQueryParameter(Utils.apiVarKey, Utils.apiVarValue)
                    .build();
            // Appending the json response to the array
            jsonValues[0] = Utils.getLinkContent(movieDetails);
            jsonValues[1] = Utils.getLinkContent(movieVideos);
            jsonValues[2] = Utils.getLinkContent(movieReviews);
            if(!jsonValues[0].isEmpty()) {
                return jsonValues;
            }else {
                listener.dismissLoading();
                return null;
            }
        } catch (Exception e) {
            listener.onConnectionError();
            listener.dismissLoading();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String[] jsonValues) {
        try {
            if(jsonValues != null && !jsonValues[0].isEmpty()) {
                // The movie Deails
                JSONObject movieObject = new JSONObject(jsonValues[0]);
                Movie movie = new Movie();
                movie.setMovieId(movieObject.getInt("id"));
                movie.setTitle(movieObject.getString("title"));
                movie.setReleaseDate(movieObject.getString("release_date"));
                movie.setDuration(movieObject.getInt("runtime"));
                movie.setVoteAverage(movieObject.getInt("vote_average"));
                movie.setPosterPath(movieObject.getString("poster_path"));
                movie.setOverview(movieObject.getString("overview"));
                listener.setViewVisible();
                // The Movie Videos
                JSONObject movieVideos = new JSONObject(jsonValues[1]);
                JSONArray videos = movieVideos.getJSONArray("results");
                if (videos.length() > 0) {
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject videoDeatils = videos.getJSONObject(i);
                        String site = videoDeatils.getString("site");
                        String key = videoDeatils.getString("key");
                        MovieVideo video = new MovieVideo(site, key);
                        movie.addToVideos(video);
                    }
                }
                // The Movie Reviews
                JSONObject movieReviews = new JSONObject(jsonValues[2]);
                JSONArray reviews = movieReviews.getJSONArray("results");
                if (reviews.length() > 0) {
                    for (int i = 0; i < reviews.length(); i++) {
                        JSONObject reviewDeatils = reviews.getJSONObject(i);
                        String author = reviewDeatils.getString("author");
                        String content = reviewDeatils.getString("content");
                        String url = reviewDeatils.getString("url");
                        MovieReview review = new MovieReview(author, content, url);
                        movie.addToReviews(review);
                    }
                }
                listener.setMovie(movie);
                listener.setValues();

            }
            listener.dismissLoading();
        } catch (JSONException e) {
            e.printStackTrace();
            listener.dismissLoading();
        }
        super.onPostExecute(jsonValues);
    }
}
