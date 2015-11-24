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

 import butterknife.Bind;
 import butterknife.ButterKnife;


public class MovieDetailsFragment extends Fragment {

    @Bind(R.id.details_title) TextView title;
    @Bind(R.id.movie_image) ImageView imageView;
    @Bind(R.id.release_date) TextView releaseDate;
    @Bind(R.id.average_vote) TextView averageVote;
    @Bind(R.id.summary) TextView summary;


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
        Movie movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        title.setText(movie.title);
        Picasso.with(getActivity()).load(movie.posterUrl)
                .error(R.drawable.honeycomb)
                .into(imageView);
        releaseDate.setText(movie.releaseDate);
        averageVote.setText(movie.voteAverage + "/10");
        summary.setText(movie.plotSynopsis);

        return view;
    }
}
