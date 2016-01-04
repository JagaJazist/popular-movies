package com.example.android.myappportfolio.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class PopularMovies extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}