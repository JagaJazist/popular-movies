package com.example.android.myappportfolio.popularmovies;

import android.util.Log;

import com.example.android.myappportfolio.popularmovies.Models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ParseMovieData {
    private static final String LOG_TAG = ParseMovieData.class.getSimpleName();

    public static Movie[] getMoviesFromJson(String moviesJson) {
        int moviesNumInJson = 20;
        List<Movie> moviesList = new ArrayList();
        for (int i = 0; i < moviesNumInJson; i++) {
            try {
                moviesList.add(getUrlForMoviePosterFromJson(moviesJson, i));
            }
            catch (JSONException e) {
                Log.d(LOG_TAG, "Couldn't parse JSON: " + e.getMessage());
            }
        }
        Movie[] result = moviesList.toArray(new Movie[moviesList.size()]);
        return result;
    }

//    private JSONObject getMovieJSONObject(String moviesJson, int movieIndex)
//            throws JSONException {
//        JSONObject jsonObject = new JSONObject(moviesJson);
//        JSONArray results = jsonObject.getJSONArray("results");
//        JSONObject movie = results.getJSONObject(movieIndex);
//        return movie;
//    }

    private static Movie getUrlForMoviePosterFromJson(String moviesJson, int movieIndex)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(moviesJson);
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject movieJSONObject = results.getJSONObject(movieIndex);
        Movie movie = new Movie(
                "http://image.tmdb.org/t/p/w500/" + movieJSONObject.getString("poster_path"),
                movieJSONObject.getString("title"),
                movieJSONObject.getString("release_date"),
                movieJSONObject.getString("vote_average"),
                movieJSONObject.getString("overview"));
        return movie;
    }
}
