package com.example.android.myappportfolio.popularmovies;

 import android.content.ContentValues;
 import android.content.Intent;
 import android.database.Cursor;
 import android.os.Bundle;
 import android.support.v4.app.Fragment;
 import android.support.v4.app.LoaderManager;
 import android.support.v4.content.CursorLoader;
 import android.support.v4.content.Loader;
 import android.util.Log;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.ImageButton;
 import android.widget.ImageView;
import android.widget.TextView;

 import com.example.android.myappportfolio.popularmovies.Data.MovieContract;
import com.squareup.picasso.Picasso;

 import butterknife.Bind;
 import butterknife.ButterKnife;


public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.details_title) TextView title;
    @Bind(R.id.movie_image) ImageView imageView;
    @Bind(R.id.release_date) TextView releaseDate;
    @Bind(R.id.average_vote) TextView averageVote;
    @Bind(R.id.summary) TextView summary;
    @Bind(R.id.favourite) ImageButton favourite;

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();

    private static final int CURSOR_LOADER_ID = 0;

    private String CURRENT_MOVIE_ID;

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


    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OLOLO", "Add fav");
                ContentValues values = new ContentValues();
                values.put(MovieContract.FavouriteMovies.FAVOURITE_MOVIE_ID, CURRENT_MOVIE_ID);
                getContext().getContentResolver().insert(MovieContract.FavouriteMovies.CONTENT_URI, values);

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) { return; }

        CURRENT_MOVIE_ID = data.getString(COL_MOVIE_ID);
        title.setText(data.getString(COL_MOVIE_TITLE));
        Picasso.with(getActivity()).load(data.getString(COL_MOVIE_POSTER))
                .error(R.drawable.honeycomb)
                .into(imageView);
        releaseDate.setText(data.getString(COL_MOVIE_RELEASE_DATE));
        averageVote.setText(data.getString(COL_MOVIE_VOTE_AVERAGE) + "/10");
        summary.setText(data.getString(COL_MOVIE_PLOT_SYNOPSIS));
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
