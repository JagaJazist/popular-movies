package com.example.android.myappportfolio.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieGridAdapter<T extends String> extends ArrayAdapter<String> {

    private final String[] values;

    public MovieGridAdapter(Activity context, List<String> values) {
        super(context, 0, values);
        this.values = values.toArray(new String[]{});
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
//        if (values.length > 0) {
        Picasso.with(getContext()).load(values[position])
                .placeholder(R.drawable.honeycomb)
                .error(R.drawable.honeycomb)
                .into(imageView);
//        }
        return imageView;
    }
}
