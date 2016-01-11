package com.example.android.myappportfolio.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.myappportfolio.popularmovies.Data.MovieContract;

import org.w3c.dom.Text;

import static com.example.android.myappportfolio.popularmovies.Data.MovieContract.ReviewEntry.COLUMN_AUTHOR;
import static com.example.android.myappportfolio.popularmovies.Data.MovieContract.ReviewEntry.COLUMN_CONTENT;

/**
 * Created by rcc on 10.01.16.
 */
public class ReviewsListAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieGridAdapter.class.getSimpleName();

    public ReviewsListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
        view.setTag(view.findViewById(R.id.review_author));
        view.setTag(view.findViewById(R.id.review_content));

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int authorColIndex = cursor.getColumnIndex(COLUMN_AUTHOR);
        int contentColIndex = cursor.getColumnIndex(COLUMN_CONTENT);

        TextView authorView = (TextView) view.findViewById(R.id.review_author);
        TextView contentView = (TextView) view.findViewById(R.id.review_content);

        authorView.setText(cursor.getString(authorColIndex));
        contentView.setText(cursor.getString(contentColIndex));
    }
}
