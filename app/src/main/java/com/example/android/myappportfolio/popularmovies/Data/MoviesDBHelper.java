package com.example.android.myappportfolio.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 899;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.MOVIE_PATH + "(" + MovieContract.MovieEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_IS_POPULAR + " INTEGER," +
                MovieContract.MovieEntry.COLUMN_IS_RATED + " INTEGER," +
                MovieContract.MovieEntry.COLUMN_IS_FAVOURITE + " INTEGER," +
                "UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT IGNORE)";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " +
                MovieContract.ReviewEntry.REVIEW_PATH + "(" + MovieContract.ReviewEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, "
                + MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, "
                + MovieContract.ReviewEntry.COLUMN_URL + " TEXT NOT NULL, "
                + " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES "
                + MovieContract.MovieEntry.MOVIE_PATH + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")"
                + " UNIQUE (" + MovieContract.ReviewEntry.COLUMN_REVIEW_ID + ") ON CONFLICT IGNORE)";

        final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE " +
                MovieContract.VideoEntry.VIDEO_PATH + "(" + MovieContract.VideoEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.VideoEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_VIDEO_ID + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_ISO + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_KEY + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_SITE + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_SIZE + " TEXT NOT NULL, "
                + MovieContract.VideoEntry.COLUMN_TYPE + " TEXT NOT NULL, "
                + " FOREIGN KEY (" + MovieContract.VideoEntry.COLUMN_MOVIE_ID + ") REFERENCES "
                + MovieContract.MovieEntry.MOVIE_PATH + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")"
                + " UNIQUE (" + MovieContract.VideoEntry.COLUMN_VIDEO_ID + ") ON CONFLICT IGNORE)";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.MOVIE_PATH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.REVIEW_PATH);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieContract.MovieEntry.MOVIE_PATH + "'");
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieContract.ReviewEntry.REVIEW_PATH + "'");
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieContract.VideoEntry.VIDEO_PATH + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}