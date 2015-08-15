package com.morxander.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by morxander on 6/26/15.
 */
public class Movie  implements Parcelable {
    boolean isAdult,isVideo;

    private int movieId,voteAverage,voteCount,duration;
    private double popularity;
    private String backdropPath,language,originalTitle,overview,releaseDate,posterPath,title;
    ArrayList<MovieVideo> videos = new ArrayList<MovieVideo>();
    ArrayList<MovieReview> reviews = new ArrayList<MovieReview>();

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void addToVideos(MovieVideo video) { this.videos.add(video); }

    public void addToReviews(MovieReview review) { this.reviews.add(review); }

    public ArrayList<MovieVideo> getVideos() {
        return videos;
    }

    public ArrayList<MovieReview> getReviews() {
        return reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

