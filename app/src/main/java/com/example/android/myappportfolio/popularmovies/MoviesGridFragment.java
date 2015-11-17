package com.example.android.myappportfolio.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MoviesGridFragment extends Fragment {

    private MovieGridAdapter<String> movieGridAdapter;

    public MoviesGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new FetchMovies().execute();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false);
        String[] values = new String[] {  };
        List<String> movies = new ArrayList<>(Arrays.asList(values));
        
        movieGridAdapter = new MovieGridAdapter<>(getActivity(), movies);
        GridView grid = (GridView) rootView.findViewById(R.id.gridView);
        grid.setAdapter(movieGridAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, position);
                startActivity(intent);
            }
        });
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class FetchMovies extends AsyncTask<Void, Void, String[]> {
        private static final String KEY = BuildConfig.MOVIE_DB_MAP_API_KEY;


        @Override
        protected String[] doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJson = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]
                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                String apiKey = "&api_key=" + BuildConfig.MOVIE_DB_MAP_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

//                Log.d("ololo", url.toString());

                // Create the request to OpenWeatherMap, and open the connection
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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJson = buffer.toString();

                String[] result = ParseMovieData.getPackOfPosterUrls(moviesJson);

                return result;

            } catch (IOException e) {
                Log.e("Ololo", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Ololo", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        public void onPostExecute(String[] result) {
            super.onPostExecute(result);
            movieGridAdapter.clear();
            if (result != null) {
                for (String movie : result) {
                    movieGridAdapter.add(movie);
                }
            }
        }
    }
}
