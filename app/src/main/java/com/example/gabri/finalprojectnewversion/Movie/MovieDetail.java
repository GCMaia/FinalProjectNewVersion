package com.example.gabri.finalprojectnewversion.Movie;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gabri.finalprojectnewversion.R;

public class MovieDetail extends AppCompatActivity {
    String title,year, rating,runtime, actors, plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent=getIntent();

        title=intent.getStringExtra("title");
        year=intent.getStringExtra("year");
        rating=intent.getStringExtra("rating");
        runtime=intent.getStringExtra("runtime");
        actors=intent.getStringExtra("actors");
        plot=intent.getStringExtra("plot");

        MovieFragment movieFragment=new MovieFragment();
        Bundle bundle=new Bundle();


        bundle.putString(title, intent.getStringExtra("title"));
        bundle.putString(year, intent.getStringExtra("year"));
        bundle.putString(rating, intent.getStringExtra("rating"));
        bundle.putString(runtime, intent.getStringExtra("runtime"));
        bundle.putString(actors, intent.getStringExtra("actors"));
        bundle.putString(plot, intent.getStringExtra("plot"));

        movieFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.empty_movie_frame, movieFragment);
        fragmentTransaction.commit();
    }
}
