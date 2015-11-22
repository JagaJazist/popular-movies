package com.example.android.myappportfolio.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.myappportfolio.popularmovies.Models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MoviesGridFragment extends Fragment {

    private static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();

    private MovieGridAdapter<Movie> movieGridAdapter;
    private ArrayList<Movie> moviesList;

    public MoviesGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            moviesList = new ArrayList<>();
        }
        else {
            moviesList = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", moviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                new FetchMovies().execute(MoviesSortingType.POPULAR);
                return true;
            case R.id.rating:
                new FetchMovies().execute(MoviesSortingType.RATING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new FetchMovies().execute();

        View rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        movieGridAdapter = new MovieGridAdapter<>(getActivity(), moviesList);
        GridView grid = (GridView) rootView.findViewById(R.id.gridView);
        grid.setAdapter(movieGridAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, moviesList.get(position));
                Log.d(LOG_TAG, moviesList.get(position).toString());
                startActivity(intent);
            }
        });
        return rootView;
    }


    public class FetchMovies extends AsyncTask<MoviesSortingType, Void, Movie[]> {
        private static final String API_KEY = BuildConfig.MOVIE_DB_MAP_API_KEY;
        private final String LOG_TAG = FetchMovies.class.getSimpleName();


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

                Log.d(LOG_TAG, url.toString());

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
            movieGridAdapter.clear();
            if (result != null) {
                for (Movie movie : result) {
                    movieGridAdapter.add(movie);
                }
            }
        }
    }
}
