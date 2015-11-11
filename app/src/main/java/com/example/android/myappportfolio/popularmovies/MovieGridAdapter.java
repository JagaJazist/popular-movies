package com.example.android.myappportfolio.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieGridAdapter extends ArrayAdapter<String> {

    private static final String LOG_TAG = MovieGridAdapter.class.getSimpleName();

    public MovieGridAdapter(Activity context, List<String> urls) {
        super(context, 0, urls);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png");
        return imageView;
    }
}
