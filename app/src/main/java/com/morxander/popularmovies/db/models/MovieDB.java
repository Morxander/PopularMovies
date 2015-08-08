package com.morxander.popularmovies.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.morxander.popularmovies.Movie;

/**
 * Created by morxander on 8/8/15.
 * This the movie Model for the DataBase
 */
@Table(name = "Categories")
public class MovieDB extends Model {
    @Column(name = "movie_pid")
    int movie_pid;
    @Column(name = "duration")
    int duration;
    @Column(name = "title")
    String title;
    @Column(name = "poster")
    String poster;
    @Column(name = "relase_date")
    String relase_date;
    @Column(name = "overview")
    String overview;
    @Column(name = "popularity")
    double popularity;
    @Column(name = "vote_average")
    int vote_average;
    @Column(name = "is_fav")
    boolean is_fav;

    public MovieDB() {
    }

    public MovieDB(int id, String title, String poster, String relaseDate, String overView, double popularity, int voteAverage, boolean isFav) {
        this.movie_pid = id;
        this.title = title;
        this.poster = poster;
        this.relase_date = relaseDate;
        this.overview = overView;
        this.popularity = popularity;
        this.vote_average = voteAverage;
        this.is_fav = isFav;
    }

    public MovieDB(Movie movie){
        this.movie_pid = movie.getMovieId();
        this.title = movie.getOriginalTitle();
        this.poster = movie.getPosterPath();
        this.relase_date = movie.getReleaseDate();
        this.overview = movie.getOverview();
        this.popularity = movie.getPopularity();
        this.vote_average = movie.getVoteAverage();
        this.is_fav = true;
    }

    public int getMovie_pid() {
        return movie_pid;
    }

    public int getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getRelase_date() {
        return relase_date;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVote_average() {
        return vote_average;
    }

    public boolean is_fav() {
        return is_fav;
    }

    public void setMovie_pid(int movie_pid) {
        this.movie_pid = movie_pid;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setRelase_date(String relase_date) {
        this.relase_date = relase_date;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }

    public void setIs_fav(boolean is_fav) {
        this.is_fav = is_fav;
    }
}
