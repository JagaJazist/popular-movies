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

    public static String getUrlForMoviePosterFromJson(String moviesJsonStr, int movieIndex)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject movie = results.getJSONObject(movieIndex);
        return movie.getString("poster_path");
    }

    public static String[] getPackOfPosterUrls(String moviesJsonStr) {
        List<String> moviesList = new ArrayList();
        for (int i = 0; i < 20; i++) {
            moviesList.add(getPosterUrl(moviesJsonStr, i));
        }
        String[] result = moviesList.toArray(new String[moviesList.size()]);
        return result;
    }

    public static String getPosterUrl(String moviesJsonStr, int movieIndex) {
        String posterFileName = "";
        String url = "";

        try {
            posterFileName = getUrlForMoviePosterFromJson(moviesJsonStr, movieIndex);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Couldn't parse JSON: " + e.getMessage());
        }
        url = "http://image.tmdb.org/t/p/w500/" + posterFileName;

        return url;
    }

}
