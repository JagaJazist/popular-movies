package com.example.android.myappportfolio.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.myappportfolio.popularmovies.Data.MovieContract;
import com.example.android.myappportfolio.popularmovies.Models.Movie;
import com.example.android.myappportfolio.popularmovies.Models.Review;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchReviews extends AsyncTask<String, Void, Review[]> {

    private static final String API_KEY = BuildConfig.MOVIE_DB_MAP_API_KEY;
    private final String LOG_TAG = FetchReviews.class.getSimpleName();
    private final Context context;

    public FetchReviews(Context context) {
        this.context = context;
    }

    @Override
    protected Review[] doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String reviewsJson;
        String movieId;

        if (params.length > 0) {
            movieId = params[0];
        } else {
            throw new IllegalStateException("Movie not specified");
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", API_KEY);
        String uri = builder.build().toString();

        try {
            URL url = new URL(uri);

            Log.d("OLOLO", url.toString());

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
            reviewsJson = buffer.toString();

            Review[] result = ParseMovieData.getReviewsFromJson(reviewsJson);

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
    public void onPostExecute(Review[] result) {
        super.onPostExecute(result);
        if (result != null) {
            ContentValues[] cvArray = new ContentValues[result.length];
            for (int i = 0; i < result.length; i++) {
                ContentValues cv = new ContentValues();
                cv.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, result[i].movieId);
                cv.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, result[i].reviewId);
                cv.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, result[i].author);
                cv.put(MovieContract.ReviewEntry.COLUMN_CONTENT, result[i].content);
                cv.put(MovieContract.ReviewEntry.COLUMN_URL, result[i].url);
                cvArray[i] = cv;
            }
            context.getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, cvArray);
        }
    }
}
