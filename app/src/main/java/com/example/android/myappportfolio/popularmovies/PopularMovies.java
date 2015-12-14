package com.example.android.myappportfolio.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class PopularMovies extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}