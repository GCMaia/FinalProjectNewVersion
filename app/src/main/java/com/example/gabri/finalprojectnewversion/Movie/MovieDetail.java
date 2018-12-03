package com.example.gabri.finalprojectnewversion.Movie;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.example.gabri.finalprojectnewversion.R;

/**
 *class passes information and allows fragment to be viewed
 */
public class MovieDetail extends AppCompatActivity {
    /**
     * class variables
     */
    String title,year, rating,runtime, actors, plot,poster;

    /**
     * onCreate method retrieves information from MovieInformationClass through intent and packages it in a bundle for fragment use
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent=getIntent();

        //poster=intent.getStringExtra("poster");
        title=intent.getStringExtra("title");
        year=intent.getStringExtra("year");
        rating=intent.getStringExtra("rating");
        runtime=intent.getStringExtra("runtime");
        actors=intent.getStringExtra("actors");
        plot=intent.getStringExtra("plot");

        MovieFragment movieFragment=new MovieFragment();
        Bundle bundle=new Bundle();

        //bundle.putString("poster",poster);
        bundle.putString("title",title);
        bundle.putString("year",year);
        bundle.putString("rating", rating);
        bundle.putString("runtime", runtime);
        bundle.putString("actors",actors);
        bundle.putString("plot",plot);
        bundle.putString("origin","origin");

        movieFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.empty_movie_frame, movieFragment);
        fragmentTransaction.commit();
    }
}
