package com.morxander.popularmovies;

import android.view.View;

/**
 * Created by morxander on 8/18/15.
 */
public interface DeatilsJobsInterface {
    // Method to init the UI Components
    void initComponents(View rootView);
    // Method to set the poster till the movie loaded
    void setPoster(View rootView);
    // Method to set the values of the UI Components
    void setValues();
    // Method to show the loading dialog
    void showLoading();
    // Method to dismiss the loading dialog
    void dismissLoading();
    // Method to set the mobie object
    void setMovie(Movie movie);
    // Method to set the UI Components visible
    void setViewVisible();
    // Method to set the UI Components invisible
    void setViewInvisible();
    // Method to show the connection error toast
    void onConnectionError();
}
