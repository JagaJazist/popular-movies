package com.example.android.myappportfolio.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    // Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.
    String posterUrl;
    String title;
    String releaseDate;
    String voteAverage;
    String plotSynopsis;

    public Movie (String posterUrl, String title, String releaseDate, String voteAverage, String plotSynopsis) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    private Movie (Parcel in) {
        this.posterUrl = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readString();
        this.plotSynopsis = in.readString();
    }

    public String toString() {
        String divider = " - ";
        return posterUrl + divider + title + divider + releaseDate + divider + voteAverage + divider + plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
        dest.writeString(plotSynopsis);
    }

    static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
