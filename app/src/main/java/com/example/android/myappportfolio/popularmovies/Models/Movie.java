package com.example.android.myappportfolio.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public String movie_id;
    public String posterUrl;
    public String title;
    public String releaseDate;
    public String voteAverage;
    public String plotSynopsis;

    public Movie (String movie_id, String posterUrl, String title, String releaseDate, String voteAverage, String plotSynopsis) {
        this.movie_id = movie_id;
        this.posterUrl = posterUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    private Movie (Parcel in) {
        this.movie_id = in.readString();
        this.posterUrl = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readString();
        this.plotSynopsis = in.readString();
    }

    public String toString() {
        String divider = " - ";
        return movie_id + divider + posterUrl + divider + title + divider + releaseDate + divider + voteAverage + divider + plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movie_id);
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(plotSynopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
