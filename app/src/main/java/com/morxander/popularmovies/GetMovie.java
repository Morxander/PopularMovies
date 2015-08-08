package com.morxander.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by morxander on 6/26/15.
 */
public class GetMovie extends AsyncTask<String, Void, String[]> {
    @Override
    protected String[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        // String Array will contain the moview details ,videos , reviews json
        String[] jsonValues = new String[3];
        String movieId = params[0];

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
        return jsonValues;
    }

    @Override
    protected void onPostExecute(String[] jsonValues) {
        try {
            if(jsonValues != null) {
                // The movie Deails
                JSONObject movieObject = new JSONObject(jsonValues[0]);
                MovieDetails.movie.setTitle(movieObject.getString("title"));
                MovieDetails.movie.setReleaseDate(movieObject.getString("release_date"));
                MovieDetails.movie.setDuration(movieObject.getInt("runtime"));
                MovieDetails.movie.setVoteAverage(movieObject.getInt("vote_average"));
                MovieDetails.movie.setPosterPath(movieObject.getString("poster_path"));
                MovieDetails.movie.setOverview(movieObject.getString("overview"));
                MovieDetails.movie_year.setVisibility(View.VISIBLE);
                MovieDetails.movie_rate.setVisibility(View.VISIBLE);
                MovieDetails.ratingBar.setVisibility(View.VISIBLE);
                MovieDetails.black_line.setVisibility(View.VISIBLE);
                MovieDetails.movie_overview.setVisibility(View.VISIBLE);
                // The Movie Videos
                JSONObject movieVideos = new JSONObject(jsonValues[1]);
                JSONArray videos = movieVideos.getJSONArray("results");
                if (videos.length() > 0) {
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject videoDeatils = videos.getJSONObject(i);
                        String site = videoDeatils.getString("site");
                        String key = videoDeatils.getString("key");
                        MovieVideo video = new MovieVideo(site, key);
                        MovieDetails.movie.addToVideos(video);
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
                        MovieDetails.movie.addToReviews(review);
                    }
                }
                MovieDetails.PlaceholderDetailsFragment.setValues();
            }else{
                Log.v(Utils.LOG_TAG,"Failed to get movie");
            }
            MovieDetails.loading.dismiss();
//            MovieDetails.PlaceholderDetailsFragment.setValues();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("morxander",jsonValues[2]);
            MovieDetails.loading.dismiss();
        }
        super.onPostExecute(jsonValues);
    }
}
