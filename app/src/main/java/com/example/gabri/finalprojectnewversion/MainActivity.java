package com.example.gabri.finalprojectnewversion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gabri.finalprojectnewversion.CBCNews.CBCNewsMain;
import com.example.gabri.finalprojectnewversion.FoodNutrition.FoodNutritionMain;
import com.example.gabri.finalprojectnewversion.Movie.MovieInformationMain;
import com.example.gabri.finalprojectnewversion.OCTranspo.OCTranspoMain;

/**
 *  Project Main Class, initial point for the whole application, it shows all the 4 buttons that connects with the other activities
 */
public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button foodButton = findViewById(R.id.foodButton);

    foodButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, FoodNutritionMain.class);
        startActivity(intent);
      }
    });

    Button CBCButton = findViewById(R.id.CBCbutton);

    CBCButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, CBCNewsMain.class);
        startActivity(intent);
      }
    });

    Button movieButton = findViewById(R.id.movieButton);

    movieButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MovieInformationMain.class);
        startActivity(intent);
      }
    });

    Button OCButton = findViewById(R.id.OCButton);

    OCButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, OCTranspoMain.class);
        startActivity(intent);
      }
    });

  }
}