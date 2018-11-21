package com.example.gabri.finalprojectnewversion.Movie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

public class MovieInformationMain extends Activity {
    protected static final String ACTIVITY_NAME = "MovieInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information_main);

        Button search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MovieInformationMain.this, "Cannot Find That Movie", Toast.LENGTH_LONG).show();
            }
        });

    }
}
