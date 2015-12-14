package com.example.android.myappportfolio.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.myappportfolio.popularmovies.Data.MovieContract;
import com.squareup.picasso.Picasso;

public class MovieGridAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieGridAdapter.class.getSimpleName();
    private Context mContext;

    public MovieGridAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mContext = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        view.setTag(view.findViewById(R.id.movie_image));

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
        final String posterURL = cursor.getString(index);
        Picasso.with(mContext).load(posterURL)
                .error(R.drawable.honeycomb)
                .into((ImageView)view);
    }
}
