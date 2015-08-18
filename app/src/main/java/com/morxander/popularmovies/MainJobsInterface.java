package com.morxander.popularmovies;

/**
 * Created by morxander on 8/15/15.
 */
public interface MainJobsInterface {
    // Method to show the connection error toast
    void onConnectionError();
    // Method to show the main error toast
    void somethingWrongHappened();
    // Method to clear the image urls arraylist
    void clearImagesUrls();
    // Method to add item to the image urls arraylist
    void addToImagesUrls(String url);
    // Method to clear the movies arraylist
    void clearMoviesList();
    // Method to add to the movies arraylist
    void addToMoviesList(Movie movie);
    // It explains itself
    void notifyAdapter();
}
