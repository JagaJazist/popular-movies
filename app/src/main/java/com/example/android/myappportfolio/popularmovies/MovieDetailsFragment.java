package com.example.android.myappportfolio.popularmovies;

 import android.content.ContentValues;
 import android.content.Intent;
 import android.database.Cursor;
 import android.net.Uri;
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
 import android.widget.ListView;
 import android.widget.TextView;

 import com.example.android.myappportfolio.popularmovies.Data.MovieContract;
 import com.squareup.picasso.NetworkPolicy;
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
    @Bind(R.id.reviews_list) ListView reviewsList;

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();

    private static final int DETAILS_LOADER_ID = 0;
    private static final int REVIEWS_LOADER_ID = 1;
    private static final int VIDEOS_LOADER_ID = 2;

    private Uri mCurrentUri;
    private int mIsFavourite;
    private ReviewsListAdapter reviewsListAdapter;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.MOVIE_PATH + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_PLOT_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_IS_FAVOURITE
    };

    private static final String[] REVIEW_COLUMNS = {
            MovieContract.ReviewEntry.REVIEW_PATH + "." + MovieContract.ReviewEntry._ID,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_REVIEW_ID,
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT,
            MovieContract.ReviewEntry.COLUMN_URL
    };

    private static final String[] VIDEO_COLUMNS = {
            MovieContract.VideoEntry.VIDEO_PATH + "." + MovieContract.VideoEntry._ID,
            MovieContract.VideoEntry.COLUMN_MOVIE_ID,
            MovieContract.VideoEntry.COLUMN_VIDEO_ID,
            MovieContract.VideoEntry.COLUMN_ISO,
            MovieContract.VideoEntry.COLUMN_KEY,
            MovieContract.VideoEntry.COLUMN_NAME,
            MovieContract.VideoEntry.COLUMN_SITE,
            MovieContract.VideoEntry.COLUMN_SIZE,
            MovieContract.VideoEntry.COLUMN_TYPE
    };

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_POSTER = 3;
    static final int COL_MOVIE_RELEASE_DATE = 4;
    static final int COL_MOVIE_VOTE_AVERAGE = 5;
    static final int COL_MOVIE_PLOT_SYNOPSIS = 6;
    static final int COL_MOVIE_IS_FAVOURITE = 7;

    static final int COL_REVIEW_ID = 1;
    static final int COL_REVIEW_AUTHOR = 2;
    static final int COL_REVIEW_CONTENT = 3;
    static final int COL_REVIEW_URL = 4;

    static final int COL_VIDEO_MOVIE_ID = 1;
    static final int COL_VIDEO_ID = 2;
    static final int COL_ISO = 3;
    static final int COL_KEY = 4;
    static final int COL_NAME = 5;
    static final int COL_SITE = 6;
    static final int COL_SIZE = 7;
    static final int COL_TYPE = 8;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reviewsListAdapter = new ReviewsListAdapter(getActivity(), null, 0);

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        reviewsList.setAdapter(reviewsListAdapter);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                if (mIsFavourite == 0) {
                    mIsFavourite = 1;
                    cv.put(MovieContract.MovieEntry.COLUMN_IS_FAVOURITE, 1);
                    getContext().getContentResolver().update(mCurrentUri, cv, null, null);
                    favourite.setImageResource(android.R.drawable.star_big_on);
                } else {
                    mIsFavourite = 0;
                    cv.put(MovieContract.MovieEntry.COLUMN_IS_FAVOURITE, 0);
                    getContext().getContentResolver().update(mCurrentUri, cv, null, null);
                    favourite.setImageResource(android.R.drawable.star_big_off);
                }
            }
        });

        Intent intent = getActivity().getIntent();
        String movieId = intent.getStringExtra("mov_id");
        FetchReviews fetchReviews = new FetchReviews(getActivity());
        fetchReviews.execute(movieId);

        FetchVideos fetchVideos = new FetchVideos(getActivity());
        fetchVideos.execute(movieId);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAILS_LOADER_ID, null, this);
        getLoaderManager().initLoader(REVIEWS_LOADER_ID, null, this);
        getLoaderManager().initLoader(VIDEOS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        mCurrentUri = intent.getData();

        switch(id) {
            case DETAILS_LOADER_ID:
                return new CursorLoader(
                        getActivity(),
                        mCurrentUri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        null);
            case REVIEWS_LOADER_ID:
                Uri reviewUri = MovieContract.ReviewEntry.buildReviewsUri(
                        Long.parseLong(intent.getStringExtra("mov_id")));
                Log.d("OLOLO", reviewUri.toString());
                return new CursorLoader(
                        getActivity(),
                        reviewUri,
                        REVIEW_COLUMNS,
                        null,
                        null,
                        null
            );
            case VIDEOS_LOADER_ID:
                Uri videoUri = MovieContract.VideoEntry.buildReviewsUri(
                        Long.parseLong(intent.getStringExtra("mov_id")));
                Log.d("OLOLO", videoUri.toString());
                return new CursorLoader(
                        getActivity(),
                        videoUri,
                        VIDEO_COLUMNS,
                        null,
                        null,
                        null
                );
            default: return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) { return; }

        int id = loader.getId();
        switch(id) {
            case DETAILS_LOADER_ID:

                title.setText(data.getString(COL_MOVIE_TITLE));
                Picasso.with(getActivity()).load(data.getString(COL_MOVIE_POSTER))
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .error(R.drawable.honeycomb)
                        .into(imageView);
                releaseDate.setText(data.getString(COL_MOVIE_RELEASE_DATE));
                averageVote.setText(data.getString(COL_MOVIE_VOTE_AVERAGE) + "/10");
                summary.setText(data.getString(COL_MOVIE_PLOT_SYNOPSIS));
                mIsFavourite = data.getInt(COL_MOVIE_IS_FAVOURITE);
                if (mIsFavourite == 1) {
                    favourite.setImageResource(android.R.drawable.star_big_on);
                } else {
                    favourite.setImageResource(android.R.drawable.star_big_off);
                }
                break;
            case REVIEWS_LOADER_ID:
                String reviewContent = data.getString(COL_REVIEW_CONTENT);
                Log.d("OLOLO CONTENT", reviewContent);
                reviewsListAdapter.swapCursor(data);
                break;
            case VIDEOS_LOADER_ID:
                String videoContent = data.getString(COL_KEY);
                Log.d("OLOLO CONTENT", videoContent);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
