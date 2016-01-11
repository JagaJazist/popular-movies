package com.example.android.myappportfolio.popularmovies.Models;

public class Video {

    public String videoId;
    public String movieId;
    public String iso_639_1;
    public String key;
    public String name;
    public String site;
    public String size;
    public String type;

    public Video(String videoId,
                 String movieId,
                 String key,
                 String iso_639_1,
                 String name,
                 String site,
                 String size,
                 String type) {
        this.videoId = videoId;
        this.movieId = movieId;
        this.key = key;
        this.iso_639_1 = iso_639_1;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }
}
