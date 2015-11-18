package com.example.android.myappportfolio.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myappportfolio.popularmovies.Models.Movie;
import com.squareup.picasso.Picasso;


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
        Movie movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        TextView title = (TextView) view.findViewById(R.id.details_title);
        title.setText(movie.title);

        ImageView imageView = (ImageView) view.findViewById(R.id.movie_image);
        Picasso.with(getActivity()).load(movie.posterUrl)
                .error(R.drawable.honeycomb)
                .into(imageView);

        TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
        releaseDate.setText(movie.releaseDate);

        TextView averageVote = (TextView) view.findViewById(R.id.average_vote);
        averageVote.setText(movie.voteAverage + "/10");

        TextView summary = (TextView) view.findViewById(R.id.summary);
        summary.setText(movie.plotSynopsis);

        return view;
    }
}
