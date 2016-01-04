package com.example.android.myappportfolio.popularmovies.Models;


public class Review {

    public String reviewId;
    public String movieId;
    public String author;
    public String content;
    public String url;

    public Review(String reviewId, String movieId, String author, String content, String url) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;
    }
}
