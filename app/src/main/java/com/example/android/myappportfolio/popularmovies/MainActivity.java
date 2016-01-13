package com.example.android.myappportfolio.popularmovies;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();

        Fragment fragB = new MovieDetailsFragment();
//        int containerID = R
        String tag = null;
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(tag);
        ft.replace()
    }
}
