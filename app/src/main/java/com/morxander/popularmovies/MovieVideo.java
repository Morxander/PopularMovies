package com.morxander.popularmovies;

/**
 * Created by morxander on 8/7/15.
 */
public class MovieVideo {
    String site,Key;

    public MovieVideo(String site, String key) {
        this.site = site;
        Key = key;
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return Key;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setKey(String key) {
        Key = key;
    }
}
