package com.example.android.myappportfolio.popularmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
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

import java.util.ArrayList;


public class MoviesGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MoviesGridFragment.class.getSimpleName();

    private MovieGridAdapter mMovieGridAdapter;
    private static final int CURSOR_LOADER_ID = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.MOVIE_PATH + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS
    };

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_POSTER = 3;
    static final int COL_MOVIE_RELEASE_DATE = 4;
    static final int COL_MOVIE_VOTE_AVERAGE = 5;
    static final int COL_MOVIE_PLOT_SYNOPSIS = 6;

    public MoviesGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        getMovies(MoviesSortingType.POPULAR);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
        switch (item.getItemId()) {
            case R.id.popular:
                getMovies(MoviesSortingType.POPULAR);
                return true;
            case R.id.rating:
                getMovies(MoviesSortingType.RATING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieGridAdapter = new MovieGridAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        GridView grid = (GridView) rootView.findViewById(R.id.gridView);
        grid.setAdapter(mMovieGridAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class).
                            setData(MovieContract.MovieEntry.buildMoviesUri(cursor.getLong(COL_ID)));
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void getMovies(MoviesSortingType sortingType) {
        FetchMovies fetchMovies = new FetchMovies(getActivity());
        fetchMovies.execute(sortingType);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri moviesUri = MovieContract.MovieEntry.CONTENT_URI;
        Log.d("OLOLO", "onCreateLoader");
//        String sortOrder = MovieContract.MovieEntry._ID + " ASC";
        return new CursorLoader(getActivity(),
                moviesUri,
                MOVIE_COLUMNS,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieGridAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMovieGridAdapter.swapCursor(null);
    }
}
