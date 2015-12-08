package com.example.android.myappportfolio.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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

import com.example.android.myappportfolio.popularmovies.Data.MovieContract;
import com.example.android.myappportfolio.popularmovies.Data.MoviesLoader;
import com.example.android.myappportfolio.popularmovies.Models.Movie;

import java.util.ArrayList;


public class MoviesGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();

    MovieGridAdapter<Movie> movieGridAdapter;
    private ArrayList<Movie> moviesList;
    private static final int CURSOR_LOADER_ID = 0;

    public MoviesGridFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Cursor c =
                getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieEntry._ID},
                        null,
                        null,
                        null);
        if (c.getCount() == 0){
            fetchMovies();
        }
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void fetchMovies() {

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
                new FetchMovies(this).execute(MoviesSortingType.POPULAR);
                return true;
            case R.id.rating:
                new FetchMovies(this).execute(MoviesSortingType.RATING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new FetchMovies(this).execute();

        View rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        movieGridAdapter = new MovieGridAdapter<>(getActivity(), moviesList);
        GridView grid = (GridView) rootView.findViewById(R.id.gridView);
        grid.setAdapter(movieGridAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, moviesList.get(position));
                startActivity(intent);
            }
        });
        return rootView;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        // Создаем новый CursorLoader с нужными параметрами
        Loader<String> mLoader = null;
        // условие можно убрать, если вы используете только один загрузчик
        if (id == LOADER_ID) {
            mLoader = new MoviesLoader(this, args);
            Log.d(TAG, "onCreateLoader");
        }
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
