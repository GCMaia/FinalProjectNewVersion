package com.example.gabri.finalprojectnewversion.FoodNutrition;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabri.finalprojectnewversion.R;

public class FoodNutritionSearchResult extends AppCompatActivity {
    String foodNamePassed, calPassed, fatPassed;
    /*
    this is onCreate method , it resopnsible to create the activity, and put initialization code,
    and use the super class method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition_search_result);
        TextView foodtType = findViewById(R.id.nutritionTextView6);
        TextView calories = findViewById(R.id.nutritionTextView7);
        final TextView fat = findViewById(R.id.nutritionTextView8);

        Bundle searchRst = this.getIntent().getExtras();
        foodNamePassed = searchRst.getString("foodname");
        calPassed = searchRst.getString("cal");
        fatPassed = searchRst.getString("fat");

        foodtType.setText(foodNamePassed);
        calories.setText(calPassed);
        fat.setText(fatPassed);

        Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                FoodDatabaseHelp foodDatabaseHelp=new FoodDatabaseHelp(FoodNutritionSearchResult.this);
                foodDatabaseHelp.insertValue(foodNamePassed,fatPassed,calPassed);
                foodDatabaseHelp.close();
                Toast.makeText(FoodNutritionSearchResult.this, "Saved", Toast.LENGTH_LONG).show();

            }
        });
        Button viewFavorites = (Button) findViewById(R.id.viewFavorites);
        viewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodNutritionSearchResult.this, FoodNutrition_favourites_list.class);
                intent.putExtra("foodname", foodNamePassed);
                intent.putExtra("cal", calPassed);
                intent.putExtra("fat", fatPassed);
                startActivity(intent);
            }
        });




        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){

                Intent intent = new Intent(FoodNutritionSearchResult.this, FoodNutritionMain.class);
                finish();


            }
        });
    }

}
