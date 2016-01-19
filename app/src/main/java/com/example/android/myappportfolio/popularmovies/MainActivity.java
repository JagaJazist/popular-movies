package com.example.android.myappportfolio.popularmovies;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MoviesGridFragment.Callback {

    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";
    private static final String CURRENT_MOVIE_ID = "current_movie_id";
    String mSelectedMovieId;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        if (savedInstanceState != null) {
            mSelectedMovieId = savedInstanceState.getString(CURRENT_MOVIE_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(CURRENT_MOVIE_ID, mSelectedMovieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(String movieId) {
        mSelectedMovieId = movieId;
        if (mTwoPane) {
            onMovieChanged(movieId);
        } else {
            Intent intent = new Intent(this, DetailsActivity.class)
                    .putExtra(MovieDetailsFragment.DETAIL_MOVIE_ID, movieId);
            startActivity(intent);
        }
    }

    void onMovieChanged(final String newMovieId) {

        if(mTwoPane) {
            if (newMovieId == null) {
                mSelectedMovieId = null;
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
                if ((fragment != null)&&(fragment.getView() != null)) {
                    fragment.getView().setVisibility(View.GONE);
                }
            } else {
                mSelectedMovieId = newMovieId;
                final int WHAT = 1;
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == WHAT) {
                            Bundle args = new Bundle();
                            args.putString(MovieDetailsFragment.DETAIL_MOVIE_ID, newMovieId);
                            MovieDetailsFragment fragment = new MovieDetailsFragment();
                            fragment.setArguments(args);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.movie_detail_container, fragment, DETAIL_FRAGMENT_TAG)
                                    .commit();
                        }
                    }
                };
                handler.sendEmptyMessage(WHAT);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onMovieChanged(mSelectedMovieId);
    }
}
