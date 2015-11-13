package com.example.android.myappportfolio.popularmovies;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


public class BlankFragment extends Fragment {

    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        return fragment;
    }

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String moviesJson = new FetchMovies().execute(null, null, null).;



        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        String[] values = new String[] { "http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png","http://i.imgur.com/DvpvklR.png",
                "http://i.imgur.com/DvpvklR.png", "http://i.imgur.com/DvpvklR.png" };
        
        MovieGridAdapter adapter = new MovieGridAdapter(getActivity(), values);
        GridView grid = (GridView) rootView.findViewById(R.id.gridView);
        grid.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
