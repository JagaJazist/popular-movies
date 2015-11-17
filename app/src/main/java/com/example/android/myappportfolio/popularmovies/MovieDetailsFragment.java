package com.example.android.myappportfolio.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.myappportfolio.popularmovies.Models.Movie;

import org.w3c.dom.Text;

/**
 * Created by rcc on 16.11.15.
 */
public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView text = (TextView) view.findViewById(R.id.details_text);
        Movie movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        text.setText(movie.toString());

        return view;
    }
}
