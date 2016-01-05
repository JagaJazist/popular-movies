package com.example.android.myappportfolio.popularmovies;

import android.util.Log;

import com.example.android.myappportfolio.popularmovies.Models.Movie;
import com.example.android.myappportfolio.popularmovies.Models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ParseMovieData {
    private static final String LOG_TAG = ParseMovieData.class.getSimpleName();

    public static Review[] getReviewsFromJson(String reviewsJson) {
        List<Review> reviewList = new LinkedList<>();
        int totalResults = 0;
        try {
            JSONObject jsonObject = new JSONObject(reviewsJson);
            totalResults = Integer.parseInt(jsonObject.getString("total_results"));


            for (int i = 0; (i < totalResults) && (i < 5); i++) {
                JSONArray results = jsonObject.getJSONArray("results");
                JSONObject review = results.getJSONObject(i);
                reviewList.add(new Review(
                        review.getString("id"),
                        jsonObject.getString("id"),
                        review.getString("author"),
                        review.getString("content"),
                        review.getString("url")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewList.toArray(new Review[totalResults]);
    }

    public static Movie[] getMoviesFromJson(String moviesJson) {
        int moviesNumInJson = 20;
        List<Movie> moviesList = new ArrayList();
        for (int i = 0; i < moviesNumInJson; i++) {
            try {
                moviesList.add(getMovieFromJson(moviesJson, i));
            }
            catch (JSONException e) {
                Log.d(LOG_TAG, "Couldn't parse JSON: " + e.getMessage());
            }
        }
        Movie[] result = moviesList.toArray(new Movie[moviesList.size()]);
        return result;
    }


    private static Movie getMovieFromJson(String moviesJson, int movieIndex)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(moviesJson);
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject movieJSONObject = results.getJSONObject(movieIndex);
        Movie movie = new Movie(
                movieJSONObject.getString("id"),
                "http://image.tmdb.org/t/p/w342/" + movieJSONObject.getString("poster_path"),
                movieJSONObject.getString("title"),
                movieJSONObject.getString("release_date"),
                movieJSONObject.getString("vote_average"),
                movieJSONObject.getString("overview"));
        return movie;
    }
}
