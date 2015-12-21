package com.example.android.myappportfolio.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.myappportfolio.popularmovies.Data.MovieContract;
import com.example.android.myappportfolio.popularmovies.Models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchMovies extends AsyncTask<MoviesSortingType, Void, Movie[]> {
    private static final String API_KEY = BuildConfig.MOVIE_DB_MAP_API_KEY;
    private final String LOG_TAG = FetchMovies.class.getSimpleName();
    private final Context context;

    public FetchMovies(Context context) {
        this.context = context;
    }


    @Override
    protected Movie[] doInBackground(MoviesSortingType... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJson;
        String sortingParameter = "popularity.desc";

        if (params.length > 0) {
            switch (params[0]) {
                case POPULAR:
                    sortingParameter = "popularity.desc";
                    break;
                case RATING:
                    sortingParameter = "vote_average.desc";
                    break;
            }
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("sort_by", sortingParameter)
                .appendQueryParameter("api_key", API_KEY)
                .fragment("section-name");
        String uri = builder.build().toString();

        try {
            URL url = new URL(uri);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJson = buffer.toString();

            Movie[] result = ParseMovieData.getMoviesFromJson(moviesJson);

            return result;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    @Override
    public void onPostExecute(Movie[] result) {
        super.onPostExecute(result);
        if (result != null) {
            ContentValues[] cvArray = new ContentValues[result.length];
            for (int i = 0; i < result.length; i++) {
                ContentValues cv = new ContentValues();
                cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, result[i].movie_id);
                cv.put(MovieContract.MovieEntry.COLUMN_TITLE, result[i].title);
                cv.put(MovieContract.MovieEntry.COLUMN_POSTER, result[i].posterUrl);
                cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, result[i].releaseDate);
                cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, result[i].voteAverage);
                cv.put(MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS, result[i].plotSynopsis);
                cvArray[i] = cv;
            }
            context.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }
    }
}
