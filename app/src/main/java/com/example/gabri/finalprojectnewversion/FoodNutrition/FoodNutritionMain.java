package com.example.gabri.finalprojectnewversion.FoodNutrition;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.example.gabri.finalprojectnewversion.CBCNews.CBCNewsMain;
import com.example.gabri.finalprojectnewversion.Movie.MovieInformationMain;
import com.example.gabri.finalprojectnewversion.OCTranspo.OCTranspoMain;
import com.example.gabri.finalprojectnewversion.R;
import android.view.Menu;
import android.view.MenuItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;


public class FoodNutritionMain extends AppCompatActivity {
    private android.support.v7.widget.Toolbar foodToolbar;
    private RelativeLayout foodLayout;
    private Button nutritionButton;
    private EditText foodName;
    private  TextView calories;

    private ProgressBar progressBar;
    private int viewPosition;
    private Boolean clickAdd;
    private double total;

    private FoodAdapter adapter;

    final ArrayList<String> foodCaloriesListview = new ArrayList<>();
    final ArrayList<String> foodNameListview = new ArrayList<>();
    private String nameOfFood;
    Double cal = 0.0;
    String id;
    Double fat = 0.0;

//   String JSON_STRING ="{\"text\":{\"parsed\":\"food\":\"category\":\"categoryLabel\"}}";
//   String text;
//   String parsed;
//   String hints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition_main);

        final ProgressBar progressBar = findViewById(R.id.nutritionProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        final EditText editText = findViewById(R.id.nutritionEditText);

        foodLayout = (RelativeLayout) findViewById(R.id.food_main);

        adapter = new FoodAdapter(this);

        calories = findViewById(R.id.nutritionPart);

        Toolbar foodToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.nutritionToolbar);
        setSupportActionBar(foodToolbar);
        foodToolbar.setTitle("Food Nutrition");

        SharedPreferences sharedPreferences = getSharedPreferences("lastFood", MODE_PRIVATE);


        String tempNameOfFood = sharedPreferences.getString("food", "noFood");
        if (tempNameOfFood!="noFood"){
            nameOfFood = tempNameOfFood;
            Nutrition nutrition = new Nutrition();
            nutrition.execute();
        }

        Button nutritionButton = findViewById(R.id.nutritionButton);
        nutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(FoodNutritionMain.this,
                            "Enter again", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    foodName = findViewById(R.id.nutritionEditText);
                    nameOfFood = foodName.getText().toString();
                    Nutrition nutritionQuery = new Nutrition();
                    nutritionQuery.execute();

                    foodName.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    foodCaloriesListview.clear();
                    foodNameListview.clear();

                }
            }
        });



    }

    public class FoodAdapter extends ArrayAdapter<String> {

        FoodAdapter(Context context) {
            super(context, 0);
        }

        public String getCountFoodName(int position) {

            return foodNameListview.get(position);
        }

        public String getCountFoodCalories(int position){
            return foodCaloriesListview.get(position);
        }

        public int getCount(){

            return foodNameListview.size();
        }

        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = FoodNutritionMain.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.foodinflater, null);
            TextView foodCalories  = result.findViewById(R.id.foodCalories);
            TextView foodName = result.findViewById(R.id.foodName);
            foodCalories.setText(getCountFoodCalories(position));
            foodName.setText(getCountFoodName(position));
            return result;
        }
    }


    public class Nutrition extends AsyncTask<String, Integer, String> {
        String webpage = "https://api.edamam.com/api/food-database/parser?ingr=" + nameOfFood + "&app_id=e295ccff&app_key=363908a42710ddb56b5dbd9790951f0a";

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String jsonString = sh.makeServiceCall(webpage);

            if (jsonString != null) {
                try {
                    JSONObject obj1 = new JSONObject(jsonString);
                    JSONArray obj2 = obj1.getJSONArray("parsed");
                    JSONObject obj3 = obj2.getJSONObject(0);
                    JSONObject food = obj3.getJSONObject("food");
                    JSONObject nutrients = food.getJSONObject("nutrients");
                    cal = nutrients.getDouble("ENERC_KCAL");
                    fat = nutrients.getDouble("FAT");
//                    for (int i = 0; i < food.length(); i++){
//                        JSONObject c = food.getJSONObject(i);
//                        cal = c.getString("ENERC_KCAL");
                    //                }

                } catch (final JSONException e) {
                    e.getMessage();
                }}


            return null;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            Intent intent = new Intent(FoodNutritionMain.this, FoodNutritionSearchResult.class);
            intent.putExtra("foodname", nameOfFood);
            intent.putExtra("cal",cal.toString());
            intent.putExtra("fat",fat.toString());
            startActivity(intent);




        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.food_cbc:

                AlertDialog.Builder CBCBuilder = new AlertDialog.Builder(FoodNutritionMain.this);
                CBCBuilder.setMessage("CBCNews main page").setTitle("Go to CBCNews main page?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(FoodNutritionMain.this, CBCNewsMain.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();

                break;
            case R.id.food_movie:

                AlertDialog.Builder movieBuilder = new AlertDialog.Builder(FoodNutritionMain.this);
                movieBuilder.setMessage("Movies main page").setTitle("Go to Movies main page?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(FoodNutritionMain.this, MovieInformationMain.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();

                break;

            case R.id.food_bus:

                AlertDialog.Builder foodBuilder = new AlertDialog.Builder(FoodNutritionMain.this);
                foodBuilder.setMessage("OCtranspo main page").setTitle("Go to OCTranspo main page?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(FoodNutritionMain.this, OCTranspoMain.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();


                break;
            case R.id.food_about:

                Toast aboutToast = Toast.makeText(FoodNutritionMain.this, "activity version 1.0 by Ying Lu", Toast.LENGTH_SHORT);
                aboutToast.show();
                break;

            case R.id.food_help:
                AlertDialog.Builder helpBuilder = new AlertDialog.Builder(FoodNutritionMain.this);
                helpBuilder.setTitle("Help").setMessage("Type a food name then press to search \n").show();
                break;
        }
        return true;
    }


}


