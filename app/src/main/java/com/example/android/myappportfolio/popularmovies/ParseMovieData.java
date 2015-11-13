package com.example.android.myappportfolio.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ParseMovieData {

    public static String getUrlForMoviePosterFromJson(String moviesJsonStr, int movieIndex)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject movie = results.getJSONObject(movieIndex);
        return movie.getString("poster_path");
    }

    public static String getPosterUrl(String moviesJsonStr, int movieIndex) {
        String posterFileName = "";
        String url = "";

        try {
            posterFileName = getUrlForMoviePosterFromJson(moviesJsonStr, movieIndex);
        }
        catch (JSONException e) {
            Log.e("Ololo", "Couldn't parse JSON: " + e.getMessage());
        }

        url = "http://image.tmdb.org/t/p/w500/" + posterFileName;

        Log.d("OLOLO", url);

        return url;
    }

}
