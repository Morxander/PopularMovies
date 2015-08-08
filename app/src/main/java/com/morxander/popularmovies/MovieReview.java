package com.morxander.popularmovies;

/**
 * Created by morxander on 8/7/15.
 */
public class MovieReview {
    String author,content,url;

    public MovieReview(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }
}
