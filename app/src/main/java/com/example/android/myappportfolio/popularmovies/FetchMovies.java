package com.example.android.myappportfolio.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.myappportfolio.popularmovies.Models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchMovies extends AsyncTask<MoviesSortingType, Void, Movie[]> {
    private static final String API_KEY = BuildConfig.MOVIE_DB_MAP_API_KEY;
    private MoviesGridFragment moviesGridFragment;
    private final String LOG_TAG = FetchMovies.class.getSimpleName();

    public FetchMovies(MoviesGridFragment moviesGridFragment) {
        this.moviesGridFragment = moviesGridFragment;
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
        moviesGridFragment.movieGridAdapter.clear();
        if (result != null) {
            for (Movie movie : result) {
                moviesGridFragment.movieGridAdapter.add(movie);
            }
        }
    }
}
